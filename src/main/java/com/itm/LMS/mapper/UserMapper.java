package com.itm.LMS.mapper;


import com.itm.LMS.dto.UserDTO.CreateUserdto;
import com.itm.LMS.dto.UserDTO.Userdto;
import com.itm.LMS.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    Userdto toUserDto(User user);
    List<Userdto> toUserDtoList(List<User> userList);
    User toUserEntity(CreateUserdto dto);
}
