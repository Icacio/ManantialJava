package com.example.manantial;

import java.awt.event.WindowEvent;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

public class Utils extends WindowAdapter implements ActionListener {
	static final Utils singleton = new Utils();
	
	private Utils() {}
	public static void makeCloseable(Window window) {
		window.addWindowListener(singleton);
	}
	public static void makeCloseable(ActionEvent e) {
		Utils.getWindow(e).dispose();
	}
	@Override
	public void windowClosing(WindowEvent e) {
		e.getWindow().dispose();
	}
	
	public static Dialog dialogo (Window source, String a, ActionListener b) {
		Dialog error = new Dialog(source);
		error.setLayout(new BorderLayout());
		Utils.makeCloseable(error);
		error.add(new Panel(),"East");
		error.add(new Panel(),"West");
		error.add(new Panel(),"North");
		error.add(new Panel(),"South");
		Panel center = new Panel(new BorderLayout());
		error.add(center);
		center.add(new Label(a),"North");
		Panel south = new Panel();
		Button button = new Button("Aceptar");
		south.add(new Panel());
		south.add(button);
		south.add(new Panel());
		button.addActionListener(b);
		center.add(south,"South");
		error.pack();
		error.setVisible(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		error.setLocation(screenSize.width/2-error.getWidth()/2,screenSize.height/2-error.getHeight()/2);
		return error;
	}
	
	public static boolean isNumber(String a) {
		if(a!=null) 
			return a.matches("[0-9]+");
		return false;
	}
	public static long[] arrayResize(long[] array) {
		try {
			long[] tempArray = new long[array.length+1];
			System.arraycopy(array,0,tempArray,0,array.length);
			return tempArray;
		}
		catch (NullPointerException e) {
			return new long[1];
		}
	}
	
	public static String[] arrayResize(String[] array) {
		try { 
			String[] tempArray = new String[array.length+1];
			System.arraycopy(array,0,tempArray,0,array.length);
			return tempArray;
		} catch (NullPointerException e) {
			return new String [] {""};
		}
	}
	public static int[] arrayResize(int[] array) {
		try {
			int[] tempArray = new int[array.length+1];
			System.arraycopy(array,0,tempArray,0,array.length);
			return tempArray;
		} catch (NullPointerException e) {
			return new int[1];
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		getWindow((Component)e.getSource()).dispose();
	}

	private static Window getWindow(ActionEvent e) {
		return getWindow(((Component)e.getSource()).getParent());
	}
	public static Window getWindow(Component c) {
		if (c==null) return Inventario.inventario.ventana;
		if (c.getParent() instanceof Window)
			return (Window) c.getParent();
		return getWindow(c.getParent());
	}//*/
}
