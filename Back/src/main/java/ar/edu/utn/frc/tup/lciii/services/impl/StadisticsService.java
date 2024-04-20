package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.dtos.stadistics.StatDto;
import ar.edu.utn.frc.tup.lciii.dtos.stadistics.StatSeriesDto;
import ar.edu.utn.frc.tup.lciii.dtos.stadistics.StatsResponce;
import ar.edu.utn.frc.tup.lciii.entities.UserEntity;
import ar.edu.utn.frc.tup.lciii.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class StadisticsService {
    @Autowired
    UserRepository userRepository;

    public StatsResponce getUserStadistics(int year){
        StatSeriesDto[] stats = new StatSeriesDto[2];
        StatDto[] vals = {new StatDto("Enero"),
                new StatDto("Febrero"),
                new StatDto("Marzo"),
                new StatDto("Abril"),
                new StatDto("Mayo"),
                new StatDto("Junio"),
                new StatDto("Julio"),
                new StatDto("Agosto"),
                new StatDto("Septiembre"),
                new StatDto("Octubre"),
                new StatDto("Noviembre"),
                new StatDto("Diciembre"),
        };
        StatDto[] vals2 = {new StatDto("Enero"),
                new StatDto("Febrero"),
                new StatDto("Marzo"),
                new StatDto("Abril"),
                new StatDto("Mayo"),
                new StatDto("Junio"),
                new StatDto("Julio"),
                new StatDto("Agosto"),
                new StatDto("Septiembre"),
                new StatDto("Octubre"),
                new StatDto("Noviembre"),
                new StatDto("Diciembre"),
        };

        List<UserEntity> entities =userRepository.findAllByCreationTimeAfter(
                LocalDateTime.of(year,1,1,0,0));

        for (UserEntity e : entities) {
            BigDecimal v = vals[e.getCreationTime().getMonthValue()-1].getValue();
            v = v.add(BigDecimal.ONE);
            vals[e.getCreationTime().getMonthValue()-1].setValue(v);
            vals2[e.getCreationTime().getMonthValue()-1].setValue(v);
        }
        stats[0] = new StatSeriesDto("diary", Arrays.stream(vals).toList());

        vals2[0].setValue(vals[0].getValue());
        for (int i=1; i<vals2.length; i++) {
            vals2[i].setValue(vals2[i].getValue().add(vals2[i-1].getValue()));
        }
        stats[1] = new StatSeriesDto("anual", Arrays.stream(vals2).toList());

        return new StatsResponce(Arrays.stream(stats).toList());
    }
}
