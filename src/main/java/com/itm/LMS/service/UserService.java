package com.itm.LMS.service;

import com.itm.LMS.dto.UserDTO.CreateUserdto;
import com.itm.LMS.dto.UserDTO.UpdateUserdto;
import com.itm.LMS.dto.UserDTO.PatchUserdto;
import com.itm.LMS.dto.UserDTO.Userdto;
import com.itm.LMS.exceptions.DuplicateUserException;
import com.itm.LMS.mapper.UserMapper;
import com.itm.LMS.model.Role;
import com.itm.LMS.model.User;
import com.itm.LMS.repo.UserRepository;
import com.itm.LMS.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // Create
    @Transactional
    public Userdto createUser(CreateUserdto dto) {
        Optional<User> existingUser = userRepository.findByUsername(dto.getUsername());
        if (existingUser.isPresent()) {
            throw new DuplicateUserException(dto.getUsername());
        }
        User user = userMapper.toUserEntity(dto);
        User saved = userRepository.save(user);
        return userMapper.toUserDto(saved);
    }

    public Userdto getUserById(Long id) {
        return userMapper.toUserDto(userRepository.findById(id));
    }

    // Get all
    public List<Userdto> getAllUsers(CustomUserDetails currentUser) {
        List<User> users;

        if (currentUser.getRole() == Role.ADMIN) {
            users = userRepository.findAll(); // method already exists
        } else if (currentUser.getRole() == Role.MANAGER) {
            users = userRepository.findByRoleIn(List.of(Role.MANAGER, Role.STAFF));
        } else {
            throw new AccessDeniedException("Not allowed to view all users");
        }

        return users.stream().map(userMapper::toUserDto).collect(Collectors.toList());
    }


    // Get all users with pagination
    public Page<Userdto> getAllUsersPaginated(CustomUserDetails currentUser, int page, int size) {

        List<User> users;

        if (currentUser.getRole() == Role.ADMIN) {
            users = userRepository.findAll(); // all users
        } else if (currentUser.getRole() == Role.MANAGER) {
            users = userRepository.findByRoleIn(List.of(Role.MANAGER, Role.STAFF));
        } else {
            throw new AccessDeniedException("Not allowed to view all users");
        }

        List<Userdto> dtos = users.stream().map(userMapper::toUserDto).collect(Collectors.toList());

        int start = Math.min(page * size, dtos.size());
        int end = Math.min(start + size, dtos.size());
        List<Userdto> pageContent = dtos.subList(start, end);

        return new PageImpl<>(pageContent, PageRequest.of(page, size), dtos.size());
    }


    // Update
    @Transactional
    public Userdto updateUser(Long id, UpdateUserdto dto) {
        User existing = userRepository.findById(id);
        existing.setUsername(dto.getUsername());
        existing.setPassword(dto.getPassword());
        existing.setRole(dto.getRole());
        User updated = userRepository.save(existing);
        return userMapper.toUserDto(updated);
    }

    // Patch
    @Transactional
    public Userdto patchUser(Long id, PatchUserdto dto) {
        User existing = userRepository.findById(id);
        if (dto.getUsername() != null) existing.setUsername(dto.getUsername());
        if (dto.getPassword() != null) existing.setPassword(dto.getPassword());
        if (dto.getRole() != null) existing.setRole(dto.getRole());
        User updated = userRepository.save(existing);
        return userMapper.toUserDto(updated);
    }

    // Delete
    @Transactional
    public void deleteUser(Long id) {
        User existing = userRepository.findById(id);
        userRepository.delete(existing);
    }
}
