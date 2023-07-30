package puri.feed.model;

import lombok.Data;

@Data
public class RecommendModel {
	private int rcmMngDemand; // 관리요구
	private int rcmLightDemand; // 광요구
	private int rcmOpTemp; // 생육온도
    private int rcmWinTemp; // 겨울최저온도
    private int rcmHumdity; // 습도
    private int rcmWatering; // 물주기      
}
