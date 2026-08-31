package dev.aryank.promptcanvas.service.impl;

import dev.aryank.promptcanvas.dto.project.FileContentResponse;
import dev.aryank.promptcanvas.dto.project.FileNode;
import dev.aryank.promptcanvas.entity.Project;
import dev.aryank.promptcanvas.entity.ProjectFile;
import dev.aryank.promptcanvas.error.ResourceNotFoundException;
import dev.aryank.promptcanvas.mapper.ProjectFileMapper;
import dev.aryank.promptcanvas.repository.ProjectFileRepository;
import dev.aryank.promptcanvas.repository.ProjectRepository;
import dev.aryank.promptcanvas.service.ProjectFileService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectFileServiceImpl implements ProjectFileService {

    private final ProjectRepository projectRepository;
    private final ProjectFileRepository projectFileRepository;
    private final MinioClient minioClient;
    private final ProjectFileMapper projectFileMapper;

    @Value("${minio.project-bucket}")
    private String projectBucket;

    @Override
    public List<FileNode> getFileTree(Long id, Long userId) {

        List<ProjectFile> projectFileList = projectFileRepository.findByProjectId(id);
        return projectFileMapper.toListOfFileNodes(projectFileList);
    }

    @Override
    public FileContentResponse getFileContent(Long id, String path, Long userId) {
        return null;
    }

    @Override
    public void saveFile(Long projectId, String path, String content) {
        Project project = projectRepository.findById(projectId).orElseThrow(
                () ->  new ResourceNotFoundException("Project", projectId.toString())
        );

        String cleanPath = path.startsWith("/") ? path.substring(1) : path;
        String objectKey = projectId + "/"  + cleanPath;

        try {
            byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
            InputStream inputStream = new ByteArrayInputStream(contentBytes);
            // saving the file content
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(projectBucket)
                            .object(objectKey)
                            .stream(inputStream, (long) contentBytes.length, (long) -1)
                            .contentType(determineContentType(path))
                            .build()
            );
            /// saving the metadata
            ProjectFile file = projectFileRepository.findByProjectIdAndPath(projectId, cleanPath)
                    .orElseGet(() -> ProjectFile.builder()
                            .project(project)
                            .path(cleanPath)
                            .minioObjectKey(objectKey)
                            .createdAt(Instant.now())
                            .build());

            file.setUpdatedAt(Instant.now());
            projectFileRepository.save(file);
            log.info("File {} has been saved", objectKey);

        } catch (Exception e){
            log.error("Error while saving file {}", objectKey, e);
            throw new RuntimeException("Error while saving file " + objectKey, e);
        }
    }

    private String determineContentType(String path) {
        String type = URLConnection.guessContentTypeFromName(path);
        if (type != null) { return type; }
        if (path.endsWith(".jsx") || path.endsWith(".ts") ||  path.endsWith(".tsx")) return "text/javascript";
        if (path.endsWith(".json")) return "application/json";
        if (path.endsWith(".css")) return "text/css";

        return "text/plain";
    }

}
