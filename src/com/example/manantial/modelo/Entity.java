package com.example.manantial.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class Entity extends Tabla {

	public Entity(String name) {
		super(name);
	}
	public Entity (String name, long[] codigo2, String[] nombre2, int[] precio2, int[] cantid2) {
		super(name,codigo2, nombre2, precio2,cantid2);
	}
	
	@Override
	public void save() {
		try (var con = getCon();var st = con.prepareStatement("INSERT INTO Inventario (codigo,nombre,precio,cantidad) VALUES (?,?,?,?)")) {
			for (int i = 0; i < length;i++) {
				if (!changed[i]) continue;
				st.setLong(1,codigo[i]);
				st.setString(2,nombre[i]);
				st.setInt(3,precio[i]);
				st.setInt(4,cantidad[i]);
				try {
					st.executeUpdate();
				} catch (SQLException e) {
					if (!e.getSQLState().equals("23505"))
						throw e;
					update(con.prepareStatement("UPDATE Inventario SET cantidad = ? WHERE codigo = ?"),cantidad[i],codigo[i]);
				}
			}
		} catch (SQLException e) {
			abort(e);
		}
	}

	private void update(PreparedStatement st, int amt, long code) throws SQLException {
		st.setLong(2,code);
		st.setInt(1,amt);
		st.executeUpdate();
	}
	protected abstract Connection getCon() throws SQLException;
	protected abstract void abort(SQLException e);
}
