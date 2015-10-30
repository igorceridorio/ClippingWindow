/*-----------------------------------------------------------
 * University of Nebraska at Kearney 
 * CSIT 422 - Computer Graphics. Fall 2015
 * 
 * AUTHOR: 96740976 - Igor Felipe Ferreira Ceridorio
 * LAST MODIFIED: 10/26/2015
 * ARCHIVE: Window.java
 * OBJECTIVE: Defines the window of the program. It contains
 * 			  the window configurations and design, the 
 * 			  listeners for the button, the mouse click and
 * 			  the keys that respond when pressed.  
 *---------------------------------------------------------*/

package clipping;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Window extends JFrame{

	protected ClipView view;
	protected int WIN_WID = 1200;
	protected int WIN_HEI = 800;
	protected boolean light = false;
	protected float flashCenter[] = {0,0};
	
	// constructor
	public Window(){
		
		// define the window configuration
		super("Flashlight Clipping!");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(WIN_WID, WIN_HEI);
		view = new ClipView(WIN_WID, WIN_HEI, light);
		add(view, BorderLayout.CENTER);
		
		// define the button
		JButton turnLights = new JButton("Turn lights ON");
		turnLights.setFocusable(false);
		
		// define the button pane
		JPanel butPane = new JPanel();
		butPane.add(turnLights);
		
		// listener that receives the button click
		turnLights.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent ae) {
				if(!light){
					turnLights.setText("Turn lights OFF");
					light = true;
					view.updateLight(light);
					repaint();
				} else {
					turnLights.setText("Turn lights ON");
					light = false;
					view.updateLight(light);
					repaint();	
				}
			}
		});
		
		// add the button to the bottom of the screen
		add(butPane, BorderLayout.SOUTH);
		
		// listener that receives the mouse click
		addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent evt){
				flashCenter[0] = evt.getX();
				flashCenter[1] = evt.getY();
				
				// set the click points
				view.setFlashCenter(flashCenter);
				repaint();
			}
			
		});
		
		// listener that receives the directions keys
		this.addKeyListener(new KeyListener() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				int code = e.getKeyCode();
				if(flashCenter[0] != 0 && flashCenter[1] != 0){
					if(code == KeyEvent.VK_UP){
						if(flashCenter[1] > 50){
							flashCenter[1] = flashCenter[1] - 5;
							view.setFlashCenter(flashCenter);
							repaint();
						}
					} else if(code == KeyEvent.VK_DOWN){
						if(flashCenter[1] < (WIN_HEI - 150)){
							flashCenter[1] = flashCenter[1] + 5;
							view.setFlashCenter(flashCenter);
							repaint();
						}
					} else if(code == KeyEvent.VK_LEFT){
						if(flashCenter[0] > 10){
							flashCenter[0] = flashCenter[0] - 5;
							view.setFlashCenter(flashCenter);
							repaint();
						}
					} else if(code == KeyEvent.VK_RIGHT){
						if(flashCenter[0] < (WIN_WID - 125)){
							flashCenter[0] = flashCenter[0] + 5;
							view.setFlashCenter(flashCenter);
							repaint();
						}
					}
				}
			}
			
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {}
			
		});
		
		// set the window as visible and focusable
		this.setVisible(true);
		this.setFocusable(true);
		
	}
	
	// main
	public static void main(String[] args){
		new Window();
	}
	
}
