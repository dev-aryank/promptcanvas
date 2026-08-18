package dev.aryank.promptcanvas.service;

import dev.aryank.promptcanvas.dto.project.ProjectRequest;
import dev.aryank.promptcanvas.dto.project.ProjectResponse;
import dev.aryank.promptcanvas.dto.project.ProjectSummaryResponse;

import java.util.List;

public interface ProjectService {
    List<ProjectSummaryResponse> getUserProjects(Long userId);

    ProjectResponse getUserProjectById(Long id, Long userId);

    ProjectResponse createProject(ProjectRequest request, Long userId);

    ProjectResponse updateProject(Long id, ProjectRequest request, Long userId);

    void softDelete(Long id, Long userId);
}
