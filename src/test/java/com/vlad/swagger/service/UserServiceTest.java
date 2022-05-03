package com.vlad.swagger.service;

import com.vlad.swagger.dto.UserDto;
import com.vlad.swagger.entity.User;
import com.vlad.swagger.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setUp() {

        User user1 = new User();
        user1.setId(1);
        user1.setName("User01");
        user1.setAge(27);

        User user2 = new User();
        user2.setId(2);
        user2.setName("User02");
        user2.setAge(35);

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        Mockito.when(userRepository.
                findAll()).thenReturn(users);

        Mockito.when(userRepository.
                findById(user1.getId())).thenReturn(java.util.Optional.of(user1));

    }

    @Test
    public void getAllUsersTest() {

        List<UserDto> list = userService.getAllUsers();

        assertThat(userService.getAllUsers()).isEqualTo(list);
    }

    @Test
    public void addSuccessTest() {

        UserDto dto = new UserDto();
        dto.setId(3);
        dto.setName("User03");
        dto.setAge(47);

        String newId = userService.add(dto);
        assertThat(newId).isNotNull();
    }

    @Test
    public void addFailConflictTest() {

        UserDto dto = new UserDto();
        dto.setId(3);
        dto.setName("User02");
        dto.setAge(35);

        assertThat(userService.add(dto)).isEqualTo("CONFLICT");
    }

    @Test
    public void addFailEmptyTest() {

        UserDto dto = null;
        assertThat(userService.add(dto)).isEqualTo("EMPTY");

    }

    @Test
    public void updateSuccessTest() {

        Integer id = 1;

        UserDto dto = new UserDto();
        dto.setName("User001");
        dto.setAge(55);

        String newId = userService.update(id, dto);
        assertThat(newId).isNotNull();
    }

    @Test
    public void updateFailEmptyTest() {

        Integer id = null;

        UserDto dto = new UserDto();
        dto.setName("User001");
        dto.setAge(55);

        assertThat(userService.update(id, dto)).isEqualTo("EMPTY");
    }

    @Test
    public void updateFailEmptyNameTest() {

        Integer id = 1;

        UserDto dto = new UserDto();
        dto.setName("");
        dto.setAge(55);

        assertThat(userService.update(id, dto)).isEqualTo("EMPTY_NAME");
    }

    @Test
    public void updateFailConflictTest() {

        Integer id = 1;

        UserDto dto = new UserDto();
        dto.setName("User02");
        dto.setAge(55);

        assertThat(userService.update(id, dto)).isEqualTo("CONFLICT");
    }

    @Test
    public void updateFailNotFoundTest() {

        Integer id = 99;

        UserDto dto = new UserDto();
        dto.setName("User01");
        dto.setAge(55);

        assertThat(userService.update(id, dto)).isEqualTo("NOT_FOUND");
    }

    @Test
    public void delSuccessTest() {

        Integer id = 1;

        String newId = userService.del(id);
        assertThat(newId).isNotNull();
    }

    @Test
    public void delFailEmptyTest() {

        Integer id = null;

        String newId = userService.del(id);
        assertThat(newId).isNotNull();
    }

    @Test
    public void delFailNotFoundTest() {

        Integer id = 99;

        String newId = userService.del(id);
        assertThat(newId).isNotNull();
    }

}
