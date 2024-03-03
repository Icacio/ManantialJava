package com.example.manantial;

import java.awt.Button;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
//import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedWriter;
import java.io.IOException;

class Inventario extends Canvas {
	static final Inventario inventario = new Inventario();
	//private static Ventana mainWindow;
	private static String working_dir = System.getProperty("os.name").toLowerCase().contains("win")?System.getenv("APPDATA")+"\\manantial\\":"\\";
	private long[] codigo;
	private int[] cantidad;
	private int[] precio;
	private String[] nombre;
	private int length;
	Ventana ventana;
	private static String contrasenia;
	public static final int width = 4;
	static Ventana singleton;
	
	public static void main (String [] args) {
		if (new File(working_dir+"file.csv").exists())
			inventario.loadFile();
		else inventario.requestPassword();//*/
	}
	
	@Override
	public void paint (Graphics graphics) {
		//Debug.print(this);
		//Graphics2D graphics = (Graphics2D)old;
		//setSize(getSize().width,)
		int panelWidth = getSize().width;
		//int panelHeigh = getSize().height;
		int a[] = {0,panelWidth*18/100,panelWidth*7/10,panelWidth*81/100,panelWidth*9/10};
		
		int cellHeigh = getFont().getSize();
		
		boolean caja = (this != inventario);
		if (caja)
			graphics.drawString("Total",a[4],cellHeigh);
		
	    graphics.drawString("Código de barras",a[0],cellHeigh);
		graphics.drawString("artículo",a[1],cellHeigh);
		graphics.drawString("precio",a[caja?2:3],cellHeigh);
		graphics.drawString("cantidad",a[caja?3:4],cellHeigh);
		
		    
		//*/
		for (int j = 0; j < length;j++) {
			for (int i = 0; i < width;i++) {
				if (!caja)
					graphics.drawString(getString(i,j),a[i>1?i+1:i],(2+j)*cellHeigh); else
					graphics.drawString(getString(i,j),a[i],(2+j)*cellHeigh);
	    	}
	    	if (caja) {
				graphics.drawString(Integer.toString(precio[j]*cantidad[j]),a[4],(2+j)*cellHeigh);//total
		    }
	    }
		setSize(getSize().width,(2+length)*cellHeigh);
		
	}

	private void loadFile() {
		try (BufferedReader reader = new BufferedReader(
				new java.io.InputStreamReader(
						new java.io.FileInputStream(working_dir+"file.csv"),"Cp1252"));) {
			String[][] grid = new String [5][2];
			String currentLine = "";
			byte x = 0;
			int y = 0;
			StringBuilder temp = new StringBuilder();
			contrasenia = reader.readLine();
			//int leng = 0;
			if (contrasenia == null) {
				Utils.dialogo(null,"Parece que el archivo del inventario no tiene el formato correcto. Será sobreescrito",
					(ActionEvent e)->{
						requestPassword();
						Utils.makeCloseable(e);
					});
			return;
			}// else leng = contrasenia.length();
			StringBuilder a = new StringBuilder();
			for (int i = 0; i < contrasenia.length(); i++) {
				if (contrasenia.charAt(i) == ',')
					break;
				a.append(contrasenia.charAt(i));
			}
			contrasenia = a.toString();
	        while((currentLine = reader.readLine()) != null) {
	        	if (y>=grid[0].length) {
    				for (int i = 0; i < 5; i++) {
    					grid[i]=Utils.arrayResize(grid[i]);
    				}
    			}
	        	final int sz = currentLine.length();
	        	for (int i = 0; i < sz;i++ ) {
	        		if (currentLine.charAt(i)!=',')
	        			temp.append(currentLine.charAt(i));
	        		else if (!temp.toString().equals("")) {
	        			String t = new String(temp.toString());	
	        			grid[x++][y] = t;
	        			temp = new StringBuilder();
	        			
	        		}
	        	}
	        	grid[x][y++] = temp.toString();
    			temp = new StringBuilder();
	        	x = 0;
	        }
	        if (grid[0].length!=0) { 
	        	length = grid[0].length;
	        	codigo = new long [length];
	        	nombre = new String [length];
	        	precio = new int[length];
	        	cantidad = new int[length];
	        	for(int i = 0; i < length;i++) {
	        		if (Utils.isNumber(grid[0][i]));
	        			codigo[i] = Long.parseLong(grid[0][i]);
	        	}
	        	nombre = grid[1];
	        	for(int i = 0; i < length;i++) {
	        		if (Utils.isNumber(grid[2][i]));
	        			precio[i] = Integer.parseInt(grid[2][i]);
	        	}
	        	for(int i = 0; i < length;i++) {
	        		if (Utils.isNumber(grid[3][i]));
	        			cantidad[i] = Integer.parseInt(grid[3][i]);
	        	}
	        	ventana = new Ventana(true);
	        } else
	        	requestPassword();
		} catch (IOException e1) {
			Utils.dialogo(null,"No se pudo leer el archivo del inventario, se creara un nuevo archivo.",(ActionEvent e)->{
				requestPassword();
				Utils.getWindow((Component)e.getSource()).dispose();
				});
			
		}
	}
	
	

