package com.vlad.swagger.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActionDto {

    private Boolean success;

    private String errorCode;
    private String errorMessage;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MetadataDto metadata;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean methodResultBool;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String redirectUrl;

}
