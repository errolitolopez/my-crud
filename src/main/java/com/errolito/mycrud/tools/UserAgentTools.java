package com.errolito.mycrud.tools;

import com.errolito.mycrud.dto.UserRequest;
import com.errolito.mycrud.dto.UserResponse;
import com.errolito.mycrud.facade.UserFacade;
import io.github.uncaughterrol.commons.exception.ApiException;
import io.github.uncaughterrol.commons.exception.ResourceAlreadyExistsException;
import io.github.uncaughterrol.commons.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserAgentTools {
    private final UserFacade userFacade;

    @Tool(description = "Find a user by their username. Returns user ID, username, full name, and account creation date.")
    public String getUserByUsername(String username) {
        return userFacade.findByUsername(username)
                .map(u -> "User found — ID: " + u.getId()
                        + ", Username: " + u.getUsername()
                        + ", Full name: " + u.getUserProfile().getFullName()
                        + ", Member since: " + u.getCreatedDate())
                .orElse("No user found with username: " + username);
    }

    @Tool(description = "Find a user by their ID. Returns user details including their profile.")
    public String getUserById(Integer id) {
        return userFacade.findById(id)
                .map(u -> "User found — ID: " + u.getId()
                        + ", Username: " + u.getUsername()
                        + ", Full name: " + u.getUserProfile().getFullName()
                        + ", Member since: " + u.getCreatedDate())
                .orElse("No user found with ID: " + id);
    }

    @Tool(description = "List all registered users. Returns a summary of all users and their profiles.")
    public String getAllUsers() {
        List<UserResponse> users = userFacade.findAll();
        if (users.isEmpty()) return "No users registered yet.";

        StringBuilder sb = new StringBuilder("Registered users:\n");
        for (UserResponse u : users) {
            sb.append("- ID: ").append(u.getId())
                    .append(", Username: ").append(u.getUsername())
                    .append(", Full name: ").append(u.getUserProfile().getFullName())
                    .append("\n");
        }
        return sb.toString();
    }

    @Tool(description = "Create a new user with a username and full name. Fails if the username is already taken.")
    public String createUser(String username, String fullName) {
        try {
            UserRequest request = UserRequest.builder()
                    .username(username)
                    .fullName(fullName)
                    .build();

            userFacade.save(request);
            return "User created — Username: " + username + ", Full name: " + fullName;

        } catch (ResourceAlreadyExistsException e) {
            return "Username '" + username + "' is already taken. Please choose a different username.";
        }
    }

    @Tool(description = "Update the username and/or full name of an existing user. Provide the current username to look them up, then supply the new values.")
    public String updateUser(String username, String fullName) {
        try {
            UserRequest request = UserRequest.builder()
                    .username(username)
                    .fullName(fullName)
                    .build();

            userFacade.update(request);
            return "User created — Username: " + username + ", Full name: " + fullName;

        } catch (ApiException e) {
            return switch (e) {
                case ResourceAlreadyExistsException ignored ->
                        "Username '" + username + "' is already taken. Please choose a different username.";
                case ResourceNotFoundException ignored -> "No user found with username: " + username;
                default ->
                        "An unexpected error occurred while updating user '" + username + "'. Please try again later.";
            };
        }
    }

    @Tool(description = "Delete a user by their username. This action is irreversible.")
    public String deleteUserByUsername(String username) {
        return userFacade.findByUsername(username)
                .map(u -> {
                    userFacade.deleteById(u.getId());
                    return "User '" + username + "' has been successfully deleted.";
                })
                .orElse("No user found with username: " + username);
    }
}