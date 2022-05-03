package com.vlad.swagger.service;

import com.vlad.swagger.dto.JobAddUserDto;
import com.vlad.swagger.dto.JobDto;
import com.vlad.swagger.entity.Job;
import com.vlad.swagger.entity.User;
import com.vlad.swagger.repository.JobRepository;
import com.vlad.swagger.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JobServiceTest {

    @MockBean
    private JobRepository jobRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    JobService jobService;

    @BeforeEach
    public void setUp() {

        Job job1 = new Job();
        job1.setId(1);
        job1.setName("Job01");
        job1.setDate(new Date(Calendar.getInstance().getTime().getTime()));

        Job job2 = new Job();
        job2.setId(1);
        job2.setName("Job02");
        job2.setDate(new Date(Calendar.getInstance().getTime().getTime()));

        List<Job> jobs = new ArrayList<>();
        jobs.add(job1);
        jobs.add(job2);

        Mockito.when(jobRepository.findAll()).thenReturn(jobs);

        User user1 = new User();
        user1.setId(1);
        user1.setName("User01");
        user1.setAge(27);

        Mockito.when(userRepository.
                findById(user1.getId())).thenReturn(java.util.Optional.of(user1));

    }

    @Test
    public void getAllGroupsTest() {

        List<JobDto> list = jobService.getAllJobs();

        assertThat(jobService.getAllJobs()).isEqualTo(list);
    }

    @Test
    public void addSuccessTest() {
        JobAddUserDto dto = new JobAddUserDto();
        dto.setName("Job03");
        dto.setUserId(1);

        String newId = jobService.add(dto);
        assertThat(newId).isNotNull();
    }

    @Test
    public void addFailEmptyTest() {
        JobAddUserDto dto = null;

        assertThat(jobService.add(dto)).isEqualTo("EMPTY");
    }

    @Test
    public void addFailNotFoundUserTest() {
        JobAddUserDto dto = new JobAddUserDto();
        dto.setName("Job03");
        dto.setUserId(99);

        assertThat(jobService.add(dto)).isEqualTo("NOT_FOUND_USER");
    }

    @Test
    public void addFailConflictTest() {
        JobAddUserDto dto = new JobAddUserDto();
        dto.setName("Job01");
        dto.setUserId(1);

        assertThat(jobService.add(dto)).isEqualTo("CONFLICT");
    }
}
