package com.example.manantial.modelo;

import static com.example.manantial.controlador.Controlador.inventario;
import java.awt.Canvas;
import java.awt.Graphics;

import com.example.manantial.controlador.Utils;

public class Inventario extends Canvas {
	
	private static String venta = "venta";
	private long[] codigo;
	private int[] cantidad;
	private int[] precio;
	private String[] nombre;
	private int length;
	int changes = 0;
	public final String fileName;
	public static final int width = 4;	
	//constructors
	public Inventario() {
		fileName = venta;
	}
	/**
     * Creates and returns a new table by reading the file with the date
     * of today and if it doesn't exists, it creates the folder for
     * future files to be saved.
     * @param   filename
     *          the file to be loaded, if it doesn't exist, it just reserves it for future saving.
     */
	public Inventario(String filename, String [][] grid) {
		fileName = filename;
		if (grid==null) {
			length = 0;
			return;
		}
		if (grid.length==0) {
			length = -1;
			return;
		}
		if (grid!=null&&grid[0].length>0) {
			length = grid[0].length;
			codigo = new long [length];
			nombre = new String [length];
			precio = new int[length];
			cantidad = new int[length];
			
			for(int i = 0; i < grid[0].length;i++) {
				if (Utils.isNumber(grid[0][i]))
					codigo[i] = Long.parseLong(grid[0][i]);
			}
			nombre = grid[1];
			for(int i = 0; i < length;i++) {
				if (Utils.isNumber(grid[2][i]))
				precio[i] = Integer.parseInt(grid[2][i]);
			}
			for(int i = 0; i < length;i++) {
				if (Utils.isNumber(grid[3][i]))
				cantidad[i] = Integer.parseInt(grid[3][i]);
			}
		} else length = 0;
	}
	//operaciones
	
	/**
     * Adds all the items from the table provided to the current one
     * 
     * @param  otro The {@code Inventario} to be merged with the current one
     * 
     * @return The function returns the object itself.
     */
	public Inventario suma (Inventario otro) {
		if (otro==this) return this;
		for(int i = 0;i < otro.length(); i++) {
			int result = 0;
			for(int j = 0; j < length();j++) {
				if (otro.getBarcode(i)==getBarcode(j)) {
					setCantidad(j,getCantidad(j)+otro.getCantidad(i));
					result++;
				}
			}
			if (result<1) {
				add(otro.getBarcode(i),otro.getString(1,i),otro.getPrecio(i),otro.getCantidad(i));
			}
		}
		changes = getChanges() + 1;
		return (this);
	}
	
	public void resta(Inventario sustraendo) {
		if (this==sustraendo) return; //this most be an issue
		for (int i = 0; i < sustraendo.length();i++) {
			for(int j = 0; j < this.length();j++) {
				if (sustraendo.getBarcode(i)==this.getBarcode(j)) {
					cantidad[j] -= sustraendo.cantidad[i];
					break;
				}
			}
		}
		
		
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

	
	public void addCantidad(int y, int value) {
		cantidad[y] +=value;
		revalidate();
		changes = getChanges() + 1;
	}
	
	public void setCantidad(int y, int value) {
		cantidad[y] = value;
		revalidate();
		changes = getChanges() + 1;
	}
	
	public void setPrecio(int y, int value) {
		precio[y] = value;
		revalidate();
		changes = getChanges() + 1;
	}
	
	public void setNombre(int y, String nom) {
		nombre[y] = nom;
		changes = getChanges() + 1;
	}

	
	public void add(long barcode, String productName, int price, int amount) {
		nombre = Utils.arrayResize(nombre);
		codigo = Utils.arrayResize(codigo);
		cantidad = Utils.arrayResize(cantidad);
		precio = Utils.arrayResize(precio);
		codigo[length] = barcode;
		nombre[length] = productName;
		precio[length] = price;
		cantidad[length++] = amount;
		revalidate();
		changes = getChanges() + 1;
	}

	public void add(long argument0, int value) {
		add(argument0,"",0,value);
	}
	//Component Method
	@Override
	public void paint (Graphics graphics) {
		int panelWidth = getSize().width;
		int a[] = {0,panelWidth*18/100,panelWidth*7/10,panelWidth*81/100,panelWidth*9/10};
		int cellHeigh = getFont().getSize();
		boolean caja = (this != inventario);
		if (caja)
			graphics.drawString("Total",a[4],cellHeigh);
		
	    graphics.drawString("Código de barras",a[0],cellHeigh);
		graphics.drawString("artículo",a[1],cellHeigh);
		graphics.drawString("precio",a[caja?2:3],cellHeigh);
		graphics.drawString("cantidad",a[caja?3:4],cellHeigh);
		
		for (int j = 0; j < length;j++) {
			for (int i = 0; i < width;i++) {
				if (!caja)
					graphics.drawString(getString(i,j),a[i>1?i+1:i],(2+j)*cellHeigh); else
					graphics.drawString(getString(i,j),a[i],(2+j)*cellHeigh);
	    	}
	    	if (caja) {
				graphics.drawString(Integer.toString(precio[j]*cantidad[j]),a[4],(2+j)*cellHeigh);//total
		    }
	    }
		setSize(getSize().width,(2+length)*cellHeigh);
		
	}
	//*/
	public void removeLine() {
		long[] newcode = new long[length-1];
		System.arraycopy(codigo,0,newcode,0,length-2);
		codigo = newcode;
		String[] newString = new String[length-1];
		System.arraycopy(codigo,0,newString,0,length-2);
		nombre = newString;
		int[] newInt = new int[length-1];
		System.arraycopy(precio,0,newInt,0,length-2);
		precio = newInt;
		int[] newCant = new int[length-1];
		System.arraycopy(precio,0,newCant,0,length-2);
		cantidad = newCant;
	}


	public void length(int i) {
		length = i;
	}

	public int getChanges() {
		return changes;
	}
}