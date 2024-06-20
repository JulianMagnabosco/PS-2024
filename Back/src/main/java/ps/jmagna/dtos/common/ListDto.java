package ps.jmagna.dtos.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class ListDto <T>{
    private List<T> list;
    private long elements;
    private int pages;
}
