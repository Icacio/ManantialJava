package com.example.manantial;

import java.awt.Button;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.BufferedWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

class Inventario extends Canvas {//main
	static final String working_dir = System.getProperty("os.name").toLowerCase().contains("win")?System.getenv("APPDATA")+"\\manantial\\":"\\";
	final static String venta = "venta";
	final static String filecsv = "file.csv";
	static final Inventario inventario = new Inventario(filecsv);
	long[] codigo;
	int[] cantidad;
	int[] precio;
	String[] nombre;
	private int length;
	private int changes = 0;
	final String fileName;
	static Ventana ventana;
	private static String contrasenia;
	public static final int width = 4;
	
	public static void main (String[] args) {
		if (inventario.length>0)
			ventana = new Ventana(true);
		else if (inventario.length==0) {
			Utils.dialogo(null,"No se pudo leer el archivo del inventario, se creara un nuevo archivo.",(e)->{
				requestPassword();
				Utils.getWindow((Component)e.getSource()).dispose();
			});
		} else {
			inventario.length = 0;
			requestPassword();}
	}
	//static constructor
	/**
     * Creates and returns a new table by loading the file with the date
     * of today and if it doesn't exists, it creates the folder for
     * future files to be saved and returns an empty table.
     */
	static Inventario today() {
		Date today = new Date();
		String date = new SimpleDateFormat("yyyy\\MM\\").format(today);
		new File(working_dir+date).mkdirs();
		date = new SimpleDateFormat("yyyy\\MM\\dd").format(today)+".csv";
		return new Inventario(date);
	}//*/

	
	static Inventario venta() {
		File ventaF = new File(working_dir+venta);
		if (ventaF.exists()) {
			Inventario retorno = new Inventario(venta);
			ventaF.delete();
			retorno.changes = 25;
			return retorno;
		}
		return new Inventario();
	}
	
	//constructors
	private Inventario() {
		fileName = venta;
	}
	/**
     * Creates and returns a new table by reading the file with the date
     * of today and if it doesn't exists, it creates the folder for
     * future files to be saved.
     * @param   filename
     *          the file to be loaded, if it doesn't exist, it just reserves it for future saving.
     */
	private Inventario(String filename) {
		fileName = filename;
		if (new File(working_dir+filename).exists()) {
			String [][] grid = loadFile(filename);
			if (grid==null) {
				length = 0;
				return;
			}
			if (grid!=null&&grid[0].length>0) {
				length = grid[0].length;
				codigo = new long [length];
				nombre = new String [length];
				precio = new int[length];
				cantidad = new int[length];
				
				for(int i = 0; i < length;i++) {
					if (Utils.isNumber(grid[0][i]))
						codigo[i] = Long.parseLong(grid[0][i]);
				}
				nombre = grid[1];
				for(int i = 0; i < length;i++) {
					if (Utils.isNumber(grid[2][i]))
					precio[i] = Integer.parseInt(grid[2][i]);
				}
				for(int i = 0; i < length;i++) {
					if (Utils.isNumber(grid[3][i]))
					cantidad[i] = Integer.parseInt(grid[3][i]);
				}
			} else length = 0;
		} else {
			if (filename.equals("file.csv")) {
				length = -1;
			}
		}
	}
	
	private static String[][] loadFile(String file) {
		try (BufferedReader reader = new BufferedReader(
				new java.io.InputStreamReader(
						new java.io.FileInputStream(working_dir+file),"Cp1252"));) {
			
			String[][] grid = new String [5][file.equals("file.csv")?2:1];
			String currentLine = "";
			byte x = 0;
			int y = 0;
			StringBuilder temp = new StringBuilder();
	        if ((currentLine = reader.readLine()) != null) {
	        	if (!file.equals(venta)) {
	        		if(file.equals(filecsv)) {
	        			int i = 0;
	        			while(i < currentLine.length()) {
	        				if(currentLine.charAt(i)==',') break;
	        				i++;
	        			}
	        			if (i==0) return null;
	        			contrasenia = currentLine.substring(0,i);
	        		}
	        		currentLine = reader.readLine();
	        	}
	        	if (currentLine==null) return null;
			do {
	        	if (currentLine.substring(0,4).equals("0,0,")||currentLine.substring(0,3).equals("0,,"))
	        		continue;
	        		
	        	if (y>=grid[0].length) {
	        		
    				for (int i = 0; i < 5; i++) {
    					grid[i]=Utils.arrayResize(grid[i]);
    				}
    			}
	        	final int sz = currentLine.length();
	        	for (int i = 0; i < sz;i++ ) {
	        		if (currentLine.charAt(i)!=',')
	        			temp.append(currentLine.charAt(i));
	        		else {
	        			if (x>4) {x--;break;}
	        			String t = new String(temp.toString());	
	        			grid[x++][y] = t;
	        			temp = new StringBuilder();
	        			
	        		}
	        	}
	        	if (!grid[1][y].equals("")&&!grid[1][y].equals("0")) {
	        		grid[x][y++] = temp.toString();
	        	}
    			temp = new StringBuilder();
	        	x = 0;
	        } while((currentLine = reader.readLine()) != null);
	        } else return null;
	        return grid;
		} catch (IOException e1) {
			return null;
		}
	}
	
