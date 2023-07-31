package puri.feed.model;

import java.time.LocalDateTime;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.Data;


@Data
public class UserModel {
	private int id;
	private String userid;
	private String username;
	private String password;
	private String email;
	@Enumerated(EnumType.ORDINAL)
	private UserType role;  
	private LocalDateTime create_date;
	// private LocalDateTime registDatetime;
	// private LocalDateTime updateDatetime;
}
