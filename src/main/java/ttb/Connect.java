package ttb;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

public class Connect {

	public static void main(String[] args) {
		
		Connection con = null;
		Statement st = null; 
		ResultSet rs = null;
		Scanner sc = new Scanner(System.in);
		
		String url = "jdbc:mysql://localhost:3306/ttb";
		
		Properties propietatsConnexio = new Properties();
		propietatsConnexio.put("user", "root");
		propietatsConnexio.put("password", "super3");
		propietatsConnexio.put("serverTimezone", "GMT+1");
		
		
		try {
			con = DriverManager.getConnection(url, propietatsConnexio);
			System.out.println("Base de dades connectada!");
			
			int lavida = 4;
			
			while(lavida != 0)
			{
				System.out.println("1) Veure Taules i Columnes\n"
						+ "2) Veure Dades\n"
						+ "0) Sortir");
				
				lavida = sc.nextInt();
				
				switch(lavida) 
				{
				case 1:
					DatabaseMetaData mt = con.getMetaData();
					String tableNamePattern  = "%";
					String[] types = {"TABLE"};
					ResultSet resultSet = null;
					System.out.println("Taules :");
					rs = mt.getTables("ttb", null, tableNamePattern, types);
					while (rs.next()) {
						int numColumns = rs.getMetaData().getColumnCount();
						
						String tableName = rs.getString(3);
						System.out.println(tableName);
						int count = tableName.length();
						for(int i = 0; i < count; i++)
						{
						System.out.print("-");
						}
						System.out.println();
						resultSet = mt.getColumns("ttb", null, tableName, "%");
						while (resultSet.next()) {
						System.out.println(resultSet.getString(4));
						
						}
						System.out.println();
						
					}
					break;
				case 2:
					sc.nextLine();
					String tabla = sc.nextLine();
					st = con.createStatement();
					rs = st.executeQuery("SELECT * FROM "+tabla);
					
					System.out.println("Dades de la taula "+tabla+":");
					while (rs.next()) {
						 int numColumns = rs.getMetaData().getColumnCount();
						 for ( int i = 1 ; i <= numColumns ; i++ ) 
						 	{
				               System.out.println( "" + rs.getObject(i));
				            }
					}
					break;
				case 0:
					break;
				}
				
			}

		} catch (SQLException e) {
 
			System.err.println("Error d'apertura de connexio: " + e.getMessage());

		} finally {
			
			

			if (con != null)
				try {
					
					con.close();

				} catch (SQLException e) {
					System.err.println("Error de tancament de connexi�: " + e.getMessage());
				}
		}
	}

}
