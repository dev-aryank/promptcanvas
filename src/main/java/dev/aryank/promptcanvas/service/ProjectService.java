package dev.aryank.promptcanvas.service;

import dev.aryank.promptcanvas.dto.project.ProjectRequest;
import dev.aryank.promptcanvas.dto.project.ProjectResponse;
import dev.aryank.promptcanvas.dto.project.ProjectSummaryResponse;

import java.util.List;

public interface ProjectService {
    List<ProjectSummaryResponse> getUserProjects();

    ProjectResponse getUserProjectById(Long id);

    ProjectResponse createProject(ProjectRequest request);

    ProjectResponse updateProject(Long id, ProjectRequest request);

    void softDelete(Long id);
}
