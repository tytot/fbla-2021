import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.SwingWorker;

class LeaderboardFetcher extends SwingWorker<String, Object> {
	@Override
	public String doInBackground() {				
		try {
		      URL url = new URL(ConnectionDetails.URL);
		      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		      conn.setRequestMethod("GET");
		      BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
		      String result = "";
	          for (String line; (line = reader.readLine()) != null;) {
	              result += line;
	          }
		      return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}