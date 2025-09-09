package com.itm.LMS.controller;

import com.itm.LMS.dto.UserDTO.UserProfiledto;
import com.itm.LMS.dto.UserDTO.Userdto;
import com.itm.LMS.payload.ApiResponse;
import com.itm.LMS.security.CustomUserDetails;
import com.itm.LMS.service.UserProfileService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<ApiResponse<List<UserProfiledto>>> getAllUsersWithProfile(
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        List<UserProfiledto> users = userProfileService.getAllUsersWithProfile(currentUser);
        return ResponseEntity.ok(ApiResponse.success("Fetched all users with profiles successfully", users));
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','STAFF')")
    public ResponseEntity<ApiResponse<UserProfiledto>> getUserWithProfileByUserId(
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        UserProfiledto user = userProfileService.getUserWithProfileByUserId(userId, currentUser);
        return ResponseEntity.ok(ApiResponse.success("Fetched user with profile successfully", user));
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','STAFF')")
    public ResponseEntity<ApiResponse<UserProfiledto>> getOwnUserWithProfile(
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        UserProfiledto user = userProfileService.getOwnUserWithProfile(currentUser);
        return ResponseEntity.ok(ApiResponse.success("Fetched your profile successfully", user));
    }
}
