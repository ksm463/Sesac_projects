package puri.feed.service;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import puri.feed.entity.*;


public interface BoardService {
    void saveBoard(Board board, User user);
    Page<Board> boardList(Pageable pageable);
    List<Board> getBoardListByUser(Long uid);
    Board BoardDetail(Long id);
    void deleteBoard(Long id, User user);
    void updateForm(Long id, Board requestBoard);
}
