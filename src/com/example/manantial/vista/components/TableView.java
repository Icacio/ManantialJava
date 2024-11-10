package com.example.manantial.vista.components;

import com.example.manantial.modelo.Tabla;
import com.example.manantial.vista.language.Language;

import java.awt.Canvas;
import java.awt.Graphics;

public class TableView extends Canvas {
	private Tabla tabla;
	private static final int[] coordinate = {0,25,75,90};

	public TableView(Tabla tabla) {
		super();
		this.tabla = tabla;
	}

	@Override
	public void validate() {
		super.validate();
		var d = getPreferredSize();
		d.height = (length()+1)*getFont().getSize();
		setPreferredSize(d);
	}
	
	@Override
	public void paint(Graphics g) {
		var width = this.getBounds().width;
		var heigh = getFont().getSize();
		g.drawString(Language.columnName[0],coordinate[0]*width/100,heigh);
		g.drawString(Language.columnName[1],coordinate[1]*width/100,heigh);
		g.drawString(Language.columnName[2],coordinate[2]*width/100,heigh);
		g.drawString(Language.columnName[3],coordinate[3]*width/100,heigh);
		for(int i = 0; i < Tabla.width; i++) {
			for(int j = 0; j < tabla.length(); j++) {
				g.drawString(tabla.getString(i, j), coordinate[i]*width/100, (j+2)*getFont().getSize());
			}
		}
	}

	public int length() {
		return tabla.length();
	}
	
	public void setCantidad(int y, int value) {
		tabla.setCantidad(y,value);
		repaint();
	}

	public void addCantidad(int y, int value) {
		tabla.addCantidad(y, value);
		repaint();
	}

	public void setTabla(Tabla tabla) {
		this.tabla = tabla;
		repaint();
	}

	public void setPrecio(int y, int value) {
		tabla.setPrecio(y, value);
		repaint();
	}

	public void setNombre(int y, String text) {
		tabla.setNombre(y, text);
		repaint();
	}

	public void add(long codigo, String nombre, int precio, int cantidad) {
		tabla.add(codigo, nombre, precio, cantidad);
		repaint(0,(length())*getFont().getSize(),getWidth(),getHeight());
	}
	
	public Tabla getTabla() {
		return tabla;
	}
}
