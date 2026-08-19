package dev.aryank.promptcanvas.service.impl;

import dev.aryank.promptcanvas.dto.project.FileContentResponse;
import dev.aryank.promptcanvas.dto.project.FileNode;
import dev.aryank.promptcanvas.service.FileService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public List<FileNode> getFileTree(Long id, Long userId) {
        return List.of();
    }

    @Override
    public FileContentResponse getFileContent(Long id, String path, Long userId) {
        return null;
    }
}
