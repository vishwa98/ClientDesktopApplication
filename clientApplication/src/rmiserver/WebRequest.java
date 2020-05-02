package rmiserver;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import common.SensorInfo;

public class WebRequest {
	
	// OAuth 2.0 
	private static String adminToken = "";
	private final static String BASE_URL = "https://fire-alarm-api-ds.herokuapp.com";
	private final static String CLIENT_ID = "2";
	private final static String CLIENT_SECRET = "geanPhlSATg433c1Uf7qbzBb7gSjhJeTSrlEoDdd";	
	// make web request to login
	public static boolean checkLogin(String username, String password) throws IOException {
		Gson gson = new Gson();
        String requestBody = gson.toJson(new TokenRequest("password", CLIENT_ID, CLIENT_SECRET, username, password));
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL+"/oauth/token"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = null;
		try {
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		if(response == null) return false;
		
		String res = response.body();
		
		Token token = gson.fromJson(res, Token.class);
		
		if(token.error != null) {
			System.out.println("Username or Password is wrong!!");
			return false;
		}
		else {
			adminToken = token.access_token;
			System.out.println("Admin authentication token has been retrieved...");
			System.out.println("Authentication successful...");
			return true;
		}
	}
	
	// logout locally
	public static void logout() {
		adminToken = "";
	}
	
	// retrieve current list of sensor information
	public static List<SensorInfo> getSensorInfo(){
		String url = BASE_URL+"/api/sensorinfo";
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder(URI.create(url)).build();
		
		List<SensorInfo> data = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
		.thenApply(HttpResponse::body)
		.thenApply(WebRequest::parse)
		.join();
		return data;
	}
	
	// update current sensor information
	public static void updateSensorInfo(int id, SensorInfo updatedSensor) {
		// if not logged in, return
		if(adminToken.length() == 0) { 
			System.out.println("Not logged in...");
			return;
		}

		
		Gson gson = new Gson();
        String requestBody = gson.toJson(updatedSensor);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL+"/api/update/"+id))
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer "+adminToken)
                .build();

        HttpResponse<String> response = null;
		try {
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String res = response.body();
		System.out.println("Updated = "+ res);
	}
	
	// add new sensor
	public static void addSensor(SensorInfo newSensor) {
		// if not logged in, return
		if(adminToken.length() == 0) { 
			System.out.println("Not logged in...");
			return;
		}

		
		Gson gson = new Gson();
        String requestBody = gson.toJson(newSensor);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL+"/api/sensorinfo"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer "+adminToken)
                .build();

        HttpResponse<String> response = null;
		try {
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		String res = response.body();
		System.out.println("Added = "+ res);
	}
		
	// delete the sensor by ID
	public static void deleteSensorById(int id) {
		// if not logged in, return
		if(adminToken.length() == 0) { 
			System.out.println("Not logged in...");
			return;
		}
		
		String url = BASE_URL + "/api/sensorinfo/"+id;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer "+adminToken)
                .build();

        HttpResponse<String> response = null;
		try {
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		if(response == null) {
			System.out.println("Error when sending the delete request...");
			return;
		}
		String res = response.body();
		if(res != null)System.out.println("Deleted the sensor "+ res);
		else System.out.println("Error in deleting the sensor...");
	}
		
	
	
	
	
	
	

// TESTING
//	public static void main(String[] args) {
//		try {
//			System.out.println("Access Granted: "+checkLogin("admin@admin.com", "password"));
////			addSensor(new SensorInfo(1,5,5,5,5,true, "",""));
////			updateSensorInfo(0, new SensorInfo(1,10,10,10,10,true, "",""));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	// parse the JSON data list
	private static List<SensorInfo> parse(String json) {
		Gson gson = new Gson();
		Type collectionType = new TypeToken<List<SensorInfo>>(){}.getType();
		List<SensorInfo> obj = gson.fromJson(json, collectionType);
		return obj;
	}

}
