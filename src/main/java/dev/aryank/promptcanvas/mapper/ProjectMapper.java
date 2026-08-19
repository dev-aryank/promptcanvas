package dev.aryank.promptcanvas.mapper;

import dev.aryank.promptcanvas.dto.project.ProjectResponse;
import dev.aryank.promptcanvas.dto.project.ProjectSummaryResponse;
import dev.aryank.promptcanvas.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectResponse toProjectResponse(Project project);

    ProjectSummaryResponse toProjectSummaryResponse(Project project);


    List<ProjectSummaryResponse> toListOfProjectSummaryResponse(List<Project> projects);

}
