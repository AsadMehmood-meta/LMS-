package com.itm.LMS.dto.EmployeeProfileDTO;

import lombok.Data;

@Data
public class PatchEmployeeProfileDto {
    private String name;
    private String department;
    private Integer leaveBalance;
}
