package com.example.manantial.vista.components;

import java.awt.Button;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.TextListener;
import java.awt.event.FocusListener;
import java.awt.event.TextEvent;
import java.awt.event.FocusEvent;
import java.awt.event.ActionListener;

import com.example.manantial.controlador.Utils;

public class Spinner extends Panel {
	private final NumberField spinnerText = new NumberField();
	
	public Spinner() {
		super(new GridBagLayout());
		Button arriba = new Button("^");
		arriba.setFocusable(false);
		arriba.addActionListener((e)->spinnerText.setValue(getValue()+1));//*/
		Button abajo = new Button("v");
		abajo.setFocusable(false);
		abajo.addActionListener((e)->spinnerText.setValue(getValue()>0?getValue()-1:0));
		Panel right = new Panel(new GridLayout(2,1));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = 3;
		add(spinnerText,gbc);
		right.add(arriba,gbc);
		right.add(abajo,gbc);
		gbc.gridwidth = 1;
		add(right,gbc);
	}
	
	public int setValue(int value) {
		spinnerText.setValue(value);
		return value;
	}
	public void addActionListener(ActionListener l) {
		spinnerText.addActionListener(l);
	}
	
	public int getValue() {
		return spinnerText.getValue();
	}
	class NumberField extends TextField implements TextListener, FocusListener {
		
		private int intValue = 0;
		
		private NumberField() {
			super.setText("0");
			addTextListener(this);
			addFocusListener(this);
		}
		
		int setValue(int i) {
			intValue = i;
			if (intValue!=0)
				super.setText(Integer.toString(i));
			return i;
		}
		
		int getValue() {
			if (Utils.isNumber(getText()))
				return Integer.parseInt(getText());
			return setValue(0);
		}
		
		@Override
		public void setText(String t) {
			if (!Utils.isNumber(t))
				t = "0";
			super.setText(t);
				
		}
		
		@Override
		public void textValueChanged(TextEvent e) {
			if (getText().equals("")) {
				setValue(0);
				setCaretPosition(getText().length());
				return;
			}
			if (getText().length()>3) {
				super.setText(getText().substring(0,3));
				if (Integer.toString(intValue).equals(getText()))
					return;
			}
			if (Utils.isNumber(getText())) {
				if (intValue==0) {
					if (getCaretPosition()==1)
						intValue = Integer.parseInt(getText());
					else
						if (getCaretPosition()==getText().length()) {
							setText(getText().substring(1,getText().length()));
							setCaretPosition(getText().length());
						}
				}
				
			}
			else {
				super.setText(Integer.toString(intValue));
				setCaretPosition(getText().length());
			}
		}

		@Override
		public void focusGained(FocusEvent e) {
			setCaretPosition(getText().length());
		}

		@Override
		public void focusLost(FocusEvent e) {}
	}
}
