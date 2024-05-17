//package com.ums.service;
//
//import com.ums.entity.User;
//import com.ums.entity.UserCreationDTO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.LinkedList;
//import java.util.Queue;
//import com.ums.entity.UserCreationDTO;
//import org.springframework.stereotype.Service;
//
//import java.util.LinkedList;
//import java.util.Queue;
//
//@Service
//public class QueueService {
//
//    // Mock queue to hold user creation requests
//    private Queue<UserCreationDTO> userCreationQueue = new LinkedList<>();
//
//    // Method to add user creation requests to the queue
//    public static void addToQueue(UserCreationDTO userCreationDTO) {
//        userCreationQueue.add(userCreationDTO);
//    }
//
//    // Method to process user creation requests from the queue
//    public void processQueue(int batchSize) {
//        int count = 0;
//        while (!userCreationQueue.isEmpty() && count < batchSize) {
//            UserCreationDTO request = userCreationQueue.poll();
//            if (request != null) {
//                // Process the user creation request
//                processUserCreation(request);
//                count++;
//            }
//        }
//    }
//
//    // Method to simulate processing of user creation request
//    private void processUserCreation(UserCreationDTO userCreationDTO) {
//        // Here you can perform any processing logic you want, such as calling a service method
//        System.out.println("Processing user creation request: " + userCreationDTO);
//    }
//}
