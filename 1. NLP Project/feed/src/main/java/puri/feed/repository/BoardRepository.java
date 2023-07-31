package puri.feed.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import puri.feed.entity.*;

public interface BoardRepository extends JpaRepository<Board,Long> {
    // Page<User> findAll(Pageable pageable);
    @Query(value = "SELECT * FROM board WHERE user_id = ?1",nativeQuery = true)
    List<Board> findAllBoardByUserId(Long uid);

}
