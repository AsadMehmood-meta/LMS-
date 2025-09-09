package com.itm.LMS.mapper;

import com.itm.LMS.dto.LeaveRequestDTO.ActionLeaveRequestdto;
import com.itm.LMS.dto.LeaveRequestDTO.LeaveRequestdto;
import com.itm.LMS.dto.LeaveRequestDTO.UpdateLeaveRequestdto;
import com.itm.LMS.model.EmployeeProfile;
import com.itm.LMS.model.LeaveRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface LeaveRequestMapper {

    // -------------------------------
    // Entity → DTO
    // -------------------------------
    @Mapping(source = "id", target = "id")
    @Mapping(source = "employee.profileId", target = "employeeId")
    @Mapping(source = "employee.name", target = "employeeName")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "endDate", target = "endDate")
    @Mapping(source = "days", target = "days")
    @Mapping(source = "requestComment", target = "requestComment")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "actionComment", target = "actionComment")
    @Mapping(source = "actionBy.profileId", target = "actionById")
    @Mapping(source = "actionBy.name", target = "actionByName")
    @Mapping(source = "actionAt", target = "actionAt")
    @Mapping(source = "createdAt", target = "createdAt")
    LeaveRequestdto toDTO(LeaveRequest leaveRequest);

    // -------------------------------
    // DTO → Entity (Employee updates)
    // -------------------------------
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)           // ignore id during update
    @Mapping(target = "employee", ignore = true)     // cannot map directly from DTO
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "actionComment", ignore = true)
    @Mapping(target = "actionBy", ignore = true)
    @Mapping(target = "actionAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateFromEmployeeDTO(UpdateLeaveRequestdto dto, @MappingTarget LeaveRequest leaveRequest);

    // -------------------------------
    // DTO → Entity (Action updates)
    // -------------------------------
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "endDate", ignore = true)
    @Mapping(target = "days", ignore = true)
    @Mapping(target = "requestComment", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateFromActionDTO(ActionLeaveRequestdto dto, @MappingTarget LeaveRequest leaveRequest);

    // -------------------------------
    // Helper methods for nested mapping
    // -------------------------------
    default EmployeeProfile mapEmployee(Long profileId) {
        if (profileId == null) return null;
        EmployeeProfile e = new EmployeeProfile();
        e.setProfileId(profileId);
        return e;
    }

    default EmployeeProfile mapActionBy(Long profileId) {
        if (profileId == null) return null;
        EmployeeProfile e = new EmployeeProfile();
        e.setProfileId(profileId);
        return e;
    }
}
