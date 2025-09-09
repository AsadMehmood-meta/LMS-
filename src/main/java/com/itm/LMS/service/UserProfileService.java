package com.itm.LMS.service;

import com.itm.LMS.dto.UserDTO.UserProfiledto;
import com.itm.LMS.dto.UserDTO.Userdto;
import com.itm.LMS.mapper.UserMapper;
import com.itm.LMS.model.User;
import com.itm.LMS.repo.UserRepository;
import com.itm.LMS.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public List<UserProfiledto> getAllUsersWithProfile(CustomUserDetails currentUser) {
        if (currentUser.getRole().name().equals("ADMIN")) {
            return userRepository.findAll().stream()
                    .map(userMapper::toUserProfileDto)
                    .collect(Collectors.toList());
        } else if (currentUser.getRole().name().equals("MANAGER")) {
            return userRepository.findAll().stream()
                    .filter(user -> !user.getRole().name().equals("ADMIN"))
                    .map(userMapper::toUserProfileDto)
                    .collect(Collectors.toList());
        } else {
            throw new AccessDeniedException("You are not allowed to view all users with profiles.");
        }
    }

    public UserProfiledto getUserWithProfileByUserId(Long userId, CustomUserDetails currentUser) {
        User user = userRepository.findById(userId);

        if (currentUser.getRole().name().equals("ADMIN")) {
            return userMapper.toUserProfileDto(user);
        } else if (currentUser.getRole().name().equals("MANAGER") && !user.getRole().name().equals("ADMIN")) {
            return userMapper.toUserProfileDto(user);
        } else if (currentUser.getId().equals(userId)) {
            return userMapper.toUserProfileDto(user);
        } else {
            throw new AccessDeniedException("You are not allowed to view this user’s profile.");
        }
    }

    public UserProfiledto getOwnUserWithProfile(CustomUserDetails currentUser) {
        User user = userRepository.findById(currentUser.getId());
        return userMapper.toUserProfileDto(user);
    }
}
