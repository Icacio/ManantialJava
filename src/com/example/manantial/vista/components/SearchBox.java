package com.example.manantial.vista.components;

import java.awt.TextField;
import java.util.Hashtable;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.Panel;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.TextListener;
import java.awt.event.ItemListener;
import java.awt.event.TextEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ActionEvent;

import com.example.manantial.controlador.Utils;

import static com.example.manantial.controlador.MainController.inventario;
import static com.example.manantial.controlador.MainController.tableController;
import static com.example.manantial.controlador.MainController.ventana;

public class SearchBox extends TextField implements ActionListener, TextListener, ItemListener
	{
	private Window tooltip;
	private Hashtable<String,String> table = new Hashtable<String, String>();
	public List[] results = new List[3];

	public SearchBox (int i) {
		super(null,i);
		addActionListener(this);
		addTextListener(this);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (Utils.isNumber(getText())) 
			tableController.actionPerformed(e);
	}
	
	@Override
	public void textValueChanged(TextEvent e) {
		var tooltip = window();
		String[][] coincidence = {{},{},{}};
		int coincidences = 0;
		var texto = getText();
		if (!texto.matches(" *")&&!Utils.isNumber(texto)) {
			int length = inventario.length();
			for (int i = 0; i < length;i++) {
				String a = inventario.getString(1,i);
				if (a.contains(texto)) {
					coincidence[0] = Utils.arrayAdd(coincidence[0],inventario.getString(1,i));
					coincidence[1] = Utils.arrayAdd(coincidence[1],inventario.getString(2,i));
					coincidence[2] = Utils.arrayAdd(coincidence[2],inventario.getString(3,i));
					table.put(inventario.getString(1,i),inventario.getString(0,i));
					coincidences++;
				}
			}
			if (coincidences < 1) {
				disposeWindow();
			} else {
				for (int i= 0; i < 3;i++) {
					results[i] = new List(coincidences);
					results[i].setFont(getFont());
					results[i].addItemListener(this);
				}
				tooltip.add(results[0]);
				Panel right = new Panel(new GridLayout(0,2));
				tooltip.add(right,BorderLayout.EAST);
				right.add(results[1]);
				right.add(results[2]);
				for(int i = 0; i < coincidences; i++) {
					for (int j = 0; j < 3; j++) {
						results[j].add(coincidence[j][i]);
					}
				}
				tooltip.pack();
				if (!tooltip.isVisible()) {
					tooltip.setVisible(true);
				}
			}
		} else {
			disposeWindow();
		}
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		var selected = ((List)e.getSource()).getSelectedIndex();
		if (selected!=-1) {
			setText(table.get(results[0].getItem(selected)));
		}
	}
	
	private Window window() {
		if (tooltip==null) {
			tooltip = new Window(ventana);
			tooltip.setLayout(new BorderLayout());
			tooltip.setFocusableWindowState(false);
			var point = getLocationOnScreen();
			tooltip.setLocation(point.x,point.y+getHeight());
		}
		
		return tooltip;
	}
	
	private void disposeWindow() {
		if (tooltip!=null) {
			tooltip.dispose();
			tooltip.removeAll();
		}
	}
}