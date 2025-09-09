package com.itm.LMS.dto.EmployeeProfileDTO;

import lombok.Data;

@Data
public class EmployeeProfileDto {
    private Long profileId;
    private Long userId;
    private String name;
    private String department;
    private Integer leaveBalance;
}
