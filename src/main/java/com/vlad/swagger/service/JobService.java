package com.vlad.swagger.service;

import com.vlad.swagger.dto.JobAddUserDto;
import com.vlad.swagger.dto.JobDto;
import com.vlad.swagger.entity.Job;

import java.util.List;

public interface JobService {

    List<JobDto> getAllJobs();

    String add(JobAddUserDto dto);

    JobDto convertEntityToDto(Job job);

}
