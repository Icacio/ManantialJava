package com.example.manantial.vista;

import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.awt.ScrollPane;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import com.example.manantial.modelo.Tabla;

import static com.example.manantial.controlador.MainController.inventario;

public class Ventana extends Frame implements ActionListener, WindowListener {

	public static final Ventana ventana = new Ventana();
	public final TextField barra = new TextField();
	private final TextField spinner = new TextField();
	private final Button botonInventario = new Button(Language.botones[0]);
	private final Label instrucciones = new Label(Language.mensaje[0]);
	private final Button pagar = new Button(Language.pay+"0    ");
	private final ScrollPane center = new ScrollPane();
	private Tabla drawing = inventario;
	private TableView activeTabla = new TableView(drawing);
	
	public void setVisible() {
		setTitle(Language.titulo[1]);
		addWindowListener(this);
		Panel butonera = new Panel();
		setFont(new Font("arial", Font.PLAIN, 16));
		Button botonAgregar = new Button(Language.add);
		butonera.add(instrucciones);
		butonera.add(barra);
		butonera.add(spinner);
		butonera.add(botonAgregar);
		butonera.add(botonInventario);
		butonera.add(pagar);
		activeTabla.setPreferredSize(new Dimension(
				activeTabla.getPreferredSize().width,drawing.length()*getFont().getSize()));
		add(new Panel(),BorderLayout.EAST);
		add(new Panel(),BorderLayout.WEST);
		add(new Panel(),BorderLayout.SOUTH);
		add(butonera,BorderLayout.NORTH);
		add(center);
		center.add(activeTabla);
		pack();
		super.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
	}
	@Override
	public void windowClosing(WindowEvent e) {
		setVisible(false);
		drawing.save();
		dispose();
	}
	@Override
	public void windowOpened(WindowEvent e) {}
	@Override
	public void windowClosed(WindowEvent e) {}
	@Override
	public void windowIconified(WindowEvent e) {}
	@Override
	public void windowDeiconified(WindowEvent e) {}
	@Override
	public void windowActivated(WindowEvent e) {}
	@Override
	public void windowDeactivated(WindowEvent e) {}
}