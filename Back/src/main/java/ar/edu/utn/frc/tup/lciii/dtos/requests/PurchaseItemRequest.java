package ar.edu.utn.frc.tup.lciii.dtos.requests;

import lombok.Data;

@Data
public class PurchaseItemRequest {
    Long idPub;
    int count;
}
