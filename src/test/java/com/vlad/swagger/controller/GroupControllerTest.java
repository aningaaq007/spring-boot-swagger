package com.vlad.swagger.controller;

import com.vlad.swagger.dto.GroupAddUserDto;
import com.vlad.swagger.dto.GroupDto;
import com.vlad.swagger.service.GroupService;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class GroupControllerTest {

    @MockBean
    private GroupService groupService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void allGroupsTest() throws Exception {
        GroupDto group01 = new GroupDto();
        group01.setId(1);
        group01.setName("Group01");
        group01.setDate(new Date(Calendar.getInstance().getTime().getTime()));

        GroupDto group02 = new GroupDto();
        group02.setId(2);
        group02.setName("Group02");
        group02.setDate(new Date(Calendar.getInstance().getTime().getTime()));

        when(groupService.getAllGroups()).thenReturn(Arrays.asList(group01, group02));

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/group/all")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk());

        verify(groupService, times(1)).getAllGroups();
        verifyNoMoreInteractions(groupService);
    }

    @Test
    public void addSuccessTest() throws Exception {

        when(groupService.add(any(GroupDto.class))).thenReturn("1");

        mockMvc.perform(post("/api/group/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"Group01\" }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void addFailTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/group/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\" : \"Group01\" }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(false))
                .andDo(print());
    }

    @Test
    public void addFailEmptyTest() throws Exception {

        when(groupService.add(any(GroupDto.class))).thenReturn("EMPTY");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/group/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\" : \"Group01\" }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("errorCode").value(400))
                .andDo(print());
    }

    @Test
    public void addFailConflictTest() throws Exception {

        when(groupService.add(any(GroupDto.class))).thenReturn("CONFLICT");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/group/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\" : \"Group01\" }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("errorCode").value(409))
                .andDo(print());
    }

    @Test
    public void updateSuccessTest() throws Exception {

        when(groupService.addUser(any(GroupAddUserDto.class))).thenReturn("OK");

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/group/patch")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\" : \"Group01\" }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updateFailTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/group/patch")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\" : \"Group01\" }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(false));
    }

    @Test
    public void updateFailEmptyTest() throws Exception {

        when(groupService.addUser(any(GroupAddUserDto.class))).thenReturn("EMPTY");

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/group/patch")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\" : \"Group01\" }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("errorCode").value(400));
    }

    @Test
    public void updateFailNotFoundGroupTest() throws Exception {

        when(groupService.addUser(any(GroupAddUserDto.class))).thenReturn("NOT_FOUND_GROUP");

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/group/patch")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\" : \"Group01\" }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("errorCode").value(404));
    }

    @Test
    public void updateFailNotFoundUserTest() throws Exception {

        when(groupService.addUser(any(GroupAddUserDto.class))).thenReturn("NOT_FOUND_USER");

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/group/patch")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\" : \"Group01\" }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("errorCode").value(404));
    }

    @Test
    public void updateFailConflictTest() throws Exception {

        when(groupService.addUser(any(GroupAddUserDto.class))).thenReturn("CONFLICT");

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/group/patch")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\" : \"Group01\" }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("errorCode").value(409));
    }

    @Test
    public void delSuccessTest() throws Exception {

        when(groupService.del(any(Integer.class))).thenReturn("OK");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/group/delete/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void delFailTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/group/delete/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(false));
    }

    @Test
    public void delFailEmptyTest() throws Exception {

        when(groupService.del(any(Integer.class))).thenReturn("EMPTY");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/group/delete/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("errorCode").value(400));
    }

    @Test
    public void delFailNotFoundTest() throws Exception {

        when(groupService.del(any(Integer.class))).thenReturn("NOT_FOUND");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/group/delete/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("errorCode").value(404));
    }

}
