package com.vlad.swagger.service;

import com.vlad.swagger.dto.GroupAddUserDto;
import com.vlad.swagger.dto.GroupDto;
import com.vlad.swagger.entity.Group;

import java.util.List;

public interface GroupService {

    List<GroupDto> getAllGroups();

    String add(GroupDto dto);

    String addUser(GroupAddUserDto dto);

    String del(Integer id);

    GroupDto convertEntityToDto(Group group);

    Group convertDtoToEntity(GroupDto groupLocationDTO);

}
