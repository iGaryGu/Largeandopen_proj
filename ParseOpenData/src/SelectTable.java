
import java.sql.*;
import java.util.HashMap;
import java.util.Map;


public class SelectTable {

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "org.postgresql.Driver";  
	static final String DB_URL = "jdbc:postgresql://210.61.10.89/Team5";
	
	//  Database credentials
	static final String USER = "Team5";
	static final String PASS = "2013postgres";
	
	public static Map<Integer,String>farmmap = new HashMap<Integer,String>();
	public static Map<String,Double>rivermap = new HashMap<String,Double>();
	
	public static void selectFarm() throws ClassNotFoundException, SQLException{		
		Connection conn = null;
		
		//STEP 2: Register JDBC driver
		Class.forName(JDBC_DRIVER);
		//STEP 3: Open a connection
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		//STEP 4: Execute a query
		String sql = "select  * from farm ";
		PreparedStatement selectkeyword = (PreparedStatement) conn.prepareStatement(sql);
		ResultSet farm = selectkeyword.executeQuery();
		while(farm.next()){
			int id = farm.getInt(1);
			String ID = farm.getString(2);
			String name = farm.getString(3);
			String lat = String.valueOf(farm.getDouble(4));
			String lng = String.valueOf(farm.getDouble(5));
			farmmap.put(id, lat+","+lng);
		}
		
		selectkeyword.close();
	}
	
	public static void selectRiver() throws ClassNotFoundException, SQLException{
		Connection conn = null;
		
		//STEP 2: Register JDBC driver
		Class.forName(JDBC_DRIVER);
		//STEP 3: Open a connection
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		//STEP 4: Execute a query
		String sql = "select  * from river ";
		PreparedStatement selectkeyword = (PreparedStatement) conn.prepareStatement(sql);
		ResultSet river = selectkeyword.executeQuery();
		while(river.next()){
			String lat  = String.valueOf(river.getDouble(2));
			String lon = String.valueOf(river.getDouble(3));
			double pollution = river.getDouble(4);
			rivermap.put(lat+","+lon, pollution);
//			System.out.println("lat="+lat+"lon="+lon+"pollution="+pollution);
		}
		
		selectkeyword.close();
	}
	
	// return the latest date updated
	public static String selectRiverRecentDate() throws ClassNotFoundException, SQLException{
		Connection conn = null;
		
		//STEP 2: Register JDBC driver
		Class.forName(JDBC_DRIVER);
		//STEP 3: Open a connection
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		//STEP 4: Execute a query
		String sql = "select  max(river_sample_date) from river ";
		PreparedStatement selectkeyword = (PreparedStatement) conn.prepareStatement(sql);
		ResultSet river = selectkeyword.executeQuery();
		String RecentDate = "";
		while(river.next()){
			RecentDate = river.getDate(1).toString();
		}
			
		selectkeyword.close();
		return RecentDate;
	}
	
	public static String selectFarmLatestData() throws ClassNotFoundException, SQLException{
		Connection conn = null;
		
		//STEP 2: Register JDBC driver
		Class.forName(JDBC_DRIVER);
		//STEP 3: Open a connection
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		//STEP 4: Execute a query
		String sql = "select * from farm order by farm_id DESC limit 1";
		PreparedStatement selectkeyword = (PreparedStatement) conn.prepareStatement(sql);
		ResultSet farm = selectkeyword.executeQuery();
		String LatestFarmNumber = "";
		while(farm.next()){
			LatestFarmNumber = farm.getString(2).toString();
		}
			
		selectkeyword.close();
		return LatestFarmNumber;
	}
}
