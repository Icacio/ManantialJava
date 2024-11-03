package com.example.manantial.controlador;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Array;

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
	
	private static <T> Object arrayAddP(Object oldArray, T value) {
		try {
			if (oldArray.getClass().isArray()) {
				int length = Array.getLength(oldArray);
				var newArray = Array.newInstance(oldArray.getClass().getComponentType(), length + 1);
				System.arraycopy(oldArray, 0, newArray, 0, length);
				Array.set(newArray, length, value);
				return newArray;
			} else {
				throw new IllegalArgumentException("Not an array");
			}
		} catch (NullPointerException e) {
			var wrapperClass = value.getClass();
			if (wrapperClass == Integer.class) return new int[] {(int) value};
			if (wrapperClass == Boolean.class) return new boolean[] {(boolean) value};
			if (wrapperClass == Long.class) return new long[] {(long) value};
			if (wrapperClass == String.class) return new String[] {(String) value};
			throw new IllegalArgumentException("Type not accepted");
		}
	}

	public static String[] arrayAdd(String[] array, String value) {
		return (String[]) arrayAddP(array,value);
	}
	public static long[] arrayAdd(long[] array, long value) {
		return (long[]) arrayAddP(array,value);
	}
	public static int[] arrayAdd(int[] array, int value) {
		return (int[]) arrayAddP(array,value);
	}
	public static boolean[] arrayAdd(boolean[] array, boolean value) {
		return (boolean[]) arrayAddP(array,value);
	}
}
