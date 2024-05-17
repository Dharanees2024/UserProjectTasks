package com.ums.controller;
import com.ums.entity.Login;
import com.ums.entity.User;
import com.ums.entity.UserCreationDTO;
import com.ums.exception.CustomException;
import com.ums.repository.CompanyRepository;
import com.ums.repository.IUser;
import com.ums.response.ResponseModel;
import com.ums.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@RestController
@Validated
@RequestMapping("/user")

public class UserController {
    private static final Logger log = LogManager.getLogger(UserController.class);
    @Autowired
    private IUser userRepo;

    @Autowired
    public UserService userService;

    @Autowired
    private CompanyRepository  companyRepository;

   // private final Queue<List<UserCreationDTO>> batchQueue = new LinkedList<>();

    public UserController(IUser userRepo) {
        this.userRepo = userRepo;
    }

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * com.ums.controller
     * User creation
     */
    @PostMapping("/login")
    public ResponseModel login(@RequestBody Login login) {
        try {
            User user = userRepo.findByUserName(login.getUserName());
            if (user == null) {
                return new ResponseModel(HttpStatus.NOT_FOUND.value(), "User not found", null);
            }
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            // Compare passwords using BCryptPasswordEncoder
            boolean passwordMatch = passwordEncoder.matches(login.getPassword(), user.getPassword());
            if (!passwordMatch) {
                return new ResponseModel(HttpStatus.UNAUTHORIZED.value(), "Invalid password", null);
            } else {
                return ResponseModel.success(HttpStatus.OK, "Login successful", user);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error in login: " + e.getMessage());
        }
    }

    @GetMapping("/get/{companyId}")
    public ResponseModel getUsersByCompanyId(@PathVariable(name = "companyId") String companyId, @RequestHeader(value = "Authorization") String auth) {

        List<User> users = userService.getUsersByCompanyIdAndAuthorization(companyId, auth);
        if (users != null && !users.isEmpty()) {
            return ResponseModel.success(HttpStatus.OK, "Success", users);
        } else {
            throw new CustomException(HttpStatus.NOT_FOUND, "No users found for the provided company ID");
        }
    }

//    @PostMapping("/createlist")
//    public ResponseModel createUser(@Valid @RequestBody List<UserCreationDTO> userCreationDTOList, @RequestHeader(value = "Authorization") String auth) {
//        log.info("User creation API is called!");
//        userService.processUserCreationRequests(userCreationDTOList);
//        return ResponseModel.success(HttpStatus.OK, "Success", null); // Return a success response without user details
//    }

    @PostMapping("/createlist")
    public ResponseModel createUser( @RequestBody List<UserCreationDTO> userCreationDTOList, @RequestHeader(value = "Authorization") String auth) {
        userService.createUser(userCreationDTOList);
        return ResponseModel.success(HttpStatus.OK, "User creation request received", null);
    }



    @PostMapping("/batchedRequests")
    public List<List<UserCreationDTO>> getBatchedRequests(@RequestBody List<UserCreationDTO> userCreationDTOList) {
        List<List<UserCreationDTO>> batchedRequests = new ArrayList<>();
        int batchSize = 5; // Set the batch size
        int totalRequests = Math.min(userCreationDTOList.size(),5);

        for (int i = 0; i < totalRequests; i += batchSize) {
            int endIdx = Math.min(i + batchSize, totalRequests);
            List<UserCreationDTO> batch = userCreationDTOList.subList(i, endIdx);
            batchedRequests.add(batch);
        }
        return batchedRequests;
    }

//    @PostMapping("/creates")
//    public ResponseModel createUser(@Valid @RequestBody UserCreationDTO userCreationDTO, @RequestHeader(value = "Authorization") String auth) {
//        log.info("User creation API is called!");
//        User createdUser = userService.createUser(userCreationDTO.getUserDetails(), String.valueOf(userCreationDTO.getCompany_id()), auth);
//        return ResponseModel.success(HttpStatus.OK, "Success", createdUser);
//    }

    @GetMapping("/details/{userId}")
    public ResponseModel getUserDetails(@PathVariable(name = "userId", required = true) String userId, @RequestHeader(value = "Authorization") String auth) {
        log.info("Get user details called!");
        return ResponseModel.success(HttpStatus.OK, "Success", userService.getUserDetails(userId, auth));
    }


    @PutMapping("/update/{id}")
    public ResponseModel updateUser(@PathVariable String id, @RequestBody User user, @RequestHeader(value = "Authorization") String auth) {
        log.info("user update successfully");
        return ResponseModel.success(HttpStatus.OK, "update", userService.updateUser(id, user, auth));
    }

    @GetMapping("/list")
    public ResponseModel getAllUsers(@RequestHeader(value = "Authorization") String authorizationheader) {
        return ResponseModel.success(HttpStatus.OK, "updatesucces", userService.getAllUsers(authorizationheader));
    }

    @DeleteMapping("/delete/{userName}")
    public ResponseModel deleteUserByUsername(@PathVariable String userName, @RequestHeader(value = "Authorization") String authorizationheader) {
        try {
            userService.deleteUserByUsername(userName, authorizationheader);
            return ResponseModel.success(HttpStatus.OK, "User deleted successfully", "OK");
        } catch (EntityNotFoundException e) {
            throw new CustomException(HttpStatus.NOT_FOUND, "User name not found");
        }
    }

}
/**
 * com.ums.controller
 * User creation
 */
//    @PostMapping("/login")
//    public ResponseModel login(@RequestBody LoginRequestDTO loginRequest) {
//        String username = loginRequest.getUsername();
//        String password = loginRequest.getPassword();
//
//        String loginResponse = String.valueOf(userService.login(username, password));
//
//        if (loginResponse != null) {
//            return ResponseModel.success(HttpStatus.OK, "Success", userService.login(username, password));
//        } else {
//            throw new CustomException(HttpStatus.NOT_FOUND, "Invalid username or password");
//        }
//    }



///////

//    @PostMapping("/createlist")
//    public ResponseModel createUser(@Valid @RequestBody List<UserCreationDTO> userCreationDTOList, @RequestHeader(value = "Authorization") String auth) {
//        log.info("User creation API is called!");
//        int batchSize = 5; // Set the batch size
//        int totalRequests = userCreationDTOList.size();
//        for (int i = 0; i < totalRequests; i += batchSize) {
//            int endIdx = Math.min(i + batchSize, totalRequests);
//            List<UserCreationDTO> batch = userCreationDTOList.subList(i, endIdx);
//            // Process the current batch
//            batchQueue.offer(batch);
//            processBatches();
//        }
//        return ResponseModel.success(HttpStatus.OK, "Success", null); // Return a success response without user details
//    }
//    // Method to process a batch of user creation requests
//    private void processBatches() {
//        // Process batches from the queue until it's empty
//        while (!batchQueue.isEmpty()) {
//            List<UserCreationDTO> batch = batchQueue.poll();
//            if (batch != null) {
//                // Process the current batch
//                processBatch(batch);
//            }
//        }
//    }
//
//    private void processBatch(List<UserCreationDTO> batch) {
//        // Process each user in the batch
//        for (UserCreationDTO userCreationDTO : batch) {
//            // Process the user creation request
//            processUserCreation(userCreationDTO);
//        }
//    }
//    // Method to simulate processing of user creation request
//    // Method to process user creation requests from the queue
//    public void processUserCreation(UserCreationDTO userCreationDTO) {
//        try {
//            // Convert UserCreationDTO to User entity
//            User user = convertToUserEntity(userCreationDTO);
//            // Save the user entity into the database
//            userRepo.save(user);
//            // Log success message
//            System.out.println("User created successfully: " + user);
//        } catch (Exception e) {
//            // Log error message
//            System.err.println("Error creating user: " + e.getMessage());
//            // Handle the exception as needed
//            throw new RuntimeException("Error creating user: " + e.getMessage());
//        }
//    }
//
//    // Method to convert UserCreationDTO to User entity
//    private User convertToUserEntity(UserCreationDTO userCreationDTO) {
//        // Implement the conversion logic here
//        // Example:
//        User user = new User();
//        user.setUserName(userCreationDTO.getUserDetails().getUserName());
//        user.setName(userCreationDTO.getUserDetails().getName());
//        user.setMobileNumber(userCreationDTO.getUserDetails().getMobileNumber());
//        // Set other properties as needed
//        return user;
//    }