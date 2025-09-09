package com.itm.LMS.controller;

import com.itm.LMS.dto.UserDTO.CreateUserdto;
import com.itm.LMS.dto.UserDTO.PatchUserdto;
import com.itm.LMS.dto.UserDTO.UpdateUserdto;
import com.itm.LMS.dto.UserDTO.Userdto;
import com.itm.LMS.mapper.UserMapper;
import com.itm.LMS.repo.UserRepository;
import com.itm.LMS.security.CustomUserDetails;
import com.itm.LMS.service.UserService;
import com.itm.LMS.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<ApiResponse<List<Userdto>>> getAllUsers(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        List<Userdto> users = userService.getAllUsers(currentUser);
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully" , users));
    }

    @GetMapping("/paged")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<ApiResponse<Page<Userdto>>> getAllUsersPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        Page<Userdto> usersPage = userService.getAllUsersPaginated(currentUser, page, size);
        return ResponseEntity.ok(ApiResponse.success("Paginated users retrieved successfully" , usersPage));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER') or #id == principal.id")
    public ResponseEntity<ApiResponse<Userdto>> getUserById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        Userdto user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public ResponseEntity<ApiResponse<Userdto>> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserdto dto,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        Userdto user = userService.updateUser(id, dto);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", user));
    }


    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public ResponseEntity<ApiResponse<Userdto>> patchUser(
            @PathVariable Long id,
            @RequestBody PatchUserdto dto,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        Userdto user = userService.patchUser(id, dto);
        return ResponseEntity.ok(ApiResponse.success("User patched successfully", user));
    }

    @PostMapping("/{id}/reset-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> resetPassword(@PathVariable Long id) {
        String tempPassword = userService.resetUserPassword(id);
        return ResponseEntity.ok("Temporary password for user: " + tempPassword);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success( "User deleted successfully" , null));
    }
}
