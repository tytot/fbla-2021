import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.SwingWorker;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

class LeaderboardPoster extends SwingWorker<String, Object> {
	
	private String name;
	private int timeDecis;
	
	LeaderboardPoster(String name, int timeDecis) {
		this.name = name;
		this.timeDecis = timeDecis;
	}
	
	@Override
	public String doInBackground() {				
		try {
			JsonObject body = Json.object().add("name", name).add("time", timeDecis);
			String bodyString = body.toString();

			byte[] bodyBytes = bodyString.getBytes("utf-8");
			
			URL url = new URL(ConnectionDetails.URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);
			OutputStream os = conn.getOutputStream();
			os.write(bodyBytes);
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String result = "";
			for (String line; (line = reader.readLine()) != null;) {
				result += line;
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}