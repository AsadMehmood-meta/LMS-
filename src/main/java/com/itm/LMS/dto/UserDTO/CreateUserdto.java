package com.itm.LMS.dto.UserDTO;

import com.itm.LMS.model.Role;
import lombok.Data;

@Data
public class CreateUserdto {
    private String username;
    private String Password;
    private Role role;
}