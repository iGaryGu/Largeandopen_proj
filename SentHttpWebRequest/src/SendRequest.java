import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SendRequest {

	private final String USER_AGENT = "Mozilla/5.0";
	
	public static void main(String[] args) {
		
		SendRequest http = new SendRequest();
		String farm_name = "耕心有機農場";

		try {
			http.sendPost(farm_name);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
		private void sendPost(String farm_name) throws Exception {
 
			// request url 
			String url = "http://www.i-organic.org.tw/SearchResult.aspx";
			
			// encoding text to URLEnCode
			String name_URLEncode = URLEncoder.encode(farm_name, "UTF-8");
			
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	 
			// add request header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
	 
			String urlParameters = "hdnOp=SearchModeII&Cities=&Downtown=&GovType=&CropType=&Crop=&Depts=&FarmName=" + name_URLEncode + "&QMD05=&ResponseName=&CropName=&CASNo=&passAddress=&farmAddress";
	 
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
	 
			int responseCode = con.getResponseCode();
			System.out.println("Send POST request to URL : " + url);
			System.out.println("Post parameters : " + urlParameters);
			System.out.println("Response Code : " + responseCode);
	 
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
	 
			//print result
			System.out.println(response.toString());
	 
		}

}
