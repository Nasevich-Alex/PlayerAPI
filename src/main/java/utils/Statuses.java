package utils;

public enum Statuses {
    OK(200),
    CREATED(201),
    NO_CONTENT(204),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404);

    private int code;

    Statuses(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}