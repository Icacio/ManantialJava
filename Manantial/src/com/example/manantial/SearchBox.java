package com.example.manantial;

import java.awt.Label;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.Window;
import java.awt.event.MouseEvent;

public class SearchBox extends TextField /*implements MouseListener */{

   private final Window window;
   private Label currentComponent;

   SearchBox (int i,Ventana owner) {
	   super(null,i);
	   window = new Window(owner);
   }
   
   private void hideTip() {
	   if (window != null) {
           window.setVisible(false);
       }
   }

 
    /**
     * Determines which Component the mouse cursor is over and
     * starts a thread to wait the appropriate amount of time
     * before displaying its tooltip.
     
   public void mouseEntered(MouseEvent e) {
       currentComponent = (Component) e.getSource();
   }*/
   
   public void mousePressed(MouseEvent e) {
	   if (e.getButton()==MouseEvent.BUTTON1)
       if (e.getSource() == currentComponent) {
           hideTip();
       }
   }
    /**
     * Implementation of the timer thread.
     * This waits the appropriate amount of time and then displays the tooltip.
     */
   public void a() {
	   // Place the tooltip directly below its Component.
	   Rectangle bounds = currentComponent.getBounds();
	   Point location = currentComponent.getLocationOnScreen();
	   window.setLocation(location.x, location.y + bounds.height);
	   window.setVisible(true);
   }

	
	
	/*private void draw () {
		if (is_string(texto)&&string_digits(texto)!=texto) {
		    var elex = 0;
		    for (var i = 1; i < ds_grid_height(grid_inven);i++)
		        if string_count(texto,ds_grid_get(grid_inven,1,i)) {
		            var a = draw_get_colour();
		            if (selected == ++elex) {
		                if keyboard_check_released(vk_enter) {
		                    focus(spinner)
		                    if (room==administrador) {
		                        if match() {
		                            bTexto(grid_inven[#1,i])
		                            spinner.amount = grid_inven[#2,i]
		                        }
		                        else {
		                            bTexto(grid_inven[#0,i]);
		                            spinner.amount = grid_inven[#3,i]
		                        }
		                    } else bTexto(grid_inven[#0,i]);
		                    break;
		                }
		                draw_set_colour(c_white)
		            } else if (floor(selected) == elex) {
		                if mouse_check_button_released(mb_left) {
		                    if (room==administrador) {
		                        if match() {
		                            bTexto(grid_inven[#1,i])
		                            spinner.amount = grid_inven[#2,i]
		                        }
		                        else {
		                            bTexto(grid_inven[#0,i]);
		                            spinner.amount = grid_inven[#3,i]
		                        }
		                    } else bTexto(grid_inven[#0,i]);
		                    break;
		                }
		                draw_set_colour(c_white)
		            }
		            var b = draw_get_colour()
		            if b == c_white draw_set_colour(10132122)
		            else draw_set_colour(c_ltgray)
		            draw_rectangle(x-170,y-elex*32-16,x+230,y-elex*32+16,false)
		            draw_set_colour(b);
		            draw_text(x,y-elex*32,grid_inven[#1,i])
		            draw_rectangle(x-170,y-elex*32-16,x+130,y-elex*32+16,true)
		            draw_text(x+155,y-elex*32,"$"+string(grid_inven[#2,i]))
		            draw_rectangle(x+130,y-elex*32-16,x+180,y-elex*32+16,true)
		            draw_text(x+205,y-elex*32,"\#"+string(grid_inven[#3,i]))
		            draw_rectangle(x+180,y-elex*32-16,x+230,y-elex*32+16,true)
		            if draw_get_colour()==c_white//
		            draw_set_colour(a)
		        }
		    if (selected==floor(selected)&&selected > elex) selected = elex;
		}
	}*/
}
