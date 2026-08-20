package dev.aryank.promptcanvas.service;

import dev.aryank.promptcanvas.dto.member.InviteMemberRequest;
import dev.aryank.promptcanvas.dto.member.MemberResponse;
import dev.aryank.promptcanvas.dto.member.UpdateMemberRoleRequest;

import java.util.List;

public interface ProjectMemberService {
    List<MemberResponse> getProjectMembers(long id);

    MemberResponse inviteMember(Long id, InviteMemberRequest inviteMemberRequest);

    MemberResponse updateMemberRole(Long id, Long memberId, UpdateMemberRoleRequest role);

    void removeProjectMember(Long id, Long memberId);
}
