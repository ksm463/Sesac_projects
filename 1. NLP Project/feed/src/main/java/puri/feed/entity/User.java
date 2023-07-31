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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String userid;

    @Column(nullable = false, length = 20)
    private String username;


    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false)
    private String email;

  
    @CreationTimestamp
    private Timestamp createDate;

    // Enum 으로 변경 예정
    // @ColumnDefault("'user'")
    @Enumerated(EnumType.ORDINAL)
    private RoleType role;    
}
