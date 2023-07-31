package puri.feed.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import puri.feed.entity.*;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findById(long id);
    Optional<User> findByUserid(String userid);
    Optional<User> findByUsername(String username);
    String deleteByUserid(String userid);

    // Page<User> findAll(Pageable pageable);
    @Query(value = "SELECT * FROM user WHERE userid = ?1 AND password = ?2",nativeQuery = true)
    User login(String userid, String password);
}
