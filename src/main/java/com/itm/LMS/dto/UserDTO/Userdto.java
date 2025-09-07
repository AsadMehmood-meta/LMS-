package com.itm.LMS.dto.UserDTO;

import com.itm.LMS.model.Role;
import lombok.Data;

@Data
public class Userdto {
    private Long id;
    private String username;
    private Role role;
}
