package com.vlad.swagger.dto;

import com.vlad.swagger.entity.Group;
import com.vlad.swagger.entity.Job;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto extends UserSwaggerDto{

    private Integer id;
    private List<Group> groups;
    private List<Job>jobs;
}
