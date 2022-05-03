package com.vlad.swagger.service;

import com.vlad.swagger.dto.GroupAddUserDto;
import com.vlad.swagger.dto.GroupDto;
import com.vlad.swagger.entity.Group;
import com.vlad.swagger.entity.User;
import com.vlad.swagger.repository.GroupRepository;
import com.vlad.swagger.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@Transactional
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<GroupDto> getAllGroups() {
        return groupRepository.findAll()
                .stream()
                .map(this::convertEntityToDto)
                .sorted(Comparator.comparing(GroupDto::getId))
                .collect(Collectors.toList());
    }

    @Override
    public String add(GroupDto dto) {

        if (dto != null && dto.getName() != null && !dto.getName().isEmpty()) {

            boolean checkGroup = groupRepository.findAll()
                    .stream()
                    .anyMatch(u -> u.getName().equalsIgnoreCase(dto.getName()));

            if (!checkGroup) {
                Group newGroup = convertDtoToEntity(dto);

                groupRepository.saveAndFlush(newGroup);

                return newGroup.getId().toString();
            } else {
                return "CONFLICT";
            }

        } else {
            return "EMPTY";
        }
    }

    @Override
    public String addUser(GroupAddUserDto dto) {

        if (dto != null && dto.getId() != null && dto.getUserId() != null) {

            Optional<Group> groupOptional = groupRepository.findById(dto.getId());
            if (groupOptional.isPresent()) {

                Optional<User> userOptional = userRepository.findById(dto.getUserId());

                if (userOptional.isPresent()) {

                    User user = userOptional.get();
                    Group group = groupOptional.get();

                    boolean checkUser = group.getUsers()
                            .stream()
                            .anyMatch(u -> u.getId().equals(user.getId()));

                    if (!checkUser) {
                        user.addGroup(group);
                        userRepository.saveAndFlush(user);
                    } else {
                        return "CONFLICT";
                    }
                } else {
                    return "NOT_FOUND_USER";
                }
            } else {
                return "NOT_FOUND_GROUP";
            }
        } else {
            return "EMPTY";
        }

        return "OK";
    }

    @Override
    public String del(Integer id) {

        if (id != null) {
            Optional<Group> groupOptional = groupRepository.findById(id);

            if (groupOptional.isPresent()) {
                Group group = groupOptional.get();

                List<User> users = group.getUsers().stream()
                        .filter(u -> userRepository.findById(u.getId()).isPresent())
                        .collect(Collectors.toCollection(ArrayList::new));
                users.forEach(user -> {
                    user.removeGroup(group);
                    userRepository.saveAndFlush(user);
                });
                groupRepository.delete(group);
            } else {
                return "NOT_FOUND";
            }
        } else {
            return "EMPTY";
        }
        return "OK";
    }

    @Override
    public GroupDto convertEntityToDto(Group group) {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        GroupDto groupLocationDTO;
        groupLocationDTO = modelMapper.map(group, GroupDto.class);
        return groupLocationDTO;
    }

    @Override
    public Group convertDtoToEntity(GroupDto groupLocationDTO) {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        Group group;
        group = modelMapper.map(groupLocationDTO, Group.class);
        return group;
    }
}
