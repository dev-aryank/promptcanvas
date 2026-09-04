package dev.aryank.promptcanvas.service;

import dev.aryank.promptcanvas.dto.project.FileContentResponse;
import dev.aryank.promptcanvas.dto.project.FileNode;

import java.util.List;

public interface ProjectFileService {
    List<FileNode> getFileTree(Long id);

    FileContentResponse getFileContent(Long id, String path);

    void saveFile(Long projectId, String filePath, String fileContent);
}
