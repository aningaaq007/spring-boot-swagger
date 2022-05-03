package com.vlad.swagger.service;

import com.vlad.swagger.dto.UserDto;
import com.vlad.swagger.entity.Job;
import com.vlad.swagger.entity.User;
import com.vlad.swagger.repository.JobRepository;
import com.vlad.swagger.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertEntityToDto)
                .sorted(Comparator.comparing(UserDto::getId))
                .collect(Collectors.toList());
    }

    @Override
    public String add(UserDto dto) {

        if (dto != null && dto.getName() != null && !dto.getName().isEmpty() && dto.getAge() != null) {

            boolean checkUser = userRepository.findAll()
                    .stream()
                    .anyMatch(u -> u.getName().equalsIgnoreCase(dto.getName()));

            if (!checkUser) {
                User newUser = convertDtoToEntity(dto);
                userRepository.saveAndFlush(newUser);

                return newUser.getId().toString();
            } else {
                return "CONFLICT";
            }

        } else {
            return "EMPTY";
        }
    }

    @Override
    public String update(Integer id, UserDto dto) {

        if (id != null) {

            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isPresent()) {

                User oldUser = userOptional.get();
                User newUser = convertDtoToEntity(dto);

                if (newUser.getName() != null) {

                    if (newUser.getName().isEmpty())
                        return "EMPTY_NAME";

                    boolean checkUser = userRepository.findAll()
                            .stream()
                            .anyMatch(u -> u.getName().equalsIgnoreCase(newUser.getName()));

                    if (!checkUser) {
                        oldUser.setName(newUser.getName());
                    } else {
                        return "CONFLICT";
                    }
                }

                if (newUser.getAge() != null)
                    oldUser.setAge(newUser.getAge());

                userRepository.saveAndFlush(oldUser);
            } else {
                return "NOT_FOUND";
            }
        } else {
            return "EMPTY";
        }

        return "OK";
    }

    @Override
    public String del(Integer id) {

        if (id != null) {
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isPresent()) {
                User user = userOptional.get();

                List<Job> jobs = user.getJobs().stream()
                        .filter(u -> jobRepository.findById(u.getId()).isPresent())
                        .collect(Collectors.toCollection(ArrayList::new));
                jobs.forEach(job -> {
                    job.removeUser();
                    jobRepository.saveAndFlush(job);
                    user.removeJob(job);
                });
                userRepository.saveAndFlush(user);
                userRepository.delete(user);
            } else {
                return "NOT_FOUND";
            }
        } else {
            return "EMPTY";
        }
        return "OK";
    }

    @Override
    public UserDto convertEntityToDto(User user) {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        UserDto userLocationDTO;
        userLocationDTO = modelMapper.map(user, UserDto.class);
        return userLocationDTO;
    }

    @Override
    public User convertDtoToEntity(UserDto userLocationDTO) {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        User user;
        user = modelMapper.map(userLocationDTO, User.class);
        return user;
    }

}
