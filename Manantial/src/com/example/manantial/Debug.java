package com.example.manantial;

import java.awt.Dimension;
import java.awt.Rectangle;

public class Debug {

	public static int println(int value) {
		System.out.println(value);
		return value;
	}

	public static String println(String a) {
		System.out.println(a);
		return a;
	}
	public static String print(String a) {
		System.out.print(a);
		return a;
	}
	public static long print(long a) {
		System.out.println(a);
		return a;
	}
	public static char println(char charAt) {
		System.out.println(charAt);
		return charAt;
		
	}

	public static void print(int a, int b) {
		System.out.print('(');
		System.out.print(a);
		System.out.print(',');
		System.out.print(b);
		System.out.println(')');
	}
	public static void print(Dimension d) {
		System.out.print("Dim:");
		print((int)d.getWidth(),(int)d.getHeight());
	}
	public static void print(Rectangle a) {
		print(a.getWidth(),a.getHeight());
		print(a.getX(),a.getY());
	}

	public static void print(double d, double e) {
		System.out.print('(');
		System.out.print(d);
		System.out.print(',');
		System.out.print(e);
		System.out.println(')');
	}
	public static void except() {
		for (int i = 0; i < 400;i++)
			for (int j = 0; j < 400;j++)
				Inventario.inventario.getValue(i,j);
	}
	public static void print(String[] grid) {
		for (int i = 0; i < grid.length;i++) {
			System.out.print(grid[i]+",");
			System.out.println();
		}
	}
	public static void print(int[] grid) {
		for (int i = 0; i < grid.length;i++) {
			System.out.print(grid[i]+",");
			System.out.println();
		}
	}

	public static void print(int[][] codigo, String[] nombre) {
		for(int i = 0; i < codigo.length;i++) {
			for (int j = 0; j < 4; j++) {
				System.out.print(codigo[j][i]);
				System.out.print(",");
			}
			System.out.println(nombre[i]);
		}
	}
	public static void print(String[][] grid) {
		for (int i = 0; i < grid.length;i++) {
			for (int j=0; j < grid[i].length;j++) {
				System.out.print(grid[i][j]+",");
			}
			System.out.println();
		}
	}
	public static int print(int b) {
		System.out.println(b);
		return b;
	}
	public static void print(long[] grid) {
		for (int i = 0; i < grid.length;i++) {
			System.out.print(grid[i]+",");
			System.out.println();
		}
	}
	public static void print(Inventario grid) {
		System.out.println(grid.length());
		for(int i = 0; i < grid.length();i++) {
			System.out.print(grid.getBarcode(i));
			print(",");
			System.out.print(grid.getString(1,i));
			print(",");
			System.out.print(grid.getValue(2,i));
			print(",");
			System.out.println(grid.getString(3,i));
		}
	}
	public static void print(boolean caja) {
		System.out.print(caja);
	}
	//*/
}
