package com.example.manantial.controlador;

import com.example.manantial.modelo.Inventario;
import static com.example.manantial.vista.Ventana.ventana;

public class MainController {
	
	public final static String working_dir = System.getProperty("os.name").toLowerCase().contains("win")?System.getenv("APPDATA")+"\\manantial\\":"\\";
	public static final Inventario inventario = Inventario.singleton;
	
	public static void main (String args[]) {
		ventana.setVisible();
	}

	public static void abort(Exception e) {
		System.out.println(e.getMessage());
		e.printStackTrace();
		Utils.dialogoError();
	}
}