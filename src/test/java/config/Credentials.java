package config;

import java.util.UUID;

/** Pairs login/password. Password is masking in toString, for excluding from logs and reports */
public record Credentials(String username, String password) {

    public static Credentials valid() {
        return new Credentials(ConfigReader.required("auth.username"),
                ConfigReader.required("auth.password"));
    }

    public static Credentials withWrongPassword() {
        return new Credentials(valid().username(), random());
    }

    public static Credentials withWrongUsername() {
        return new Credentials(random(), valid().password());
    }

    public static Credentials wrong() {
        return new Credentials(random(), random());
    }

    private static String random() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    @Override
    public String toString() {
        return "Credentials[username=" + username + ", password=***]";
    }
}
