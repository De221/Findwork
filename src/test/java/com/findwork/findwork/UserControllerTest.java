package com.findwork.findwork;

import com.findwork.findwork.Controllers.UserController;
import com.findwork.findwork.Entities.Users.UserPerson;
import com.findwork.findwork.Requests.EditPersonRequest;
import com.findwork.findwork.Services.UserService;
import com.findwork.findwork.Services.ValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc()
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
        user1.setId(personId);
        // When
        Mockito.when(userService.loadUserById(personId)).thenReturn(user1);
        // Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/{id}", personId)
                        .with(SecurityMockMvcRequestPostProcessors.user(user1)))
                .andExpect(status().isOk())
                .andExpect(view().name("user"))
                .andExpect(model().attribute("user", user1));
    }
    @Test
    public void getEditPagePersonTest() throws Exception
    {
        // Given
        UUID personId = UUID.fromString("0000-00-00-00-000000");
        UserPerson user1 = new UserPerson("user1@abv.bg", "password", "firstName", "lastName", LocalDate.of(1999, 1, 1));
        user1.setId(personId);

        UUID AnotherPersonId = UUID.fromString("1000-00-00-00-000000");
        UserPerson anotherUser = new UserPerson("user2@abv.bg", "password2", "firstName", "lastName", LocalDate.of(1999, 1, 1));
        anotherUser.setId(AnotherPersonId);
        // When
        Mockito.when(userService.loadUserById(personId)).thenReturn(user1);
        // Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/{id}/edit", personId)
                        .with(SecurityMockMvcRequestPostProcessors.user(anotherUser)))
                .andExpect(status().isFound()); // Assuming a redirect when "anotherUser" tries to access
                                                // the information page of "user1".

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/{id}/edit", personId)
                        .with(SecurityMockMvcRequestPostProcessors.user(user1)))
                .andExpect(status().isOk())
                .andExpect(view().name("editPerson"))
                .andExpect(model().attribute("user", user1));
    }

    @Test
    public void editPerson() throws Exception {
        // Given
        UUID personId = UUID.fromString("0000-00-00-00-000000");
        UserPerson user1 = new UserPerson("user1@abv.bg", "password", "firstName", "lastName", LocalDate.of(1999, 1, 1));
        user1.setId(personId);

        UUID AnotherPersonId = UUID.fromString("1000-00-00-00-000000");
        UserPerson anotherUser = new UserPerson("user2@abv.bg", "password2", "firstName", "lastName", LocalDate.of(1999, 1, 1));
        anotherUser.setId(AnotherPersonId);

        // When
        Mockito.when(userService.loadUserById(personId)).thenReturn(user1);
        // Then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/user/{id}", personId)
                        .with(SecurityMockMvcRequestPostProcessors.user(anotherUser))
                        .with(csrf()))
                .andExpect(status().isFound()); // Assuming a redirect when "anotherUser" tries to manipulate
                                                // the information page of "user1".

        EditPersonRequest editPersonRequest = new EditPersonRequest("ModifiedUser1@abv.bg", "password1", "firstName1", "lastName1", LocalDate.of(1999, 1, 2), "education1", "skills1");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/user/{id}", personId)
                        .with(SecurityMockMvcRequestPostProcessors.user(user1))
                        .with(csrf())
                        .requestAttr("request", editPersonRequest))
                .andExpect(status().isFound()); // Assuming successful redirect after the post method

        verify(validationService, Mockito.times(1)).validateEditPersonRequest(ArgumentMatchers.any());
        verify(userService, Mockito.times(1)).editPerson(ArgumentMatchers.any(), ArgumentMatchers.any());
    }
}
