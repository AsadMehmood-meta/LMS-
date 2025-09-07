package com.itm.LMS.service;

import com.itm.LMS.dto.UserDTO.CreateUserdto;
import com.itm.LMS.dto.UserDTO.UpdateUserdto;
import com.itm.LMS.dto.UserDTO.PatchUserdto;
import com.itm.LMS.dto.UserDTO.Userdto;
import com.itm.LMS.exceptions.DuplicateUserException;
import com.itm.LMS.mapper.UserMapper;
import com.itm.LMS.model.User;
import com.itm.LMS.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // Create
    @Transactional
    public Userdto createUser(CreateUserdto dto) {
        if (userRepository.findByUsername(dto.getUsername()) != null) {
            throw new DuplicateUserException(dto.getUsername());
        }
        User user = userMapper.toUserEntity(dto);
        User saved = userRepository.save(user);
        return userMapper.toUserDto(saved);
    }

    // Get by ID
    public Userdto getUserById(Long id) {
        return userMapper.toUserDto(userRepository.findById(id));
    }

    // Get all
    public List<Userdto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    // Get all users with pagination
    public Page<Userdto> getAllUsersPaginated(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return userRepository.findAllUsersPaginated(pageRequest)
                .map(userMapper::toUserDto);
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
