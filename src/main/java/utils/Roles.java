package utils;

public enum Roles {
    SUPERVISOR("supervisor"),
    ADMIN("admin"),
    USER("user");

    private String role;

    Roles(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}