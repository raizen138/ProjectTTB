package ttb;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

public class Connect {

	public static void main(String[] args) {
		
		Connection con = null;
		Statement st = null; 
		ResultSet rs = null;
		ResultSet resultSet = null;
		DatabaseMetaData mt = null;
		Scanner sc = new Scanner(System.in);
		
		String url = "jdbc:mysql://localhost:3306/ttb";
		
		Properties propietatsConnexio = new Properties();
		propietatsConnexio.put("user", "root");
		propietatsConnexio.put("password", "super3");
		propietatsConnexio.put("serverTimezone", "GMT+1");
		
		
		try {
			con = DriverManager.getConnection(url, propietatsConnexio);
			System.out.println("Base de dades connectada!");
			mt = con.getMetaData();
			
			int lavida = 4;
			
			while(lavida != 0)
			{
				System.out.println("1) Veure Taules i Columnes\n"
						+ "2) Veure Dades\n"
						+ "3) Generar Comanda\n"
						+ "4) Veure Comanda per client\n"
						+ "7) Preparar Comanda\n"
						+ "8) Generar Ordres de Compra\n"
						+ "9) Veure Ordres de Compra\n"
						+ "10) Veure Diari de Moviments\n"
						+ "0) Sortir");
				
				lavida = sc.nextInt();
				
				switch(lavida) 
				{
				case 1:
					String tableNamePattern  = "%";
					String[] types = {"TABLE"};
					resultSet = null;
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
					resultSet = null;
					String tabla = sc.nextLine();
					st = con.createStatement();
					rs = st.executeQuery("SELECT * FROM "+tabla);
					
					System.out.println("Dades de la taula "+tabla+":");
					resultSet = mt.getColumns("ttb", null, tabla, "%");
					System.out.print("| ");
					while (resultSet.next()) {
					System.out.print(resultSet.getString(4)+" | ");
					}
					System.out.println();
					
					while (rs.next()) {
						 int numColumns = rs.getMetaData().getColumnCount();
						 System.out.print("| ");
						 for ( int i = 1 ; i <= numColumns ; i++ ) 
						 	{
				               System.out.print(rs.getObject(i)+" | ");
				            }
						 System.out.println();
					}
					break;
				case 3: 
					resultSet = null;
					st = con.createStatement();
					rs = st.executeQuery("SELECT idClient, nomClient FROM client");
					
					System.out.println("Dades de la taula client:");
					resultSet = mt.getColumns("ttb", null, "client", "%");
					System.out.print("| ");
					for (int i = 0; i<2; i++) {
					resultSet.next();
					System.out.print(resultSet.getString(4)+" | ");
					}
					System.out.println();
					
					while (rs.next()) {
						 int numColumns = rs.getMetaData().getColumnCount();
						 System.out.print("| ");
						 for ( int i = 1 ; i <= numColumns ; i++ ) 
						 {
							 System.out.print(rs.getObject(i)+" | ");
				         }
						 System.out.println();
					}
					System.out.println("\nSelecciona un Client:");

					int client = sc.nextInt();
					
					Date date = new Date();
					java.sql.Date sqlDate = new java.sql.Date(date.getTime());

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					int hola = st.executeUpdate("INSERT into comanda (idComanda, idClient, dataComanda, dataLliurament) values (null, "+client+", '"+sdf.format(sqlDate)+"', null)");
	
			
					rs = st.executeQuery("SELECT idProducte, nomProducte, preuVenda, stock FROM producte");
					System.out.println("Dades de la taula producte:");
					resultSet = mt.getColumns("ttb", null, "producte", "%");
					resultSet.next();
					System.out.print("| ");
					System.out.print(resultSet.getString(4)+" | ");
					resultSet.next();
					System.out.print(resultSet.getString(4)+" | ");
					resultSet.next();
					resultSet.next();
					System.out.print(resultSet.getString(4)+" | ");
					resultSet.next();
					System.out.print(resultSet.getString(4)+" | ");
					resultSet.next();
					System.out.println();
					while (rs.next()) {
						 int numColumns = rs.getMetaData().getColumnCount();
						 System.out.print("| ");
						 for ( int i = 1 ; i <= numColumns ; i++ ) 
						 {
							 System.out.print(rs.getObject(i)+" | ");
				         }
						 System.out.println();
					}
					
					int cd = 0;
					int ai = 0;
					while(cd!=2)
					{
					
					System.out.println("\nSelecciona un Producte:");
					int producte = sc.nextInt();
					System.out.println("Selecciona una quantitat:");
					int qua = sc.nextInt();
					System.out.println("Selecciona un preu (0 si vols el preu per defecte):");
					int preu = sc.nextInt();
					
					hola = st.executeUpdate("INSERT into comanda_linia (idComanda, idLinia, idProducte, quantitat, preuVenda) values ((Select max(idComanda) from comanda), "+ai+", "+producte+", "+qua+", "+preu+")");
					System.out.println(hola);
					
					System.out.println("\nVols afegir una altra linia? (1: SI, 2: NO):");
					ai++;
					cd = sc.nextInt();
					}
					
					break;
				case 4:
					resultSet = null;
					st = con.createStatement();
					rs = st.executeQuery("SELECT idClient, nomClient FROM client");
					
					System.out.println("Dades de la taula client:");
					resultSet = mt.getColumns("ttb", null, "client", "%");
					System.out.print("| ");
					for (int i = 0; i<2; i++) {
					resultSet.next();
					System.out.print(resultSet.getString(4)+" | ");
					}
					System.out.println();
					
					while (rs.next()) {
						 int numColumns = rs.getMetaData().getColumnCount();
						 System.out.print("| ");
						 for ( int i = 1 ; i <= numColumns ; i++ ) 
						 {
							 System.out.print(rs.getObject(i)+" | ");
				         }
						 System.out.println();
					}
					System.out.println("\nSelecciona un Client:");

					int client2 = sc.nextInt();
					
					rs = st.executeQuery("Select idComanda, estatComanda from comanda where idClient = "+client2+"");

					System.out.println("Comandes del client:");
					resultSet = mt.getColumns("ttb", null, "comanda", "%");
					resultSet.next();
					System.out.print("| ");
					System.out.print(resultSet.getString(4)+" | ");
					resultSet.next();
					resultSet.next();
					resultSet.next();
					System.out.print(resultSet.getString(4)+" | ");
					System.out.println();
					
					while (rs.next()) {
						 int numColumns = rs.getMetaData().getColumnCount();
						 System.out.print("| ");
						 for ( int i = 1 ; i <= numColumns ; i++ ) 
						 {
							 System.out.print(rs.getObject(i)+" | ");
				         }
						 System.out.println();
					}
					
					System.out.println("\nSelecciona una Comanda:");
					int comanda = sc.nextInt();
					rs = st.executeQuery("SELECT * from comanda where idComanda = "+comanda+" and idClient = "+client2+"");
					boolean flag = false;
					while (rs.next()) {
						 int numColumns = rs.getMetaData().getColumnCount();
						 flag = true;
						 System.out.print("| ");
						 for ( int i = 1 ; i <= numColumns ; i++ ) 
						 {
							 System.out.print(rs.getObject(i)+" | ");
				         }
						 System.out.println();
					}
					
					if(flag) {
					System.out.println();
					resultSet = mt.getColumns("ttb", null, "comanda_linia", "%");
					System.out.print("| ");
					for (int i = 0; i<5; i++) {
					resultSet.next();
					System.out.print(resultSet.getString(4)+" | ");
					}
					System.out.println();
					
					rs = st.executeQuery("SELECT * from comanda_linia where idComanda = "+comanda+"");
				
					while (rs.next()) {
						 int numColumns = rs.getMetaData().getColumnCount();
						 System.out.print("| ");
						 for ( int i = 1 ; i <= numColumns ; i++ ) 
						 {
							 System.out.print(rs.getObject(i)+" | ");
				         }
						 System.out.println();
					}
					
					rs = st.executeQuery("SELECT sum(preuVenda) from comanda_linia where idComanda = "+comanda+" group by idComanda");
					
					rs.next();
					
					System.out.println("\nIMPORT TOTAL: "+rs.getObject(1));
					System.out.println();
					}
					
					break;
				case 7:
					resultSet = null;
					st = con.createStatement();
					rs = st.executeQuery("SELECT idComanda FROM comanda WHERE estatComanda = 'PENDENT'");
					
					
					
					
					break;
				case 8:
					break;
				case 9:
					break;
				case 10:
					break;
				case 0:
					break;
				}
				
			}

		} catch (SQLException e) {
 
			System.err.println("Error d'apertura de connexio: " + e.getMessage());
			e.printStackTrace();

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
