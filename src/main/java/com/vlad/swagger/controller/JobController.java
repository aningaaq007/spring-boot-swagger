package com.vlad.swagger.controller;

import com.vlad.swagger.dto.*;
import com.vlad.swagger.service.JobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "Работа", description = " ")
@Controller
@Slf4j
@RequestMapping("/api/job")
public class JobController {

    @Autowired
    private JobService jobService;


    @ApiOperation(value = "Список работ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Полный список работ с параметрами")
    })
    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = { "application/json"})
    @ResponseBody
    public ResponseEntity<List<JobDto>> allJobs() {

        List<JobDto> res = jobService.getAllJobs();

        return new ResponseEntity<>(res, HttpStatus.OK);
    }


    @ApiOperation(value = "Создать работу и назначить исполнителя")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Работа создана"),
            @ApiResponse(code = 400, message = "Ошибки валидации запроса (незаполненные параметры)"),
            @ApiResponse(code = 404, message = "Пользователь не найден"),
            @ApiResponse(code = 409, message = "Работа с таким названием уже существует")
    })
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = { "application/json"})
    public ResponseEntity<ActionDto> add(@RequestBody
                                         @Parameter(description = "Запрос для сохранения") JobAddUserDto dto) {
        try {

            String newId = jobService.add(dto);

            switch (newId) {
                case "EMPTY":
                    return new ResponseEntity<>( ActionDto.builder()
                            .success(false)
                            .errorCode("400")
                            .errorMessage("Незаполненные параметры!")
                            .build(), HttpStatus.BAD_REQUEST);
                case "NOT_FOUND_USER":
                    return new ResponseEntity<>( ActionDto.builder()
                            .success(false)
                            .errorCode("404")
                            .errorMessage("Пользователь не найден!")
                            .build(), HttpStatus.NOT_FOUND);
                case "CONFLICT":
                    return new ResponseEntity<>( ActionDto.builder()
                            .success(false)
                            .errorCode("409")
                            .errorMessage("Работа с таким названием уже существует!")
                            .build(),HttpStatus.CONFLICT);
                default:
                    break;
            }

            return new ResponseEntity<>( ActionDto.builder()
                    .success(true)
                    .metadata(MetadataDto.builder().artifactId(newId).build())
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return new ResponseEntity<>( ActionDto.builder()
                    .success(false)
                    .errorMessage(e.getMessage())
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
