package puri.feed.service.impl;

import java.util.*;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import puri.feed.entity.RoleType;
import puri.feed.entity.User;
import puri.feed.mapper.UserMapper;
import puri.feed.model.UserModel;
import puri.feed.service.UserService;
import puri.feed.repository.*;

@Log4j2
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

	private final UserMapper userMapper;
	private final UserRepository userRepository;

	private void validateDuplicateUser(User user) {
        userRepository.findByUserid(user.getUserid()).ifPresent(u ->{
            throw new IllegalArgumentException("이미 존재하는 회원입니다");
        });
    }

	@Override
	public List<UserModel> getUser() {
		return userMapper.getUser();
	}   

	@Override
	public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public User UserDetailByUserId(String userid) {
        return userRepository.findByUserid(userid).orElseThrow(()-> {
            return new IllegalArgumentException("해당 ID 에 해당하는 사용자를 찾을 수 없습니다 : " + userid);
        });
    }
	
	@Transactional
	public Long join(User user) {
        validateDuplicateUser(user);

        try {
            user.setRole(RoleType.USER);
            userRepository.save(user);
            return 1L;
        } catch(Exception e) {
            e.printStackTrace();
        }

		return -1L;
	}

    @Transactional(readOnly=true)
    public User login(User user){

        User nullUser = userRepository.login(user.getUserid(), user.getPassword());
        return nullUser;
    }

    @Transactional
    public Long updateUser(Long id, User RequestUser) {

        try{
            User user = userRepository.findById(id).orElseThrow(() -> {
                        throw new IllegalArgumentException("수정 오류");
                    }
            );
            user.setUserid(RequestUser.getUserid());
            user.setUsername(RequestUser.getUsername());
            user.setPassword(RequestUser.getPassword());
            user.setEmail(RequestUser.getEmail());
            return 1L;
        } catch(Exception e) {
            e.printStackTrace();
            return -1L;
        }
    }

    @Transactional
    public void deleteUser(String userid) {
        try {
            userRepository.deleteByUserid(userid);
        } catch (EmptyResultDataAccessException e) {
            System.out.println(e.toString());
        }
    }
}
