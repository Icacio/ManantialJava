package com.example.manantial.controlador;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.example.manantial.vista.Ventana.ventana;

import com.example.manantial.vista.Language;
import com.example.manantial.vista.MyDialog;

public final class Utils extends WindowAdapter implements ActionListener {
	public static final Utils singleton = new Utils();
	
	public static void makeCloseable(Window window) {
		window.addWindowListener(singleton);
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		e.getWindow().dispose();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		getWindow((Component)e.getSource()).dispose();
	}

	public static Window getWindow(Component c) {
		if (c==null) return ventana;
		if (c.getParent() instanceof Window)
			return (Window) c.getParent();
		return getWindow(c.getParent());
	}
	
	public static void dialogoError() {
		new MyDialog(Language.libraryError);
		System.exit(-1);
	}
}
