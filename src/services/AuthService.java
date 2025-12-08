package services;

import models.User;

/**
 * Simple holder for the currently logged-in user.
 * This keeps authentication concerns separate from UI and services.
 */
public class AuthService {

    private static User currentUser;
    private static User[] availableUsers = new User[0];

    public static void login(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setAvailableUsers(User... users) {
        availableUsers = users;
    }

    public static User[] getAvailableUsers() {
        return availableUsers;
    }
}


