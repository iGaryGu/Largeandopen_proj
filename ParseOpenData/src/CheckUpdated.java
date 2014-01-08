import java.io.IOException;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CheckUpdated {

	public static boolean checkFarmUpdated() throws ClassNotFoundException, SQLException, IOException, JSONException{
		// get the latest number of data inserted 
		String latestFarmNumer = SelectTable.selectFarmLatestData();
		String jsonFarm = DataParser.readJsonFromUrl("http://data.coa.gov.tw:8080/od/data/api/eir07/?$format=json");
		JSONArray farmArray = new JSONArray(jsonFarm); 
		JSONObject latestFarm = farmArray.getJSONObject(farmArray.length()-1);
		
		// if true means the data is already latest
		if(latestFarm.get("Number").toString().equals(latestFarmNumer)){
			System.out.println("Farm Data is already latest");
			return false;
		}
		else{
			// add data not inserted yet
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
			return true;
		}
		
	}
	
	public static boolean checkRiverUpdated() throws IOException, JSONException, ClassNotFoundException, SQLException{
		// get the latest Sample Date of data inserted 
		String latestSampleDate = SelectTable.selectRiverRecentDate();
		String jsonRiver = DataParser.readJsonFromUrl("http://opendata.epa.gov.tw/ws/Data/WQXRiver/?%24orderby=SampleDate+desc&%24skip=0&%24top=1000&format=json");
		JSONArray riverArray = new JSONArray(jsonRiver);
		JSONObject firstRiver = riverArray.getJSONObject(0);
		// if true means the data is already latest
		if(firstRiver.get("SampleDate").toString().equals(latestSampleDate)){
			System.out.println("River Data is already latest");
			return false;
		}
		else{
			for(int i = 0 ; i < riverArray.length();i++){
				JSONObject tempRiver = riverArray.getJSONObject(i);
				if(tempRiver.get("SampleDate").toString().equals(latestSampleDate)){
					break;
				}
				if(tempRiver.get("ItemEngName").toString().equals("River Pollution Index")){
					DBConnect.insertRiverIntoDB(Double.parseDouble(tempRiver.get("TWD97Lat").toString()), Double.parseDouble(tempRiver.get("TWD97Lon").toString()), Double.parseDouble(tempRiver.get("ItemValue").toString()), tempRiver.get("SampleDate").toString());
				}
			}
			System.out.println("Farm Data has finished updating");
			return true;
		}
	}

}
