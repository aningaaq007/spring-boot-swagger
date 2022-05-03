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
public class JobAddUserDto {

    @JsonProperty("job_name")
    private String name;

    @JsonProperty("user_id")
    private Integer userId;

}
