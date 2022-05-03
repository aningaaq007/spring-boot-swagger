package com.vlad.swagger.controller;

import com.vlad.swagger.dto.*;
import com.vlad.swagger.service.GroupService;
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

import java.sql.Date;
import java.util.Calendar;
import java.util.List;


@Api(tags = "Группы", description = " ")
@Controller
@Slf4j
@RequestMapping("/api/group")
public class GroupController {

    @Autowired
    private GroupService groupService;


    @ApiOperation(value = "Список групп")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Все группы с параметрами")
    })
    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = { "application/json"})
    @ResponseBody
    public ResponseEntity<List<GroupDto>> allGroups() {

        List<GroupDto> res = groupService.getAllGroups();

        return new ResponseEntity<>(res, HttpStatus.OK);
    }


    @ApiOperation(value = "Создать группу")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Группа создана"),
            @ApiResponse(code = 400, message = "Ошибки валидации запроса (незаполненные параметры)"),
            @ApiResponse(code = 409, message = "Группа с таким названием уже существует")
    })
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = { "application/json"})
    public ResponseEntity<ActionDto> add(@RequestBody
                                         @Parameter(description = "Запрос для сохранения") GroupSwaggerDto dto) {
        try {
            GroupDto newGroup = new GroupDto();
            newGroup.setName(dto.getName());
            newGroup.setDate(new Date(Calendar.getInstance().getTime().getTime()));

            String newId = groupService.add(newGroup);

            switch (newId) {
                case "EMPTY":
                    return new ResponseEntity<>( ActionDto.builder()
                            .success(false)
                            .errorCode("400")
                            .errorMessage("Незаполненные параметры!")
                            .build(), HttpStatus.BAD_REQUEST);
                case "CONFLICT":
                    return new ResponseEntity<>( ActionDto.builder()
                            .success(false)
                            .errorCode("409")
                            .errorMessage("Группа с таким именем уже существует!")
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


    @ApiOperation(value = "Добавить пользователя в группу")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно добавлен"),
            @ApiResponse(code = 400, message = "Незаполненные параметры"),
            @ApiResponse(code = 404, message = "Группа или пользователь не найден"),
            @ApiResponse(code = 409, message = "Пользователь уже есть в этой группе")
    })
    @RequestMapping(value = "/patch", method = RequestMethod.PATCH, produces = { "application/json"})
    public ResponseEntity<ActionDto> update(@RequestBody
                                            @Parameter(description = "Запрос для добавления") GroupAddUserDto dto) {
        try {

            String status = groupService.addUser(dto);

            switch (status) {
                case "EMPTY":
                    return new ResponseEntity<>( ActionDto.builder()
                            .success(false)
                            .errorCode("400")
                            .errorMessage("Незаполненные параметры!")
                            .build(), HttpStatus.BAD_REQUEST);
                case "NOT_FOUND_GROUP":
                    return new ResponseEntity<>( ActionDto.builder()
                            .success(false)
                            .errorCode("404")
                            .errorMessage("Группа не найдена!")
                            .build(), HttpStatus.NOT_FOUND);
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
                            .errorMessage("Пользователь уже есть в этой группе!")
                            .build(),HttpStatus.CONFLICT);
                default:
                    break;
            }

            return new ResponseEntity<>( ActionDto.builder()
                    .success(true)
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return new ResponseEntity<>( ActionDto.builder()
                    .success(false)
                    .errorMessage(e.getMessage())
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @ApiOperation(value = "Удалить группу")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Группа удалена"),
            @ApiResponse(code = 400, message = "Не указан id"),
            @ApiResponse(code = 404, message = "Группа не найдена")
    })
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<ActionDto> del(
            @PathVariable @Parameter(description = "id группы") Integer id) {

        try {
            String status = groupService.del(id);

            switch (status) {
                case "EMPTY":
                    return new ResponseEntity<>( ActionDto.builder()
                            .success(false)
                            .errorCode("400")
                            .errorMessage("Не указан id!")
                            .build(), HttpStatus.BAD_REQUEST);
                case "NOT_FOUND":
                    return new ResponseEntity<>( ActionDto.builder()
                            .success(false)
                            .errorCode("404")
                            .errorMessage("Группа не найдена!")
                            .build(), HttpStatus.NOT_FOUND);
                default:
                    break;
            }

            return new ResponseEntity( ActionDto.builder()
                    .success(true)
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return new ResponseEntity( ActionDto.builder()
                    .success(false)
                    .errorMessage(e.getMessage())
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
