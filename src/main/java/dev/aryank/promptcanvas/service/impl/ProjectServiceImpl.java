package dev.aryank.promptcanvas.service.impl;

import dev.aryank.promptcanvas.dto.project.ProjectRequest;
import dev.aryank.promptcanvas.dto.project.ProjectResponse;
import dev.aryank.promptcanvas.dto.project.ProjectSummaryResponse;
import dev.aryank.promptcanvas.entity.Project;
import dev.aryank.promptcanvas.entity.User;
import dev.aryank.promptcanvas.mapper.ProjectMapper;
import dev.aryank.promptcanvas.repository.ProjectRepository;
import dev.aryank.promptcanvas.repository.UserRepository;
import dev.aryank.promptcanvas.service.ProjectService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class ProjectServiceImpl implements ProjectService {

    ProjectRepository projectRepository;
    UserRepository userRepository;
    ProjectMapper projectMapper;

    @Override

    public ProjectResponse createProject(ProjectRequest request, Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow();
        Project project = Project.builder()
                .name(request.name())
                .owner(owner)
                .isPublic(false)
                .build();

        project = projectRepository.save(project);
        return  projectMapper.toProjectResponse(project);

    }

    @Override
    public List<ProjectSummaryResponse> getUserProjects(Long userId) {
//        return projectRepository.findAllAccessibleByUser(userId)
//                .stream()
//                .map(projectMapper::toProjectSummaryResponse)
//                .collect(Collectors.toList());
        return projectMapper.toListOfProjectSummaryResponse(projectRepository.findAllAccessibleByUser(userId));
    }

    @Override
    public ProjectResponse getUserProjectById(Long id, Long userId) {

        Project project = getAccessibleProjectById(id, userId);

//        Project project = projectRepository.findById(id).orElseThrow();
//        if (!project.getOwner().getId().equals(userId)) {throw new RuntimeException("User does not own this project");}
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public ProjectResponse updateProject(Long id, ProjectRequest request, Long userId) {
        Project project = getAccessibleProjectById(id, userId);

        if (!project.getOwner().getId().equals(userId)) {
            throw new RuntimeException("You are not allowed to update the name of this project");
        }

        project.setName(request.name());
        project = projectRepository.save(project);

        return  projectMapper.toProjectResponse(project);

    }

    @Override
    public void softDelete(Long id, Long userId) {
        Project project = getAccessibleProjectById(id, userId);

        if (!project.getOwner().getId().equals(userId)) {
            throw new RuntimeException("You are not allowed to delete this project");
        }
        project.setDeletedAt(Instant.now());
        projectRepository.save(project);
    }


//    ///Internal functions
    public Project getAccessibleProjectById(Long id, Long userId) {
        return projectRepository.findAccessibleProjectById(id, userId).orElseThrow();
    }
}
