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
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//import json api being used

//used to save farm information
class farmInfo{
        String number;
        String name;
        String city;
        String area;
        String address;
        String Lat;
        String Lng;
}
//used to save river information
class riverInfo{
        String basin;
        String longitude;
        String latitude;
        String itemname;
        String itemvalue;
        String itemunit;
        String SampleDate;
}

class pollutionInfo{
        String name;
        Double latitude;
        Double longitude;
        Double pollution;
        int level;
}

public class DataParser {

        /**
         * @param args
         * @throws IOException 
         * @throws JSONException 
         */
        
        public static void main(String[] args) throws IOException, JSONException, ClassNotFoundException, SQLException {
                // TODO Auto-generated method stub
                                
                        // read the open data file from http://data.gov.tw/opendata/Details?sno=345000000G-00014 in JSON
                //String jsonFarm = readJsonFromUrl("http://data.coa.gov.tw:8080/od/data/api/eir07/?$format=json");
                        // convert the string to JSONArray by JSONArray constructor
                //JSONArray farmArray = new JSONArray(jsonFarm); 
                        // parse the farm open data
                //parseFarm(farmArray);
                
                // run ten webs for 10,000 data because each web has only 1,000 data
                /*for(int i=0;i<10;i++){
                                // read the open data file from http://data.gov.tw/opendata/Details?sno=355000000I-00005 in JSON
                        String jsonRiver = readJsonFromUrl("http://opendata.epa.gov.tw/ws/Data/WQXRiver/?%24orderby=SampleDate+desc&%24skip="+String.valueOf(i*1000)+"&%24top=1000&format=json");
                                //convert the string to JSONArray by JSONArray constructor
                        JSONArray riverArray = new JSONArray(jsonRiver);
                                //parse the river open data
                        parseRiver(riverArray);
                }*/
        	boolean update_farm = false;
        	boolean update_river = false;
                        // check if open data has updated and refresh the Database
                update_farm = CheckUpdated.checkFarmUpdated();
                update_river = CheckUpdated.checkRiverUpdated();
                
                //integrate two open data to insert to pollution table
                if(update_farm == true||update_river==true){
                	deleteTable.deletePollution();
                	farmPollution();
                }
                writePollutionIntoFile();
				System.out.println("all end");
                
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
        public static void parseFarm(JSONArray farmarray) throws JSONException{
                List<farmInfo> list = new ArrayList<farmInfo>();
                String temp = "";
                for(int i = 0 ; i < farmarray.length();i++){
                        farmInfo farm = new farmInfo();
                        JSONObject object = farmarray.getJSONObject(i);
                        String farm_number = object.get("Number").toString();
                        //System.out.println(i +" "+farm_name+"+"+city+"+"+area+"|");
                        
                        farm.number = farm_number;
                        farm.name = object.get("Farm_name").toString();
                        farm.city = object.get("city").toString();
                        farm.area = object.get("area").toString();
                        
                        //skip the same farm information
                        if(!temp.equals(farm.name)){
                                //get address by parse the web page
                                farm.address = ParseUrl.getAddr(farm_number);
                                // get longitude and latitude by google api
                                LatAndLng LatAndLng1 = SearchLatAndLng.getLatAndLng(farm.address);
                                if(LatAndLng1.Status.equals("OK")){
                                        farm.Lat = LatAndLng1.Lat;
                                        farm.Lng = LatAndLng1.Lng;
                                }
                                else if(LatAndLng1.Status.equals("error")){
                                        farm.Lat = "0";
                                        farm.Lng = "0";
                                }
                        
                                System.out.println("number = "+farm.number + "name = "+farm.name + "Lat = " + farm.Lat + "Lng = "+farm.Lng + farm.address);
                                list.add(farm);
                        }
                        temp = farm.name;
                }
                
                //get the farm information
                //and use iterator design pattern
                Iterator<farmInfo> iter = list.iterator();
                while(iter.hasNext()){
                        farmInfo info = iter.next();
                        //insert the farm information to database
            DBConnect.insertFarmIntoDB(info.number,info.name,Double.valueOf(info.Lat),Double.valueOf(info.Lng),info.address);
                        System.out.println("name = "+info.name + "city = " + info.city + "area = "+info.area + info.address);
                }
        }
        
        
        //extract information from river.txt
                //and save these information
                //itemname : pH/Suspended Solid/Dissolved Oxygen/River Pollution Index/NH3-N/Chemical Oxygen Demand/Chloride
                //itemvalue : there are serveral data without value and some data show "<0.01" ,"<0.1"
                
                public static void parseRiver(JSONArray riverarray) throws JSONException{
                        List<riverInfo>list = new ArrayList<riverInfo>();
                        String rivertemp="";
                        String rivertemp2="";
                        String rivertemp3="";
                        for(int i = 0 ; i < riverarray.length();i++){
                                riverInfo river = new riverInfo();
                                JSONObject object = riverarray.getJSONObject(i);
                                river.basin = object.get("Basin").toString();
                                river.longitude = object.get("TWD97Lon").toString();
                                river.latitude = object.get("TWD97Lat").toString();
                                river.itemname = object.get("ItemEngName").toString();
                                river.itemvalue = object.get("ItemValue").toString();
                                river.itemunit =  object.get("ItemUnit").toString();
                                river.SampleDate = object.get("SampleDate").toString();
                                if(rivertemp.equals(river.basin) && rivertemp2.equals(river.longitude) && rivertemp3.equals(river.latitude)){
                                        list.add(river);
                                }
                                else{
                                        riverPollution(list);
                                        rivertemp = river.basin;
                                        rivertemp2 = river.longitude;
                                        rivertemp3 = river.latitude;
                                        list.clear();
                                }
                        }
                        riverPollution(list);
                }
                
                //get the river pollution level and insert to river table
                public static void riverPollution(List<riverInfo>list){
                        Map<String,Double>map = new HashMap<String,Double>();
                        Iterator<riverInfo>iter = list.iterator();
                        String lnglat = "";
                        String basin = null;
                        while(iter.hasNext()){
                                riverInfo info = iter.next();
                                basin = info.basin;
                                lnglat = info.longitude+","+info.latitude+","+info.SampleDate;
                                if(info.itemname.equals("River Pollution Index")){
                                        map.put(lnglat,Double.parseDouble(info.itemvalue));
                                }
                        }
                        Set<String> lnglattemp = map.keySet();
                        Iterator it = lnglattemp.iterator();
                        while(it.hasNext()){
                                String lng = it.next().toString();
                                double level = map.get(lng);
                                String[] lngtemp = lng.split(",");
                                DBConnect.insertRiverIntoDB(Double.parseDouble(lngtemp[1]), Double.parseDouble(lngtemp[0]), level, lngtemp[2]);
                        }
                }
                
                
                //integrate the two open data to insert to the pollutione table
                public static void farmPollution() throws ClassNotFoundException, SQLException{
                        SelectTable.selectFarm();
                        SelectTable.selectRiver();
                        
                        //pollution weight
                        double weight = 0; 
                        int level;
                        
                        Map<Integer,String> farm = SelectTable.farmmap;
                        Map<String,Double> river = SelectTable.rivermap;
                        
                        Set<Integer> lat = farm.keySet();
                        Set<String> latlng = river.keySet();
                        Iterator itr = lat.iterator();
                        
                        while(itr.hasNext()){
                                int id = (int) itr.next();
                                String latlng_id = farm.get(id);
                                String[] lalg = latlng_id.split(",");
                                double lattmp = Double.parseDouble(lalg[0]);
                                double lngtmp = Double.parseDouble(lalg[1]);
                                Iterator riveritr = latlng.iterator();
                                while(riveritr.hasNext()){
                                        String latlngtemp = riveritr.next().toString();
                                        double pollution_value  = river.get(latlngtemp);
                                        String[] temp = latlngtemp.split(",");
                                        double riverlat = Double.parseDouble(temp[0]);
                                        double riverlng = Double.parseDouble(temp[1]);
                                        
                                        double distance = DistanceCalculator.getDistance(lattmp, lngtmp, riverlat, riverlng);
//                                        System.out.println("farm_lattmp="+lattmp+"farm_lngtmp="+lngtmp+"riverlat="+riverlat+"riverlng="+riverlng+"distance"+distance+"pollution_value="+pollution_value);
                                        if(distance<2000){
                                                weight+=pollution_value*10;
                                        }
                                        else if(distance>2000 && distance<10000){
                                                weight+=pollution_value*5;
                                        }
                                        else{
                                                weight+=0;
                                        }
                                }
                                if(weight >= 500){
                                        level = 1;
                                }else if(weight<500&&weight>400){
                                        level = 2;
                                }else if(weight<400&&weight>300){
                                        level = 3;
                                }else if(weight<300&&weight>200){
                                        level = 4;
                                }else{
                                        level = 5;
                                }
//                                System.out.println("id="+id+"weight="+weight+"level="+level);
                                DBConnect.insertPollutionIntoDB(id, weight,level);
                                weight = 0;
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
        
        //write the result into file
        public static void writePollutionIntoFile() throws ClassNotFoundException, SQLException, JSONException, IOException{
                ArrayList<pollutionInfo> result = SelectTable.selectPollutionResult();
                Iterator<pollutionInfo>iter = result.iterator();
                JSONArray resultArray = new JSONArray();
                while(iter.hasNext()){
                        pollutionInfo info = iter.next();
                        JSONObject tempObject = new JSONObject();
                        tempObject.put("name", info.name);
                        tempObject.put("latitude", info.latitude);
                        tempObject.put("longitude", info.longitude);
                        tempObject.put("pollution", info.pollution);
                        tempObject.put("level", info.level);
                        resultArray.put(tempObject);
                }
                //System.out.println(resultArray);
                FileWriter file = new FileWriter("PollutionInfo.txt");
                file.write(resultArray.toString());
                file.flush();
                file.close();
        }
}
