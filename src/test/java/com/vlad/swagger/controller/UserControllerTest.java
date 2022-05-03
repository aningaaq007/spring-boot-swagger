package com.vlad.swagger.controller;

import com.vlad.swagger.dto.GroupAddUserDto;
import com.vlad.swagger.dto.GroupDto;
import com.vlad.swagger.dto.UserDto;
import com.vlad.swagger.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Date;
import java.util.Arrays;
import java.util.Calendar;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void allUsersTest() throws Exception {
        UserDto user01 = new UserDto();
        user01.setId(1);
        user01.setName("User01");

        UserDto user02 = new UserDto();
        user02.setId(2);
        user02.setName("User02");

        when(userService.getAllUsers()).thenReturn(Arrays.asList(user01, user02));

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/user/all")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk());

        verify(userService, times(1)).getAllUsers();
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void addSuccessTest() throws Exception {

        when(userService.add(any(UserDto.class))).thenReturn("1");

        mockMvc.perform(post("/api/user/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"User01\", \"age\": 18 }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void addFailTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"User01\", \"age\": 18 }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(false))
                .andDo(print());
    }

    @Test
    public void addFailEmptyTest() throws Exception {

        when(userService.add(any(UserDto.class))).thenReturn("EMPTY");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"User01\", \"age\": 18 }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("errorCode").value(400))
                .andDo(print());
    }

    @Test
    public void addFailConflictTest() throws Exception {

        when(userService.add(any(UserDto.class))).thenReturn("CONFLICT");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"User01\", \"age\": 18 }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("errorCode").value(409))
                .andDo(print());
    }

    @Test
    public void updateSuccessTest() throws Exception {

        when(userService.update(any(Integer.class),any(UserDto.class))).thenReturn("OK");

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/user/patch/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\" : \"User01\" }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updateFailTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/user/patch/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\" : \"User01\" }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(false));
    }

    @Test
    public void updateFailEmptyTest() throws Exception {

        when(userService.update(any(Integer.class),any(UserDto.class))).thenReturn("EMPTY");

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/user/patch/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\" : \"User01\" }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("errorCode").value(400));
    }

    @Test
    public void updateFailEmptyNameTest() throws Exception {

        when(userService.update(any(Integer.class),any(UserDto.class))).thenReturn("EMPTY_NAME");

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/user/patch/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\" : \"User01\" }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("errorCode").value(400));
    }

    @Test
    public void updateFailNotFoundTest() throws Exception {

        when(userService.update(any(Integer.class),any(UserDto.class))).thenReturn("NOT_FOUND");

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/user/patch/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\" : \"User01\" }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("errorCode").value(404));
    }

    @Test
    public void updateFailConflictTest() throws Exception {

        when(userService.update(any(Integer.class),any(UserDto.class))).thenReturn("CONFLICT");

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/user/patch/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\" : \"User01\" }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("errorCode").value(409));
    }

    @Test
    public void delSuccessTest() throws Exception {

        when(userService.del(any(Integer.class))).thenReturn("OK");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/delete/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void delFailTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/delete/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(false));
    }

    @Test
    public void delFailEmptyTest() throws Exception {

        when(userService.del(any(Integer.class))).thenReturn("EMPTY");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/delete/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("errorCode").value(400));
    }

    @Test
    public void delFailNotFoundTest() throws Exception {

        when(userService.del(any(Integer.class))).thenReturn("NOT_FOUND");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/delete/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("errorCode").value(404));
    }
}
