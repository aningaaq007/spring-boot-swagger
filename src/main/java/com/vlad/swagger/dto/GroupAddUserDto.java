package com.vlad.swagger.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupAddUserDto {

    @JsonProperty("group_id")
    private Integer id;

    @JsonProperty("user_id")
    private Integer userId;

}
