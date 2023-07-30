package puri.feed.dto;

import lombok.Data;

@Data
public class ChatDto {
	private Integer ctype; // 0 user 1 server
	private String cnum; // 메세지 넘버
	private String cid; // 채팅 user  id
	private String cmsg; // 채팅 질의    
}
