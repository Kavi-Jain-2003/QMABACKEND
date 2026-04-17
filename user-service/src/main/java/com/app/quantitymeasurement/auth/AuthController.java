package com.app.quantitymeasurement.auth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.app.quantitymeasurement.model.User;
import com.app.quantitymeasurement.repository.UserRepository;
import com.app.quantitymeasurement.security.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {

        System.out.println("LOGIN HIT: " + user.getUsername());

        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword()
                    )
            );

            if (!authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("status", 401, "message", "Login failed"));
            }

            String token = jwtUtil.generateToken(user.getUsername());

            return ResponseEntity.ok(Map.of(
                    "status", 200,
                    "token", token
            ));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", 401, "message", "Invalid username or password"));
        }
    }

    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            String username = user.getUsername();
            String password = user.getPassword();

            if (username == null || username.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", 400,
                        "message", "username is required"
                ));
            }

            if (password == null || password.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", 400,
                        "message", "password is required"
                ));
            }

            username = username.trim();

            if (userRepository.existsByUsername(username)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                        "status", 409,
                        "message", "user already exists"
                ));
            }

            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole("ROLE_USER");
            user.setProvider("LOCAL");

            userRepository.save(user);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", 201,
                    "message", "user registered successfully"
            ));
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "status", 409,
                    "message", "registration failed because the user already exists or data is invalid"
            ));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", 500,
                    "message", "registration failed"
            ));
        }
    }

    // ================= GOOGLE LOGIN =================
    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> body) {

        String email = body.get("email");
        String provider = body.getOrDefault("provider", "google");

        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "email is required"));
        }

        User user = userRepository.findByUsername(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUsername(email);
                    newUser.setPassword("OAUTH_USER"); // not used for auth
                    newUser.setRole("ROLE_USER");
                    newUser.setProvider(provider.toUpperCase());
                    return userRepository.save(newUser);
                });

        System.out.println("GOOGLE LOGIN: user ready → " + user.getUsername());

        String token = jwtUtil.generateToken(user.getUsername());

        return ResponseEntity.ok(Map.of(
                "status", 200,
                "token", token
        ));
    }

    // ================= SECURE TEST =================
    @GetMapping("/secure")
    public String secure() {
        return "This is secured API";
    }
}
