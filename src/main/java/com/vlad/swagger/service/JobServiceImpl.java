package com.vlad.swagger.service;

import com.vlad.swagger.dto.JobAddUserDto;
import com.vlad.swagger.dto.JobDto;
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

import java.sql.Date;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@Transactional
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<JobDto> getAllJobs() {
        return jobRepository.findAll()
                .stream()
                .map(this::convertEntityToDto)
                .sorted(Comparator.comparing(JobDto::getId))
                .collect(Collectors.toList());
    }

    @Override
    public String add(JobAddUserDto dto) {

        if (dto != null && dto.getName() != null && !dto.getName().isEmpty() && dto.getUserId() != null) {

            boolean checkJob = jobRepository.findAll()
                    .stream()
                    .anyMatch(u -> u.getName().equalsIgnoreCase(dto.getName()));

            if (!checkJob) {

                Optional<User> userOptional = userRepository.findById(dto.getUserId());

                if (userOptional.isPresent()) {

                    User user = userOptional.get();

                    Job job = new Job();
                    job.setName(dto.getName());
                    job.setDate(new Date(Calendar.getInstance().getTime().getTime()));
                    job.setDateEmployment(new Date(Calendar.getInstance().getTime().getTime()));
                    jobRepository.saveAndFlush(job);

                    user.addJob(job);
                    userRepository.saveAndFlush(user);

                    if (job.getId() != null)
                        return job.getId().toString();
                    else
                        return "OK";

                } else {
                    return "NOT_FOUND_USER";
                }

            } else {
                return "CONFLICT";
            }

        } else {
            return "EMPTY";
        }

    }

    @Override
    public JobDto convertEntityToDto(Job job) {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        JobDto jobLocationDTO;
        jobLocationDTO = modelMapper.map(job, JobDto.class);
        return jobLocationDTO;
    }

}
