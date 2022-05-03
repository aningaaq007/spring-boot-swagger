package com.vlad.swagger.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vlad.swagger.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobDto {

    private Integer id;
    private String name;

    @JsonProperty("date_creation")
    private Date date;

    private User user;

}
