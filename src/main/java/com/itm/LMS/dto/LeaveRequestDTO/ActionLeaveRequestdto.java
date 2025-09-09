package com.itm.LMS.dto.LeaveRequestDTO;


import com.itm.LMS.model.LeaveStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActionLeaveRequestdto {
    private LeaveStatus status;       // APPROVED / REJECTED
    private String actionComment;     // Admin/Manager comment
}
