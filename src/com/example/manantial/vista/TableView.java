package com.example.manantial.vista;

import com.example.manantial.modelo.Tabla;
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
	public void paint(Graphics g) {
		var width = this.getBounds().width;
		for(int i = 0; i < Tabla.width; i++) {
			for(int j = 0; j < tabla.length(); j++) {
				g.drawString(tabla.getString(i, j), coordinate[i]*width/100, j*16);
			}
		}
	}
}
