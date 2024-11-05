package com.example.manantial.controlador;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;

import com.example.manantial.modelo.Inventario;
import com.example.manantial.modelo.Tabla;
import com.example.manantial.vista.Language;
import com.example.manantial.vista.MyDialog;
import com.example.manantial.vista.PasswordGetter;
import com.example.manantial.vista.components.TableView;
import com.example.manantial.vista.Ventana;
import com.example.manantial.vista.components.SearchBox;
import com.example.manantial.vista.components.Spinner;

import static com.example.manantial.controlador.FileController.fc;

public class MainController implements ActionListener {
	
	public static final Ventana ventana = Ventana.singleton;
	public final static String working_dir = System.getProperty("os.name").toLowerCase().contains("win")?System.getenv("APPDATA")+"\\Manantial\\":System.getProperty("file.separator");
	public static final Inventario inventario = Inventario.singleton;
	public static final MainController tableController = new MainController();
	public final TableView tableDrawn = new TableView(inventario);
	public final SearchBox barra = new SearchBox(45);
	public final Spinner spinner = new Spinner();
	public final Button botonInventario = new Button(Language.views[caja?1:0]);
	public final Label instrucciones = new Label(Language.mensaje[0]);
	static final String initialPayMessage = Language.pay+"0    ";
	public final Button pagar = new Button(initialPayMessage);
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
		pagar.addActionListener(this::pagar);
		butonera.add(botonInventario);
		botonInventario.addActionListener(this::changeView);
		tableDrawn.setFont(ventana.getFont());
		if (caja) {
			tableDrawn.setTabla(venta());
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
				if (caja) {//y está en la caja
					if (inventario.getCantidad(resultado)<1) {
						new MyDialog(Language.notEnough);
						return;
					}
					
					int cajaIndex = -1;
					for (int j = 0; j < tableDrawn.length(); j++) {
						if (codigo==tableDrawn.getTabla().getBarcode(j)) {
							cajaIndex = j;//compara el código con todos los datos de la venta actual
						}
					}
					if (cajaIndex != -1) {//si existe en la caja
						if (amount+tableDrawn.getTabla().getCantidad(cajaIndex)>inventario.getCantidad(resultado)) {
							new MyDialog(Language.notEnough);
						}
						else
							tableDrawn.addCantidad(cajaIndex,amount);//lo suma
					} else {//no lo agrega
						tableDrawn.add(codigo,inventario.getString(1,resultado),inventario.getPrecio(resultado),amount);
					}
					var height = tableDrawn.length();
					int suma = 0;
					for (int i = 0; i < height; i++) {
						suma += (tableDrawn.getTabla().getCantidad(i)*tableDrawn.getTabla().getPrecio(i));
					}
					pagar.setLabel(Language.pay+suma);
					
				} else {//y está en la administración
					tableDrawn.addCantidad(resultado,amount);//lo suma
				}
				spinner.setValue(0);
				barra.setText("");
			} else {//si no existe en el inventario
				if (!caja) { //y está en la administración
					if (!inserting) {//si no está insertando nada
						inventario.add(codigo,amount);
						inserting = true;
						instrucciones.setText(Language.mensaje[1]);
						spinner.setValue(0);
						barra.setText("");
					}
				}
			}
		} else {//si es texto
			if (!caja&&inserting) {//si es la administración del inventario Y estamos insertando algo
				int leng = tableDrawn.length()-1;
				tableDrawn.setPrecio(leng,spinner.getValue());
				tableDrawn.setNombre(leng,barra.getText());
				inserting = false;
				instrucciones.setText(Language.mensaje[0]);
				spinner.setValue(0);
				barra.setText("");
				var d = tableDrawn.getPreferredSize();
				d.height = (tableDrawn.length()+1)*tableDrawn.getFont().getSize();
				tableDrawn.setPreferredSize(d);
				tableDrawn.revalidate();
				tableDrawn.repaint();
			}
		}
		barra.requestFocusInWindow();
	}
	
	public void close() {
		tableDrawn.getTabla().save();
		if (caja)
			inventario.save();
	}
	
	private void changeView(ActionEvent e) {
		if (caja) {
			if (!validatePassword())
				return;
			else {
				pagar.setVisible(false);
				tableDrawn.getTabla().save();
				tableDrawn.setTabla(inventario);
			}
		} else {
				pagar.setVisible(true);
				tableDrawn.setTabla(venta());
		}
		caja = !caja;
		var index = caja?0:1;
		ventana.setTitle(Language.views[1-index]);
		botonInventario.setLabel(Language.views[index]);
		ventana.pack();
		tableDrawn.revalidate();
	}
	
	private Tabla venta() {
		var venta = fc.read(working_dir+"venta","venta");
		var suma = 0;
		for (int i = 0; i < venta.length();i++)
			suma += (venta.getCantidad(i)*venta.getPrecio(i));
		pagar.setLabel(Language.pay+suma);
		return venta;
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

	private void pagar(ActionEvent e) {
		today().suma(tableDrawn.getTabla()).save();
		inventario.resta(tableDrawn.getTabla());
		tableDrawn.setTabla(venta());
		pagar.setLabel(initialPayMessage);
	}
	
	private Tabla today() {
		var date = LocalDate.now();
		var tableName = date.getYear()+System.getProperty("file.separator")+date.getMonthValue()+System.getProperty("file.separator")+date.getDayOfMonth()+".csv";
		var fileName = working_dir+tableName;
		return fc.read(fileName,tableName);
	}

	private MainController() {}

	public void write(Tabla tabla, String tableName) {
		var fileName = working_dir+tableName;
		fc.write(tabla,fileName);
	}
}