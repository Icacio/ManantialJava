package com.example.manantial.vista;

import java.lang.String;
import java.util.Hashtable;

import com.example.manantial.controlador.Utils;

import static com.example.manantial.controlador.Controlador.inventario;
import static com.example.manantial.controlador.Controlador.ventana;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
//import java.awt.event.MouseMotionListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;

public class SearchBox extends TextField implements KeyListener,
	TextListener, FocusListener, ComponentListener, ItemListener, ActionListener {

   private Window window;
   private Hashtable<String,String> table;
   public List[] results = new List[3];

   SearchBox (int i) {
	   super(null,i);
	   addTextListener(this);
	   addFocusListener(this);
	   addComponentListener(this);
	   addKeyListener(this);
	   addActionListener(this);
   }
   
	@Override
	public void actionPerformed(ActionEvent e) {
		if (Utils.isNumber(getText())) 
			ventana.actionPerformed(e);
		else {
			var sel = results[0].getSelectedItem();
			if (sel!=null)
				setText(table.get(sel));
		}
	}
	
	@Override
	public void textValueChanged(TextEvent e) {
		window().removeAll();
		String[][] coincidence = {{},{},{}};
		int coincidences = 0;
		if (!getText().equals("")&&!getText().equals(" ")&&!Utils.isNumber(getText())) {
		    int length = inventario.length();
		    for (int i = 0; i < length;i++) {
		    	String a = inventario.getString(1,i);
		        if (a.contains(getText())) {
		        	coincidence[0] = Utils.arrayResize(coincidence[0]);
		        	coincidence[0][coincidences] = inventario.getString(1,i);
		        	table.put(inventario.getString(1,i),inventario.getString(0,i));
		        	coincidence[1] = Utils.arrayResize(coincidence[1]);
		        	coincidence[1][coincidences] = inventario.getString(2,i);
		        	coincidence[2] = Utils.arrayResize(coincidence[2]);
		        	coincidence[2][coincidences++] = inventario.getString(3,i);
		        }
		    }
		    if (coincidences < 1) {
		    	window().setVisible(false);
		    } else {
		    	for (int i= 0; i < 3;i++) {
		    		results[i] = new List(coincidences);
		    		List list = results[i];
		    		list.addItemListener(this);
		    		//list.addMouseMotionListener(this);
		    	}
		    	window.add(results[0]);
		    	Panel right = new Panel(new GridLayout(0,2));
		    	window.add(right,BorderLayout.EAST);
		    	right.add(results[1]);
		    	right.add(results[2]);
		    	for(int i = 0; i < coincidences; i++) {
		    		for (int j = 0; j < 3; j++) {
		    			results[j].add(coincidence[j][i]);
		    		}
		    	}
		    	window().pack();
		    	if (!window.isVisible()) {
		    		window.setVisible(true);
		    	}
		    }
		} else {
			window().setVisible(false);
		}           
	}

	public void actionPerformed(List list) {
		int selected;
		int [] non = {-1,-1};
		if (list==results[0]) {
			selected = results[0].getSelectedIndex();
			non[0] = 1;
			non[1] = 2;
		} else if (list == results[1]) {
			selected = results[1].getSelectedIndex();
			non[0] = 0;
			non[1] = 2;
		} else if (list == results[2]) {
			selected = results[2].getSelectedIndex();
			non[0] = 1;
			non[1] = 0;
		} else return;
		results[non[0]].select(selected);
		results[non[1]].select(selected);
	}
	/*@Override
	public void mouseMoved(MouseEvent e) {
		List list = (List)e.getSource();
		var a = (e.getY()+2)*(list.getRows()+1)/list.getHeight();
		list.select(a);
		actionPerformed(list);
	}*/
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		actionPerformed((List)e.getSource());
		setText(table.get(results[0].getSelectedItem()));
	}
	
	private Window window() {
		if (window==null) {
			window = new Window(Utils.getWindow(this));
			window.setFocusableWindowState(false);
			window.setLayout(new BorderLayout());
		}
		
		return window;
	}

	@Override
	public void focusLost(FocusEvent e) {
		window.dispose();
		window = null;
		results = new List[3];
		table = null;
	}
	
	@Override
	public void focusGained(FocusEvent e) {
		java.awt.Point location = getLocationOnScreen();
		window().setLocation(location.x, location.y + getBounds().height);
		table = new Hashtable<String, String>();
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
	public void mouseDragged(MouseEvent e) {}
	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
}
