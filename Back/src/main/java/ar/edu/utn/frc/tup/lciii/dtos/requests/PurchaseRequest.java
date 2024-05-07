package ar.edu.utn.frc.tup.lciii.dtos.requests;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseRequest {
    Long idUser;
    List<PurchaseItemRequest> items;
}
