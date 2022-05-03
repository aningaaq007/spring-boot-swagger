package com.vlad.swagger.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class DefaultControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void redirectTest() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders
                .get("/")
                .accept(MediaType.TEXT_HTML);

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void showLoginFormTest() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders
                .get("/login")
                .accept(MediaType.TEXT_HTML);

        mockMvc.perform(request)
                .andExpect(status().isOk());
    }

}
