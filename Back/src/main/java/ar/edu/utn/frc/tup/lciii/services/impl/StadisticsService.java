package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.dtos.stadistics.StatDto;
import ar.edu.utn.frc.tup.lciii.dtos.stadistics.StatSeriesDto;
import ar.edu.utn.frc.tup.lciii.dtos.stadistics.StatsResponce;
import ar.edu.utn.frc.tup.lciii.entities.PublicationEntity;
import ar.edu.utn.frc.tup.lciii.entities.UserEntity;
import ar.edu.utn.frc.tup.lciii.repository.PublicationRepository;
import ar.edu.utn.frc.tup.lciii.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

@Service
public class StadisticsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PublicationRepository publicationRepository;

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

        YearMonth last = YearMonth.from(LocalDateTime.of(year,12,1,0,0));
        int lastday = last.atEndOfMonth().getDayOfMonth();
        List<UserEntity> entities =userRepository.findAllByCreationTimeBetween(
                LocalDateTime.of(year,1,1,0,0),
                LocalDateTime.of(year,12,lastday,23,59));

        if(entities.isEmpty()){
            return new StatsResponce(null, true);
        }

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

        return new StatsResponce(Arrays.stream(stats).toList(), false);
    }
    public StatsResponce getPublicationStadistics(int year, int month){
        StatSeriesDto stats = new StatSeriesDto();
        StatDto[] vals = {new StatDto("Artisticas"),
                new StatDto("Cientificas"),
                new StatDto("Tecnológicas"),
                new StatDto("Otro"),
        };

        YearMonth last = YearMonth.from(LocalDateTime.of(year,month,1,0,0));
        int lastday = last.atEndOfMonth().getDayOfMonth();
        List<PublicationEntity> entities =
                publicationRepository.findAllByCreationTimeBetween(
                        LocalDateTime.of(year,month,1,0,0),
                        LocalDateTime.of(year,month,lastday,23,59));

        if(entities.isEmpty()){
            return new StatsResponce(null, true);
        }

        for (PublicationEntity p : entities){
            int i = p.getType().ordinal();
            BigDecimal v = vals[i-1].getValue().add(BigDecimal.ONE);
            vals[i-1].setValue(v);
        }

        stats.setName("count");
        stats.setSeries(Arrays.stream(vals).toList());

        return new StatsResponce(List.of(stats), false);
    }

}
