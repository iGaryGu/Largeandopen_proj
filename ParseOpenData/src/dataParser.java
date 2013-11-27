/*
 * This code is used for parse the data from 
 * http://data.gov.tw/opendata/Details?sno=345000000G-00014 and
 * http://data.gov.tw/opendata/Details?sno=355000000I-00005.
 * 
 * we use the iterator design pattern 
 * 
 * */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//used to save farm information
class farminfo{
	String name;
	String city;
	String area;
}
//used to save river information
class riverinfo{
	String basin;
	String longitude;
	String latitude;
	String itemname;
	String itemvalue;
	String itemunit;
}
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
//		parseriver(riverArray);
	}
	
	//extract information from the farm.txt
	public static void parsefarm(JSONArray farmarray) throws JSONException{
		List<farminfo> list = new ArrayList<farminfo>();
		String temp = "";
		for(int i = 0 ; i < farmarray.length();i++){
			farminfo farminfo = new farminfo();
			JSONObject object = farmarray.getJSONObject(i);
			farminfo.name = object.get("Farm_name").toString();
			farminfo.city = object.get("city").toString();
			farminfo.area = object.get("area").toString();
			//skip the same farm information
			if(!temp.equals(farminfo.name)){
				list.add(farminfo);
			}
			temp = farminfo.name;
		}
		
		//get the farm information
		//and use iterator design pattern
		Iterator<farminfo> iter = list.iterator();
		while(iter.hasNext()){
			farminfo info = iter.next();
			System.out.println("name = "+info.name + "city = " + info.city + "area = "+info.area );
		}
	}
	
	
	//extract information from river.txt
	//itemname : pH/Suspended Solid/Dissolved Oxygen/River Pollution Index/NH3-N/Chemical Oxygen Demand/Chloride
	//itemvalue : there are serveral data without value and some data show "<0.01" ,"<0.1"
	
	public static void parseriver(JSONArray riverarray) throws JSONException{
		List<riverinfo>list = new ArrayList<riverinfo>();
		for(int i = 0 ; i < riverarray.length();i++){
			riverinfo riverinfo = new riverinfo();
			JSONObject object = riverarray.getJSONObject(i);
			riverinfo.basin = object.get("Basin").toString();
			riverinfo.longitude = object.get("TWD97Lon").toString();
			riverinfo.latitude = object.get("TWD97Lat").toString();
			riverinfo.itemname = object.get("ItemEngName").toString();
			riverinfo.itemvalue = object.get("ItemValue").toString();
			riverinfo.itemunit =  object.get("ItemUnit").toString();
			list.add(riverinfo);
		}
		
		//get tne river information
		//and use iterator design pattern
		Iterator<riverinfo> iter = list.iterator();
		while(iter.hasNext()){
			riverinfo info = iter.next();
			System.out.println("name = "+info.basin + "longitude = " + info.longitude + "latitude = "+info.latitude + "itemname =" + info.itemname+"itemvalue = " + info.itemvalue+"itemunit = " + info.itemunit);
		}
	}
	
	//read the file and convert it to string
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

