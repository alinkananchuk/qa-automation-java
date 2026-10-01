package config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Config sources (highest to lowest priority):
 * 1) system property           -Dauth.password=...
 * 2) environment variable       AUTH_PASSWORD
 * 3) secrets.properties        (project root, not committed, optional)
 * 4) config.properties         (classpath: src/main/resources, required)
 */
public final class ConfigReader {

    private static final String CONFIG_FILE = "config.properties";
    private static final String SECRETS_FILE = "secrets.properties";
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (in == null) {
                throw new IllegalStateException(CONFIG_FILE + " was not found on the classpath. "
                        + "It must be located in src/main/resources/ (target/classes/ after the build)");
            }
            try (Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                PROPS.load(reader);
            }
            Path secrets = Path.of(SECRETS_FILE);
            if (Files.exists(secrets)) {
                try (Reader reader = Files.newBufferedReader(secrets, StandardCharsets.UTF_8)) {
                    PROPS.load(reader);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read configuration", e);
        }
    }

    private ConfigReader() {
    }

    public static String get(String key) {
        String value = System.getProperty(key);
        if (isBlank(value)) {
            value = System.getenv(key.toUpperCase().replace('.', '_'));
        }
        if (isBlank(value)) {
            value = PROPS.getProperty(key);
        }
        return isBlank(value) ? null : value.trim();
    }

    public static String required(String key) {
        String value = get(key);
        if (value == null) {
            throw new IllegalStateException("Parameter '" + key + "' is not specified. Keys found in "
                    + CONFIG_FILE + ": " + PROPS.stringPropertyNames()
                    + ". Check the key name or provide -D" + key
                    + " / environment variable " + key.toUpperCase().replace('.', '_'));
        }
        return value;
    }

    public static String baseUrl() {
        return required("base.url");
    }

    public static String basicAuthPath() {
        return required("basic.auth.path");
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}