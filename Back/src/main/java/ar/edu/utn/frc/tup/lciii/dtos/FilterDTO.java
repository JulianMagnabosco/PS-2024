package ar.edu.utn.frc.tup.lciii.dtos;

import lombok.Data;

@Data
public class FilterDTO {
    private String columnName;

    private Object columnValue;
}
