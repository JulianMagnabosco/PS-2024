package ps.jmagna.controllers;

import ps.jmagna.dtos.user.*;
import ps.jmagna.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;


import java.time.Instant;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private JwtEncoder encoder;
    @Autowired
    private AuthService service;

    @PostMapping("/signin")
    public LoginResponce auth(Authentication authentication) {
        Instant now = Instant.now();
        long expiry = 36000L;
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();
        LoginResponce loginResponce = service.login(authentication.getName());
        loginResponce.setToken(this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue());
        return loginResponce;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserRequest data){
        UserDto dto = service.register(data);
        return ResponseEntity.ok(dto);

    }
    @PostMapping("/test/signup")
    public UserTestResponce testSingUp(@RequestBody UserRequest data) {
        return service.testSingUp(data);
    }

    @PostMapping("/reset/req")
    public boolean requestPasswordToken(@RequestParam String email) {
        return service.requestPasswordToken(email);
    }

    @PostMapping("/reset")
    public boolean changePassword(@RequestBody PasswordRequest data) {
        return service.changePassword(data);
    }
}
