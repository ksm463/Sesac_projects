package puri.feed.service;


import puri.feed.dto.*;

public interface ChatService {
	ChatDto send(ChatDto RequestChat, String uid) throws Exception;
}
