package com.itm.LMS.controller;

import com.itm.LMS.dto.UserDTO.CreateUserdto;
import com.itm.LMS.dto.UserDTO.PatchUserdto;
import com.itm.LMS.dto.UserDTO.UpdateUserdto;
import com.itm.LMS.dto.UserDTO.Userdto;
import com.itm.LMS.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Userdto> createUser(@RequestBody CreateUserdto dto) {
        Userdto created = userService.createUser(dto);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Userdto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<Userdto>> getAllUsersPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Userdto> usersPage = userService.getAllUsersPaginated(page, size);
        return ResponseEntity.ok(usersPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Userdto> getUserById(@PathVariable Long id) {
        Userdto user = userService.getUserById(id); // throws UserNotFoundException automatically if not found
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Userdto> updateUser(@PathVariable Long id, @RequestBody UpdateUserdto dto) {
        Userdto user = userService.updateUser(id, dto); // throws UserNotFoundException automatically
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Userdto> patchUser(@PathVariable Long id, @RequestBody PatchUserdto dto) {
        Userdto user = userService.patchUser(id, dto); // throws UserNotFoundException automatically
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id); // throws UserNotFoundException automatically
        return ResponseEntity.noContent().build();
    }
}
