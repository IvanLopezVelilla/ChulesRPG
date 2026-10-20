package database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AssetService {

    private final String API_URL="https://ventiqkhrs.cc.cd/assets/full";

    private final Gson gson=new Gson();

    public ArrayList<Asset> getAssets() {

        try {

            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();

            BufferedReader reader;

            if (responseCode >= 200 && responseCode < 300) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                System.out.println("HTTP ERROR: " + responseCode);
            }

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();

            System.out.println("API RESPONSE: " + response);

            Type listType = new TypeToken<List<Asset>>() {}.getType();

            return gson.fromJson(response.toString(), listType);

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}





















