package dev.aryank.promptcanvas.service.impl;

import dev.aryank.promptcanvas.dto.member.InviteMemberRequest;
import dev.aryank.promptcanvas.dto.member.MemberResponse;
import dev.aryank.promptcanvas.dto.member.UpdateMemberRoleRequest;
import dev.aryank.promptcanvas.service.ProjectMemberService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectMemberServiceImpl implements ProjectMemberService {
    @Override
    public List<MemberResponse> getProjectMembers(long id, Long userId) {
        return List.of();
    }

    @Override
    public MemberResponse inviteMember(Long id, InviteMemberRequest inviteMemberRequest, Long userId) {
        return null;
    }

    @Override
    public MemberResponse updateMemberRole(Long id, Long memberId, UpdateMemberRoleRequest role, Long userId) {
        return null;
    }

    @Override
    public MemberResponse deleteProjectMember(Long id, Long memberId, Long userId) {
        return null;
    }
}
