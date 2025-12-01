package models;

/**
 * Abstract user of the system. Stores identity data and role,
 * and exposes a hook for role-based permissions.
 */
public abstract class User {
    private static int userCount = 0;

    protected String id;
    protected String name;
    protected String email;
    protected String role;

    public User(String name, String email, String role) {
        this.id = generateUserId();
        this.name = name;
        this.email = email;
        this.role = role;
    }

    private static String generateUserId() {
        userCount++;
        return "USR-" + userCount;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getDisplayLabel() {
        return name + " (" + role + ")";
    }

    // Permission hook – subclasses provide behavior
    public abstract boolean canDeleteTasks();
}
