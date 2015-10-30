/*-----------------------------------------------------------
 * University of Nebraska at Kearney 
 * CSIT 422 - Computer Graphics. Fall 2015
 * 
 * AUTHOR: 96740976 - Igor Felipe Ferreira Ceridorio
 * LAST MODIFIED: 10/26/2015
 * ARCHIVE: ClipView.java
 * OBJECTIVE: Contains the main logic of the program. This
 * 			  class is responsible to generate the initial
 * 			  base pattern, to set where the flashlight is
 * 			  pointing, and to clip the base pattern within
 * 			  the current point of the flashlight.  
 *---------------------------------------------------------*/

package clipping;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ClipView extends JPanel {

	protected boolean light; // check if the light is on or off
	protected ArrayList<Pattern> pattern; // base pattern
	protected int numTriangle; // define the amount of objects in the pattern
	protected float[] flashCenter = { 0, 0 }; // store the center of the flashlight

	// constructor
	public ClipView(int WIN_WID, int WIN_HEI, boolean light) {

		this.light = light;
		this.pattern = new ArrayList<Pattern>();
		this.numTriangle = 10;
		this.setSize(WIN_WID, WIN_HEI);

		// generate nested triangle objects for the pattern
		for (int i = 0; i < numTriangle; i++) {
			Pattern triangle = new Pattern();
			float genPoint[] = genInitPoint();
			triangle.genPattern(genPoint[0], genPoint[1]);
			pattern.add(triangle);
		}

	}

	// generate random initial points to the patterns
	public float[] genInitPoint() {
		float point[] = { 0, 0 };

		Random generator = new Random();
		point[0] = generator.nextInt(this.getWidth());
		point[1] = generator.nextInt(this.getHeight());

		return point;
	}

	// update the light value, in case it has changed in Window class
	public void updateLight(boolean light) {
		this.light = light;
	}

	// method that set the flashlight center points
	public void setFlashCenter(float[] flashCenter) {
		this.flashCenter[0] = flashCenter[0] - 5;
		this.flashCenter[1] = flashCenter[1] - 45;
	}

	// method used to clip the image
	public boolean clip(int miX, int maX, int miY, int maY, Point p1, Point p2) {
		boolean p10, p11, p12, p13, p20, p21, p22, p23;
		while (true) {
			// determine bitcodes
			p10 = p1.x < miX;
			p11 = p1.x > maX;
			p12 = p1.y < miY;
			p13 = p1.y > maY;

			p20 = p2.x < miX;
			p21 = p2.x > maX;
			p22 = p2.y < miY;
			p23 = p2.y > maY;

			// trivial acceptance - contained within the view port
			if (!(p10 || p11 || p12 || p13 || p20 || p21 || p22 || p23)) {
				return true;
			}
			// trivial rejection
			if (p10 && p20 || p11 && p21 || p12 && p22 || p13 && p23) {
				return false;
			}

			if (p10 || p11 || p12 || p13) {
				if (p10) {
					p1.y = calculateYintercept(p1, p2, miX);
					p1.x = miX;
				} else if (p11) {
					p1.y = calculateYintercept(p1, p2, maX);
					p1.x = maX;
				} else if (p12) {
					p1.x = calculateXintercept(p1, p2, miY);
					p1.y = miY;
				} else if (p13) {
					p1.x = calculateXintercept(p1, p2, maY);
					p1.y = maY;
				}
			} else if (p20 || p21 || p22 || p23) {
				if (p20) {
					p2.y = calculateYintercept(p1, p2, miX);
					p2.x = miX;
				} else if (p21) {
					p2.y = calculateYintercept(p1, p2, maX);
					p2.x = maX;
				} else if (p22) {
					p2.x = calculateXintercept(p1, p2, miY);
					p2.y = miY;
				} else if (p23) {
					p2.x = calculateXintercept(p1, p2, maY);
					p2.y = maY;
				}
			}

			try {
				repaint();
			} catch (Exception e) {
			}
		}
	}

	// methods used to calculate x and y intercepts
	public int calculateYintercept(Point p1, Point p2, int m_X) {
		return p1.y + (int) ((p2.y - p1.y) * (double) (m_X - p1.x) / (p2.x - p1.x));
	}

	public int calculateXintercept(Point p1, Point p2, int m_Y) {
		int newYdiff = (m_Y - p1.y);
		int yDiff = (p2.y - p1.y);
		int xDiff = (p2.x - p1.x);
		double ratio = (xDiff * (double) newYdiff / yDiff);
		return p1.x + (int) ratio;
	}

	// paint method
	public void paint(Graphics g) {
		super.paint(g);

		// if the light is off
		if (!light) {

			// change the background color to black
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());

			// draw the area around the click
			if (flashCenter[0] != 0 && flashCenter[1] != 0) {
				g.setColor(Color.WHITE);
				g.fillRect((int) flashCenter[0], (int) flashCenter[1], 100, 100);

				// draw the 'shadow' of the flashlight
				int xShaPoints[] = { (int) flashCenter[0], (int) flashCenter[0], ((int) flashCenter[0] + 100), 50, 0, 0 };
				int yShaPoints[] = { (int) flashCenter[1], ((int) flashCenter[1] + 100), ((int) flashCenter[1] + 100), this.getHeight(), this.getHeight(), (this.getHeight() - 10) };
				g.setColor(Color.GRAY);
				g.drawPolygon(xShaPoints, yShaPoints, 6);
				g.fillPolygon(xShaPoints, yShaPoints, 6);
				g.setColor(Color.BLACK);
				g.drawLine((int) flashCenter[0], ((int) flashCenter[1] + 100), 40, this.getHeight());

				// set the minX, maxX, minY, maxY values to be used in the clipping
				int minX, maxX, minY, maxY;
				minX = (int) flashCenter[0];
				maxX = (int) (flashCenter[0] + 100);
				minY = (int) flashCenter[1];
				maxY = (int) (flashCenter[1] + 100);

				// clip all the pattern objects within the flashlight area
				for (int i = 0; i < numTriangle; i++) {
					ArrayList<Float> xPoints = pattern.get(i).getxPoints();
					ArrayList<Float> yPoints = pattern.get(i).getyPoints();

					// call the clip method for the current pattern
					Point p1 = new Point();
					Point p2 = new Point();
					for (int j = 0; j < (xPoints.size() - 1); j++) {
						p1.setLocation(xPoints.get(j), yPoints.get(j));
						p2.setLocation(xPoints.get(j + 1), yPoints.get(j + 1));
						
						// if the clip happens, draw the new clipped line inside the flashlight window
						if (clip(minX, maxX, minY, maxY, p1, p2)) {
							g.setColor(pattern.get(i).getColor());
							g.drawLine(p1.x, p1.y, p2.x, p2.y);
						}
					}						
				}

			}

		// else the background will show the whole generated pattern
		} else {

			// change the background to white
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());

			// draw the complete pattern
			for (int i = 0; i < numTriangle; i++) {
				pattern.get(i).paint(g);
			}
		}
	}

}
