package com.example.mini_game;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class WeatherActivity extends AppCompatActivity {
    private EditText cityInput;
    private TextView weatherInfo;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 20, 20, 20);
        
        TextView title = new TextView(this);
        title.setText("Weather Info");
        title.setTextSize(28);
        title.setTypeface(null, android.graphics.Typeface.BOLD);
        title.setPadding(0, 0, 0, 20);
        layout.addView(title);
        
        cityInput = new EditText(this);
        cityInput.setHint("Enter city name");
        cityInput.setPadding(15, 15, 15, 15);
        layout.addView(cityInput);
        
        Button searchBtn = new Button(this);
        searchBtn.setText("Get Weather");
        searchBtn.setTextSize(18);
        searchBtn.setPadding(0, 15, 0, 15);
        searchBtn.setOnClickListener(v -> searchWeather());
        layout.addView(searchBtn);
        
        weatherInfo = new TextView(this);
        weatherInfo.setTextSize(16);
        weatherInfo.setPadding(0, 20, 0, 0);
        layout.addView(weatherInfo);
        
        Button backBtn = new Button(this);
        backBtn.setText("Back");
        backBtn.setOnClickListener(v -> finish());
        layout.addView(backBtn);
        
        setContentView(layout);
    }
    
    private void searchWeather() {
        String city = cityInput.getText().toString().trim();
        if (city.isEmpty()) {
            Toast.makeText(this, "Enter a city name", Toast.LENGTH_SHORT).show();
            return;
        }
        
        new Thread(() -> {
            try {
                String url = "https://api.open-meteo.com/v1/forecast?latitude=0&longitude=0&current=temperature_2m,weather_code&timezone=auto";
                
                // Using Open-Meteo (free, no API key needed)
                String geoUrl = "https://geocoding-api.open-meteo.com/v1/search?name=" + city + "&count=1&language=en&format=json";
                
                HttpURLConnection geoConn = (HttpURLConnection) new URL(geoUrl).openConnection();
                geoConn.setRequestMethod("GET");
                geoConn.setConnectTimeout(5000);
                
                BufferedReader geoReader = new BufferedReader(new InputStreamReader(geoConn.getInputStream()));
                StringBuilder geoResponse = new StringBuilder();
                String line;
                while ((line = geoReader.readLine()) != null) {
                    geoResponse.append(line);
                }
                geoReader.close();
                
                JSONObject geoJson = new JSONObject(geoResponse.toString());
                if (geoJson.getJSONArray("results").length() == 0) {
                    runOnUiThread(() -> weatherInfo.setText("City not found"));
                    return;
                }
                
                JSONObject result = geoJson.getJSONArray("results").getJSONObject(0);
                double lat = result.getDouble("latitude");
                double lon = result.getDouble("longitude");
                String cityName = result.getString("name");
                String country = result.optString("country", "");
                
                String weatherUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + lat + "&longitude=" + lon + "&current=temperature_2m,weather_code&timezone=auto";
                
                HttpURLConnection weatherConn = (HttpURLConnection) new URL(weatherUrl).openConnection();
                weatherConn.setRequestMethod("GET");
                weatherConn.setConnectTimeout(5000);
                
                BufferedReader weatherReader = new BufferedReader(new InputStreamReader(weatherConn.getInputStream()));
                StringBuilder weatherResponse = new StringBuilder();
                while ((line = weatherReader.readLine()) != null) {
                    weatherResponse.append(line);
                }
                weatherReader.close();
                
                JSONObject weatherJson = new JSONObject(weatherResponse.toString());
                JSONObject current = weatherJson.getJSONObject("current");
                double temp = current.getDouble("temperature_2m");
                
                String finalInfo = String.format("📍 %s, %s\n🌡️ Temperature: %.1f°C", cityName, country, temp);
                runOnUiThread(() -> weatherInfo.setText(finalInfo));
                
            } catch (Exception e) {
                runOnUiThread(() -> weatherInfo.setText("Error: " + e.getMessage()));
            }
        }).start();
    }
}