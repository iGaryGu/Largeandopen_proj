/*
 * This code is used for parse the data from 
 * http://data.gov.tw/opendata/Details?sno=345000000G-00014 and
 * http://data.gov.tw/opendata/Details?sno=355000000I-00005.
 * 
 * 
 * */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class dataParser {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws JSONException 
	 */
	
	public static void main(String[] args) throws IOException, JSONException {
		// TODO Auto-generated method stub
		String farmData = readFileToString("../farm.txt");	
		String riverData = readFileToString("../river.txt");
		JSONArray farmArray = new JSONArray(farmData); 
		JSONArray riverArray = new JSONArray(riverData);
		parsefarm(farmArray);
		parseriver(riverArray);
		
	}
	
	//extract information from the farm.txt
	public static void parsefarm(JSONArray farmarray) throws JSONException{
		for(int i = 0 ; i < farmarray.length();i++){
			JSONObject object = farmarray.getJSONObject(i);
			String farm_name = object.get("Farm_name").toString();
			String city = object.get("city").toString();
			String area = object.get("area").toString();
			System.out.println(i +" "+farm_name+"+"+city+"+"+area+"|");
			
		}
	}
	
	//extract information from river.txt
	//itemname : pH/Suspended Solid/Dissolved Oxygen/River Pollution Index/NH3-N/Chemical Oxygen Demand/Chloride
	//itemvalue : there are serveral data without value and some data show "<0.01" ,"<0.1"
	
	public static void parseriver(JSONArray riverarray) throws JSONException{
		for(int i = 0 ; i < riverarray.length();i++){
			JSONObject object = riverarray.getJSONObject(i);
			String basin = object.get("Basin").toString();
			String longitude = object.get("TWD97Lon").toString();
			String latitude = object.get("TWD97Lat").toString();
			String itemname = object.get("ItemEngName").toString();
			String itemvalue = object.get("ItemValue").toString();
			String itemunit =  object.get("ItemUnit").toString();
			
			
			System.out.println(i +" "+basin+"+"+longitude+"+"+latitude+"+"+itemname+"+"+itemvalue+"+"+itemunit+"|");
			
		}
	}
	
	
	
	public static String readFileToString(String filePath) throws IOException{
		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[10];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return  fileData.toString();	
	}
	
}
