package com.ums.dto;

import com.ums.entity.UserCreationDTO;
import org.springframework.stereotype.Component;
import java.util.LinkedList;
import java.util.Queue;

@Component
public class RequestQueue {

    private final Queue<UserCreationDTO> queue = new LinkedList<>();

    public void addToQueue(UserCreationDTO userCreationDTO) {
        queue.offer(userCreationDTO); // Add the request body to the queue
    }

    public UserCreationDTO pollFromQueue() {
        return queue.poll(); // Remove and return the request body from the queue
    }

    public boolean isQueueEmpty() {
        return queue.isEmpty(); // Check if the queue is empty
    }
}
