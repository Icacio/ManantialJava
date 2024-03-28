package com.example.manantial.controlador;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.manantial.modelo.Inventario;
import com.example.manantial.vista.Language;
import com.example.manantial.vista.Ventana;

import java.awt.Component;

public class Controlador {
	
	public final static String working_dir = System.getProperty("os.name").toLowerCase().contains("win")?System.getenv("APPDATA")+"\\manantial\\":"\\";
	public final static String venta = "venta";
	public final static String filecsv = "file.csv";
	public final static Inventario inventario = new Inventario(filecsv,loadFile(filecsv));
	public static final Ventana ventana = new Ventana(inventario.length()>0?true:false);
	static String contrasenia;
	
	public static void main (String[] args) {
		if (inventario.length()>0) {
			ventana.pack();
			ventana.setVisible(true);
		}
		else if (inventario.length()==0) {
			Utils.dialogo(null,Language.fileNotReadable,(e)->{
				new PasswordGetter(null);
				Utils.getWindow((Component)e.getSource()).dispose();
			});
		} else {
			inventario.length(0);
			new PasswordGetter(null);
		}
	}
	
	static String[][] loadFile(String file) {
		try (BufferedReader reader = new BufferedReader(
				new java.io.InputStreamReader(
						new java.io.FileInputStream(working_dir+file),"Cp1252"));) {
			
			String[][] grid = new String [5][1];
			String currentLine = "";
			byte x = 0;
			int y = 0;
			StringBuilder temp = new StringBuilder();
	        if ((currentLine = reader.readLine()) != null) {
	        	if (!file.equals(venta)) {
	        		if(file.equals(filecsv)) {
	        			int i = 0;
	        			while(i < currentLine.length()) {
	        				if(currentLine.charAt(i)==',') break;
	        				i++;
	        			}
	        			if (i==0) return null;
	        			contrasenia = currentLine.substring(0,i);
	        		}
	        		currentLine = reader.readLine();
	        	}
	        	if (currentLine==null) return null;
			do {
	        	if (currentLine.substring(0,4).equals("0,0,")||currentLine.substring(0,3).equals("0,,"))
	        		continue;
	        	if (y>=grid[0].length) {
    				for (int i = 0; i < 5; i++) {
    					grid[i]=Utils.arrayResize(grid[i]);
    				}
    			}
	        	final int sz = currentLine.length();
	        	for (int i = 0; i < sz;i++) {
	        		grid[x][y] = "";
	        		if (currentLine.charAt(i)!=',') {
	        			temp.append(currentLine.charAt(i));
	        		}
	        		else {
	        			if (x>4) {x--;break;}
	        			String t = new String(temp.toString());	
	        			grid[x++][y] = t;
	        			temp = new StringBuilder();
	        		}
	        	}
	        	if (!grid[1][y].equals("")&&
	        			!grid[1][y].equals("0")) {
	        		grid[x][y++] = temp.toString();
	        	}
    			temp = new StringBuilder();
	        	x = 0;
	        } while((currentLine = reader.readLine()) != null);
	        } else return null;
	        return grid;
		} catch (FileNotFoundException e1) {
			String[][] a = {};
        	return a;
		} catch (IOException e1) {
			return null;
		} catch (NullPointerException e1) {
			return null;
		}
	}

	static boolean fileExists(String filename) {
		return new File(working_dir+filename).exists();
	}

	public static boolean password(String text) {
		return contrasenia.equals(text);
	}
	
	public static void save(Inventario inve) {
		if (inve.length()<0) return;
		if (inve.getChanges()==0&&(inve.fileName!=venta||inve.fileName.equals(venta))) return;
		int i;
		for (i = inve.fileName.length()-1; i > -1; i--) {
			if (inve.fileName.charAt(i)=='\\') {
				break;
			}
		}
		if (i!=inve.fileName.length()) {
			File file = new File(working_dir+inve.fileName.substring(0,i+1));
			file.mkdir();}
		try (BufferedWriter writer = Utils.file(inve.fileName)) {
			if (!inve.fileName.equals(venta))
				writer.write(Controlador.contrasenia+Language.header+"\n");
			for(i = 0; i < inve.length();i++) {
				for(int j = 0; j < Inventario.width; j++) {
					writer.write(inve.getString(j,i));
					writer.write(',');
				}
				writer.write("\n");
			}
			
			
		} catch (IOException e) {
			
		}
	}
	/**
     * Creates and returns a new table by loading the file with the date
     * of today and if it doesn't exists, it creates the folder for
     * future files to be saved and returns an empty table.
     */
	public static Inventario today() {
		Date today = new Date();
		String date = new SimpleDateFormat("yyyy\\MM\\").format(today);
		new File(date).mkdirs();
		date = new SimpleDateFormat("yyyy\\MM\\dd").format(today)+".csv";
		if (fileExists(date))
			return new Inventario(date,loadFile(date));
		return null;
	}
	
	public static Inventario venta() {
		if (fileExists(venta)) {
			Inventario retorno = new Inventario(venta,loadFile(venta));
			new File(venta).delete();
			return retorno;
		}
		return new Inventario();
	}
}