package dev.aryank.promptcanvas.controller;

import dev.aryank.promptcanvas.dto.member.InviteMemberRequest;
import dev.aryank.promptcanvas.dto.member.MemberResponse;
import dev.aryank.promptcanvas.dto.member.UpdateMemberRoleRequest;
import dev.aryank.promptcanvas.service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{id}/members")
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getProjectMembers(@PathVariable long id){
        Long userId = 1L;
        return ResponseEntity.ok(projectMemberService.getProjectMembers(id, userId));
    }

    @PostMapping
    public ResponseEntity<MemberResponse> inviteMember(@PathVariable Long id,
                                                       @RequestBody InviteMemberRequest inviteMemberRequest){
        Long userId = 1L;
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectMemberService.inviteMember(id, inviteMemberRequest, userId));
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<MemberResponse> updateMemberRole(@PathVariable Long memberId,
                                                           @RequestBody UpdateMemberRoleRequest role,
                                                           @PathVariable Long id){
        Long userId = 1L;
        return ResponseEntity.ok(projectMemberService.updateMemberRole(id, memberId, role, userId));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> removeMember(@PathVariable Long memberId,
                                                       @PathVariable Long id){
        Long userId = 1L;
        projectMemberService.removeProjectMember(id, memberId, userId);
        return ResponseEntity.noContent().build();
    }
}
