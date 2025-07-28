import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AsyncPostClient {
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final String URL = "http://localhost:8080/trigger";

    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable task = () -> {
            String payload = "trigger-input-" + System.currentTimeMillis();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL))
                    .header("Content-Type", "text/plain")
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        System.out.println("Status: " + response.statusCode() + " | Body: " + response.body());
                    })
                    .exceptionally(ex -> {
                        System.out.println("Exception: " + ex.getMessage());
                        return null;
                    });
        };

        scheduler.scheduleAtFixedRate(task, 0, 500, TimeUnit.MILLISECONDS);

        // Optional shutdown after 20 seconds (20 milliseconds was probably too small)
        scheduler.schedule(() -> {
            System.out.println("Shutting down client...");
            scheduler.shutdown();
        }, 20, TimeUnit.SECONDS); // ⬅️ Changed to 20 SECONDS from 20 MILLISECONDS
    }
}
