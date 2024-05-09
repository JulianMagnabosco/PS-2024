package ps.jmagna.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListUsersResponce {
    int countTotal;
    List<UserMinDto> list;
}
