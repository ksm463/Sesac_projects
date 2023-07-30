package puri.feed.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import org.json.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import puri.feed.model.ChatModel;


@Controller
@RequiredArgsConstructor
public class ChatController {
	String returnMsg = "";

	@GetMapping("chat")
	public String chat(Model model) {
		return "content/chat";
	}    

    @PostMapping("chat")
    public String chat(ChatModel chat, Model model) throws Exception{
        System.out.println("[debug] chat : " + chat);

		RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("msg_num", 102);
        jsonObject.put("user_id", 1);
        jsonObject.put("user_msg", chat.getCmsg());


        String restRequestUrl  = "http://3.134.63.248:5000/chat";
        // String restRequestUrl  = "http://127.0.0.1:5000/chat";
        HttpEntity<String> chatRequest = new HttpEntity<>(jsonObject.toString(), httpHeaders);

        ResponseEntity<String> responseEntity = 
            restTemplate.exchange(restRequestUrl , HttpMethod.POST, chatRequest, String.class);  

        String result = "";
        System.out.println(responseEntity.getStatusCode());
        ObjectMapper objectMapper = new ObjectMapper();
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // ObjectMapper mapper = new ObjectMapper();
            // SuggestDto.response resp = mapper.readValue(responseEntity.getBody(), SuggestDto.response.class);
            String resp = responseEntity.getBody();
            System.out.println("[debug] chat resp : " + resp);
            JsonNode root = objectMapper.readTree(resp);
            String resText = "";

            resText = root.get("message").asText();

            result = resText;
        }

        model.addAttribute("Result", result);
        System.out.println("[debug] Result : " + result);   
        
        // return "redirect:/chat";
        return chat(model);
    }

    @GetMapping("emotion")
	public String emotion(Model model) {
		return "content/emotion";
	}    

    @PostMapping("emotion")
    public String emotion(ChatModel chat, Model model) throws Exception{
        System.out.println("[debug] chat : " + chat);

		RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("msg_num", 102);
        jsonObject.put("user_id", 1);
        jsonObject.put("user_msg", chat.getCmsg());


        String restRequestUrl  = "http://3.134.63.248:5000/emotion";
        //  String restRequestUrl  = "http://127.0.0.1:5000/emotion";
        HttpEntity<String> chatRequest = new HttpEntity<>(jsonObject.toString(), httpHeaders);

        ResponseEntity<String> responseEntity = 
            restTemplate.exchange(restRequestUrl , HttpMethod.POST, chatRequest, String.class);  

        String result = "";
        System.out.println(responseEntity.getStatusCode());
        ObjectMapper objectMapper = new ObjectMapper();
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // ObjectMapper mapper = new ObjectMapper();
            // SuggestDto.response resp = mapper.readValue(responseEntity.getBody(), SuggestDto.response.class);
            String resp = responseEntity.getBody();
            System.out.println("[debug] chat resp : " + resp);
            JsonNode root = objectMapper.readTree(resp);
            String resText = "";

            resText = root.get("message").asText();

            result = resText;
        }

        model.addAttribute("Result", result);
        System.out.println("[debug] Result : " + result);   
        
        // return "redirect:/chat";
        return emotion(model);
    }

    @GetMapping("chatbot")
	public String bot(Model model) {
		return "chatbot";
	}    

    @ResponseBody
    @PostMapping("chatbot")
    public String bot(@RequestParam(required = false, 
    value = "arr[]") List<String> arr) {

        return null;
    }
}
