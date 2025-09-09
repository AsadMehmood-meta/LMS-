package com.itm.LMS.service;

import com.itm.LMS.dto.LeaveRequestDTO.CreateLeaveRequestdto;
import com.itm.LMS.dto.LeaveRequestDTO.LeaveRequestdto;
import com.itm.LMS.dto.LeaveRequestDTO.UpdateLeaveRequestdto;
import com.itm.LMS.model.LeaveRequest;
import com.itm.LMS.exceptions.ResourceNotFoundException;
import com.itm.LMS.exceptions.UnauthorizedActionException;
import com.itm.LMS.exceptions.InvalidLeaveStatusException;
import com.itm.LMS.mapper.LeaveRequestMapper;
import com.itm.LMS.model.EmployeeProfile;
import com.itm.LMS.model.LeaveStatus;
import com.itm.LMS.repo.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InitiateLeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveRequestMapper leaveRequestMapper;

    public List<LeaveRequestdto> getAllLeaveRequests(EmployeeProfile employee) {
        return leaveRequestRepository.findAll().stream()
                .filter(lr -> lr.getEmployee().getProfileId().equals(employee.getProfileId()))
                .map(leaveRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<LeaveRequestdto> getAllPendingLeaveRequests(EmployeeProfile employee) {
        return leaveRequestRepository.findAll().stream()
                .filter(lr -> lr.getEmployee().getProfileId().equals(employee.getProfileId())
                        && lr.getStatus() == LeaveStatus.PENDING)
                .map(leaveRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<LeaveRequestdto> getAllApprovedLeaveRequests(EmployeeProfile employee) {
        return leaveRequestRepository.findAll().stream()
                .filter(lr -> lr.getEmployee().getProfileId().equals(employee.getProfileId())
                        && lr.getStatus() == LeaveStatus.APPROVED)
                .map(leaveRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<LeaveRequestdto> getAllRejectedLeaveRequests(EmployeeProfile employee) {
        return leaveRequestRepository.findAll().stream()
                .filter(lr -> lr.getEmployee().getProfileId().equals(employee.getProfileId())
                        && lr.getStatus() == LeaveStatus.REJECTED)
                .map(leaveRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    public LeaveRequestdto getLeaveRequestById(Long id, EmployeeProfile employee) {
        LeaveRequest leave = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));

        if (!leave.getEmployee().getProfileId().equals(employee.getProfileId())) {
            throw new UnauthorizedActionException("Not authorized to view this leave request");
        }

        return leaveRequestMapper.toDTO(leave);
    }

    public LeaveRequestdto createLeaveRequest(CreateLeaveRequestdto dto, EmployeeProfile employee) {
        LeaveRequest leave = LeaveRequest.builder()
                .employee(employee)
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .days(dto.getDays())
                .requestComment(dto.getRequestComment())
                .status(LeaveStatus.PENDING)
                .build();

        leaveRequestRepository.save(leave);
        return leaveRequestMapper.toDTO(leave);
    }

    public LeaveRequestdto updateLeaveRequest(Long id, UpdateLeaveRequestdto dto, EmployeeProfile employee) {
        LeaveRequest leave = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));

        if (!leave.getEmployee().getProfileId().equals(employee.getProfileId())) {
            throw new UnauthorizedActionException("Not authorized to update this leave request");
        }

        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new InvalidLeaveStatusException("Only PENDING leave requests can be updated");
        }

        leaveRequestMapper.updateFromEmployeeDTO(dto, leave);
        leaveRequestRepository.save(leave);
        return leaveRequestMapper.toDTO(leave);
    }

    public void deleteLeaveRequest(Long id, EmployeeProfile employee) {
        LeaveRequest leave = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));

        if (!leave.getEmployee().getProfileId().equals(employee.getProfileId())) {
            throw new UnauthorizedActionException("Invalid leave request id");
        }

        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new InvalidLeaveStatusException("Only PENDING leave requests can be deleted");
        }

        leaveRequestRepository.delete(leave);
    }
}
