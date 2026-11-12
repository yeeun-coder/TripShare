package com.inhatc.tripshare.controller;

import com.inhatc.tripshare.dto.LoginRequest;
import com.inhatc.tripshare.dto.SignUpRequest;
import com.inhatc.tripshare.dto.UserResponse;
import com.inhatc.tripshare.service.TripShareService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final TripShareService service;

    public AuthController(TripShareService service) {
        this.service = service;
    }

    @PostMapping("/signup")
    public UserResponse signUp(@Valid @RequestBody SignUpRequest request) {
        return service.signUp(request);
    }

    @PostMapping("/login")
    public UserResponse login(@Valid @RequestBody LoginRequest request) {
        return service.login(request);
    }
}
