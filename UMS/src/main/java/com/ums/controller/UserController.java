package com.ums.controller;

import com.ums.dto.LoginRequestDTO;
import com.ums.dto.UserDto;
import com.ums.entity.User;
import com.ums.entity.UserCreationDTO;
import com.ums.exception.CustomException;
import com.ums.repository.IUser;
import com.ums.response.ResponseModel;
import com.ums.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/user")

public class UserController {
    private static final Logger log = LogManager.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * com.ums.controller
     * User creation
     */
    @PostMapping("/login")
    public ResponseModel login(@RequestBody LoginRequestDTO loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        String loginResponse = String.valueOf(userService.login(username, password));

        if (loginResponse != null) {
            return ResponseModel.success(HttpStatus.OK, "Success", userService.login(username, password));
        } else {
            throw new CustomException(HttpStatus.NOT_FOUND, "Invalid username or password");
        }
    }

    @GetMapping("/get/{companyId}")
    public ResponseModel getUsersByCompanyId(
            @PathVariable(name = "companyId") String companyId,
            @RequestHeader(value = "Authorization") String auth) {

        List<User> users = userService.getUsersByCompanyIdAndAuthorization(companyId, auth);

        if (users != null && !users.isEmpty()) {
            return ResponseModel.success(HttpStatus.OK, "Success", users);
        } else {
            throw new CustomException(HttpStatus.NOT_FOUND, "No users found for the provided company ID");
        }
    }

    @PostMapping("/creates")
    public ResponseModel createUser(@Valid @RequestBody UserCreationDTO userCreationDTO, @RequestHeader(value = "Authorization") String auth) {
        log.info("User creation API is called!");
        User createdUser = userService.createUser(userCreationDTO.getUserDetails(), String.valueOf(userCreationDTO.getCompany_id()), auth);
        return ResponseModel.success(HttpStatus.OK, "Success", createdUser);
    }

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

    //    @PostMapping("/creates")
//    public ResponseModel createUser(@Valid @RequestBody User userDetails,@RequestHeader(value ="Authorization") String auth) {
//        log.info("User creation API is called!");
//        return ResponseModel.success(HttpStatus.OK, "Success", userService.createUser(userDetails, auth));
//           //  }



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





