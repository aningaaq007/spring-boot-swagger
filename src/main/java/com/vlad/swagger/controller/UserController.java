package com.vlad.swagger.controller;

import com.vlad.swagger.dto.ActionDto;
import com.vlad.swagger.dto.MetadataDto;
import com.vlad.swagger.dto.UserDto;
import com.vlad.swagger.dto.UserSwaggerDto;
import com.vlad.swagger.service.UserService;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "Пользователи", description = " ")
@Controller
@Slf4j
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;


    @ApiOperation(value = "Список пользователей")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Все пользователи с параметрами")
    })
    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = { "application/json"})
    @ResponseBody
    public ResponseEntity<List<UserDto>> allUsers() {

        List<UserDto> res = userService.getAllUsers();

        return new ResponseEntity<>(res, HttpStatus.OK);
    }


    @ApiOperation(value = "Создать пользователя")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Пользователь создан"),
            @ApiResponse(code = 400, message = "Ошибки валидации запроса (незаполненные параметры)"),
            @ApiResponse(code = 409, message = "Пользователь с таким именем уже существует")
    })
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = { "application/json"})
    public ResponseEntity<ActionDto> add(@RequestBody
                          @Parameter(description = "Запрос для сохранения") UserSwaggerDto dto) {
        try {
            UserDto newUser = new UserDto();
            newUser.setName(dto.getName());
            newUser.setAge(dto.getAge());

            String newId = userService.add(newUser);

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
                            .errorMessage("Пользователь с таким именем уже существует!")
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


    @ApiOperation(value = "Изменить данные пользователя")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Успешно изменен"),
            @ApiResponse(code = 400, message = "Не указан id или попытка стереть имя пользователю"),
            @ApiResponse(code = 404, message = "Пользователь не найден"),
            @ApiResponse(code = 409, message = "Пользователь с таким именем уже существует (если переименовываем, но данное имя занято другим пользователем )")
    })
    @RequestMapping(value = "/patch/{id}", method = RequestMethod.PATCH, produces = { "application/json"})
    public ResponseEntity<ActionDto> update(@RequestBody
                                                @Parameter(description = "Запрос для изменения. Можно делать с одним параметром.") UserSwaggerDto dto,
                                            @PathVariable @Parameter(description = "id пользователя") Integer id) {
        try {
            UserDto newUser = new UserDto();
            newUser.setName(dto.getName());
            newUser.setAge(dto.getAge());

            String status = userService.update(id, newUser);

            switch (status) {
                case "EMPTY":
                    return new ResponseEntity<>( ActionDto.builder()
                            .success(false)
                            .errorCode("400")
                            .errorMessage("Не указан id!")
                            .build(), HttpStatus.BAD_REQUEST);
                case "EMPTY_NAME":
                    return new ResponseEntity<>( ActionDto.builder()
                            .success(false)
                            .errorCode("400")
                            .errorMessage("Нельзя удалять имя пользователю!")
                            .build(), HttpStatus.BAD_REQUEST);
                case "NOT_FOUND":
                    return new ResponseEntity<>( ActionDto.builder()
                            .success(false)
                            .errorCode("404")
                            .errorMessage("Пользователь не найден!")
                            .build(), HttpStatus.NOT_FOUND);
                case "CONFLICT":
                    return new ResponseEntity<>( ActionDto.builder()
                            .success(false)
                            .errorCode("409")
                            .errorMessage("Пользователь с таким именем уже существует!")
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


    @ApiOperation(value = "Удалить пользователя")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Пользователь удалён"),
            @ApiResponse(code = 400, message = "Не указан id"),
            @ApiResponse(code = 404, message = "Пользователь не найден")
    })
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<ActionDto> del(
            @PathVariable @Parameter(description = "id пользователя") Integer id) {

        try {
            String status = userService.del(id);

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
                            .errorMessage("Пользователь не найден!")
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
