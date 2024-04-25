package com.ums.service;

import com.ums.dto.UserDto;
import com.ums.entity.Company;
import com.ums.entity.User;
import com.ums.exception.CustomException;
import com.ums.repository.CompanyRepository;
import com.ums.repository.IUser;
import com.ums.response.ResponseModel;
import com.ums.utils.enums.Role;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.net.UnknownServiceException;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
public class UserService {

    @Autowired
    private IUser userRepo;

    @Autowired
    private CompanyRepository companyRepository;

    /* login initial  */
    /**/
    public ResponseEntity<String> login(String username, String password) {
        if ("admin".equals(username) && "password123".equals(password)) {
            return ResponseEntity.ok("123 login successfull");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    /* User creation  */
    public User createUser(User userDetails, String company_id, String auth) {
        // Validate user details
        String username = userDetails.getUserName();
        String mobileNumber = userDetails.getMobileNumber().toString();

        if (username.length() < 2 || username.length() > 10) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Username must be between 2 to 10 characters");
        }
        if (!StringUtils.isEmpty(mobileNumber) && mobileNumber.length() != 10) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Mobile number must be 10 characters");
        }
        if (Objects.nonNull(userRepo.findByUserName(username))) {
            throw new CustomException(HttpStatus.CONFLICT, "Username already exists: " + username);
        }

        // Fetch the company by companyId
        Company company = companyRepository.findById(company_id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Company not found"));

        // Set the company in userDetails
        userDetails.setCompany(company);

        // Save the user entity
        return userRepo.save(userDetails);
    }


    public List<User> getUsersByCompanyIdAndAuthorization(String companyId, String auth) {
        return userRepo.findByCompanyId(companyId);
    }




//    public User createUser(User userDetails, String auth) {
//        //token(auth);
//        String username = userDetails.getUserName();
//        String mobileNumber = userDetails.getMobileNumber().toString();
//        Long companyid=userDetails.getCompanyId();
//
//        if (username.length() < 2 || username.length() > 10) {
//            throw new CustomException(HttpStatus.BAD_REQUEST, "Username must be between 2 to 10 characters");
//        }
//
//        if (!StringUtils.isEmpty(mobileNumber) && mobileNumber.length() !=10 ) {
//            throw new CustomException(HttpStatus.BAD_REQUEST, "Mobile number must be  10 characters");
//        }
//        if (Objects.nonNull(userRepo.findByUserName(username))) {
//            throw new CustomException(HttpStatus.CONFLICT, "Username already exists: " + username);
//        }
//        return userRepo.save(userDetails);
//    }


    //    /* token function  */
//    /**/
//    public void token(String auth) {
//        if (auth == null || auth.isEmpty()) {
//            throw new CustomException(HttpStatus.UNAUTHORIZED, "Token not found");
//        } else if (!auth.equals("123")) {
//            throw new CustomException(HttpStatus.UNAUTHORIZED, "Invalid token");
//        }
//    }
    /*Get user details */
    /**/
    public UserDto getUserDetails(String userId,String auth) {
        //token(auth);
        Map<String, String> errors = new HashMap<>();
        User user = userRepo.findById(userId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "User Not Found"));
        return new UserDto().builder()
                .id(user.getId())
                .isValid(Boolean.TRUE)
                .name(user.getName())
                .isDeleted(user.getIsDelete())
                .role(String.valueOf(Role.User))
                .build();

    }
    /* update user */
    /**/
    public User updateUser(String id, User user, String auth) {
        // token(auth);
        User optionalUser = userRepo.findById(id).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "user not found"));
        optionalUser.setUserName(user.getUserName());
        optionalUser.setName(user.getName());
        optionalUser.setMobileNumber(user.getMobileNumber());
        return userRepo.save(optionalUser);
    }
    /* getAllusers */
    /**/
    public List<User> getAllUsers(String auth) {
        //token(auth);
        return userRepo.findAll();
    }

    /* deleteusers */
    /**/
    public void deleteUserByUsername(String userName, String authorizationheader) {
        //token(authorizationheader);
        userRepo.deleteByUserName(userName);

    }
}