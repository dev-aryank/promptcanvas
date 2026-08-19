package dev.aryank.promptcanvas.service;

import dev.aryank.promptcanvas.dto.member.InviteMemberRequest;
import dev.aryank.promptcanvas.dto.member.MemberResponse;
import dev.aryank.promptcanvas.dto.member.UpdateMemberRoleRequest;

import java.util.List;

public interface ProjectMemberService {
    List<MemberResponse> getProjectMembers(long id, Long userId);

    MemberResponse inviteMember(Long id, InviteMemberRequest inviteMemberRequest, Long userId);

    MemberResponse updateMemberRole(Long id, Long memberId, UpdateMemberRoleRequest role, Long userId);

    void removeProjectMember(Long id, Long memberId, Long userId);
}
