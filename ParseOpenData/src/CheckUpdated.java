import java.io.IOException;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CheckUpdated {

	public static void checkFarmUpdated() throws ClassNotFoundException, SQLException, IOException, JSONException{
		String latestFarmNumer = SelectTable.selectFarmLatestData();
		String jsonFarm = DataParser.readJsonFromUrl("http://data.coa.gov.tw:8080/od/data/api/eir07/?$format=json");
		JSONArray farmArray = new JSONArray(jsonFarm); 
		JSONObject latestFarm = farmArray.getJSONObject(farmArray.length()-1);
		if(latestFarm.get("Number").toString().equals(latestFarmNumer)){
			System.out.println("Farm Data is already latest");
			return;
		}
		else{
			int dataPosition= 0;
			for(int i = farmArray.length()-1; i>=0 ; i--){
				JSONObject tempFarm = farmArray.getJSONObject(i);
				if(tempFarm.get("Number").toString().equals(latestFarmNumer)){
					dataPosition = i;
					break;
				}
			}
			for(int i = dataPosition+1;i<farmArray.length();i++){
				JSONObject tempFarm = farmArray.getJSONObject(i);
				String farm_number = tempFarm.get("Number").toString();
				String farm_name = tempFarm.get("Farm_name").toString();
				String farm_address = ParseUrl.getAddr(farm_number);
				LatAndLng LatAndLng1 = SearchLatAndLng.getLatAndLng(farm_address);
				String farm_lat = "0";
				String farm_lng = "0";
				if(LatAndLng1.Status.equals("OK")){
					farm_lat = LatAndLng1.Lat;
					farm_lng = LatAndLng1.Lng;
					DBConnect.insertFarmIntoDB(farm_number,farm_name, Double.valueOf(farm_lat), Double.valueOf(farm_lng), farm_address);
				}
			}
			System.out.println("Farm Data has finished updating");
			return;
		}
		
	}
	
	public static void checkRiverUpdated(){
		
	}

}
