package com.itm.LMS.controller;

import com.itm.LMS.dto.EmployeeProfileDTO.*;
import com.itm.LMS.payload.ApiResponse;
import com.itm.LMS.security.CustomUserDetails;
import com.itm.LMS.service.EmployeeProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class EmployeeProfileController {

    private final EmployeeProfileService profileService;

    // create - admin or the user themselves
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','STAFF')")
    public ResponseEntity<EmployeeProfileDto> createProfile(
            @Valid @RequestBody CreateEmployeeProfileDto dto,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        EmployeeProfileDto created = profileService.createProfile(dto, currentUser);
        return ResponseEntity.status(201).body(created);
    }

    // get all (admin, manager)
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<List<EmployeeProfileDto>> getAllProfiles(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(profileService.getAllProfiles(currentUser));
    }

    @GetMapping("/paginated")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<Page<EmployeeProfileDto>> getAllProfilesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        return ResponseEntity.ok(profileService.getAllProfilesPaginated(currentUser, page, size));
    }

    // get by profile id
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','STAFF')")
    public ResponseEntity<EmployeeProfileDto> getProfileById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        return ResponseEntity.ok(profileService.getProfileById(id, currentUser));
    }

    // get by userId
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','STAFF')")
    public ResponseEntity<EmployeeProfileDto> getByUserId(
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        return ResponseEntity.ok(profileService.getProfileByUserId(userId, currentUser));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','STAFF')")
    public ResponseEntity<EmployeeProfileDto> updateProfile(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEmployeeProfileDto dto,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        return ResponseEntity.ok(profileService.updateProfile(id, dto, currentUser));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','STAFF')")
    public ResponseEntity<EmployeeProfileDto> patchProfile(
            @PathVariable Long id,
            @RequestBody PatchEmployeeProfileDto dto,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        return ResponseEntity.ok(profileService.patchProfile(id, dto, currentUser));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>>deleteProfile(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        profileService.deleteProfile(id, currentUser);
        return ResponseEntity.ok(ApiResponse.success( "User deleted successfully" , null));
    }
}
