package com.vlad.swagger.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vlad.swagger.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Date;
import java.util.List;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class GroupDto extends GroupSwaggerDto {

    private Integer id;

    @JsonProperty("date_of_formation")
    private Date date;

    private List<User> users;

}
