package ps.jmagna.repository;


import ps.jmagna.entities.CartEntity;
import ps.jmagna.entities.PublicationEntity;
import ps.jmagna.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {
        Optional<CartEntity> getByUserAndPublication(UserEntity user, PublicationEntity publication);
        List<CartEntity> findAllByUser_Id(Long userId);
        List<CartEntity> findAllByUser(UserEntity user);
}
