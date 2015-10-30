/*-----------------------------------------------------------
 * University of Nebraska at Kearney 
 * CSIT 422 - Computer Graphics. Fall 2015
 * 
 * AUTHOR: 96740976 - Igor Felipe Ferreira Ceridorio
 * LAST MODIFIED: 10/26/2015
 * ARCHIVE: Pattern.java
 * OBJECTIVE: This class is responsible for generating a 
 * 			  pattern object, that is composed by nested
 * 			  triangles. It stores the x and y points 
 * 			  generated for the object in ArrayList. It also
 * 			  has a paint method responsible for printing 
 * 			  the whole pattern when called.  
 *---------------------------------------------------------*/

package clipping;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Pattern extends JFrame{

	protected float side; // side of the triangle
	protected float sideHalf; // half of the side of the initial triangle
	protected float h; // height of the initial triangle
	protected ArrayList<Float> xPoints; // store all the x points of the pattern
	protected ArrayList<Float> yPoints; // store all the y points of the pattern
	protected Color random; // store the color given to the object
	
	// constructor
	public Pattern(){
		this.side = 400;
		this.sideHalf = 0.5F * side;
		this.h = sideHalf * (float) Math.sqrt(3);
		this.xPoints = new ArrayList<Float>();
		this.yPoints = new ArrayList<Float>();
		this.random = new Color((int) (Math.random() * 0x1000000));
	}
	
	// return the color associated to this pattern
	public Color getColor(){
		return this.random;
	}
	
	// return a copy of the xPoints ArrayList
	public ArrayList<Float> getxPoints(){
		return this.xPoints;
	}
	
	// return a copy of the yPoints ArrayList
	public ArrayList<Float> getyPoints(){
		return this.yPoints;
	}
	
	// generate all the points of the pattern to be drawn
	public void genPattern(float xStart, float yStart){
		
		float xA, yA, xB, yB, xC, yC, xA1, yA1, xB1, yB1, xC1, yC1; // points variables
		float p, q; // variables used to rotate the triangle
		
		// set the rotation
		q = 0.1F;
		p = 1 - q;
		
		// set the initial points
		xA = xStart - sideHalf;
        yA = yStart - 0.5F * h;
        xB = xStart + sideHalf;
        yB = yA;
        xC = xStart;
        yC = yStart + 0.5F *h;
        
        // add the first set of points to the ArrayLists
        xPoints.add(xA); xPoints.add(xB); xPoints.add(xC);
        yPoints.add(yA); yPoints.add(yB); yPoints.add(yC);

        // generate the nested triangles
        for(int i = 0; i < 18; i++){
        	xA1 = p * xA + q * xB;
            yA1 = p * yA + q * yB;
            xB1 = p * xB + q * xC;
            yB1 = p * yB + q * yC;
            xC1 = p * xC + q * xA;
            yC1 = p * yC + q * yA;
            
            xA =  xA1;
            xB =  xB1;
            xC =  xC1;
            yA =  yA1;
            yB =  yB1;
            yC =  yC1;
            
            // add the new set of points to the ArrayLists
            xPoints.add(xA); xPoints.add(xB); xPoints.add(xC);
            yPoints.add(yA); yPoints.add(yB); yPoints.add(yC);
        }
		
	}
	
	// method to round X value
    int iX(float x){
        return Math.round(x);
    }
    
    // method to round Y value
    int iY(float y){ 
    	return  Math.round(y);
    }
	
	// paint method to this Pattern object
	public void paint(Graphics g){
		super.paint(g);
		
		g.setColor(random);
		for(int i = 0; i < 54; i++){
			g.drawLine(iX(xPoints.get(i)), iY(yPoints.get(i)), iX(xPoints.get(i+1)), iY(yPoints.get(i+1)));
			g.drawLine(iX(xPoints.get(i+1)), iY(yPoints.get(i+1)), iX(xPoints.get(i+2)), iY(yPoints.get(i+2)));
			g.drawLine(iX(xPoints.get(i+2)), iY(yPoints.get(i+2)), iX(xPoints.get(i)), iY(yPoints.get(i)));
		}
	}
	
}
