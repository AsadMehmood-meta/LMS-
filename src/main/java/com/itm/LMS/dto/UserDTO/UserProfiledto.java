package com.itm.LMS.dto.UserDTO;

import com.itm.LMS.dto.EmployeeProfileDTO.EmployeeProfileDto;
import com.itm.LMS.model.Role;
import lombok.Data;

@Data
public class UserProfiledto {
    private Long id;
    private String username;
    private Role role;
    private EmployeeProfileDto profile;
}
