package com.example.manantial;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Ventana extends Frame implements ActionListener, WindowListener {
	private final SearchBox barra = new SearchBox(45);
	private final Spinner spinner = new Spinner();
	private final Button botonInventario = new Button("Inventario");
	private final Inventario inventario = Inventario.inventario;
	private final Label instrucciones = new Label("Ingresa el código de barras y la cantidad a añadir.");
	final Button pagar = new Button("Pagar: $0    ");
	ScrollPane center = new ScrollPane();
	Inventario drawing;
	boolean flag = false;
	private boolean caja;
	
	private Ventana(boolean caja) {
		super(caja?"Caja":"Inventario");
		this.caja = caja;
		drawing = caja?new Inventario():inventario;
		generalSettings();
		pack();
	}
	private void changeRoom(boolean caja) {
		if (caja!=this.caja)
			changeRoom();
	}
	
	private void changeRoom() {
		center.removeAll();
		caja = !caja;
		if (caja) {
			Utils.makeCloseable(this);
			setTitle("Caja");
			drawing.save();
			botonInventario.setLabel("Inventario");
			drawing = new Inventario();
			pagar.setVisible(true);
		} else {
			addWindowListener(this);
			setTitle("Inventario");
			botonInventario.setLabel("Volver");
			drawing = Inventario.inventario;
			pagar.setVisible(false);
		}
		center.add(drawing);
		revalidate();
	}
	
	private void generalSettings() {
		Utils.makeCloseable(this);
		Panel butonera = new Panel();
		Button botonAgregar = new Button("Agregar");
		botonAgregar.addActionListener((this));
		butonera.add(instrucciones);
		butonera.add(barra);
		barra.addActionListener(this);
		
		butonera.add(spinner);
		spinner.addActionListener(this);
		butonera.add(botonAgregar);
		butonera.add(botonInventario);
		butonera.add(pagar);
		botonInventario.addActionListener((ActionEvent e) -> changeRoom());
		if (!caja) {
			botonInventario.setLabel("Volver");
			if (inventario.length()==0)
				botonInventario.setVisible(false);
		}
		add(new Panel(),"East");
		add(new Panel(),"West");
		add(new Panel(),"South");
		add(butonera,"North");
		add(center);
		center.add(drawing);
	}

	private void agregar(String string) {
		if (Utils.isNumber(string))
			agregar(Long.parseLong(string),spinner.getValue()!=0?spinner.getValue():1);
		else {
			//Debug.
			if (!caja&&flag) {
				int leng = drawing.length()-1;
				drawing.setPrecio(leng,spinner.getValue());
				drawing.setNombre(leng,barra.getText());
				flag = false;
				instrucciones.setText("Ingresa el código de barras y la cantidad a añadir.");
			}
		}
	}

	private void agregar(long argument0,int amount) {
		int resultado = -1;
		int yDelInventario = -1;
		for(int i = 0; i < inventario.length();i++)
		    if (argument0==inventario.getBarcode(i))
		        resultado = i;
		if (resultado != -1) {//existe en el inventario
		int _leng = drawing.length();
		    if (caja) {//if it is the checkout
		    	yDelInventario = resultado;
		    	resultado = -1;
		        for(int i = 0; i < _leng; i++)
		        	if (argument0==drawing.getBarcode(i))
		        		resultado = i; //if exists in the checkout
		        if (resultado != -1) {//y también en la caja
		            if (amount+drawing.getValue(3,resultado)>inventario.getValue(3,yDelInventario)) {
		            	Utils.dialogo(this,"Inventario insuficiente",Utils.singleton);
		            	return;
		            }
	                drawing.addCantidad(resultado,amount);
		        	int total = 0;
		        	for(int i = 0; i < drawing.length();i++) {
		        		total += (drawing.getValue(2,i)*drawing.getValue(3,i));
		        	}
		        	pagar.setLabel("Pagar: $"+Integer.toString(total));//*/
	                
		        } else {
		        	if (amount>inventario.getValue(3,yDelInventario)) {
		            	Utils.dialogo(this,"Inventario insuficiente",Utils.singleton);
		            	return;
		            }
		        	drawing.add(
		        			argument0,
		        			inventario.getString(1,yDelInventario),
		        			inventario.getValue(2,yDelInventario),
		        			amount);
		        	int total = 0;
		        	for(int i = 0; i < drawing.length();i++) {
		        		total += (drawing.getValue(2,i)*drawing.getValue(3,i));
		        	}
		        	pagar.setLabel("Pagar: $"+Integer.toString(Debug.print(total)));
		        }
		    } else {
		    	inventario.addCantidad(resultado,amount);}
		} else {
			if (!caja) {
				if (!flag ) {
					inventario.add(argument0,amount);
					flag = true;
					instrucciones.setText("Ingresa el nombre de artículo y el precio.");
				}
		    }
		}
        spinner.setValue(0);
        barra.setText("");
	}

	/*private void guiSettings(Inventario drawing) {
		/*center.removeAll();
		for (int i = 0; i < inventario.length();i++) {
			codigo.add(drawing.getLabel(0,i));
			nombre.add(drawing.getLabel(1,i));
			right.add(drawing.getLabel(2,i));
			right.add(drawing.getLabel(3,i));
		}
	}//*/

	@Override
	public void actionPerformed(ActionEvent e) {
		agregar(barra.getText());
		barra.requestFocusInWindow();
	}

	@Override
	public void windowClosing(WindowEvent e) {
		inventario.save();
		dispose();
	}
	public static Window singleton() {
		if (Inventario.singleton==null)
			return new Ventana(true);
		return Inventario.singleton;
	}
	public static Ventana singleton(boolean caja) {
		if (Inventario.singleton == null)
			return new Ventana(caja);
		Inventario.singleton.changeRoom(caja);
		return Inventario.singleton;
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