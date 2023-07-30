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

import org.json.*;
import org.json.simple.parser.JSONParser;

import lombok.RequiredArgsConstructor;
import puri.feed.service.RecommendService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import puri.feed.model.RecommendModel;

@Controller
@RequiredArgsConstructor
public class RecommendController {
	String returnMsg = "";

	private final RecommendService recommendService;

	@GetMapping("recommend")
	public String recommend(Model model) {
		return "content/recommend";
	}    

    @PostMapping("recommend")
    public String recommend(RecommendModel recommend, Model model) throws Exception{
        System.out.println("[debug] recommend : " + recommend);

		RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("msg_num", 101);
        jsonObject.put("user_id", 1);
        jsonObject.put("mng_demand", recommend.getRcmMngDemand());
        jsonObject.put("light_demand", recommend.getRcmLightDemand());
        jsonObject.put("op_temp", recommend.getRcmOpTemp());
        jsonObject.put("win_min_temp", recommend.getRcmWinTemp());
        jsonObject.put("humdity", recommend.getRcmHumdity());
        jsonObject.put("watering_spring", recommend.getRcmWatering());

        String restRequestUrl  = "http://3.134.63.248:5000/recommend";
        // String restRequestUrl  = "http://127.0.0.1:5000/recommend";
        HttpEntity<String> rcmRequest = new HttpEntity<>(jsonObject.toString(), httpHeaders);

        ResponseEntity<String> responseEntity = 
            restTemplate.exchange(restRequestUrl , HttpMethod.POST, rcmRequest, String.class);  

        String result = "";
        System.out.println(responseEntity.getStatusCode());
        ObjectMapper objectMapper = new ObjectMapper();
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // ObjectMapper mapper = new ObjectMapper();
            // SuggestDto.response resp = mapper.readValue(responseEntity.getBody(), SuggestDto.response.class);
            String resp = responseEntity.getBody();
            System.out.println("[debug] recommendations : " + resp);
            JsonNode root = objectMapper.readTree(resp);

            // for( Iterator<JsonNode> i = root.get( "sugList" ).iterator(); i.hasNext(); )
            //     if( i.next().has( "path" ) )
            //         result += i.toString();

            int count = root.get("recommendations").size();
            String sugText = "";
            int i = 0;
            for (i = 0 ; i < count ; i++){
                sugText = sugText + root.get("recommendations").get(i).get("sNo").asText() + ", ";
                sugText = sugText + root.get("recommendations").get(i).get("sNum").asText() + ", ";
                sugText = sugText + root.get("recommendations").get(i).get("sName").asText() + " | \n";
            }
            
            result = sugText;
        }

        model.addAttribute("rcmResult", result);
        System.out.println("[debug] rcmResult : " + result);   
        // return "redirect:/recommend";
        return recommend(model);
    }
}
