package com.example.manantial.controlador;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.example.manantial.modelo.Inventario;
import com.example.manantial.modelo.Tabla;
import com.example.manantial.vista.Language;
import com.example.manantial.vista.MyDialog;
import com.example.manantial.vista.PasswordGetter;
import com.example.manantial.vista.TableView;
import com.example.manantial.vista.Ventana;
import com.example.manantial.vista.components.SearchBox;
import com.example.manantial.vista.components.Spinner;

public class MainController implements ActionListener {
	
	public static final Ventana ventana = Ventana.singleton;
	public final static String working_dir = System.getProperty("os.name").toLowerCase().contains("win")?System.getenv("APPDATA")+"\\Manantial\\":"\\";
	public static final Inventario inventario = Inventario.singleton;
	public static final MainController tableController = new MainController();
	public final TableView tableDrawn = new TableView(inventario);
	public final SearchBox barra = new SearchBox(45);
	public final Spinner spinner = new Spinner();
	public final Button botonInventario = new Button(Language.views[caja?1:0]);
	public final Label instrucciones = new Label(Language.mensaje[0]);
	public final Button pagar = new Button(Language.pay+"0    ");
	public final ScrollPane center = new ScrollPane();
	public static boolean caja;
	private boolean inserting = false;
	
	public static void main (String args[]) {
		inventario.getInventario();
		tableController.configureVentana();
	}
	private void configureVentana() {
		ventana.setTitle(Language.views[caja?1:0]);
		ventana.addWindowListener(ventana);
		Panel butonera = new Panel();
		ventana.setFont(new Font("arial", Font.PLAIN, 16));
		Button botonAgregar = new Button(Language.add);
		butonera.add(instrucciones);
		butonera.add(barra);
		butonera.add(spinner);
		spinner.addActionListener(this);
		butonera.add(botonAgregar);
		botonAgregar.addActionListener(this);
		butonera.add(pagar);
		butonera.add(botonInventario);
		botonInventario.addActionListener(this::changeView);
		tableDrawn.setPreferredSize(new Dimension(
				tableDrawn.getPreferredSize().width,
				tableDrawn.length()*ventana.getFont().getSize()));
		if (caja) {
			tableDrawn.tabla = venta();
		} else {
			pagar.setVisible(false);
			botonInventario.setVisible(false);
		}
		ventana.add(new Panel(),BorderLayout.EAST);
		ventana.add(new Panel(),BorderLayout.WEST);
		ventana.add(new Panel(),BorderLayout.SOUTH);
		ventana.add(butonera,BorderLayout.NORTH);
		ventana.add(center);
		center.add(tableDrawn);
		ventana.pack();
		ventana.setVisible(true);
	}

	public static void abort(Exception e) {
		System.out.println(e.getMessage());
		e.printStackTrace();
		Utils.dialogoError();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String string = barra.getText();
		if (Utils.isNumber(string)) {
			var codigo = Long.parseLong(string);
			var amount= spinner.getValue()!=0?spinner.getValue():1;
			int resultado = -1;
			for(int i = 0; i < inventario.length();i++)
				if (codigo==inventario.getBarcode(i)) //compara el código con todos los datos del arreglo
					resultado = i;
			if (resultado != -1) {//si existe en el inventario
				if (caja) {
					
				} else {//y está en la administración
					inventario.addCantidad(resultado,amount);//lo suma
				}
				spinner.setValue(0);
				barra.setText("");
				tableDrawn.repaint();
			} else {//si no existe en el inventario
				if (!caja) { //y está en la administración
					if (!inserting) {//si no está insertando nada
						inventario.add(codigo,amount);
						inserting = true;
						instrucciones.setText(Language.mensaje[1]);
						spinner.setValue(0);
						barra.setText("");
						tableDrawn.repaint();
					}
				}
			}
		} else {//si es texto
			if (!caja&&inserting) {//si es la administración del inventario Y estamos insertando algo
				int leng = tableDrawn.length()-1;
				tableDrawn.tabla.setPrecio(leng,spinner.getValue());
				tableDrawn.tabla.setNombre(leng,barra.getText());
				inserting = false;
				instrucciones.setText(Language.mensaje[0]);
				spinner.setValue(0);
				barra.setText("");
				tableDrawn.repaint();
			}
		}
		barra.requestFocusInWindow();
	}
	
	public void close() {
		tableDrawn.tabla.save();
		if (caja)
			inventario.save();
	}
	
	private void changeView(ActionEvent e) {
		if (caja) {
			if (!validatePassword())
				return;
			else {
				pagar.setVisible(false);
				tableDrawn.tabla = inventario;
			}
		} else {
				pagar.setVisible(true);
				tableDrawn.tabla = venta();
		}
		caja = !caja;
		var index = caja?0:1;
		ventana.setTitle(Language.views[1-index]);
		botonInventario.setLabel(Language.views[index]);
		tableDrawn.repaint();
		ventana.revalidate();
	}
	
	private Tabla venta() {
		return new Tabla();
	}
	
	private boolean validatePassword() {
		var getter = new PasswordGetter(false);
		var response = getter.response;
		if (response==null)
			return false;
		try (var con = DriverManager.getConnection("jdbc:derby:"+working_dir+"\\Manantial;user=root;password="+response)) {
			return true;
		} catch (SQLException e) {
			if (e.getSQLState().equals("08004")) {
				new MyDialog(Language.passwordError);
				return false;
			}
			Utils.dialogoError();
		}
		return false;
	}

	private MainController() {}
}