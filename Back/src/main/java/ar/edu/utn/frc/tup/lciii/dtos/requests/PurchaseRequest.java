package ar.edu.utn.frc.tup.lciii.dtos.requests;

import lombok.Data;

@Data
public class PurchaseRequest {
    Long idUser;
    Long idPub;
    int count;
}
