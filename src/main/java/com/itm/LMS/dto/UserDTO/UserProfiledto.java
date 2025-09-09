package com.itm.LMS.dto.UserDTO;

import com.itm.LMS.model.Role;
import com.itm.LMS.dto.LeaveRequestDTO.LeaveRequestdto;
import lombok.*;
import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfiledto {

    private Long id;
    private String username;
//    private String password;
    private Role role;

    // EmployeeProfile fields
    private String name;
    private String department;
    private Integer leaveBalance;

    // List of leave requests
    @Builder.Default
    private List<LeaveRequestdto> leaveRequests = new ArrayList<>();
}
