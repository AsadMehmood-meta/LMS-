package com.itm.LMS.repo;

import com.itm.LMS.model.EmployeeProfile;
import com.itm.LMS.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Long> {
    Optional<EmployeeProfile> findByUserId(Long userId);

    Optional<EmployeeProfile> findByUser(User user);

    boolean existsByUserId(Long userId);
}
