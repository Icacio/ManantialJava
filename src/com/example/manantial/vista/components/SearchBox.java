package com.example.manantial.vista.components;

import java.awt.TextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import com.example.manantial.controlador.Utils;

import static com.example.manantial.controlador.MainController.tableController;

public class SearchBox extends TextField implements ActionListener
	{
   public SearchBox (int i) {
	   super(null,i);
	   addActionListener(this);
   }
	@Override
	public void actionPerformed(ActionEvent e) {
		if (Utils.isNumber(getText())) 
			tableController.actionPerformed(e);
	}
}