package com.example.manantial.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.example.manantial.controlador.MainController.working_dir;

import com.example.manantial.controlador.Utils;
import com.example.manantial.vista.Language;
import com.example.manantial.vista.MyDialog;
import com.example.manantial.vista.PasswordGetter;


public class Inventario extends Tabla {

	public static final Inventario singleton = new Inventario();

	private Inventario() {
		super("Inventario");
		getInventario();
	}
	
	private void getInventario() {
		try (var con = getCon(false);var st = con.createStatement()) {//read the database without password
			readTable(st);
		} catch (SQLException e) {
			switch (e.getSQLState()) {
			case "XJ004"://database doesn't exist
				var pass = new PasswordGetter(true).response;
				try (var con = getCon(true);var st = con.createStatement()) {
					con.setSchema("APP");
					setPassword(pass,con);
					createTable(st);
				} catch (SQLException e1) {
					abort(e1);
				}
				break;
			case "08004"://Authentication error
				do {
					try (var con = getCon(";user=root;password="+Utils.requestPassword())) {
						con.setSchema("APP");
						readTable(con.createStatement());
					} catch (SQLException e1) {
						if (!e1.getSQLState().equals("08004"))
							abort(e1);
						new MyDialog(Language.passwordError);
						e = e1;
					}
				} while (e.getSQLState().equals("08004"));
				break;
				default:
					abort(e);
			}
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

	private void readTable(Statement st) throws SQLException {
		int i = 0;
		try (var rs = st.executeQuery("SELECT COUNT(*) AS rowcount FROM Inventario")) {
			while(rs.next()) {
				i = rs.getInt("rowcount");
			}
		} catch (SQLException e) {
			if (e.getSQLState().equals("42X05")) {
				createTable(st);
				return;
			} else throw e;
		}
		var rs = st.executeQuery("SELECT * FROM Inventario");
		var codigo = new long[i];
		var nombre = new String[i];
		var precio = new int[i];
		var cantid = new int[i];
		var j = 0;
		while(rs.next()) {
			codigo[j] = rs.getLong("codigo");
			nombre[j] = rs.getString("nombre");
			precio[j] = rs.getInt("precio");
			cantid[j] = rs.getInt("cantidad");
		}
		this.codigo = codigo;
		this.nombre = nombre;
		this.precio = precio;
		this.cantidad = cantid;
		rs.close();
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
	
	private void createTable(Statement st) throws SQLException {
		st.execute("CREATE TABLE Inventario ("
			+ "codigo int NOT NULL,"
			+ "nombre varchar(255) NOT NULL,"
			+ "precio int NOT NULL,"
			+ "cantidad int NOT NULL,"
			+ "PRIMARY KEY (codigo))");
	}
	
	public static void abort(SQLException e) {
		System.out.print(e.getSQLState());
		abort(e);
	}
}
