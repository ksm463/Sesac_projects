package puri.feed.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import puri.feed.service.SalaryService;
import puri.feed.model.*;
import java.util.*;


@RestController
@RequiredArgsConstructor
public class SalaryController {
    
	private final SalaryService salaryService;

	@GetMapping("salary")
	public ModelAndView goHome(HttpServletRequest request) { 
		ModelAndView mav = new ModelAndView();
		List<SalaryModel> salaryList = salaryService.getSalary();
		mav.addObject("salaryList", salaryList);
		mav.setViewName("content/salary.html"); // view 지정
		return mav;
	}    
}
