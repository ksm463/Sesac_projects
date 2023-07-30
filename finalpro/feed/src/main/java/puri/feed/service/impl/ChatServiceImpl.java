package puri.feed.service.impl;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import puri.feed.dto.ChatDto;
import puri.feed.service.ChatService;
import org.json.*;

@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService{

	public ChatDto send(ChatDto requestChat, String uid) throws Exception {
        ChatDto responseChat = new ChatDto();
        
		RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        
        JSONObject jsonObject = new JSONObject();


        requestChat.setCid(uid);
        requestChat.setCnum("101");
        requestChat.setCtype(0); // 0 - 웹사용자 , 1- 챗봇서버

        jsonObject.put("cid", requestChat.getCid());
        jsonObject.put("cnum", requestChat.getCnum());
        jsonObject.put("ctype", requestChat.getCtype()); 
        jsonObject.put("cmsg", requestChat.getCmsg());


        String restRequestUrl  = "http://3.134.63.248:5000/chatbot";
        // String restRequestUrl  = "http://127.0.0.1:5000/chatbot";
        HttpEntity<String> chatRequest = new HttpEntity<>(jsonObject.toString(), httpHeaders);

        ResponseEntity<String> responseEntity = 
            restTemplate.exchange(restRequestUrl , HttpMethod.POST, chatRequest, String.class);  

        System.out.println(responseEntity.getStatusCode());
        ObjectMapper objectMapper = new ObjectMapper();
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // ObjectMapper mapper = new ObjectMapper();
            // SuggestDto.response resp = mapper.readValue(responseEntity.getBody(), SuggestDto.response.class);
            String resp = responseEntity.getBody();
            System.out.println("[debug] chat resp : " + resp);
            JsonNode root = objectMapper.readTree(resp);

            String cid = "";
            String cmsg = "";
            Integer ctype = 0;
            String cnum = "";

            cid = root.get("cid").asText();
            cmsg = root.get("cmsg").asText();
            ctype = root.get("ctype").asInt();
            cnum = root.get("cnum").asText();

            responseChat.setCid(cid);
            responseChat.setCmsg(cmsg);
            responseChat.setCtype(ctype);
            responseChat.setCnum(cnum);
        }


        return responseChat;
    }


}
