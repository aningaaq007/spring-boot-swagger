package com.vlad.swagger.service;

import com.vlad.swagger.dto.GroupAddUserDto;
import com.vlad.swagger.dto.GroupDto;
import com.vlad.swagger.entity.Group;
import com.vlad.swagger.entity.User;
import com.vlad.swagger.repository.GroupRepository;
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
public class GroupServiceTest {

    @MockBean
    private GroupRepository groupRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private GroupService groupService;

    @BeforeEach
    public void setUp() {

        Group group1 = new Group();
        group1.setId(1);
        group1.setName("Group01");
        group1.setDate(new Date(Calendar.getInstance().getTime().getTime()));

        Group group2 = new Group();
        group2.setId(2);
        group2.setName("Group02");
        group2.setDate(new Date(Calendar.getInstance().getTime().getTime()));

        Mockito.when(groupRepository.
                findByName(group1.getName())).thenReturn(group1);

        List<Group> groups = new ArrayList<>();
        groups.add(group1);
        groups.add(group2);

        Mockito.when(groupRepository.
                findAll()).thenReturn(groups);

        Mockito.when(groupRepository.
                findById(group1.getId())).thenReturn(java.util.Optional.of(group1));

        Mockito.when(groupRepository.
                findById(group2.getId())).thenReturn(java.util.Optional.of(group2));

        User user1 = new User();
        user1.setId(1);
        user1.setName("User01");
        user1.setAge(27);

        Mockito.when(userRepository.
                findById(user1.getId())).thenReturn(java.util.Optional.of(user1));

        User user2 = new User();
        user2.setId(2);
        user2.setName("User02");
        user2.setAge(35);
        user2.addGroup(group2);

        Mockito.when(userRepository.
                findById(user2.getId())).thenReturn(java.util.Optional.of(user2));

    }


    @Test
    public void getAllGroupsTest() {

        List<GroupDto> list = groupService.getAllGroups();

        assertThat(groupService.getAllGroups()).isEqualTo(list);
    }

    @Test
    public void addSuccessTest() {

        GroupDto dto = new GroupDto();
        dto.setId(3);
        dto.setName("Group03");
        dto.setDate(new Date(Calendar.getInstance().getTime().getTime()));

        String newId = groupService.add(dto);
        assertThat(newId).isNotNull();
    }

    @Test
    public void addFailConflictTest() {

        GroupDto dto = new GroupDto();
        dto.setId(3);
        dto.setName("Group02");
        dto.setDate(new Date(Calendar.getInstance().getTime().getTime()));

        assertThat(groupService.add(dto)).isEqualTo("CONFLICT");
    }

    @Test
    public void addFailEmptyTest() {

        GroupDto dto = null;
        assertThat(groupService.add(dto)).isEqualTo("EMPTY");

    }

    @Test
    public void addUserSuccessTest() {

        GroupAddUserDto dto = new GroupAddUserDto();
        dto.setId(1);
        dto.setUserId(1);

        String newId = groupService.addUser(dto);
        assertThat(newId).isNotNull();
    }

    @Test
    public void addUserFailEmptyTest() {

        GroupAddUserDto dto = null;

        String newId = groupService.addUser(dto);
        assertThat(newId).isNotNull();
    }

    @Test
    public void addUserFailConflictTest() {

        GroupAddUserDto dto = new GroupAddUserDto();
        dto.setId(2);
        dto.setUserId(2);

        String newId = groupService.addUser(dto);
        assertThat(newId).isNotNull();
    }

    @Test
    public void addUserFailNotFoundUserTest() {

        GroupAddUserDto dto = new GroupAddUserDto();
        dto.setId(1);
        dto.setUserId(3);

        String newId = groupService.addUser(dto);
        assertThat(newId).isNotNull();
    }

    @Test
    public void addUserFailNotFoundGroupTest() {

        GroupAddUserDto dto = new GroupAddUserDto();
        dto.setId(3);
        dto.setUserId(1);

        String newId = groupService.addUser(dto);
        assertThat(newId).isNotNull();
    }

    @Test
    public void delSuccessTest() {

        Integer id = 2;

        String newId = groupService.del(id);
        assertThat(newId).isNotNull();
    }

    @Test
    public void delFailEmptyTest() {

        Integer id = null;

        String newId = groupService.del(id);
        assertThat(newId).isNotNull();
    }

    @Test
    public void delFailNotFoundTest() {

        Integer id = 99;

        String newId = groupService.del(id);
        assertThat(newId).isNotNull();
    }

}
