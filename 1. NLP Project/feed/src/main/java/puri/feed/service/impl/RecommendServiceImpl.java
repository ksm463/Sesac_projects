package puri.feed.service.impl;


import org.springframework.stereotype.Service;

import puri.feed.service.RecommendService;

@Service
public class RecommendServiceImpl implements RecommendService{


	@Override
	public String userDemand() {
		return "userDemand";
	}    
}
