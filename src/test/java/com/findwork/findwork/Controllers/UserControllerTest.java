package com.findwork.findwork.Controllers;

import com.findwork.findwork.Entities.Users.UserPerson;
import com.findwork.findwork.Services.UserService;
import com.findwork.findwork.Services.ValidationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest
{
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private ValidationService validationService;
    @Test
    public void getPersonPageTest() throws Exception {
        // Given
        UUID personId = UUID.fromString("0000-00-00-00-000000");
        UserPerson user1 = new UserPerson("user1@abv.bg", "password", "firstName", "lastName", LocalDate.of(1999, 1, 1));
        // When
        Mockito.when(userService.loadUserById(personId)).thenReturn(user1);
        // Then
        mockMvc.perform(MockMvcRequestBuilders //returns status code 401 Unautorized
                        .get("/person/{id}", personId))
                        .andExpect(status().isOk())
                        .andExpect(view().name(user1.getName()))
                        .andExpect(model().attribute("user", user1));
    }
}
