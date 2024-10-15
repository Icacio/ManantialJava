package com.example.manantial.controlador;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.example.manantial.modelo.Inventario;
import com.example.manantial.modelo.Tabla;
import com.example.manantial.vista.Language;
import com.example.manantial.vista.TableView;
import com.example.manantial.vista.Ventana;
import com.example.manantial.vista.components.SearchBox;
import com.example.manantial.vista.components.Spinner;

public class MainController implements ActionListener {
	
	public static final Ventana ventana = Ventana.singleton;
	public final static String working_dir = System.getProperty("os.name").toLowerCase().contains("win")?System.getenv("APPDATA")+"\\Manantial\\":"\\";
	public static final Inventario inventario = Inventario.singleton;
	public static final MainController tableController = new MainController();
	public final TableView tableDrawn = new TableView(inventario);
	public final SearchBox barra = new SearchBox(45);
	public final Spinner spinner = new Spinner();
	public final Button botonInventario = new Button(Language.views[caja?0:1]);
	public final Label instrucciones = new Label(Language.mensaje[0]);
	public final Button pagar = new Button(Language.pay+"0    ");
	public final ScrollPane center = new ScrollPane();
	public static boolean caja = inventario.length()>0;
	private boolean inserting = false;
	
	public static void main (String args[]) {
		tableController.configureVentana();
	}
	private void configureVentana() {
		ventana.setTitle(Language.views[caja?1:0]);
		ventana.addWindowListener(ventana);
		Panel butonera = new Panel();
		ventana.setFont(new Font("arial", Font.PLAIN, 16));
		Button botonAgregar = new Button(Language.add);
		butonera.add(instrucciones);
		butonera.add(barra);
		butonera.add(spinner);
		butonera.add(botonAgregar);
		butonera.add(botonInventario);
		butonera.add(pagar);
		tableDrawn.setPreferredSize(new Dimension(
				tableDrawn.getPreferredSize().width,
				tableDrawn.length()*ventana.getFont().getSize()));
		if (caja) {
			tableDrawn.tabla = new Tabla();
		} else {
			pagar.setVisible(false);
			botonInventario.setVisible(false);
		}
		ventana.add(new Panel(),BorderLayout.EAST);
		ventana.add(new Panel(),BorderLayout.WEST);
		ventana.add(new Panel(),BorderLayout.SOUTH);
		ventana.add(butonera,BorderLayout.NORTH);
		ventana.add(center);
		center.add(tableDrawn);
		ventana.pack();
		ventana.setVisible(true);
	}

	public static void abort(Exception e) {
		System.out.println(e.getMessage());
		e.printStackTrace();
		Utils.dialogoError();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}
	
	public void close() {
		tableDrawn.tabla.save();
		if (caja)
			inventario.save();
	}
	
	private MainController() {}
}