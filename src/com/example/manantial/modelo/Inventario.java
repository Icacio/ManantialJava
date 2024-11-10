package com.example.manantial.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.example.manantial.controlador.MainController;
import com.example.manantial.vista.MyDialog;
import com.example.manantial.vista.PasswordGetter;

import static com.example.manantial.vista.language.Language.passwordError;
import static com.example.manantial.controlador.Utils.requestPassword;
import static com.example.manantial.controlador.MainController.working_dir;

public class Inventario extends Entity {
	public static final Inventario singleton = new Inventario();
	private static final String userPlusPass = ";user=root;password=";
	private String pass;
	private static boolean ran = false;
	
	private Inventario () {
		super("Inventario");
	}
	
	public void getInventario() {
		if (ran) return;
		ran = true;
		try (var con = getCon(false);var st = con.createStatement()) {//read the database without password
			readTable(st);
		} catch (SQLException e) {
			switch (e.getSQLState()) {
			case "XJ004"://database doesn't exist
				var pass = new PasswordGetter(true).response;
				try (var con = getCon(true);var st = con.createStatement()) {
					con.setSchema("APP");
					createTable(st,pass);
					MainController.caja = false;
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
		if (i > 0) {
			MainController.caja = true;
			var rs = st.executeQuery("SELECT * FROM Inventario");
			var codigo = new long[i];
			var nombre = new String[i];
			var precio = new int[i];
			var cantid = new int[i];
			i = 0; 
			while(rs.next()) {
				codigo[i] = rs.getLong("codigo");
				nombre[i] = rs.getString("nombre");
				precio[i] = rs.getInt("precio");
				cantid[i++] = rs.getInt("cantidad");
			}
			this.codigo = codigo;
			this.nombre = nombre;
			this.precio = precio;
			this.cantidad = cantid;
			rs.close();
		} else MainController.caja = false;
		length = codigo.length;
		changed = new boolean[length];
		for (int i1 = 0; i1 < length; i1++) {
			changed[i1] = false;
		}
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

	@Override
	protected void abort(SQLException e) {
		System.out.println(e.getSQLState());
		MainController.abort(e);
	}

	@Override
	protected Connection getCon() throws SQLException {
		return getCon(userPlusPass+pass);
	}
}