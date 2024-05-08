package ar.edu.utn.frc.tup.lciii.dtos.purchase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponce {
    String resource;
    String topic;
}
