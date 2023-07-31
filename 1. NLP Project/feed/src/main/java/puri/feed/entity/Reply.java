package puri.feed.entity;

import java.sql.Timestamp;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // 답변내용
    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "boardId")
    private Board board;

    @ManyToOne // 여러 개의 답변을 한명의 유저만 작성 할 수 있다
    @JoinColumn(name = "userId")
    private User user;

    @CreationTimestamp
    private Timestamp timestamp;
}
