/*
 * Large and Open Source Projects  Group 5
 * member: 陳柏嵩 沈士閔 林俊翰 顧又榮
 * 
 * Date: 11/26
 * 
 * purpose:
 * we try to figure out if the pollution of the river pollute 
 * the food we eat.
 *
 *
 * This class is used for parse the data from 
 * http://data.gov.tw/opendata/Details?sno=345000000G-00014 and
 * http://data.gov.tw/opendata/Details?sno=355000000I-00005.
 * 
 * we use the iterator design pattern 
 * 
 * */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//import json api being used

//used to save farm information
class farminfo{
	String number;
	String name;
	String city;
	String area;
	String address;
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
		// read the open data file from http://data.gov.tw/opendata/Details?sno=345000000G-00014 in JSON
		String jsonfarm = readJsonFromUrl("http://data.coa.gov.tw/od/data/api/eir07/?$format=json");
		// read the open data file from http://data.gov.tw/opendata/Details?sno=355000000I-00005 in JSON
		String jsonriver = readJsonFromUrl("http://opendata.epa.gov.tw/ws/Data/WQXRiver/?format=json");
		
		//convert the string to JSONArray by JSONArray constructor
		JSONArray farmArray = new JSONArray(jsonfarm); 
		JSONArray riverArray = new JSONArray(jsonriver);
		
		//ready to parse the farm and river open data
		parsefarm(farmArray);
		parseriver(riverArray);
	}
	
	public static String readJsonFromUrl(String url) throws IOException, JSONException {
	    InputStream is = new URL(url).openStream();
	    try {
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	      StringBuilder sb = new StringBuilder();
		    int cp;
		    while ((cp = rd.read()) != -1) {
		      sb.append((char) cp);
		    }
		    return sb.toString();
	    } finally {
	      is.close();
	    }
	  }
	
	//extract information from the farm.txt
	//and save these information
	public static void parsefarm(JSONArray farmarray) throws JSONException{
		List<farminfo> list = new ArrayList<farminfo>();
		String temp = "";
		for(int i = 0 ; i < farmarray.length();i++){
			farminfo farminfo = new farminfo();
			JSONObject object = farmarray.getJSONObject(i);
			String farm_number = object.get("Number").toString();
			String farm_name = object.get("Farm_name").toString();
			String city = object.get("city").toString();
			String area = object.get("area").toString();
			//System.out.println(i +" "+farm_name+"+"+city+"+"+area+"|");
			
			farminfo.number = farm_number;
			farminfo.name = object.get("Farm_name").toString();
			farminfo.city = object.get("city").toString();
			farminfo.area = object.get("area").toString();
			//get address by parse the web page
			farminfo.address = parseUrl.getAddr(farm_number);
			
			System.out.println(farminfo.name+" address: "+farminfo.address);
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
			System.out.println("name = "+info.name + "city = " + info.city + "area = "+info.area + info.address);
		}
	}
	
	
	//extract information from river.txt
	//and save these information
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
			//System.out.println("name = "+info.basin + "longitude = " + info.longitude + "latitude = "+info.latitude + "itemname =" + info.itemname+"itemvalue = " + info.itemvalue+"itemunit = " + info.itemunit);
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
