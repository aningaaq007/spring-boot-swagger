package com.vlad.swagger.service;

import com.vlad.swagger.dto.UserDto;
import com.vlad.swagger.entity.User;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();

    String add(UserDto dto);

    String del(Integer id);

    String update(Integer id, UserDto dto);

    UserDto convertEntityToDto(User user);

    User convertDtoToEntity(UserDto userLocationDTO);

}
