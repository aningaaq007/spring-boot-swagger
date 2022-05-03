package com.vlad.swagger.controller;

import com.vlad.swagger.dto.GroupAddUserDto;
import com.vlad.swagger.dto.GroupDto;
import com.vlad.swagger.dto.JobAddUserDto;
import com.vlad.swagger.dto.JobDto;
import com.vlad.swagger.service.JobService;
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
public class JobControllerTest {

    @MockBean
    private JobService jobService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void allJobsTest() throws Exception {
        JobDto job01 = new JobDto();
        job01.setId(1);
        job01.setName("Job01");
        job01.setDate(new Date(Calendar.getInstance().getTime().getTime()));

        JobDto job02 = new JobDto();
        job02.setId(2);
        job02.setName("Job02");
        job02.setDate(new Date(Calendar.getInstance().getTime().getTime()));


        when(jobService.getAllJobs()).thenReturn(Arrays.asList(job01, job02));

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/job/all")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk());

        verify(jobService, times(1)).getAllJobs();
        verifyNoMoreInteractions(jobService);
    }

    @Test
    public void addSuccessTest() throws Exception {

        when(jobService.add(any(JobAddUserDto.class))).thenReturn("1");

        mockMvc.perform(post("/api/job/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"job_name\": \"Job01\", \"user_id\": 1 }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void addFailTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/job/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"job_name\": \"Job01\", \"user_id\": 1 }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(false))
                .andDo(print());
    }

    @Test
    public void addFailEmptyTest() throws Exception {

        when(jobService.add(any(JobAddUserDto.class))).thenReturn("EMPTY");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/job/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"job_name\": \"Job01\", \"user_id\": 1 }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("errorCode").value(400))
                .andDo(print());
    }

    @Test
    public void addFailNotFoundUserTest() throws Exception {

        when(jobService.add(any(JobAddUserDto.class))).thenReturn("NOT_FOUND_USER");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/job/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"job_name\": \"Job01\", \"user_id\": 1 }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("errorCode").value(404));
    }

    @Test
    public void addFailConflictTest() throws Exception {

        when(jobService.add(any(JobAddUserDto.class))).thenReturn("CONFLICT");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/job/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"job_name\": \"Job01\", \"user_id\": 1 }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("errorCode").value(409))
                .andDo(print());
    }

}
