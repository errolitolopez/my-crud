package com.errolito.mycrud.tools;

import com.errolito.mycrud.dto.UserQuery;
import com.errolito.mycrud.entity.User;
import com.errolito.mycrud.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserAgentTools {
    private final UserService userService;

    @Tool(description = "Find a user by their username. Returns user ID, username, full name, and account creation date.")
    public String getUserByUsername(String username) {
        return userService.findByUsername(username)
                .map(u -> {
                    String fullName = u.getUserProfile() != null
                            ? u.getUserProfile().getFullName()
                            : "No profile found";
                    return "User found — ID: " + u.getId()
                            + ", Username: " + u.getUsername()
                            + ", Full name: " + fullName
                            + ", Member since: " + u.getCreatedDate();
                })
                .orElse("No user found with username: " + username);
    }

    @Tool(description = "Find a user by their ID. Returns user details including their profile.")
    public String getUserById(Integer id) {
        return userService.findById(id)
                .map(u -> {
                    String fullName = u.getUserProfile() != null
                            ? u.getUserProfile().getFullName()
                            : "No profile found";
                    return "User found — ID: " + u.getId()
                            + ", Username: " + u.getUsername()
                            + ", Full name: " + fullName
                            + ", Member since: " + u.getCreatedDate();
                })
                .orElse("No user found with ID: " + id);
    }

    @Tool(description = "List all registered users. Returns a summary of all users and their profiles.")
    public String getAllUsers() {
        List<User> users = userService.findAll(new UserQuery(), Pageable.unpaged()).getContent();
        if (users.isEmpty()) return "No users registered yet.";

        StringBuilder sb = new StringBuilder("Registered users:\n");
        for (User u : users) {
            String fullName = u.getUserProfile() != null
                    ? u.getUserProfile().getFullName()
                    : "No profile";
            sb.append("- ID: ").append(u.getId())
                    .append(", Username: ").append(u.getUsername())
                    .append(", Full name: ").append(fullName)
                    .append("\n");
        }
        return sb.toString();
    }

    @Tool(description = "Update the full name of a user's profile by their username.")
    public String updateFullName(String username, String newFullName) {
        return userService.findByUsername(username)
                .map(u -> {
                    if (u.getUserProfile() == null) {
                        return "User " + username + " has no profile to update.";
                    }

                    u.getUserProfile().setFullName(newFullName);
                    userService.save(u);
                    return "Full name updated to '" + newFullName + "' for user: " + username;
                })
                .orElse("No user found with username: " + username);
    }
}