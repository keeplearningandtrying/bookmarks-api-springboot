package com.sivalabs.bookmarksapi.users.web.controllers;

import com.sivalabs.bookmarksapi.common.annotations.AnyAuthenticatedUser;
import com.sivalabs.bookmarksapi.common.annotations.CurrentUser;
import com.sivalabs.bookmarksapi.config.security.SecurityUtils;
import com.sivalabs.bookmarksapi.users.entities.User;
import com.sivalabs.bookmarksapi.users.models.AuthUserDTO;
import com.sivalabs.bookmarksapi.users.models.ChangePasswordRequest;
import com.sivalabs.bookmarksapi.users.services.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class AuthUserController {
    private final UserService userService;
    private final SecurityUtils securityUtils;

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<AuthUserDTO> me() {
        User loginUser = securityUtils.loginUser();
        if (loginUser != null) {
            AuthUserDTO userDTO =
                    AuthUserDTO.builder()
                            .name(loginUser.getName())
                            .email(loginUser.getEmail())
                            .role(loginUser.getRole())
                            .build();
            return ResponseEntity.ok(userDTO);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/change-password")
    @AnyAuthenticatedUser
    public void changePassword(
            @RequestBody @Valid ChangePasswordRequest changePasswordRequest,
            @CurrentUser User loginUser) {
        String email = loginUser.getEmail();
        log.info("process=change_password, email={}", email);
        userService.changePassword(email, changePasswordRequest);
    }
}
