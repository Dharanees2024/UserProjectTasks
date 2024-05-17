package com.ums.Controller;
import com.ums.controller.UserController;
import com.ums.entity.Login;
import com.ums.entity.User;
import com.ums.repository.IUser;
import com.ums.response.ResponseModel;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private IUser userRepo;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLogin_UserFoundAndPasswordMatch() {
        // Mocking userRepo behavior
        User mockUser = new User("testUser", new BCryptPasswordEncoder().encode("password"));
        when(userRepo.findByUserName("testUser")).thenReturn(mockUser);
        // Calling the method to be tested with valid credentials
        ResponseModel response = userController.login(new Login("testUser", "password"));
        // Verifying the response
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("Login successful", response.getMessage());
        assertEquals(mockUser, response.getData());
    }

    @Test
    public void testLogin_UserNotFound() {
        // Mocking userRepo behavior
        when(userRepo.findByUserName("nonExistentUser")).thenReturn(null);
        // Calling the method to be tested with non-existent user
        ResponseModel response = userController.login(new Login("nonExistentUser", "password"));
        // Verifying the response
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals("User not found", response.getMessage());
        assertEquals(null, response.getData());
    }

    @Test
    public void testLogin_InvalidPassword() {
        // Mocking userRepo behavior
        User mockUser = new User("testUser", new BCryptPasswordEncoder().encode("password"));
        when(userRepo.findByUserName("testUser")).thenReturn(mockUser);
        // Calling the method to be tested with invalid password
        ResponseModel response = userController.login(new Login("testUser", "invalidPassword"));
        // Verifying the response
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        assertEquals("Invalid password", response.getMessage());
        assertEquals(null, response.getData());
    }


}
