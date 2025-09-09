package com.itm.LMS.security;

import com.itm.LMS.model.EmployeeProfile;
import com.itm.LMS.model.Role;
import com.itm.LMS.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final User user;

    private final EmployeeProfile employeeProfile;


    public CustomUserDetails(User user, EmployeeProfile employeeProfile) {
        this.user = user;
        this.employeeProfile = employeeProfile;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName = user.getRole().name();
        if (!roleName.startsWith("ROLE_")) {
            roleName = "ROLE_" + roleName; 
        }
        return List.of(new SimpleGrantedAuthority(roleName));
    }

    @Override
    public String getPassword() { return user.getPassword(); }

    @Override
    public String getUsername() { return user.getUsername(); }

    public Long getId() { return user.getId(); }
    public Role getRole() { return user.getRole(); }

    public Long getUserId() {
        return user.getId();
    }

    public String getRoleName() {
        return user.getRole().name();
    }

    public EmployeeProfile getEmployeeProfile() {
        return employeeProfile;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    public boolean hasRole(String role) {
        return this.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + role));
    }
}
