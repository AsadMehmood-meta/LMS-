package com.itm.LMS.dto.EmployeeProfileDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateEmployeeProfileDto {
    @NotBlank
    private String name;

    @NotBlank
    private String department;

    private Integer leaveBalance;
}
