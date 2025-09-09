package com.itm.LMS.controller;

import com.itm.LMS.dto.LeaveRequestDTO.ActionLeaveRequestdto;
import com.itm.LMS.dto.LeaveRequestDTO.LeaveRequestdto;
import com.itm.LMS.payload.ApiResponse;
import com.itm.LMS.security.CustomUserDetails;
import com.itm.LMS.service.ActionLeaveRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/action-leaves")
@RequiredArgsConstructor
public class ActionLeaveRequestController {

    private final ActionLeaveRequestService actionLeaveRequestService;

    // Get all leave requests
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<List<LeaveRequestdto>>> getAllLeaveRequests() {
        List<LeaveRequestdto> leaves = actionLeaveRequestService.getAllLeaveRequests();
        return ResponseEntity.ok(ApiResponse.success("Fetched all leave requests", leaves));
    }

    // Get all pending leave requests
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<List<LeaveRequestdto>>> getAllPendingLeaveRequests() {
        List<LeaveRequestdto> leaves = actionLeaveRequestService.getAllPendingLeaveRequests();
        return ResponseEntity.ok(ApiResponse.success("Fetched all pending leave requests", leaves));
    }

    // Get all approved leave requests
    @GetMapping("/approved")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<List<LeaveRequestdto>>> getAllApprovedLeaveRequests() {
        List<LeaveRequestdto> leaves = actionLeaveRequestService.getAllApprovedLeaveRequests();
        return ResponseEntity.ok(ApiResponse.success("Fetched all approved leave requests", leaves));
    }

    // Get all rejected leave requests
    @GetMapping("/rejected")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<List<LeaveRequestdto>>> getAllRejectedLeaveRequests() {
        List<LeaveRequestdto> leaves = actionLeaveRequestService.getAllRejectedLeaveRequests();
        return ResponseEntity.ok(ApiResponse.success("Fetched all rejected leave requests", leaves));
    }

    // Get leave request by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<LeaveRequestdto>> getLeaveRequestById(@PathVariable Long id) {
        LeaveRequestdto leave = actionLeaveRequestService.getLeaveRequestById(id);
        return ResponseEntity.ok(ApiResponse.success("Fetched leave request", leave));
    }

    // Approve or Reject a leave request
    @PatchMapping("/{id}/action")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<LeaveRequestdto>> actionOnLeaveRequest(
            @PathVariable Long id,
            @RequestBody ActionLeaveRequestdto dto,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        LeaveRequestdto leave = actionLeaveRequestService.actionOnLeaveRequest(
                id, dto, currentUser.getEmployeeProfile()
        );
        return ResponseEntity.ok(ApiResponse.success("Leave request updated successfully", leave));
    }

    // Delete leave request by ID (admin only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteLeaveRequest(@PathVariable Long id) {
        actionLeaveRequestService.deleteLeaveRequestById(id);
        return ResponseEntity.ok(ApiResponse.success("Leave request deleted successfully", null));
    }
}
