package com.example.manantial.controlador;

import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import com.example.manantial.vista.Language;

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

import static com.example.manantial.controlador.Controlador.working_dir;

public final class Utils extends WindowAdapter implements ActionListener {
	public static final Utils singleton = new Utils();
	
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
	/**
     * Creates a generic Dialog
     * 
     * @param  source The parent for the dialog
     * 
     * @param  text The message to put on the Dialog
     * 
     * @param  b what to do when accepted
     * 
     * @return The function returns the object itself.
     */
	public static Dialog dialogo (Window source, String text, ActionListener b) {
		Dialog error = new Dialog(source);
		error.setLayout(new BorderLayout());
		Utils.makeCloseable(error);
		error.add(new Panel(),BorderLayout.EAST);
		error.add(new Panel(),BorderLayout.WEST);
		error.add(new Panel(),BorderLayout.NORTH);
		error.add(new Panel(),BorderLayout.SOUTH);
		Panel center = new Panel(new BorderLayout());
		error.add(center);
		center.add(new Label(text),BorderLayout.NORTH);
		Panel south = new Panel();
		Button button = new Button(Language.accept);
		south.add(new Panel());
		south.add(button);
		south.add(new Panel());
		button.addActionListener(b);
		center.add(south,BorderLayout.SOUTH);
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

	public static Window getWindow(ActionEvent e) {
		return getWindow(((Component)e.getSource()).getParent());
	}
	public static Window getWindow(Component c) {
		if (c==null) return Controlador.ventana;
		if (c.getParent() instanceof Window)
			return (Window) c.getParent();
		return getWindow(c.getParent());
	}//*/
	
	public static BufferedWriter file(String string) throws UnsupportedEncodingException, FileNotFoundException {
		string = working_dir+string;
		int i;
		for (i = string.length()-1; i > -1; i--) {
			if (string.charAt(i)=='\\') break;
		}
		new File(string.substring(0,i)).mkdirs();
		BufferedWriter a = 
		new BufferedWriter(
				new java.io.OutputStreamWriter(new java.io.FileOutputStream(string),"Cp1252"));
		return a;

		
	}
}
