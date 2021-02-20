import framework.annotation.Lunatic;
import framework.annotation.Polite;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Lunatic
public class RandomnessHttpSource implements RandomnessSource {
    @Override
    @Polite(after = "Bonne journée !")
    public double nextUniformRandom() {
        try {
            HttpRequest request = HttpRequest
                    .newBuilder(URI.create("http://www.randomnumberapi.com/api/v1.0/random"))
                    .build();

            String body = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString())
                    .body();

            return Double.parseDouble(body.substring(0, body.length() - 1).substring(1));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return Double.NaN;
        }
    }
}
