package com.itm.LMS.dto.LeaveRequestDTO;

import com.itm.LMS.model.LeaveStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequestdto {
    private Long id;

    private Long employeeId;
    private String employeeName;       

    private LocalDate startDate;
    private LocalDate endDate;
    private Integer days;

    private String requestComment;      

    private LeaveStatus status;
    private String actionComment;      

    private Long actionById;
    private String actionByName;       

    private LocalDate actionAt;
    private LocalDateTime createdAt;
}
