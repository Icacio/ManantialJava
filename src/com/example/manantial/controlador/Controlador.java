package com.example.manantial.controlador;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.example.manantial.modelo.Tabla;
import com.example.manantial.vista.PasswordGetter;

import static com.example.manantial.vista.Ventana.ventana;

public class Controlador {
	
	public final static String working_dir = System.getProperty("os.name").toLowerCase().contains("win")?System.getenv("APPDATA")+"\\manantial\\":"\\";
	public static final Tabla inventario = getInventario();
	
	public static void main (String args[]) {
		ventana.setVisible();
	}
	
	private static Tabla getInventario() {
		try (var con = getCon(false);var st = con.createStatement()) {//read the database without password
			return readTable(st,"Inventario");
		} catch (SQLException e) {
			switch (e.getSQLState()) {
			case "XJ004"://database doesn't exist
				var pass = new PasswordGetter().response;
				try (var con = getCon(true);var st = con.createStatement()) {
					con.setSchema("APP");
					setPassword(pass,con);
					return createTable(st,"Inventario");
				} catch (SQLException e1) {
					abort(e1);
				}
				break;
			}
			abort(e);
			return new Tabla();
		}
	}
	
	private static Connection getCon(boolean create) throws SQLException {
		var f = create?";create=true":";create=false";
		return getCon(f);
	}
	
	private static Connection getCon(String url) throws SQLException {
		var a = DriverManager.getConnection("jdbc:derby:"+working_dir+"\\Manantial"+url);
		a.setSchema("APP");
		return a;
	}

	static Tabla readTable(Statement st, String tableName) throws SQLException {
		int i = 0;
		try (var rs = st.executeQuery("SELECT COUNT(*) AS rowcount FROM "+tableName)) {
			while(rs.next()) {
				i = rs.getInt("rowcount");
			}
		} catch (SQLException e) {
			if (e.getSQLState().equals("42X05")) {
				return null;
			} else throw e;
		}
		try (var rs = st.executeQuery("SELECT * FROM "+tableName)) {
			var codigo = new long[i];
			var nombre = new String[i];
			var precio = new int[i];
			var cantid = new int[i];
			while(rs.next()) {
				codigo[i] = rs.getLong("codigo");
				nombre[i] = rs.getString("nombre");
				precio[i] = rs.getInt("precio");
				cantid[i] = rs.getInt("cantidad");
			}
			rs.close();
			return new Tabla(codigo,nombre,precio,cantid);
		} catch (SQLException e) {
			throw e;
		}
	}
	
	public static void setPassword(String text,Connection conn) {
		try (Statement s = conn.createStatement()) {
			s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.connection.requireAuthentication', 'true')");
			s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.authentication.provider', 'BUILTIN')");
			s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.user.root', '"+text+"')");
			s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.database.propertiesOnly', 'true')");
		} catch (SQLException e) {
			abort(e);
		}
	}
	
	private static Tabla createTable(Statement st, String tableName) throws SQLException {
		st.execute("CREATE TABLE "+tableName+" ("
			+ "codigo int NOT NULL,"
			+ "nombre varchar(255) NOT NULL,"
			+ "precio int NOT NULL,"
			+ "cantidad int NOT NULL,"
			+ "PRIMARY KEY (codigo))");
		return new Tabla();
	}

	public static void abort(SQLException e1) {
		System.out.println(e1.getMessage());
		System.out.print(e1.getSQLState());
		e1.printStackTrace();
		System.exit(-1);
	}
}