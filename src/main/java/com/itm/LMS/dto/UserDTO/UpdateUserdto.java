package com.itm.LMS.dto.UserDTO;

import com.itm.LMS.model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUserdto {
    @NotBlank
    private String username;

    @NotBlank
    private String Password;

    @NotNull
    private Role role;
}