package com.itm.LMS.mapper;

import com.itm.LMS.dto.LeaveRequestDTO.LeaveRequestdto;
import com.itm.LMS.dto.UserDTO.UserProfiledto;
import com.itm.LMS.model.LeaveRequest;
import com.itm.LMS.model.User;
import org.mapstruct.*;
import java.util.*;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "profile.name", target = "name", defaultValue = "")
    @Mapping(source = "profile.department", target = "department", defaultValue = "")
    @Mapping(source = "profile.leaveBalance", target = "leaveBalance", defaultValue = "0")
    @Mapping(source = "profile.leaveRequests", target = "leaveRequests", qualifiedByName = "mapLeaveRequests")
    UserProfiledto toDTO(User user);

    @Named("mapLeaveRequests")
    default List<LeaveRequestdto> mapLeaveRequests(List<LeaveRequest> leaveRequests) {
        if (leaveRequests == null) return new ArrayList<>();
        return leaveRequests.stream()
                .map(lr -> LeaveRequestdto.builder()
                        .id(lr.getId())
                        .employeeId(lr.getEmployee() != null ? lr.getEmployee().getProfileId() : null)
                        .employeeName(lr.getEmployee() != null ? lr.getEmployee().getName() : null)
                        .startDate(lr.getStartDate())
                        .endDate(lr.getEndDate())
                        .days(lr.getDays())
                        .requestComment(lr.getRequestComment())
                        .status(lr.getStatus())
                        .actionComment(lr.getActionComment())
                        .actionById(lr.getActionBy() != null ? lr.getActionBy().getProfileId() : null)
                        .actionByName(lr.getActionBy() != null ? lr.getActionBy().getName() : null)
                        .actionAt(lr.getActionAt())
                        .createdAt(lr.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "profile", ignore = true)
    void updateFromDTO(UserProfiledto dto, @MappingTarget User user);
}
