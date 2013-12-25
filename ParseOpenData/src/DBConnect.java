import java.sql.*;

public class DBConnect {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "org.postgresql.Driver";  
	static final String DB_URL = "jdbc:postgresql://210.61.10.89/Team5";

	//  Database credentials
	static final String USER = "Team5";
	static final String PASS = "2013postgres";

	public static void insertFarmIntoDB(String name, double lat, double lng, String address) {
		Connection conn = null;
		PreparedStatement insertStmt = null;
		try{
			//STEP 2: Register JDBC driver
			Class.forName(JDBC_DRIVER);
			//STEP 3: Open a connection
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			//STEP 4: Execute a query
			String sql = "insert into farm (farm_name, farm_lat, farm_lng, farm_address) values (?,?,?,?)";
			
			insertStmt = conn.prepareStatement(sql);
			insertStmt.setString(1, name);
			insertStmt.setDouble(2, lat);
			insertStmt.setDouble(3, lng);
			insertStmt.setString(4, address);
			insertStmt.executeUpdate();
			
		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}finally{
			//finally block used to close resources
			try{
				if(insertStmt!=null)
					insertStmt.close();
			}catch(SQLException se2){
			}// nothing we can do
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}//end finally try
		}//end try
	}//end function
	
	public static void insertRiverIntoDB(double lat, double lng, double pollution) {
		Connection conn = null;
		PreparedStatement insertStmt = null;
		try{
			//STEP 2: Register JDBC driver
			Class.forName(JDBC_DRIVER);
			//STEP 3: Open a connection
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			//STEP 4: Execute a query
			String sql = "insert into river (river_lat, river_lng, river_pollution) values (?,?,?)";
			
			insertStmt = conn.prepareStatement(sql);
			insertStmt.setDouble(1, lat);
			insertStmt.setDouble(2, lng);
			insertStmt.setDouble(3, pollution);
			insertStmt.executeUpdate();
			
		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}finally{
			//finally block used to close resources
			try{
				if(insertStmt!=null)
					insertStmt.close();
			}catch(SQLException se2){
			}// nothing we can do
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}//end finally try
		}//end try
	}//end function
	
	public static void insertPollutionIntoDB(int f_id, double pollution) {
		Connection conn = null;
		PreparedStatement insertStmt = null;
		try{
			//STEP 2: Register JDBC driver
			Class.forName(JDBC_DRIVER);
			//STEP 3: Open a connection
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			//STEP 4: Execute a query
			String sql = "insert into pollution (farm_id, pollution) values (?,?)";
			
			insertStmt = conn.prepareStatement(sql);
			insertStmt.setInt(1, f_id);
			insertStmt.setDouble(2, pollution);
			insertStmt.executeUpdate();
			
		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}finally{
			//finally block used to close resources
			try{
				if(insertStmt!=null)
					insertStmt.close();
			}catch(SQLException se2){
			}// nothing we can do
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}//end finally try
		}//end try
	}//end function
}//end class
