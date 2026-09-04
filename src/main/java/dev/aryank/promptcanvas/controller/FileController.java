package dev.aryank.promptcanvas.controller;


import dev.aryank.promptcanvas.dto.project.FileContentResponse;
import dev.aryank.promptcanvas.dto.project.FileNode;
import dev.aryank.promptcanvas.service.ProjectFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{id}/files")
public class FileController {
    private final ProjectFileService projectFileService;

    @GetMapping
    public ResponseEntity<List<FileNode>> getFileTree(@PathVariable Long id){
        return ResponseEntity.ok(projectFileService.getFileTree(id));
    }

    @GetMapping("/{*path}")
    public ResponseEntity<FileContentResponse> getFile(@PathVariable Long id, @PathVariable String path){
        Long userId = 1L;
        return ResponseEntity.ok(projectFileService.getFileContent(id, path));
    }


}
