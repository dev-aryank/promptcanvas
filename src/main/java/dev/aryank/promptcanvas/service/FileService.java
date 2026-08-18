package dev.aryank.promptcanvas.service;

import dev.aryank.promptcanvas.dto.project.FileContentResponse;
import dev.aryank.promptcanvas.dto.project.FileNode;

import java.util.List;

public interface FileService {
    List<FileNode> getFileTree(Long id, Long userId);

    FileContentResponse getFileContent(Long id, String path, Long userId);
}
