package dev.aryank.promptcanvas.service.impl;

import dev.aryank.promptcanvas.dto.member.InviteMemberRequest;
import dev.aryank.promptcanvas.dto.member.MemberResponse;
import dev.aryank.promptcanvas.dto.member.UpdateMemberRoleRequest;
import dev.aryank.promptcanvas.entity.Project;
import dev.aryank.promptcanvas.entity.ProjectMember;
import dev.aryank.promptcanvas.entity.ProjectMemberId;
import dev.aryank.promptcanvas.entity.User;
import dev.aryank.promptcanvas.mapper.ProjectMemberMapper;
import dev.aryank.promptcanvas.repository.ProjectMemberRepository;
import dev.aryank.promptcanvas.repository.ProjectRepository;
import dev.aryank.promptcanvas.repository.UserRepository;
import dev.aryank.promptcanvas.service.ProjectMemberService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Transactional
public class ProjectMemberServiceImpl implements ProjectMemberService {

    ProjectMemberRepository projectMemberRepository;
    ProjectRepository projectRepository;
    ProjectMemberMapper projectMemberMapper;
    UserRepository userRepository;

    @Override
    public List<MemberResponse> getProjectMembers(long id, Long userId) {
        Project project = getAccessibleProjectById(id, userId);

        return projectMemberRepository.findByIdProjectId(id)
                .stream()
                .map(projectMemberMapper::toProjectMemberResponseFromMember)
                .toList();

    }

    @Override
    public MemberResponse inviteMember(Long id, InviteMemberRequest inviteMemberRequest, Long userId) {
        Project project = getAccessibleProjectById(id, userId);

        User invitee = userRepository.findByUsername(inviteMemberRequest.username()).orElseThrow();

        if (invitee.getId().equals(userId)) {
            throw new RuntimeException("You cannot invite yourself");
        }

        ProjectMemberId projectMemberId = new ProjectMemberId(id, invitee.getId());

        if (projectMemberRepository.existsById(projectMemberId)) {
            throw new RuntimeException("Cannot invite once again");
        }

        ProjectMember member = ProjectMember.builder()
                .id(projectMemberId)
                .project(project)
                .user(invitee)
                .projectRole(inviteMemberRequest.role())
                .invitedAt(Instant.now())
                .build();
        projectMemberRepository.save(member);

        return projectMemberMapper.toProjectMemberResponseFromMember(member);
    }

    @Override
    public MemberResponse updateMemberRole(Long id, Long memberId, UpdateMemberRoleRequest request, Long userId) {
        Project project = getAccessibleProjectById(id, userId);

        ProjectMemberId projectMemberId = new ProjectMemberId(id, memberId);
        ProjectMember projectMember = projectMemberRepository.findById(projectMemberId).orElseThrow();


        projectMember.setProjectRole(request.role());
        projectMemberRepository.save(projectMember);

        return projectMemberMapper.toProjectMemberResponseFromMember(projectMember);
    }

    @Override
    public void removeProjectMember(Long id, Long memberId, Long userId) {
        Project project = getAccessibleProjectById(id, userId);

        ProjectMemberId projectMemberId = new ProjectMemberId(id, memberId);
        if (!projectMemberRepository.existsById(projectMemberId)) {
            throw new RuntimeException("Member not present in project");
        }
        projectMemberRepository.deleteById(projectMemberId);
    }

    //    ///Internal functions
    public Project getAccessibleProjectById(Long id, Long userId) {
        return projectRepository.findAccessibleProjectById(id, userId).orElseThrow();
    }

}
