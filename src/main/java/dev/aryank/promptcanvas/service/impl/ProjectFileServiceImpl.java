package dev.aryank.promptcanvas.service.impl;

import dev.aryank.promptcanvas.dto.project.FileContentResponse;
import dev.aryank.promptcanvas.dto.project.FileNode;
import dev.aryank.promptcanvas.service.ProjectFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProjectFileServiceImpl implements ProjectFileService {
    @Override
    public List<FileNode> getFileTree(Long id, Long userId) {
        return List.of();
    }

    @Override
    public FileContentResponse getFileContent(Long id, String path, Long userId) {
        return null;
    }

    @Override
    public void saveFile(Long projectId, String filePath, String fileContent) {
        log.info("Saving file: {}", filePath);
        // SAVE THE FILE METADATA INTO THE POSTGRES
        // SAVE THE CONTENT INSIDE MINIO

    }
}
