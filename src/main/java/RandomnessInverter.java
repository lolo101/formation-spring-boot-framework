import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RandomnessInverter {
    public static void main(String... args) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest
                .newBuilder(URI.create("http://www.randomnumberapi.com/api/v1.0/random"))
                .build();

        String body = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString())
                .body();

        double randomNumber = Double.parseDouble(body.substring(0, body.length() - 1).substring(1));

        System.out.println(1.0 / randomNumber);
    }
}
