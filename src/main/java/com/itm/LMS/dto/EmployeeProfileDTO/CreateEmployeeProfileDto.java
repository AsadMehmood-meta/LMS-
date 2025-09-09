package com.itm.LMS.dto.EmployeeProfileDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateEmployeeProfileDto {
    @NotNull
    private Long userId;

    @NotBlank
    private String name;

    @NotBlank
    private String department;

    private Integer leaveBalance;
}
