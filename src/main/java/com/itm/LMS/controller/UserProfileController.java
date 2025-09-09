package com.itm.LMS.controller;

import com.itm.LMS.dto.UserDTO.UserProfiledto;
import com.itm.LMS.payload.ApiResponse;
import com.itm.LMS.security.CustomUserDetails;
import com.itm.LMS.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-profiles")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    // Get all users with profiles and their leave requests
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<ApiResponse<List<UserProfiledto>>> getAllUsersWithProfile(
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        List<UserProfiledto> users = userProfileService.getAllUsersWithProfile(currentUser);
        return ResponseEntity.ok(ApiResponse.success("Fetched all users with profiles successfully", users));
    }

    @GetMapping("/paged")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<ApiResponse<Page<UserProfiledto>>> getAllUsersWithProfilePaged(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<UserProfiledto> usersPage = userProfileService.getAllUsersWithProfilePaged(currentUser, pageable);

        return ResponseEntity.ok(ApiResponse.success(
                "Fetched all users with profiles (paged) successfully", usersPage));
    }


    // Get a single user by ID with profile and leave requests
    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<ApiResponse<UserProfiledto>> getUserWithProfileByUserId(
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        UserProfiledto user = userProfileService.getUserWithProfileByUserId(userId, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Fetched user with profile successfully", user));
    }

    // Get logged-in user's own profile and leave requests
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','STAFF')")
    public ResponseEntity<ApiResponse<UserProfiledto>> getOwnUserWithProfile(
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        UserProfiledto user = userProfileService.getOwnUserWithProfile(currentUser);
        return ResponseEntity.ok(ApiResponse.success("Fetched your profile successfully", user));
    }
}
