package ps.jmagna.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ps.jmagna.dtos.notification.NotificationDto;
import ps.jmagna.services.AuthService;
import ps.jmagna.services.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/api/not")
public class NotificationController {
    @Autowired
    NotificationService notificationService;
    @Autowired
    AuthService authService;
    @GetMapping("/list")
    public List<NotificationDto> getDrafts(@RequestParam int size, @AuthenticationPrincipal Jwt authentication) {
        return notificationService.getAll(size,authService.findUser(authentication));
    }
}
