package com.itm.LMS.service;

import com.itm.LMS.dto.LeaveRequestDTO.LeaveRequestdto;
import com.itm.LMS.dto.UserDTO.UserProfiledto;
import com.itm.LMS.mapper.LeaveRequestMapper;
import com.itm.LMS.mapper.UserMapper;
import com.itm.LMS.mapper.UserProfileMapper;
import com.itm.LMS.model.EmployeeProfile;
import com.itm.LMS.model.User;
import com.itm.LMS.repo.EmployeeProfileRepository;
import com.itm.LMS.repo.LeaveRequestRepository;
import com.itm.LMS.repo.UserRepository;
import com.itm.LMS.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;
    private final LeaveRequestMapper leaveRequestMapper;

    // Get all users with profiles and their leave requests
    public List<UserProfiledto> getAllUsersWithProfile(CustomUserDetails currentUser) {
        List<User> users;

        switch (currentUser.getRole()) {
            case ADMIN:
                users = userRepository.findAll();
                break;
            case MANAGER:
                users = userRepository.findAll().stream()
                        .filter(user -> !user.getRole().equals("ADMIN"))
                        .collect(Collectors.toList());
                break;
            default:
                throw new AccessDeniedException("You are not allowed to view all users with profiles.");
        }

        return users.stream()
                .map(userProfileMapper::toDTO)  // mapper now handles null profiles & leaveRequests
                .collect(Collectors.toList());
    }


    // Get all users with pagination
    public Page<UserProfiledto> getAllUsersWithProfilePaged(CustomUserDetails currentUser, Pageable pageable) {
        List<User> users;

        if (currentUser.getRole().name().equals("ADMIN")) {
            users = userRepository.findAll(); // All users
        } else if (currentUser.getRole().name().equals("MANAGER")) {
            users = userRepository.findAll().stream()
                    .filter(user -> !user.getRole().name().equals("ADMIN"))
                    .collect(Collectors.toList());
        } else {
            throw new AccessDeniedException("You are not allowed to view all users with profiles.");
        }

        int start = Math.min((int) pageable.getOffset(), users.size());
        int end = Math.min(start + pageable.getPageSize(), users.size());

        List<UserProfiledto> pagedUsers = users.subList(start, end).stream()
                .map(userProfileMapper::toDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(pagedUsers, pageable, users.size());
    }

    // Get single user profile with leaves
    public UserProfiledto getUserWithProfileByUserId(Long userId, CustomUserDetails currentUser) {
        User user = userRepository.findById(userId);

        if (currentUser.getRole().name().equals("ADMIN")
                || (currentUser.getRole().name().equals("MANAGER") && !user.getRole().name().equals("ADMIN"))
                || currentUser.getId().equals(userId)) {
            return mapUserWithLeaves(user);
        } else {
            throw new AccessDeniedException("You are not allowed to view this user’s profile.");
        }
    }

    // Get own user profile with leaves
    public UserProfiledto getOwnUserWithProfile(CustomUserDetails currentUser) {
        User user = userRepository.findById(currentUser.getId());
        return mapUserWithLeaves(user);
    }

    // Helper method to map user to UserProfiledto including leave requests
    private UserProfiledto mapUserWithLeaves(User user) {
        EmployeeProfile employee = employeeProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Employee profile not found"));

        List<LeaveRequestdto> leaves = leaveRequestRepository.findByEmployee(employee).stream()
                .map(leaveRequestMapper::toDTO)
                .collect(Collectors.toList());

        UserProfiledto dto = userMapper.toUserProfileDto(user);
        dto.setLeaveRequests(leaves);
        return dto;
    }
}
