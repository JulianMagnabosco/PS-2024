package ps.jmagna.dtos.purchase;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseRequest {
    Long idUser;
    List<PurchaseItemRequest> items;
}
