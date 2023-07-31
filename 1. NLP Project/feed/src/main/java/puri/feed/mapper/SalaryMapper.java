package puri.feed.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.*;
import puri.feed.model.SalaryModel;

@Repository
@Mapper
public interface SalaryMapper {
	public List<SalaryModel> getSalary();    
}
