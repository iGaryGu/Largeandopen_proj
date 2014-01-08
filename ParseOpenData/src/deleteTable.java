import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class deleteTable {
	// JDBC driver name and database URL
		static final String JDBC_DRIVER = "org.postgresql.Driver";  
		static final String DB_URL = "jdbc:postgresql://210.61.10.89/Team5";

		//  Database credentials
		static final String USER = "Team5";
		static final String PASS = "2013postgres";
		
		public static void deletePollution(){
			Connection conn = null;
			Statement stmt;
			
			//STEP 2: Register JDBC driver
			try {
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				stmt = conn.createStatement();
                String query = "delete from pollution";
                int deletedRows=stmt.executeUpdate(query);
                if(deletedRows>0){
                    System.out.println("Deleted All Rows In The Table Successfully...");
                }else{
                	System.out.println("Table already empty."); 
                }
			
			} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
			}
			//STEP 3: Open a connection
			
			
		}
}
