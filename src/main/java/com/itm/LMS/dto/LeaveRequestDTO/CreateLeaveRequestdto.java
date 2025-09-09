package com.itm.LMS.dto.LeaveRequestDTO;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateLeaveRequestdto {
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer days;
    private String requestComment;   // Employee reason
}
