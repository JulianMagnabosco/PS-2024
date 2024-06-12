package ps.jmagna.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import ps.jmagna.entities.SectionEntity;
import ps.jmagna.entities.UserEntity;
import ps.jmagna.enums.SecType;
import ps.jmagna.repository.SectionRepository;
import ps.jmagna.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

import static ps.jmagna.services.PublicationService.compressBytes;

@Component
public class DBConfig implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    SectionRepository sectionRepository;
    @Autowired
    UserRepository userRepository;

    @Value("${dev.test}")
    String ok;

    public boolean test() throws IOException {
        List<Path> images = Files.list(Paths.get("src/main/resources/test-img"))
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());

        List<Path> userImages = Files.list(Paths.get("src/main/resources/test-users"))
                .filter(Files::isRegularFile)
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
                sectionRepository.save(s);
                index++;
                if (index>images.size()-1) index=9;
            }
        }
        index=0;
        for (UserEntity u : userRepository.findAll()){
            Path randomImage = userImages.get(index);
            byte[] bytes = Files.readAllBytes(randomImage);
            System.out.println(bytes.length);
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
            throw new RuntimeException(e.getMessage());
        }
    }
}
