package pbouda.nativeimage.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class Application {

    private static final Duration SOCKET_TIMEOUT = Duration.ofSeconds(10);
    private static final URI ENDPOINT_URI;

    static {
        try {
            ENDPOINT_URI = new URI("http://localhost:8080/notes");
        } catch (URISyntaxException e) {
            throw new RuntimeException("Cannot parse endpoint url", e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();

        httpClient.sendAsync(get(), HttpResponse.BodyHandlers.ofString())
                .handle((resp, ex) -> {
                   if (ex == null) {
                       System.out.println(resp.body());
                   } else {
                       ex.printStackTrace();
                   }
                   return null;
                });

        Thread.currentThread().join();
    }

    private static HttpRequest.Builder requestBuilder() {
        return HttpRequest.newBuilder()
                .timeout(SOCKET_TIMEOUT)
                .uri(ENDPOINT_URI);
    }

    private static HttpRequest post(String body) {
        return requestBuilder()
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
    }

    private static HttpRequest get() {
        return requestBuilder().build();
    }
}
