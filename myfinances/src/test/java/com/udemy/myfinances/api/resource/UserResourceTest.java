package com.udemy.myfinances.api.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udemy.myfinances.api.dto.UserDto;
import com.udemy.myfinances.exception.AuthException;
import com.udemy.myfinances.exception.BusinessLogicException;
import com.udemy.myfinances.model.entity.User;
import com.udemy.myfinances.service.EntryService;
import com.udemy.myfinances.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UserResource.class)
@AutoConfigureWebMvc
public class UserResourceTest {

    static final String API = "/api/users";
    static final MediaType JSON = MediaType.APPLICATION_JSON;

    @Autowired
    MockMvc mvc;

    @MockBean
    UserService service;

    @MockBean
    EntryService entryService;

    @Test
    public void itMustAuthenticateAUser() throws Exception {
        // Scenario
        String name = "User";
        String email = "user@email.com";
        String password = "password123";

        UserDto dto = UserDto.builder().email(email).name(name).password(password).build();
        User user = User.builder().id(1L).email(email).name(name).password(password).build();

        Mockito.when(service.authenticate(email, password)).thenReturn(user);

        String json =  new ObjectMapper().writeValueAsString(dto);

        // Execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API.concat("/authenticate")).accept(JSON).contentType(JSON).content(json);

        // Verification
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(user.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value(user.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("name").value(user.getName()));
    }

    @Test
    public void itMustReturnBadRequestWhenGettingAnAuthenticationError() throws Exception {
        // Scenario
        String name = "User";
        String email = "user@email.com";
        String password = "password123";

        UserDto dto = UserDto.builder().email(email).name(name).password(password).build();

        Mockito.when(service.authenticate(email, password)).thenThrow(AuthException.class);

        String json =  new ObjectMapper().writeValueAsString(dto);

        // Execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API.concat("/authenticate")).accept(JSON).contentType(JSON).content(json);

        // Verification
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void itMustCreateAUser() throws Exception {
        // Scenario
        String name = "User";
        String email = "user@email.com";
        String password = "password";

        UserDto dto = UserDto.builder().email(email).name(name).password(password).build();
        User user = User.builder().id(1L).email(email).name(name).password(password).build();

        Mockito.when(service.save(Mockito.any(User.class))).thenReturn(user);

        String json =  new ObjectMapper().writeValueAsString(dto);

        // Execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API).accept(JSON).contentType(JSON).content(json);

        // Verification
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(user.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value(user.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("name").value(user.getName()));
    }

    @Test
    public void itMustReturnBadRequestWhenCreatingAnInvalidUser() throws Exception {
        // Scenario
        String name = "User";
        String email = "usuario@email.com";
        String password = "password123";

        UserDto dto = UserDto.builder().email(email).name(name).password(password).build();

        Mockito.when(service.save(Mockito.any(User.class))).thenThrow(BusinessLogicException.class);

        String json =  new ObjectMapper().writeValueAsString(dto);

        // Execution
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(API).accept(JSON).contentType(JSON).content(json);

        // Verification
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
