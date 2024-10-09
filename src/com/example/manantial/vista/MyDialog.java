package com.example.manantial.vista;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionListener;

import com.example.manantial.controlador.Utils;
import static com.example.manantial.vista.Ventana.ventana;

public class MyDialog extends Dialog {
	public MyDialog (String text, ActionListener b) {
		this(text, Dialog.ModalityType.MODELESS, b);
	}

	public MyDialog(String text) {
		this(text, Dialog.DEFAULT_MODALITY_TYPE, Utils.singleton);
	}
	
	private MyDialog(String text,ModalityType mode, ActionListener b) {
		super(ventana,mode);
		myDialog(text).addActionListener(b);
		setVisible(true);
	}
	
	private Button myDialog (String text) {
		setLayout(new BorderLayout());
		Utils.makeCloseable(this);
		add(new Panel(),BorderLayout.EAST);
		add(new Panel(),BorderLayout.WEST);
		add(new Panel(),BorderLayout.NORTH);
		add(new Panel(),BorderLayout.SOUTH);
		Panel center = new Panel(new BorderLayout());
		add(center);
		center.add(new Label(text),BorderLayout.NORTH);
		Panel south = new Panel();
		Button button = new Button(Language.accept);
		south.add(new Panel());
		south.add(button);
		south.add(new Panel());
		center.add(south,BorderLayout.SOUTH);
		pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(screenSize.width/2-getWidth()/2,screenSize.height/2-getHeight()/2);
		return button;
	}
}
