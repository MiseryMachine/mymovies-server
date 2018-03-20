package com.rjs.mymovies.server.service;

import com.rjs.mymovies.server.model.User;
import com.rjs.mymovies.server.model.dto.UserDto;
import com.rjs.mymovies.server.model.security.Role;
import com.rjs.mymovies.server.repos.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
public class UserService extends BaseService<User, UserRepository> {
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean userExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    @Transactional
    public User createUser(UserDto userDto) {
        User user = userRepository.findByUsername(userDto.getUsername());

        if (user == null) {
            user = new User();
            user.setUsername(userDto.getUsername());
            user.getRoles().add(Role.ROLE_USER);

            return saveUser(user, userDto);
        }

        // Can't create a user with existing username
        return null;
    }

    @Transactional
    public User updateUser(UserDto userDto) {
        User user = userRepository.findByUsername(userDto.getUsername());

        return user != null ? saveUser(user, userDto) : null;
    }

    public String encode(String value) {
        if (StringUtils.isNotBlank(value)) {
            return encoder.encode(value);
        }

        return "";
    }

    private User saveUser(User user, UserDto userDto) {
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        // Ensure that every user has a "User" role.
        user.getRoles().add(Role.ROLE_USER);

        String pw = userDto.getPassword();

        if (StringUtils.isNotBlank(pw)) {
            user.setPassword(encoder.encode(pw));
        }

        return save(user);
    }
}
