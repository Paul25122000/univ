package app.services;

import app.components.Component;
import app.promotions.Promotion;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIHandler {

    private static final String API = "https://tonu.rocks/school/AWSMApp/api/";

    public static StringBuffer getRecords(String endpoint, String query) throws IOException {
        StringBuffer getResponse = new StringBuffer();
        URL url = new URL(API + endpoint + query);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            getResponse.append(line);
        }
        reader.close();
        System.out.println(getResponse);
        return getResponse;
    }

    public static StringBuilder makeRequest(String METHOD, String endpoint, String BODY) throws IOException {
        System.out.println(BODY);
        String requestAPI = API + endpoint;
        if (METHOD.equals("UPDATE")) {
            requestAPI = requestAPI + "/update";
            METHOD = "PUT";
        }
        URL url = new URL(requestAPI);
        System.out.println(requestAPI);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(METHOD);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        os.write(BODY.getBytes());
        os.flush();
        os.close();
        int responseCode = conn.getResponseCode();
        StringBuilder response = new StringBuilder();
        System.out.println(" Response Code :  " + responseCode);
        System.out.println(" Response Message : " + conn.getResponseMessage());
        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // print result
            System.out.println(response.toString());
        } else {
            System.out.println(METHOD + " FAILED");
        }
        return response;
    }

    public static String getDashboard() throws IOException {
        StringBuffer getResponse = new StringBuffer();
        URL url = new URL(API);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            getResponse.append(line);
        }
        reader.close();
        return getResponse.toString();
    }

    public static Promotion getPromotion(int system_id) throws IOException, JSONException {
        StringBuffer promotion = APIHandler.getRecords("promotions", "?system_id="+system_id);
        JSONArray jsonArray = new JSONArray(promotion.toString());

        int recordAmount = jsonArray.length();

        for (int i = 0; i < recordAmount; i++) {
            JSONObject record = jsonArray.getJSONObject(i);
            return new Promotion(
                    record.getInt("id"),
                    record.getInt("system_id"),
                    record.getString("category"),
                    record.getString("name"),
                    record.getInt("amount"),
                    record.getString("provider"),
                    record.getString("image")
            );
        }
        return null;
    }
}