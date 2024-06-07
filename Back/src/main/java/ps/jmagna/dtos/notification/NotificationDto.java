package ps.jmagna.dtos.notification;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import ps.jmagna.entities.UserEntity;

import java.time.LocalDateTime;

@Data
public class NotificationDto {
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    LocalDateTime dateTime;
    String code;
    String title;
    String text;
}
