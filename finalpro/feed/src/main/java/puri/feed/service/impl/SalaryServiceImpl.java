package puri.feed.service.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puri.feed.mapper.SalaryMapper;
import puri.feed.model.SalaryModel;
import puri.feed.service.SalaryService;


@Service
public class SalaryServiceImpl implements SalaryService{

	@Autowired
	private SalaryMapper mapper;
	
	public List<SalaryModel> getSalary() {
		return mapper.getSalary();
	}   

}
