package com.itm.LMS.security;

import com.itm.LMS.model.EmployeeProfile;
import com.itm.LMS.model.User;
import com.itm.LMS.repo.EmployeeProfileRepository;
import com.itm.LMS.repo.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final EmployeeProfileRepository employeeProfileRepository;

    public CustomUserDetailsService(UserRepository userRepository, EmployeeProfileRepository employeeProfileRepository) {
        this.userRepository = userRepository;
        this.employeeProfileRepository = employeeProfileRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        EmployeeProfile employeeProfile = employeeProfileRepository.findByUser(user)
                .orElse(null); // or throw exception if needed

        return new CustomUserDetails(user, employeeProfile);
    }

}
