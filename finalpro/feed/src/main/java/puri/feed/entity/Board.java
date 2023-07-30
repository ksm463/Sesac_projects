package puri.feed.entity;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    // 1. 대용량 데이터
    @Lob 
    private String content;

    // 조회수
    @ColumnDefault("0")
    private int count;

    // 2
    // Many : Board
    // One : User
    // 한명의 유저는 여러개의 게시글 작성 가능
    @ManyToOne
    // 3
    @JoinColumn(name = "userId") // 필드 값 user 가 아니라 userid 로 저장 (FK키 설정)
    private User user; // DB 는 오브젝트를 저장 할 수 없다. 자바는 오브젝트를 저장할 수 있다

    @CreationTimestamp
    private Timestamp createDate;

    @OneToMany(mappedBy = "board",fetch = FetchType.EAGER) // FK 키가 아님, DB에 컬럼 만들기 X
    private List<Reply> reply;
}
