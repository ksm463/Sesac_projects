
package puri.feed.dto;

import lombok.Data;


@Data
public class RecommendDto {

    @Data
    public static class request {
        private int msgNum;
        private int userId;
        private int rcmMngDemand;
        private int rcmLightDemand;
        private int rcmOpTemp;
        private int rcmWinTemp;
        private int rcmHumdity;
        private int rcmWatering;
    }

    @Data
    public static class response {
        private int     sNo;
        private int     sNum;
        private String  SName;
    }
}