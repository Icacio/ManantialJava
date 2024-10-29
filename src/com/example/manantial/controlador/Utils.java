package com.example.manantial.controlador;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.example.manantial.controlador.MainController.ventana;

import com.example.manantial.vista.Language;
import com.example.manantial.vista.MyDialog;
import com.example.manantial.vista.PasswordGetter;

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
	
	public static String requestPassword() {
		return new PasswordGetter(false).response;
	}

	public static boolean isNumber(String t) {
		return t.matches("[0-9]+|[0-9]+.[0-9]*");
	}

	public static String [] arrayAdd(String[] array, String value) {
		var newArray = new String[array.length+1];
		System.arraycopy(array, 0, newArray, 0, array.length);
		newArray[newArray.length-1] = value;
		return newArray;
	}
	
	public static long[] arrayAdd(long[] array, long value) {
		long[] newArray = new long[array.length+1];
		System.arraycopy(array, 0, newArray, 0, array.length);
		newArray[newArray.length-1] = value;
		return newArray;
	}
	
	public static int[] arrayAdd(int[] array, int value) {
		var newArray = new int[array.length+2];
		System.arraycopy(array, 0, newArray, 0, array.length);
		newArray[newArray.length-1] = value;
		return newArray;
	}

	public static boolean[] arrayAdd(boolean[] array, boolean value) {
		var newArray = new boolean[array.length+2];
		System.arraycopy(array, 0, newArray, 0, array.length);
		newArray[newArray.length-1] = value;
		return newArray;
	}
}
