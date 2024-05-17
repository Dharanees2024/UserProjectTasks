package com.ums.service;
import com.ums.dto.UserDto;
import com.ums.entity.Company;
import com.ums.entity.Login;
import com.ums.entity.User;
import com.ums.entity.UserCreationDTO;
import com.ums.exception.CustomException;
import com.ums.repository.CompanyRepository;
import com.ums.repository.IUser;
import com.ums.response.ResponseModel;
import com.ums.utils.enums.Role;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

@Slf4j
@Service
public class UserService {

    @Autowired
    private IUser userRepo;


    @Autowired
    private CompanyRepository companyRepository;


    @PostConstruct
    public void init() {
        processQueue(); // Start processing immediately without delay
    }

    private final Queue<List<UserCreationDTO>> batchQueue = new LinkedList<>();

    public ResponseModel login(Login user) {
        try {
            User users = userRepo.findByUserName(user.getUserName());
            if (users == null) {
                throw new RuntimeException("Invalid username or password");
            }
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (!passwordEncoder.matches(user.getPassword(), users.getPassword()) && !user.getPassword().equals(users.getPassword())) {
                throw new RuntimeException("Invalid username or password");
            }
            return new ResponseModel(HttpStatus.OK.value(), "Login successful", user);
        } catch (Exception e) {
            throw new RuntimeException("Error in login: " + e.getMessage());
        }
    }

    @Transactional
    public void createUser(List<UserCreationDTO> userCreationDTOList) {
        int batchSize = 5;
        for (int i = 0; i < userCreationDTOList.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, userCreationDTOList.size());
            List<UserCreationDTO> batch = new ArrayList<>(userCreationDTOList.subList(i, endIndex));
            batchQueue.add(batch);
        }
        processQueue();
    }

    private void processQueue() {
        while (!batchQueue.isEmpty()) {
            List<UserCreationDTO> batch = batchQueue.poll();
            if (batch != null) {
                for (UserCreationDTO dto : batch) {
                    try {
                        processUserCreation(dto);
                    } catch (CustomException e) {
                        log.error("Error processing user creation: {}", e.getMessage());
                        throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing user creation: " + e.getMessage());
                    }
                }
            }
        }
    }

    private void processUserCreation(UserCreationDTO dto) {
        User userDetails = dto.getUserDetails();
        String companyId = dto.getCompany_id();
        // Validate user details
        String username = userDetails.getUserName();
        String mobileNumber = userDetails.getMobileNumber();
        if (username.length() < 2 || username.length() > 10) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Username must be between 2 to 10 characters");
        }
        if (mobileNumber != null && mobileNumber.length() != 10) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Mobile number must be 10 characters");
        }
        // Check if the username already exists
        User usersWithSameUsername = userRepo.findByUserName(username);
        if (usersWithSameUsername != null) {
            throw new CustomException(HttpStatus.CONFLICT, "Username already exists: " + username);
        }
        // Fetch the company by companyId
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Company not found"));
        // Set the company in userDetails
        userDetails.setCompany(company);
        try {
            // Save the user entity
            log.info("Saving user: {}", userDetails);
            userRepo.save(userDetails);
            log.info("User saved successfully: {}", userDetails);
        } catch (Exception e) {
            // Log error and handle exception
            log.error("Error saving user: {}", e.getMessage(), e);
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save user");
        }
    }

    public List<User> getUsersByCompanyIdAndAuthorization(String companyId, String auth) {
        return userRepo.findByCompanyId(companyId);
    }

    public UserDto getUserDetails(String userId, String auth) {
        //token(auth);
        Map<String, String> errors = new HashMap<>();
        User user = userRepo.findById(userId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "User Not Found"));
        return new UserDto().builder().id(user.getId()).isValid(Boolean.TRUE).name(user.getName()).isDeleted(user.getIsDelete()).role(String.valueOf(Role.User)).build();

    }

    /* update user */
    /**/
    public User updateUser(String id, User user, String auth) {
        //token(auth);
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
    public void deleteUserByUsername(String userName, String authorizationheader) {
        //token(authorizationheader);
        userRepo.deleteByUserName(userName);

    }

    public User findByUserName(String userName) {
        return userRepo.findByUserName(userName);
    }


}





/* login initial  */
/**/
//    public ResponseEntity<String> login(String username, String password) {
//        if ("admin".equals(username) && "password123".equals(password)) {
//            return ResponseEntity.ok("123 login successfull");
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
//        }
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

//    public User createUser(User userDetails, String auth) {
//        //token(auth);
//        String username = userDetails.getUserName();
//        String mobileNumber = userDetails.getMobileNumber().toString();
//
//
//        if (username.length() < 2 || username.length() > 10) {
//            throw new CustomException(HttpStatus.BAD_REQUEST, "Username must be between 2 to 10 characters");
//        }
//
//        if (!StringUtils.isEmpty(mobileNumber) && mobileNumber.length() != 10) {
//            throw new CustomException(HttpStatus.BAD_REQUEST, "Mobile number must be  10 characters");
//        }
//        if (Objects.nonNull(userRepo.findByUserName(username))) {
//            throw new CustomException(HttpStatus.CONFLICT, "Username already exists: " + username);
//        }
//        return userRepo.save(userDetails);
//    }

/**/


////////////////////////-------------->>>>>>>>>
/* User creation  */
//    public User createUser(User userDetails, String company_id, String auth) {
//        // Validate user details
//        String username = userDetails.getUserName();
//        String mobileNumber = userDetails.getMobileNumber().toString();
//        if (username.length() < 2 || username.length() > 10) {
//            throw new CustomException(HttpStatus.BAD_REQUEST, "Username must be between 2 to 10 characters");
//        }
//        if (!StringUtils.isEmpty(mobileNumber) && mobileNumber.length() != 10) {
//            throw new CustomException(HttpStatus.BAD_REQUEST, "Mobile number must be 10 characters");
//        }
//        if (Objects.nonNull(userRepo.findByUserName(username))) {
//            throw new CustomException(HttpStatus.CONFLICT, "Username already exists: " + username);
//        }
//        // Fetch the company by companyId
//        Company company = companyRepository.findById(company_id).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Company not found"));
//        // Set the company in userDetails
//        userDetails.setCompany(company);
//        // Add the user creation request to the queue
//        UserCreationDTO userCreationDTO = new UserCreationDTO(userDetails, company_id);
//        userCreationQueue.add(userCreationDTO);
//        // Save the user entity
//        return userRepo.save(userDetails);
//    }


