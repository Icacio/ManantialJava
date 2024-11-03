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
import java.awt.event.KeyListener;
import java.awt.event.FocusListener;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionEvent;
import java.awt.event.TextEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.FocusEvent;

import com.example.manantial.controlador.Utils;

import static com.example.manantial.controlador.MainController.inventario;
import static com.example.manantial.controlador.MainController.tableController;
import static com.example.manantial.controlador.MainController.ventana;

public class SearchBox extends TextField implements ActionListener, TextListener, ItemListener, KeyListener
	, FocusListener
	, ComponentListener {

	private Window tooltip;
	private Hashtable<String,String> table;
	public List[] results = new List[3];

	public SearchBox (int i) {
		super(null,i);
		addActionListener(this);
		addTextListener(this);
		addKeyListener(this);
		addFocusListener(this);
	}
	
	@Override
	public void addNotify() {
		super.addNotify();
		Utils.getWindow(this).addComponentListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (Utils.isNumber(getText())) 
			tableController.actionPerformed(e);
		else {
			var sel = results[0].getSelectedItem();
			if (sel!=null)
				setText(table.get(sel));
		}
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

	@Override
	public void focusGained(FocusEvent e) {
		window();
		table = new Hashtable<String, String>();
		results = new List[3];
	}

	@Override
	public void focusLost(FocusEvent e) {
		tooltip.dispose();
		results = null;
		table = null;
	}
	
	@Override
	public void componentMoved(ComponentEvent e) {
		if (Utils.getWindow(this).isVisible()) {
			java.awt.Point location = getLocationOnScreen();
			window().setLocation(location.x, location.y + getBounds().height);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (results == null) return;
		if (results[0]==null) return;
		int index;
		if (e.getKeyCode()==KeyEvent.VK_DOWN) {
			index = results[0].getSelectedIndex()+1;
		} else if(e.getKeyCode()==KeyEvent.VK_UP) {
			index = results[0].getSelectedIndex()-1;
		} else return;
		results[0].select(index);
		results[1].select(index);
		results[2].select(index);
	}
	
	public void componentResized(ComponentEvent e) {}
	public void componentShown(ComponentEvent e) {}
	public void componentHidden(ComponentEvent e) {}
	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
}