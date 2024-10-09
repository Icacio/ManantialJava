package com.example.manantial.vista;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class PasswordGetter extends Dialog implements ActionListener, WindowListener {
	private Label instruccion;
	private TextField confirmPassword = new TextField();
	private TextField password = new TextField();
	public String response;
	private boolean creatingPassword;
	
	public PasswordGetter(boolean doWeCreatePassword) {
		super(null,"Contraseña",Dialog.DEFAULT_MODALITY_TYPE);
		creatingPassword = doWeCreatePassword;
		instruccion = new Label(Language.passGetter[doWeCreatePassword?0:2],1);
		Button button = new Button(Language.accept);
		Panel center = new Panel();
		Panel bottom = new Panel();
		int width = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
		int height = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
		Dimension prefSize = new Dimension(width/6,height/29);
		
		addWindowListener(this);
		setResizable(false);
		add(new Panel(),"South");add(new Panel(),"West");add(new Panel(),"East");add(new Panel(),"North");
		add(center);
		
		password.setEchoChar('*');
		password.addActionListener(this);
		password.setPreferredSize(prefSize);
		
		button.addActionListener(this);
		
		center.setLayout(new GridLayout(0,1));
		center.add(instruccion);
		center.add(password);

		if (creatingPassword) {
			Label confirmar = new Label(Language.passGetter[1],1);
			confirmPassword.setEchoChar('*');
			confirmPassword.addActionListener(this);
			confirmPassword.setPreferredSize(prefSize);
			center.add(confirmar);
			center.add(confirmPassword);
		}
		
		center.add(bottom);
		
		bottom.add(new Panel());
		bottom.add(button);
		bottom.add(new Panel());
		
		pack();
		setLocation(width/2-width/10,height/2-height/8);
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		var pass = password.getText(); 
		if (!creatingPassword||pass.equals(confirmPassword.getText())) {
			if (!pass.equals("")) {
				if (pass.contains("'")) {
					pass.replace("'","\\'");
				}
				response=pass;
				dispose();
			}
		} else  {
			error(Language.passwordError);
		}
	}
	
	private void error(String error) {
		instruccion.setText(error);
		pack();
		java.awt.Toolkit.getDefaultToolkit().beep();
	}
	@Override
	public void windowClosing(WindowEvent e) {
		System.exit(0);
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
