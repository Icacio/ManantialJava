package com.example.manantial.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.example.manantial.controlador.MainController;
import com.example.manantial.vista.MyDialog;
import com.example.manantial.vista.PasswordGetter;

import static com.example.manantial.vista.Language.passwordError;
import static com.example.manantial.controlador.Utils.requestPassword;
import static com.example.manantial.controlador.MainController.working_dir;

public class Inventario extends Tabla {
	public static final Inventario singleton = new Inventario();
	private static final String userPlusPass = ";user=root;password=";
	private String pass;
	
	private Inventario() {
		getInventario();
	}

	@Override
	public void save() {
		try (var con = getCon(userPlusPass+pass);var st = con.createStatement()) {
			upsert(st);
		} catch (SQLException e) {
			abort(e);
		}
	}
	
	private void upsert(Statement st) throws SQLException {
		var sql = "INSERT INTO Inventario (codigo,nombre,precio,cantidad) VALUES ";
		for (int i = 0; i < length;i++) {
			var values = "("+codigo[i]+",'"+nombre[i]+"',"+precio[i]+","+cantidad[i]+")";
			try {
				st.executeUpdate(sql+values);
			} catch (SQLException e) {
				if (!e.getSQLState().equals("23505"))
					throw e;
			}
		}
	}

	private void getInventario() {
		try (var con = getCon(false);var st = con.createStatement()) {//read the database without password
			readTable(st);
		} catch (SQLException e) {
			switch (e.getSQLState()) {
			case "XJ004"://database doesn't exist
				var pass = new PasswordGetter(true).response;
				System.out.println(pass);
				try (var con = getCon(true);var st = con.createStatement()) {
					con.setSchema("APP");
					createTable(st,pass);
					return;
				} catch (SQLException e1) {
					abort(e1);
				}
				break;
			case "08004"://Authentication error
				do {
					var pas = requestPassword(); 
					try (var con = getCon(userPlusPass+pas)) {
						readTable(con.createStatement());
						this.pass = pas;
						return;
					} catch (SQLException e1) {
						if (!e1.getSQLState().equals("08004"))
							abort(e1);
						new MyDialog(passwordError);
						e = e1;
					}
				} while (e.getSQLState().equals("08004"));
				break;
			}
			abort(e);
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

	void readTable(Statement st) throws SQLException {
		int i = 0;
		try (var rs = st.executeQuery("SELECT COUNT(*) AS rowcount FROM Inventario")) {
			while(rs.next()) {
				i = rs.getInt("rowcount");
			}
		} catch (SQLException e) {
			if (e.getSQLState().equals("42X05")) {
				return;
			} else throw e;
		}
		var rs = st.executeQuery("SELECT * FROM Inventario");
		var codigo = new long[i+1];
		var nombre = new String[i+1];
		var precio = new int[i+1];
		var cantid = new int[i+1];
		i = 0; 
		while(rs.next()) {
			codigo[i] = rs.getLong("codigo");
			nombre[i] = rs.getString("nombre");
			precio[i] = rs.getInt("precio");
			cantid[i++] = rs.getInt("cantidad");
		}
		rs.close();
		this.codigo = codigo;
		this.nombre = nombre;
		this.precio = precio;
		this.cantidad = cantid;
		length = codigo.length;
	}
	
	private void createTable(Statement st,String pass) throws SQLException {
		st.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.connection.requireAuthentication', 'true')");
		st.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.authentication.provider', 'BUILTIN')");
		st.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.user.root', '"+pass+"')");
		st.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.database.propertiesOnly', 'true')");
		st.execute("CREATE TABLE Inventario ("
			+ "codigo bigint NOT NULL,"
			+ "nombre varchar(255) NOT NULL,"
			+ "precio int NOT NULL,"
			+ "cantidad int NOT NULL,"
			+ "PRIMARY KEY (codigo))");
		this.pass = pass;
	}

	public static void abort(SQLException e) {
		System.out.println(e.getSQLState());
		MainController.abort(e);
	}
}