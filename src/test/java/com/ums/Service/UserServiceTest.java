package com.ums.Service;
import com.ums.controller.UserController;
import com.ums.entity.Company;
import com.ums.entity.Login;
import com.ums.entity.User;
import com.ums.exception.CustomException;
import com.ums.repository.CompanyRepository;
import com.ums.repository.IUser;
import com.ums.response.ResponseModel;
import com.ums.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    private IUser userRepo;

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void login_success() {

        User user = new User();
        user.setUserName("ajaykumar");
        user.setPassword("$2a$10$rdDyzNhDIubrd/0oeu701OAwelNA8ux1jJbKdqMQJ/mjlax9o5KFq"); // Pre-hashed password
        when(userRepo.findByUserName("ajaykumar")).thenReturn(user);
        Login login = new Login("ajaykumar", "$2a$10$rdDyzNhDIubrd/0oeu701OAwelNA8ux1jJbKdqMQJ/mjlax9o5KFq");
        ResponseModel response = userService.login(login);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("Login successful", response.getMessage());
    }

    @Test
    void testFindByUserName() {
        String username = "john_doe";
        User user = new User();
        when(userRepo.findByUserName(username)).thenReturn(user);
        User foundUser = userRepo.findByUserName(username);
        assertEquals(user, foundUser);
    }

    @Test
    void testFindByCompanyId() {

        String companyId = "1";
        List<User> users = List.of(new User(), new User());
        when(userRepo.findByCompanyId(companyId)).thenReturn(users);
        List<User> foundUsers = userRepo.findByCompanyId(companyId);
        assertEquals(users.size(), foundUsers.size());
    }

    @Test
    public void testUserNotFound() {
        Login login = new Login("username", "password");
        when(userService.findByUserName(login.getUserName())).thenReturn(null);
        ResponseModel response = userController.login(login);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals("User not found", response.getMessage());
        assertEquals(null, response.getData());
    }

    @Test
    public void testGetAllUsers() {
        List<User> mockUsers = new ArrayList<>();
        mockUsers.add(new User("1", "John"));
        mockUsers.add(new User("2", "Alice"));
        when(userRepo.findAll()).thenReturn(mockUsers);
        List<User> result = userService.getAllUsers("dummyAuthToken");
        verify(userRepo, times(1)).findAll();
        assertEquals(mockUsers.size(), result.size());
        assertEquals(mockUsers.get(0).getId(), result.get(0).getId());
        assertEquals(mockUsers.get(0).getName(), result.get(0).getName());
        assertEquals(mockUsers.get(1).getId(), result.get(1).getId());
        assertEquals(mockUsers.get(1).getName(), result.get(1).getName());
    }

    @Test
    public void testDeleteUserByUsername() {
        String userName = "testUser";
        String authorizationHeader = "dummyAuthToken";
        userService.deleteUserByUsername(userName, authorizationHeader);
        verify(userRepo, times(1)).deleteByUserName(userName);
    }

    @Test
    public void testCreateUser() {
        // Prepare test data
        User userDetails = new User();
        userDetails.setUserName("testUser");
        userDetails.setMobileNumber("1234567890");
        String companyId = "company123";
        String auth = "dummyAuth";
        // Mock the behavior of companyRepository.findById() to return a company
        Company mockCompany = new Company();
        mockCompany.setId(companyId);
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(mockCompany));
        // Mock the behavior of userRepo.findByUserName() to return null (indicating the username doesn't exist)
        when(userRepo.findByUserName(userDetails.getUserName())).thenReturn(null);
        // Mock the behavior of userRepo.save() to return the saved user
        User savedUser = new User();
        savedUser.setId("userId123");
        when(userRepo.save(any(User.class))).thenReturn(savedUser);
        // Call the createUser method
        User result = userService.createUser(userDetails, companyId, auth);
        // Verify that companyRepository.findById() was called with the correct parameter
        verify(companyRepository, times(1)).findById(companyId);
        // Verify that userRepo.findByUserName() was called with the correct parameter
        verify(userRepo, times(1)).findByUserName(userDetails.getUserName());
        // Verify that userDetails's company was set correctly
        assertEquals(mockCompany, userDetails.getCompany());
        // Verify that userRepo.save() was called with the correct parameter
        verify(userRepo, times(1)).save(userDetails);
        // Verify that the result matches the saved user
        assertEquals(savedUser, result);
    }

    @Test
    public void testCreateUser_ValidDetails() {
        // Prepare test data
        User userDetails = new User();
        userDetails.setUserName("newUser"); // New username
        userDetails.setMobileNumber("1234567890");
        String companyId = "company123";
        String auth = "dummyAuth";
        // Mock the behavior of userRepo.findByUserName() to return null (indicating the username does not exist)
        when(userRepo.findByUserName(userDetails.getUserName())).thenReturn(null);
        // Mock the behavior of companyRepository.findById() to return a Company object
        Company mockCompany = new Company();
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(mockCompany));
        // Mock the behavior of userRepo.save() to return the saved User object
        when(userRepo.save(any(User.class))).thenReturn(userDetails);
        // Call the createUser method
        User createdUser = userService.createUser(userDetails, companyId, auth);
        // Verify that userRepo.findByUserName() was called with the correct username
        verify(userRepo, times(1)).findByUserName(userDetails.getUserName());
        // Verify that companyRepository.findById() was called with the correct company ID
        verify(companyRepository, times(1)).findById(companyId);
        // Verify that userRepo.save() was called with the userDetails object
        verify(userRepo, times(1)).save(userDetails);
        // Verify that the createdUser object is not null
        assertNotNull(createdUser);
        // Verify that the createdUser object is the same as the userDetails object passed to the method
        assertEquals(userDetails, createdUser);
    }

    @Test
    public void testUpdateUser_Success() {
        // Prepare test data
        String userId = "user123";
        User updateUser = new User();
        updateUser.setUserName("newUsername");
        updateUser.setName("New Name");
        updateUser.setMobileNumber("9876543210");
        // Mock the behavior of userRepo.findById() to return a User object
        User existingUser = new User();
        when(userRepo.findById(userId)).thenReturn(Optional.of(existingUser));
        // Mock the behavior of userRepo.save() to return the saved User object
        when(userRepo.save(any(User.class))).thenReturn(updateUser);
        // Call the updateUser method
        User updatedUser = userService.updateUser(userId, updateUser, "dummyAuth");
        // Verify that userRepo.findById() was called with the correct user ID
        verify(userRepo, times(1)).findById(userId);
        // Verify that userRepo.save() was called with the updated User object
        verify(userRepo, times(1)).save(existingUser);
        // Verify that the updatedUser object is not null
        assertNotNull(updatedUser);
        // Verify that the updatedUser object is the same as the updateUser object passed to the method
        assertEquals(updateUser, updatedUser);
    }

    @Test
    public void testGetUsersByCompanyIdAndAuthorization() {
        // Prepare test data
        String companyId = "company123";
        String auth = "dummyAuth";
        // Mock the behavior of userRepo.findByCompanyId() to return a list of users
        List<User> mockUsers = new ArrayList<>();
        mockUsers.add(new User());
        mockUsers.add(new User());
        when(userRepo.findByCompanyId(companyId)).thenReturn(mockUsers);
        // Call the getUsersByCompanyIdAndAuthorization method
        List<User> result = userService.getUsersByCompanyIdAndAuthorization(companyId, auth);
        // Verify that userRepo.findByCompanyId() was called with the correct company ID
        verify(userRepo, times(1)).findByCompanyId(companyId);
        // Verify that the result matches the mock user data
        assertEquals(mockUsers.size(), result.size());
        assertEquals(mockUsers.get(0).getId(), result.get(0).getId());
        assertEquals(mockUsers.get(0).getName(), result.get(0).getName());
        assertEquals(mockUsers.get(1).getId(), result.get(1).getId());
        assertEquals(mockUsers.get(1).getName(), result.get(1).getName());
    }


}




