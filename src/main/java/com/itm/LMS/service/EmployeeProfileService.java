package com.itm.LMS.service;

import com.itm.LMS.dto.EmployeeProfileDTO.*;
import com.itm.LMS.exceptions.DuplicateProfileException;
import com.itm.LMS.exceptions.ProfileNotFoundException;
import com.itm.LMS.model.EmployeeProfile;
import com.itm.LMS.model.Role;
import com.itm.LMS.model.User;
import com.itm.LMS.repo.EmployeeProfileRepository;
import com.itm.LMS.repo.UserRepository;
import com.itm.LMS.mapper.EmployeeProfileMapper;
import com.itm.LMS.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeProfileService {

    private final EmployeeProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final EmployeeProfileMapper mapper;

    @Transactional
    public EmployeeProfileDto createProfile(CreateEmployeeProfileDto dto, CustomUserDetails currentUser) {

        if (currentUser.getRole() != Role.ADMIN && !currentUser.getId().equals(dto.getUserId())) {
            throw new AccessDeniedException("Not allowed to create profile for other users");
        }

        if (profileRepository.existsByUserId(dto.getUserId())) {
            throw new DuplicateProfileException(dto.getUserId());
        }

        User user = userRepository.findById(dto.getUserId());

        EmployeeProfile profile = EmployeeProfile.builder()
                .name(dto.getName())
                .department(dto.getDepartment())
                .leaveBalance(dto.getLeaveBalance() != null ? dto.getLeaveBalance() : 20)
                .user(user)
                .build();

        EmployeeProfile saved = profileRepository.save(profile);

        user.setProfile(saved);
        userRepository.save(user);

        return mapper.toDto(saved);
    }

    public EmployeeProfileDto getProfileById(Long profileId, CustomUserDetails currentUser) {
        EmployeeProfile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException(profileId));

        enforceReadPermission(profile, currentUser);

        return mapper.toDto(profile);
    }

    public EmployeeProfileDto getProfileByUserId(Long userId, CustomUserDetails currentUser) {
        EmployeeProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ProfileNotFoundException(userId));

        enforceReadPermission(profile, currentUser);

        return mapper.toDto(profile);
    }

    public List<EmployeeProfileDto> getAllProfiles(CustomUserDetails currentUser) {

        if (currentUser.getRole() != Role.ADMIN
                && currentUser.getRole() != Role.MANAGER) {
            throw new AccessDeniedException("Not allowed to list all profiles");
        }

        return profileRepository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public Page<EmployeeProfileDto> getAllProfilesPaginated(CustomUserDetails currentUser, int page, int size) {
        if (currentUser.getRole() != Role.ADMIN
                && currentUser.getRole() != Role.MANAGER) {
            throw new AccessDeniedException("Not allowed to list all profiles");
        }

        Page<EmployeeProfile> p = profileRepository.findAll(PageRequest.of(page, size));
        List<EmployeeProfileDto> dtos = p.getContent().stream().map(mapper::toDto).collect(Collectors.toList());
        return new PageImpl<>(dtos, p.getPageable(), p.getTotalElements());
    }

    @Transactional
    public EmployeeProfileDto updateProfile(Long profileId, UpdateEmployeeProfileDto dto, CustomUserDetails currentUser) {
        EmployeeProfile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException(profileId));

        if (!currentUser.getRole().equals(Role.ADMIN)
                && !profile.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Not allowed to update this profile");
        }

        profile.setName(dto.getName());
        profile.setDepartment(dto.getDepartment());
        if (dto.getLeaveBalance() != null) profile.setLeaveBalance(dto.getLeaveBalance());

        EmployeeProfile saved = profileRepository.save(profile);
        return mapper.toDto(saved);
    }

    @Transactional
    public EmployeeProfileDto patchProfile(Long profileId, PatchEmployeeProfileDto dto, CustomUserDetails currentUser) {
        EmployeeProfile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException(profileId));

        if (!currentUser.getRole().equals(Role.ADMIN)
                && !profile.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Not allowed to patch this profile");
        }

        if (dto.getName() != null) profile.setName(dto.getName());
        if (dto.getDepartment() != null) profile.setDepartment(dto.getDepartment());
        if (dto.getLeaveBalance() != null) profile.setLeaveBalance(dto.getLeaveBalance());

        EmployeeProfile saved = profileRepository.save(profile);
        return mapper.toDto(saved);
    }

    @Transactional
    public void deleteProfile(Long profileId, CustomUserDetails currentUser) {
        EmployeeProfile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException(profileId));

        if (!currentUser.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("Not allowed to delete profile");
        }

        User user = profile.getUser();
        if (user != null) {
            user.setProfile(null);
            userRepository.save(user);
        }

        profileRepository.delete(profile);
    }

    private void enforceReadPermission(EmployeeProfile profile, CustomUserDetails currentUser) {

        if (currentUser.getRole() == Role.ADMIN ||
                currentUser.getRole() == Role.MANAGER) {
            return;
        }

        if (!profile.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Not allowed to view this profile");
        }
    }
}
