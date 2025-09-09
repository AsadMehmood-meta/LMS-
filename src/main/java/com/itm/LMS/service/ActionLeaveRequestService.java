package com.itm.LMS.service;

import com.itm.LMS.dto.LeaveRequestDTO.ActionLeaveRequestdto;
import com.itm.LMS.dto.LeaveRequestDTO.LeaveRequestdto;
import com.itm.LMS.exceptions.InvalidLeaveStatusException;
import com.itm.LMS.exceptions.ResourceNotFoundException;
import com.itm.LMS.exceptions.UnauthorizedActionException;
import com.itm.LMS.mapper.LeaveRequestMapper;
import com.itm.LMS.model.EmployeeProfile;
import com.itm.LMS.model.LeaveRequest;
import com.itm.LMS.model.LeaveStatus;
import com.itm.LMS.repo.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ActionLeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveRequestMapper leaveRequestMapper;

    // Get all leave requests
    public List<LeaveRequestdto> getAllLeaveRequests() {
        return leaveRequestRepository.findAll().stream()
                .map(leaveRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Get all pending leave requests
    public List<LeaveRequestdto> getAllPendingLeaveRequests() {
        return leaveRequestRepository.findAll().stream()
                .filter(lr -> lr.getStatus() == LeaveStatus.PENDING)
                .map(leaveRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Get all approved leave requests
    public List<LeaveRequestdto> getAllApprovedLeaveRequests() {
        return leaveRequestRepository.findAll().stream()
                .filter(lr -> lr.getStatus() == LeaveStatus.APPROVED)
                .map(leaveRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Get all rejected leave requests
    public List<LeaveRequestdto> getAllRejectedLeaveRequests() {
        return leaveRequestRepository.findAll().stream()
                .filter(lr -> lr.getStatus() == LeaveStatus.REJECTED)
                .map(leaveRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Get leave request by ID
    public LeaveRequestdto getLeaveRequestById(Long id) {
        LeaveRequest leave = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));
        return leaveRequestMapper.toDTO(leave);
    }

    // Admin/Manager action on leave request
    public LeaveRequestdto actionOnLeaveRequest(Long id, ActionLeaveRequestdto dto, EmployeeProfile actionBy) {
        LeaveRequest leave = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));

        // Cannot act on non-pending leave
        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new InvalidLeaveStatusException("Only PENDING leave requests can be acted upon");
        }

        // Manager cannot act on their own leave
        if (leave.getEmployee().getProfileId().equals(actionBy.getProfileId())) {
            throw new UnauthorizedActionException("You cannot approve/reject your own leave request");
        }

        // Apply action
        leave.setStatus(dto.getStatus());
        leave.setActionComment(dto.getActionComment());
        leave.setActionBy(actionBy);
        leave.setActionAt(LocalDate.now());

        leaveRequestRepository.save(leave);
        return leaveRequestMapper.toDTO(leave);
    }

    // Delete leave request by ID (admin only)
    public void deleteLeaveRequestById(Long id) {
        LeaveRequest leave = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));
        leaveRequestRepository.delete(leave);
    }
}
