package com.itm.LMS.controller;

import com.itm.LMS.dto.LeaveRequestDTO.*;
import com.itm.LMS.payload.ApiResponse;
import com.itm.LMS.security.CustomUserDetails;
import com.itm.LMS.service.InitiateLeaveRequestService;
import com.itm.LMS.model.EmployeeProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee/leaves")
@RequiredArgsConstructor
public class InitiateLeaveRequestController {

    private final InitiateLeaveRequestService leaveRequestService;

    // Get all leave requests of current employee
    @GetMapping
    @PreAuthorize("hasRole('STAFF') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<LeaveRequestdto>>> getAllLeaveRequests(
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        EmployeeProfile employee = currentUser.getEmployeeProfile();
        List<LeaveRequestdto> leaves = leaveRequestService.getAllLeaveRequests(employee);
        return ResponseEntity.ok(ApiResponse.success("Fetched all leave requests", leaves));
    }

    // Get all pending leave requests of current employee
    @GetMapping("/pending")
    @PreAuthorize("hasRole('STAFF') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<LeaveRequestdto>>> getAllPendingLeaveRequests(
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        EmployeeProfile employee = currentUser.getEmployeeProfile();
        List<LeaveRequestdto> leaves = leaveRequestService.getAllPendingLeaveRequests(employee);
        return ResponseEntity.ok(ApiResponse.success("Fetched all pending leave requests", leaves));
    }

    // Get all approved leave requests of current employee
    @GetMapping("/approved")
    @PreAuthorize("hasRole('STAFF') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<LeaveRequestdto>>> getAllApprovedLeaveRequests(
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        EmployeeProfile employee = currentUser.getEmployeeProfile();
        List<LeaveRequestdto> leaves = leaveRequestService.getAllApprovedLeaveRequests(employee);
        return ResponseEntity.ok(ApiResponse.success("Fetched all approved leave requests", leaves));
    }


    // Get all rejected leave requests of current employee
    @GetMapping("/rejected")
    @PreAuthorize("hasRole('STAFF') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<LeaveRequestdto>>> getAllRejectedLeaveRequests(
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        EmployeeProfile employee = currentUser.getEmployeeProfile();
        List<LeaveRequestdto> leaves = leaveRequestService.getAllRejectedLeaveRequests(employee);
        return ResponseEntity.ok(ApiResponse.success("Fetched all rejected leave requests", leaves));
    }

    // Get leave request by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('STAFF') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<LeaveRequestdto>> getLeaveRequestById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        EmployeeProfile employee = currentUser.getEmployeeProfile();
        LeaveRequestdto leave = leaveRequestService.getLeaveRequestById(id, employee);
        return ResponseEntity.ok(ApiResponse.success("Fetched leave request", leave));
    }

    // Create new leave request
    @PostMapping
    @PreAuthorize("hasRole('STAFF') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<LeaveRequestdto>> createLeaveRequest(
            @RequestBody CreateLeaveRequestdto dto,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        EmployeeProfile employee = currentUser.getEmployeeProfile();
        LeaveRequestdto leave = leaveRequestService.createLeaveRequest(dto, employee);
        return ResponseEntity.ok(ApiResponse.success("Leave request created successfully", leave));
    }

    // Update leave request (only pending)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STAFF') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<LeaveRequestdto>> updateLeaveRequest(
            @PathVariable Long id,
            @RequestBody UpdateLeaveRequestdto dto,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        EmployeeProfile employee = currentUser.getEmployeeProfile();
        LeaveRequestdto leave = leaveRequestService.updateLeaveRequest(id, dto, employee);
        return ResponseEntity.ok(ApiResponse.success("Leave request updated successfully", leave));
    }

    // Delete leave request (only pending)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('STAFF') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteLeaveRequest(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        EmployeeProfile employee = currentUser.getEmployeeProfile();
        leaveRequestService.deleteLeaveRequest(id, employee);
        return ResponseEntity.ok(ApiResponse.success("Leave request deleted successfully", null));
    }
}
