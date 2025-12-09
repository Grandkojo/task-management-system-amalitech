package models;

/**
 * Admin user with elevated permissions (can delete tasks).
 */
public class AdminUser extends User {

    public AdminUser(String name, String email) {
        super(name, email, "Admin");
    }

    @Override
    public boolean canDeleteTasks() {
        return true;
    }
}


