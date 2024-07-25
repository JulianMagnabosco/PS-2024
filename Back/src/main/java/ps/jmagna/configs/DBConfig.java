package ps.jmagna.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import ps.jmagna.entities.SectionEntity;
import ps.jmagna.entities.UserEntity;
import ps.jmagna.enums.SecType;
import ps.jmagna.repository.SectionRepository;
import ps.jmagna.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

import static ps.jmagna.services.PublicationService.compressBytes;

@Component
public class DBConfig implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    SectionRepository sectionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ResourcePatternResolver resourcePatternResolver;
    @Value("${dev.test}")
    String ok;

    public boolean test() throws IOException {

        List<Path> images = Stream.of(
                        Objects.requireNonNull(new File("src/main/resources/static/test-img").listFiles()))
                .filter(file -> !file.isDirectory())
                .map(File::toPath)
                .collect(Collectors.toList());

        List<Path> userImages = Stream.of(
                        Objects.requireNonNull(new File("src/main/resources/static/test-users").listFiles()))
                .filter(file -> !file.isDirectory())
                .map(File::toPath)
                .collect(Collectors.toList());

        images.sort(Comparator.comparing((e)->e.getFileName().toString().toLowerCase()));
        if (images.isEmpty()) {
            throw new IOException("No images found in the directory");
        }

        int index=0;

        for (SectionEntity s : sectionRepository.findAll()){
            if(s.getType().equals(SecType.PHOTO)||s.getType().equals(SecType.STEP)){
                Path randomImage = images.get(index);
                byte[] bytes = Files.readAllBytes(randomImage);
                s.setImage(compressBytes(bytes));
//                System.out.println(randomImage+": "+s.getPublication().getName());
                sectionRepository.save(s);
                index++;
                if (index>images.size()-1) index=9;
            }
        }
        index=0;
        for (UserEntity u : userRepository.findAll()){
            Path randomImage = userImages.get(index);
            byte[] bytes = Files.readAllBytes(randomImage);
            u.setIcon(compressBytes(bytes));
            userRepository.save(u);
            index++;
            if (index>userImages.size()-1) index=0;
        }
        System.out.println("Cargadas las imagenes:"+images.size());
        return true;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if(Objects.equals(ok, "false")) return;
        try {
            test();
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
