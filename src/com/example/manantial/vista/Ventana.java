package com.example.manantial.vista;

import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import static com.example.manantial.controlador.MainController.tableController;

public class Ventana extends Frame implements WindowListener {
	public static final Ventana singleton = new Ventana();
	
	@Override
	public void windowClosing(WindowEvent e) {
		setVisible(false);
		tableController.close();
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