package com.itm.LMS.mapper;

import com.itm.LMS.model.EmployeeProfile;
import com.itm.LMS.dto.EmployeeProfileDTO.EmployeeProfileDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeProfileMapper {

    @Mapping(source = "user.id", target = "userId")
    EmployeeProfileDto toDto(EmployeeProfile entity);

    EmployeeProfile toEntity(EmployeeProfileDto dto);
}
