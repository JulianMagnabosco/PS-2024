package ps.jmagna.dtos.publication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartDto extends PublicationMinDto{
    int selectedCount;
}
