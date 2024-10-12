package com.example.manantial.modelo;
import java.awt.Canvas;
public class Tabla extends Canvas {
	
	protected long[] codigo;
	protected int[] cantidad;
	protected int[] precio;
	protected String[] nombre;
	protected int length;
	public static final int width = 4;
	//constructors
	public Tabla() {
		codigo = new long[1];
		nombre = new String[1];
		precio = new int[1];
		cantidad = new int[1];
		length = 0;
	}
	public Tabla(long[] codigo2, String[] nombre2, int[] precio2, int[] cantid2) {
		codigo = codigo2;
		nombre = nombre2;
		precio = precio2;
		cantidad = cantid2;
		length = codigo.length;
	}
	
	public long getBarcode(int y) {
		return codigo[y];
	}
	
	public String getString(int x,int y) {
		if (y>length)
			y = length-1;
		else if (y < 0) y = 0;
		switch (x) {
		case 0: return Long.toString(codigo[y]);
		case 1: return nombre[y]==null?"":nombre[y];
		case 2: return Integer.toString(precio[y]);
		case 3: return Integer.toString(cantidad[y]);
		default:return "Undefined" ;
		}
	}
	
	public int getPrecio(int y) {
		return precio[y];
	}
	
	public int getCantidad(int y) {
		return cantidad[y];
	}
	
	public int length() {
		return length;
	}
}