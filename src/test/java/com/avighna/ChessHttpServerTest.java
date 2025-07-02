package com.avighna;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.URI; // idk waht i need fix later
import java.net.http.*;
import org.junit.jupiter.api.Test;

class ChessHttpServerTest {
  @Test
  public void helloWorldTest() throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();

    HttpRequest req = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080")).build();

    HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
  }
}
