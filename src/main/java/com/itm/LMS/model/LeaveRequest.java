package com.itm.LMS.model;

import com.itm.LMS.model.EmployeeProfile;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "leave_request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Employee requesting leave
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeProfile employee;

    // Employee request fields
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private Integer days;

    @Column(name = "request_comment")
    private String requestComment; // Employee reason

    // Admin/Manager action fields
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveStatus status = LeaveStatus.PENDING;

    @Column(name = "action_comment")
    private String actionComment; // Admin/Manager comment

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "action_by")
    private EmployeeProfile actionBy;

    @Column(name = "action_at")
    private LocalDate actionAt;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

}
