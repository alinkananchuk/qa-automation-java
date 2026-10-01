package api;

import config.ConfigReader;
import config.Credentials;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class BasicAuthClient {
    private final HttpClient client = HttpClient.newHttpClient();
    private final URI url = URI.create(ConfigReader.baseUrl() + ConfigReader.basicAuthPath());

    public int getStatusCode(Credentials credentials) {
        return send(credentials).statusCode();
    }

    public int getStatusCodeWithoutAuth() {
        return send(null).statusCode();
    }

    private HttpResponse<String> send(Credentials credentials) {
        HttpRequest.Builder request = HttpRequest.newBuilder(url).GET();
        if (credentials != null) {
            String raw = credentials.username() + ":" + credentials.password();
            request.header("Authorization",
                    "Basic " + Base64.getEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8)));
        }
        try {
            return client.send(request.build(), HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new IllegalStateException("Error of HTTP-request", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Request interrupted", e);
        }
    }

}
