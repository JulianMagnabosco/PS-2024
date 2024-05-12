package ps.jmagna.dtos.purchase;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseRequest {
    List<PurchaseItemRequest> items;
}
