package ar.edu.utn.frc.tup.lciii.dtos.purchase;

import com.mercadopago.resources.preference.Preference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotPurchaseResponce {
    String resource;
    String topic;
}
