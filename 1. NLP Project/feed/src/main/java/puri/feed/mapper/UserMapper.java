package puri.feed.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.*;
import puri.feed.model.UserModel;

@Repository
@Mapper
public interface UserMapper {
	// @Select("SELECT * FROM user LIMIT 1")
	// String getUser();    
    public List<UserModel> getUser();     
}
