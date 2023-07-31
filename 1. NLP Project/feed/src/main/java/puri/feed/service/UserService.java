package puri.feed.service;

import java.util.List;
import java.util.Optional;

import puri.feed.model.UserModel;
import puri.feed.entity.*;

public interface UserService {
	User UserDetailByUserId(String userid);
	Long updateUser(Long id, User RequestUser);
	void deleteUser(String userid);
	List<UserModel> getUser(); 
	Optional<User> findByUsername(String username); 
	Long join(User user);
	User login(User user);
}
