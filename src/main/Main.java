package main;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        JsonParser jp = new JsonParser();

        String apiKey = "1234";
        String strUrlApi = "https://v6.exchangerate-api.com/v6/"+ apiKey + "/pair/";

        Map<String, String> currencyCodes = new HashMap<>();
        currencyCodes.put("colombia","COP");
        currencyCodes.put("united states","USD");
        currencyCodes.put("argentina","ARS");
        currencyCodes.put("europe","EUR");
        currencyCodes.put("canada","CAD");
        currencyCodes.put("egypt", "EGP");

        while (true){
            System.out.println("Convert from ");
            System.out.println(currencyCodes.values());
            String from_choice = scanner.next().trim().toUpperCase();

            System.out.println("Convert to ");
            System.out.println(currencyCodes.values());
            String to_choice = scanner.next().trim().toUpperCase();
            System.out.println("Type corresponding amount");
            double qty = Double.parseDouble(scanner.next().trim());
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(strUrlApi+from_choice+"/"+to_choice+"/"+qty))
                        .build();
                HttpResponse<InputStream> response = client
                        .send(request, HttpResponse.BodyHandlers.ofInputStream());

                JsonElement root = jp.parse(new InputStreamReader(response.body()));
                JsonObject jsonResponse = root.getAsJsonObject();
                String strResponse = jsonResponse.get("conversion_result").getAsString();
                System.out.println(strResponse + " " + to_choice);
                System.out.println("Do you wish to continue? (Y/N)");
                String flag = scanner.next();
                if (flag.equalsIgnoreCase("n")){
                    System.out.println("Finalizing program...");
                    break;
                } else if (!flag.equalsIgnoreCase("y")) {
                    System.out.println("Invalid input. Finalizing program...");
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

    }
}