	public String getString(int x,int y) {
		if (y>length)
			y = length;
		else if (y < 0) y = 0;
		switch (x) {
		case 0: return Long.toString(codigo[y]);
		case 1: return nombre[y];
		case 2: return Integer.toString(precio[y]);
		case 3: return Integer.toString(cantidad[y]);
		default:return "Undefined" ;
		}
	}
	
	public int getValue(int x, int y) {
		switch (x) {
		case 2: return precio[y];
		case 3: return cantidad[y];
		default: return 0;
		}
	}

	public int length() {
		return length;
	}
	
	public Component getLabel(int i, int j) {
		Label temp = new Label(getString(i,j));
		return temp;
	}

	public long getBarcode(int y) {
		return codigo[y];
	}

	public void addCantidad(int y, int value) {
		cantidad[y] +=value;
		revalidate();
	}
	
	public void setCantidad(int y, int value) {
		cantidad[y] = value;
		revalidate();
	}
	
	public void setPrecio(int y, int value) {
		precio[y] = value;
		revalidate();
	}
	
	public void setNombre(int y, String nom) {
		nombre[y] = nom;
	}

	
	void add(long barcode, String productName, int price, int amount) {
		nombre = Utils.arrayResize(nombre);
		codigo = Utils.arrayResize(codigo);
		cantidad = Utils.arrayResize(cantidad);
		precio = Utils.arrayResize(precio);
		codigo[length] = barcode;
		nombre[length] = productName;
		precio[length] = price;
		cantidad[length++] = amount;
		revalidate();
		
	}

	void add(long argument0, int value) {
		add(argument0,"",0,value);
	}
	
	private void requestPassword() {
		class PasswordGetter extends Frame implements ActionListener {
			private Label instruccion;
			private TextField confirmPassword;
			private TextField password;
			public PasswordGetter() {
				instruccion = new Label("Cree contraseña:",1);
				password = new TextField();
				password.setEchoChar('*');
				password.addActionListener(this);
				Label confirmar = new Label("Repita contraseña:",1);
				confirmPassword = new TextField();
				confirmPassword.setEchoChar('*');
				confirmPassword.addActionListener(this);
				Button button = new Button("Aceptar");
				button.addActionListener(this);
				Utils.makeCloseable(this);
				int width = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
				int height = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
				setResizable(false);
				//safecode
				add(new Panel(),"South");add(new Panel(),"West");add(new Panel(),"East");add(new Panel(),"North");
				Panel center = new Panel();
				add(center);
				Dimension prefSize = new Dimension(width/6,height/29);
				password.setPreferredSize(prefSize);
				confirmPassword.setPreferredSize(prefSize);
				center.setLayout(new GridLayout(0,1));
				center.add(instruccion);
				center.add(password);
				center.add(confirmar);
				center.add(confirmPassword);
				Panel bottom = new Panel();
				center.add(bottom);
				bottom.add(new Panel());
				bottom.add(button);
				bottom.add(new Panel());
				setVisible(true);
				pack();
				setLocation(width/2-width/10,height/2-height/8);
			}
			@Override
			public void actionPerformed(ActionEvent e) {
				if (password.getText().equals(confirmPassword.getText())) {
					if (!password.getText().equals("")) {
						contrasenia = password.getText();
						length = 0;
						dispose();
						password = null;
						confirmPassword = null;
						instruccion = null;
						ventana = new Ventana(false);
					}
				} else  {
					instruccion.setText("Contraseña no coincide");
					pack();
					java.awt.Toolkit.getDefaultToolkit().beep();
				}
			}
		}
		new PasswordGetter();
	}

	void save() {
		try(BufferedWriter writer = new BufferedWriter(
				new java.io.OutputStreamWriter(
						new java.io.FileOutputStream(working_dir+"file.csv"),"Cp1252"))) {
			writer.write(contrasenia+",artículo,precio,cantidad,tags\n");
			for(int i = 0; i < length;i++) {
				for(int j = 0; j < width;j++) {
					writer.write(getString(j,i));
					//if (j!=width-1)
						writer.write(',');
				}
				if (i!=length-1)
					writer.newLine();
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			Utils.dialogo(Utils.getWindow(this),"El archivo no se pudo guargar",Utils.singleton);
		} 
	}
}