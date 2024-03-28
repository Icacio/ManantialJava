package com.example.manantial.controlador;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import com.example.manantial.vista.Language;

import static com.example.manantial.controlador.Controlador.contrasenia;
import static com.example.manantial.controlador.Controlador.inventario;
import static com.example.manantial.controlador.Controlador.ventana;

public class PasswordGetter extends Dialog implements ActionListener, WindowListener {
	private String[] error = Language.passwordError;
	private Label instruccion;
	private TextField confirmPassword;
	private TextField password;
	
	public PasswordGetter(Frame a) {
		super(a);
		instruccion = new Label(Language.passGetter[0],1);
		password = new TextField();
		password.setEchoChar('*');
		password.addActionListener(this);
		Label confirmar = new Label(Language.passGetter[1],1);
		confirmPassword = new TextField();
		confirmPassword.setEchoChar('*');
		confirmPassword.addActionListener(this);
		Button button = new Button(Language.accept);
		button.addActionListener(this);
		addWindowListener(this);
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
				inventario.length(0);
				dispose();
				password = null;
				confirmPassword = null;
				instruccion = null;
				ventana.pack();
				ventana.setVisible(true);
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
	@Override
	public void windowClosing(WindowEvent e) {
		dispose();
		ventana.dispose();
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