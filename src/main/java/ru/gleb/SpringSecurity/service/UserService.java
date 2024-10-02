package ru.gleb.SpringSecurity.service;

import org.springframework.stereotype.Service;
import ru.gleb.SpringSecurity.dto.UserDto;
import ru.gleb.SpringSecurity.entity.User;

import java.util.List;

@Service
public interface UserService {

    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();
}
