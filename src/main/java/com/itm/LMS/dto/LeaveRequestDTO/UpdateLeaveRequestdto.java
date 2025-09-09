package com.itm.LMS.dto.LeaveRequestDTO;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateLeaveRequestdto {
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer days;
    private String requestComment;   // Employee can update reason/comment
}
