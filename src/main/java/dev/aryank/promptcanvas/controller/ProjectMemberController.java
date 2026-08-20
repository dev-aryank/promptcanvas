package dev.aryank.promptcanvas.controller;

import dev.aryank.promptcanvas.dto.member.InviteMemberRequest;
import dev.aryank.promptcanvas.dto.member.MemberResponse;
import dev.aryank.promptcanvas.dto.member.UpdateMemberRoleRequest;
import dev.aryank.promptcanvas.service.ProjectMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{id}/members")
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getProjectMembers(@PathVariable long id){

        return ResponseEntity.ok(projectMemberService.getProjectMembers(id));
    }

    @PostMapping
    public ResponseEntity<MemberResponse> inviteMember(@PathVariable Long id,
                                                       @RequestBody @Valid InviteMemberRequest inviteMemberRequest){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectMemberService.inviteMember(id, inviteMemberRequest));
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<MemberResponse> updateMemberRole(@PathVariable Long memberId,
                                                           @RequestBody @Valid UpdateMemberRoleRequest role,
                                                           @PathVariable Long id){

        return ResponseEntity.ok(projectMemberService.updateMemberRole(id, memberId, role));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> removeMember(@PathVariable Long memberId,
                                                       @PathVariable Long id){

        projectMemberService.removeProjectMember(id, memberId);
        return ResponseEntity.noContent().build();
    }
}
