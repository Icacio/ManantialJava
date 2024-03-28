package com.example.manantial.vista;

import static com.example.manantial.controlador.Controlador.inventario;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.ScrollPane;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import com.example.manantial.controlador.Controlador;
import com.example.manantial.controlador.Utils;
import com.example.manantial.modelo.Inventario;

public class Ventana extends Frame implements ActionListener, WindowListener {
	public final SearchBox barra = new SearchBox(45);;
	private final Spinner spinner = new Spinner();
	private final Button botonInventario = new Button(Language.botones[0]);
	private final Label instrucciones = new Label(Language.mensaje[0]);
	private final Button pagar = new Button(Language.pay+"0    ");
	private final ScrollPane center = new ScrollPane();
	private Inventario drawing;
	boolean flag = false;
	private boolean caja;
	
	public Ventana(boolean caja) {
		super(Language.titulo[caja?1:0]);
		this.caja = caja;
		drawing = caja?Controlador.venta():inventario;
		generalSettings();
	}
		
	private void generalSettings() {
		addWindowListener(this);
		Panel butonera = new Panel();
		Button botonAgregar = new Button(Language.add);
		botonAgregar.addActionListener((this));
		spinner.addActionListener(this);
		butonera.add(instrucciones);
		butonera.add(barra);
		addComponentListener(barra);
		butonera.add(spinner);
		butonera.add(botonAgregar);
		butonera.add(botonInventario);
		butonera.add(pagar);
		pagar.addActionListener((e)->pagar());
		botonInventario.addActionListener((e) -> reviewPassword());
		if (!caja) {
			pagar.setVisible(false);
			botonInventario.setLabel(Language.botones[1]);
			if (inventario.length()==0)
				botonInventario.setVisible(false);
		}
		add(new Panel(),BorderLayout.EAST);
		add(new Panel(),BorderLayout.WEST);
		add(new Panel(),BorderLayout.SOUTH);
		add(butonera,BorderLayout.NORTH);
		add(center);
		center.add(drawing);
	}
	
	private void changeRoom() {
		center.removeAll();
		caja = !caja;
		if (caja) {
			if (flag) {
				inventario.removeLine();
				flag = false;
			}
			Controlador.save(inventario);
			setTitle(Language.titulo[0]);
			botonInventario.setLabel(Language.botones[0]);
			drawing = Controlador.venta();
			pagar.setVisible(true);
		} else {
			Controlador.save(drawing);
			setTitle(Language.titulo[1]);
			botonInventario.setLabel(Language.botones[1]);
			drawing = inventario;
			pagar.setVisible(false);
		}
		drawing.setPreferredSize(new Dimension(drawing.getPreferredSize().width,(2+drawing.length())*getFont().getSize()));
		center.add(drawing);
		pack();
		barra.requestFocusInWindow();
	}

	private void reviewPassword() {
		class PasswordGetter extends Dialog implements ActionListener {
			private final String error = Language.wrongPassword;
			private Label instruccion;
			private TextField password;
			public PasswordGetter(Frame a) {
				super(a);
				instruccion = new Label(Language.askPassword,Label.CENTER);
				password = new TextField();
				password.setEchoChar('*');
				password.addActionListener(PasswordGetter.this);
				Button button = new Button(Language.accept);
				button.addActionListener(this);
				Utils.makeCloseable(this);
				int width = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
				int height = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
				setResizable(false);
				add(new Panel(),"South");add(new Panel(),"West");add(new Panel(),"East");add(new Panel(),"North");
				Panel center = new Panel();
				add(center);
				Dimension prefSize = new Dimension(width/6,height/29);
				password.setPreferredSize(prefSize);
				center.setLayout(new GridLayout(0,1));
				center.add(instruccion);
				center.add(password);
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
				if (Controlador.password(password.getText())) {
					changeRoom();
					dispose();
				} else  {
					instruccion.setText(error);
					pack();
					java.awt.Toolkit.getDefaultToolkit().beep();
				}
			}
		}
		if (caja)
			new PasswordGetter(null);
		else changeRoom();
	}

	private void pagar() {
		inventario.resta(drawing);
		Controlador.save(inventario);
		Controlador.save(Controlador.today().suma(drawing));
		drawing = Controlador.venta();
		center.add(drawing);
		revalidate();
		barra.requestFocusInWindow();
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
		            if (amount+drawing.getCantidad(resultado)>inventario.getCantidad(yDelInventario)) {
		            	Utils.dialogo(this,Language.notEnough,Utils.singleton);
		            	return;
		            }
	                drawing.addCantidad(resultado,amount);
		        	int total = 0;
		        	for(int i = 0; i < drawing.length();i++) {
		        		total += (drawing.getPrecio(i)*drawing.getCantidad(i));
		        	}
		        	pagar.setLabel("Pagar: $"+Integer.toString(total));//*/
	                
		        } else {
		        	if (amount>inventario.getCantidad(yDelInventario)) {
		            	Utils.dialogo(this,Language.notEnough,Utils.singleton);
		            	return;
		            }
		        	drawing.add(
		        			argument0,
		        			inventario.getString(1,yDelInventario),
		        			inventario.getPrecio(yDelInventario),
		        			amount);
		        	int total = 0;
		        	for(int i = 0; i < drawing.length();i++) {
		        		total += (drawing.getPrecio(i)*drawing.getCantidad(i));
		        	}
		        	pagar.setLabel(Language.pay+Integer.toString(total));
		        	pack();
		        }
		    } else {
		    	inventario.addCantidad(resultado,amount);}
		} else {
			if (!caja) {
				if (!flag ) {
					inventario.add(argument0,amount);
					flag = true;
					instrucciones.setText(Language.mensaje[1]);
					pack();
				}
		    }
		}
        spinner.setValue(0);
        barra.setText("");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String string = barra.getText();
		if (Utils.isNumber(string))
			agregar(Long.parseLong(string),spinner.getValue()!=0?spinner.getValue():1);
		else {
			if (!caja&&flag) {
				int leng = drawing.length()-1;
				drawing.setPrecio(leng,spinner.getValue());
				drawing.setNombre(leng,barra.getText());
				flag = false;
				instrucciones.setText(Language.mensaje[0]);
				pack();
	            barra.setText("");
	            spinner.setValue(0);
			}
		}
		barra.requestFocusInWindow();
	}

	@Override
	public void windowClosing(WindowEvent e) {
		Controlador.save(drawing);
		if (caja)
			Controlador.save(inventario);
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