	//operaciones
	void save() {
		if (length()<0) return;
		if (changes==0) return;
		int i;
		for (i = fileName.length()-1; i > -1; i--) {
			if (fileName.charAt(i)=='\\') {
				break;
			}
		}
		if (i!=fileName.length()) {
			File file = new File(working_dir+fileName.substring(0,i+1));
			file.mkdir();}
		try (BufferedWriter writer = Utils.file(fileName)) {
			if (!fileName.equals(venta))
				writer.write(contrasenia+",artículo,precio,cantidad,tags\n");
			for(i = 0; i < length;i++) {
				for(int j = 0; j < width; j++) {
					writer.write(getString(j,i));
					writer.write(',');
				}
				writer.write("\n");
			}
			
			
		} catch (IOException e) {
			
		}
	}
	/**
     * Adds all the items from the table provided to the current one
     * 
     * @param  otro The {@code Inventario} to be merged with the current one
     * 
     * @return The function returns the object itself.
     */
	Inventario suma (Inventario otro) {
		if (otro==this) return this;
		for(int i = 0;i < otro.length(); i++) {
			int result = 0;
			for(int j = 0; j < length();j++) {
				if (otro.getBarcode(i)==getBarcode(j)) {
					setCantidad(j,getCantidad(j)+otro.getCantidad(i));
					result++;
				}
			}
			if (result<1) {
				add(otro.getBarcode(i),otro.getString(1,i),otro.getPrecio(i),otro.getCantidad(i));
			}
		}
		changes++;
		return (this);
	}
	
	public void resta(Inventario sustraendo) {
		if (this==sustraendo) return; //this most be an issue
		for (int i = 0; i < sustraendo.length();i++) {
			for(int j = 0; j < this.length();j++) {
				if (sustraendo.getBarcode(i)==this.getBarcode(j)) {
					cantidad[j] -= sustraendo.cantidad[i];
					break;
				}
			}
		}
		
		
	}
	
	public long getBarcode(int y) {
		return codigo[y];
	}
	
	public String getString(int x,int y) {
		if (y>length)
			y = length-1;
		else if (y < 0) y = 0;
		switch (x) {
		case 0: return Long.toString(codigo[y]);
		case 1: return nombre[y]==null?"":nombre[y];
		case 2: return Integer.toString(precio[y]);
		case 3: return Integer.toString(cantidad[y]);
		default:return "Undefined" ;
		}
	}
	
	public int getPrecio(int y) {
		return precio[y];
	}
	
	public int getCantidad(int y) {
		return cantidad[y];
	}
	
	public int length() {
		return length;
	}

	
	public void addCantidad(int y, int value) {
		cantidad[y] +=value;
		revalidate();
		changes++;
	}
	
	public void setCantidad(int y, int value) {
		cantidad[y] = value;
		revalidate();
		changes++;
	}
	
	public void setPrecio(int y, int value) {
		precio[y] = value;
		revalidate();
		changes++;
	}
	
	public void setNombre(int y, String nom) {
		nombre[y] = nom;
		changes++;
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
		changes++;
	}

	void add(long argument0, int value) {
		add(argument0,"",0,value);
	}
	//Component Method
	@Override
	public void paint (Graphics graphics) {
		int panelWidth = getSize().width;
		int a[] = {0,panelWidth*18/100,panelWidth*7/10,panelWidth*81/100,panelWidth*9/10};
		int cellHeigh = getFont().getSize();
		boolean caja = (this != inventario);
		if (caja)
			graphics.drawString("Total",a[4],cellHeigh);
		
	    graphics.drawString("Código de barras",a[0],cellHeigh);
		graphics.drawString("artículo",a[1],cellHeigh);
		graphics.drawString("precio",a[caja?2:3],cellHeigh);
		graphics.drawString("cantidad",a[caja?3:4],cellHeigh);
		
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
	//flow method
	private static void requestPassword() {
		class PasswordGetter extends Dialog implements ActionListener {
			private final String error[] = {"Las contraseñas no coinciden","No se admiten comas."};
			private Label instruccion;
			private TextField confirmPassword;
			private TextField password;
			public PasswordGetter(Frame a) {
				super(a);
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
					if (password.getText().contains(",")) {
						error(error[1]);
					} else
					if (!password.getText().equals("")) {
						contrasenia = password.getText();
						inventario.length = 0;
						dispose();
						password = null;
						confirmPassword = null;
						instruccion = null;
						ventana = new Ventana(false);
					}
				} else  {
					error(error[0]);
				}
			}
			private void error(String error) {
				instruccion.setText(error);
				pack();
				java.awt.Toolkit.getDefaultToolkit().beep();
			}
		}
		new PasswordGetter(null);
	}//*/
	public void removeLine() {
		long[] newcode = new long[length-1];
		System.arraycopy(codigo,0,newcode,0,length-2);
		codigo = newcode;
		String[] newString = new String[length-1];
		System.arraycopy(codigo,0,newString,0,length-2);
		nombre = newString;
		int[] newInt = new int[length-1];
		System.arraycopy(precio,0,newInt,0,length-2);
		precio = newInt;
		int[] newCant = new int[length-1];
		System.arraycopy(precio,0,newCant,0,length-2);
		cantidad = newCant;
	}
	public static boolean password(String text) {
		return contrasenia.equals(text);
	}
}