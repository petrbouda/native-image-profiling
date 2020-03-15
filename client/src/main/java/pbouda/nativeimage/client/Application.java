package pbouda.nativeimage.client;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

import javax.json.Json;
import javax.json.JsonObject;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Application {

    private static final Duration SOCKET_TIMEOUT = Duration.ofSeconds(1);
    private static final URI ENDPOINT_URI;
    private static final URI ENDPOINT_WORDS_URI;

    private static final Lorem LOREM = LoremIpsum.getInstance();

    private static final Duration EXECUTION_TIME = Duration.ofMinutes(10);

    static {
        try {
            ENDPOINT_URI = new URI("http://localhost:8090/notes");
            ENDPOINT_WORDS_URI = new URI("http://localhost:8090/notes/words");
        } catch (URISyntaxException e) {
            throw new RuntimeException("Cannot parse endpoint url", e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(1))
                .build();

        ScheduledExecutorService exitExecutors = Executors.newSingleThreadScheduledExecutor();
        exitExecutors.schedule(() -> {
            System.out.println("Client is about to exit! Time is over! Seconds: " + EXECUTION_TIME.toSeconds());
            System.exit(0);
        }, EXECUTION_TIME.toSeconds(), TimeUnit.SECONDS);

        ScheduledExecutorService getExecutors = Executors.newSingleThreadScheduledExecutor();
        getExecutors.scheduleAtFixedRate(() -> invoke(httpClient, get()), 0, 200, TimeUnit.MILLISECONDS);
        getExecutors.scheduleAtFixedRate(() -> invoke(httpClient, post()), 0, 500, TimeUnit.MILLISECONDS);

        Thread.currentThread().join(Duration.ofMinutes(1).toMillis());
    }

    private static void invoke(HttpClient httpClient, HttpRequest request) {
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.discarding())
                .handle((resp, ex) -> {
                    System.out.println(resp.request().method() + " / " + resp.statusCode());
                    return null;
                });
    }

    private static HttpRequest.Builder requestBuilder() {
        return HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .timeout(SOCKET_TIMEOUT)
                .uri(ENDPOINT_URI);
    }

    private static HttpRequest.Builder requestWordsBuilder() {
        return HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .timeout(SOCKET_TIMEOUT)
                .uri(ENDPOINT_URI);
    }

    private static HttpRequest post() {
        JsonObject json = Json.createObjectBuilder()
                .add("firstname", LOREM.getFirstName())
                .add("lastname", LOREM.getLastName())
                .add("email", LOREM.getEmail())
                .add("subject", LOREM.getTitle(10))
                .add("content", LOREM.getParagraphs(5, 15))
                .build();

        String body = json.toString();

        System.out.println("BYTES: " + body.getBytes().length);

        return requestBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
    }

    private static HttpRequest get() {
        return requestBuilder().build();
    }
}
