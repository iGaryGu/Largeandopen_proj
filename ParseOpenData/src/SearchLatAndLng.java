import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* Purpose : search latitude and longitude 
 * Usage   : call function getLatAndLng and give address as parameter and return LatAndLng Object
 * Example : LatAndLng LatAndLng1 = getLatAndLng(address);
 *           latitude is LatAndLng1.Lat
 *           longitude is LatAndLng1.Lng
 */
public class SearchLatAndLng {

	static int numGoogle = 0; 
	// google only support to search 2500 times a day
	// so we need to check it
	
	public static void main(String[] args) {

	}
	
	
	static JSONObject getOKJSONObject(String info){
		JSONObject json = null;
		try {
			// make sure it doesn't search more than 2000 in order to prevent infinite loop
			numGoogle++;
			if(numGoogle > 2000){
				return null;
			}
			// get URL content
			URL url = new URL(info);
			URLConnection conn = url.openConnection();
 
			// open the stream and put it into BufferedReader
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
 
			String inputLine;
			StringBuffer response = new StringBuffer();
			 
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}

			br.close();
			//System.out.println(response.toString());
			json = new JSONObject(response.toString());
			String Status = json.get("status").toString();
			if(Status.equals("OK")){
				return json;
			}
			else if(Status.equals("OVER_QUERY_LIMIT")){
				// OVER_QUERY_LIMIT means the times of search exceed the limit google support 
				// wait one seconds , because it can only search 10 times a second.
				Thread.sleep(1000);
				return getOKJSONObject(info);
			}
			else{
				return null;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	static LatAndLng getLatAndLng(String Addr){
		String Addr_URLEncode = "";
		String Lat = "";
		String Lng = "";
		LatAndLng LatAndLng1 = null;
		JSONObject json = null;
		try {
			Addr_URLEncode = URLEncoder.encode(Addr, "UTF-8");
			String url_from = "http://maps.googleapis.com/maps/api/geocode/json?address=" + Addr_URLEncode +"&sensor=false";
			json = getOKJSONObject(url_from);
			if(json == null) return new LatAndLng("", "", "error");
			JSONArray Result = json.getJSONArray("results");
			JSONObject Geometry = Result.getJSONObject(0).getJSONObject("geometry");
			JSONObject Location = Geometry.getJSONObject("location");
			Lat = Location.get("lat").toString();
			Lng = Location.get("lng").toString();
			LatAndLng1 = new LatAndLng(Lat, Lng, "OK");
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return LatAndLng1;
	}
}

class LatAndLng {
	String Lat;
	String Lng;
	String Status;
	LatAndLng(String lat, String lng, String status){
		Lat = lat;
		Lng = lng;
		Status = status;
	}
}
