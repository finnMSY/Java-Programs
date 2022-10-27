/*
 * ==========================================================================================
 * AnimationViewer.java : Moves shapes around on the screen according to different paths.
 * It is the main drawing area where shapes are added and manipulated.
 * YOUR UPI: fmas592
 * YOUR ID: 544773353
 * YOUR NAME: Finn Massey
 * ==========================================================================================
 */

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.Field;

class AnimationViewer extends JComponent implements Runnable {
	private Thread animationThread = null;		// the thread for animation
	private static int DELAY = 120;				 // the current animation speed
	private ArrayList<Shape> shapes = new ArrayList<Shape>(); //create the ArrayList to store shapes
	private ShapeType currentShapeType=Shape.DEFAULT_SHAPETYPE; // the current shape type,
	private PathType currentPathType=Shape.DEFAULT_PATHTYPE;	// the current path type
	private Color currentColor=Shape.DEFAULT_COLOR; // the current fill colour of a shape
	private int currentPanelWidth=Shape.DEFAULT_PANEL_WIDTH, currentPanelHeight = Shape.DEFAULT_PANEL_HEIGHT, currentWidth=Shape.DEFAULT_WIDTH, currentHeight=Shape.DEFAULT_HEIGHT;
	private String currentText = Shape.DEFAULT_TEXT;


	public AnimationViewer() {
		start();
		addMouseListener(new MyMouseAdapter());
	}
	protected void createNewShape(int x, int y) {
		switch (currentShapeType) {
			case RECTANGLE: {
				RectangleShape r = new RectangleShape(x, y, currentWidth, currentHeight, currentPanelWidth, currentPanelHeight, currentColor, currentPathType, currentText);
				shapes.add(r);
				break;
			}
			case OVAL: {
				OvalShape o = new OvalShape(x, y, currentWidth, currentHeight, currentPanelWidth, currentPanelHeight, currentColor, currentPathType, currentText);
				shapes.add(o);
				break;
			}
		}
	}


	public final void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Shape currentShape: shapes) {
			currentShape.move();
			currentShape.draw(g);
			currentShape.drawHandles(g);
			currentShape.drawString(g);
		}
	}
	public void setCurrentColor(Color bc) {
		this.currentColor = bc;
		for (Shape currentShape: shapes){
			if(currentShape.isSelected()){
				currentShape.setColor(currentColor);
			}
		}
	}

	public void setCurrentText(String text){
		this.currentText = text;
		for (Shape currentShape: shapes){
			if(currentShape.isSelected()){
				currentShape.setText(currentText);
			}
		}
	}

	public void createAndAddShape(String sentence) {
		String[] shapePar = sentence.split(",");
		int x = Integer.parseInt(shapePar[3]);
		int y = Integer.parseInt(shapePar[4]);
		currentPathType = PathType.valueOf(shapePar[1]);
		currentText = shapePar[5];
		currentColor = getColorFromString(shapePar[2]);

		if (shapePar[0].equals("OVAL")) {
			currentShapeType = ShapeType.OVAL;
		}
		else if (shapePar[0].equals("RECTANGLE")) {
			currentShapeType = ShapeType.RECTANGLE;
		}

		createNewShape(x, y);
	}

	public boolean loadShapes(String filename) {
		Scanner fileInput = null;
		try {
			fileInput = new Scanner(new File(filename));
			while (fileInput.hasNext()) {
				String shapeLine = fileInput.nextLine();
				createAndAddShape(shapeLine);
			}
			return true;
		} catch (IOException e) {
			return false;
		} finally {
			if (fileInput!= null)
				fileInput.close();
		}
	}

	// you don't need to make any changes after this line ______________
	public void setCurrentShapeType(ShapeType value) { currentShapeType = value; }
	public void setCurrentPathType(PathType value) { currentPathType = value; }
	public ShapeType getCurrentShapeType() { return currentShapeType; }
	public PathType getCurrentPathType() { return currentPathType; }
	public int getCurrentWidth() { return currentWidth; }
	public int getCurrentHeight() { return currentHeight; }
	public Color getCurrentColor() { return currentColor; }
	public Color getColorFromString(String value) {
		try {
			Field field = Color.class.getField(value);
			return (Color)field.get(null);
		} catch (Exception e) {
			return Color.black;
		}
	}
	class MyMouseAdapter extends MouseAdapter {
		public void mouseClicked( MouseEvent e ) {
			boolean found = false;
			for (Shape currentShape: shapes)
				if ( currentShape.contains( e.getPoint()) ) { // if the mousepoint is within a shape, then set the shape to be selected/deselected
					currentShape.setSelected( ! currentShape.isSelected() );
					found = true;
				}
			if (!found) createNewShape(e.getX(), e.getY());
		}
	}
	public void update(Graphics g){ paint(g); }
	public void resetMarginSize() {
		currentPanelWidth = getWidth();
		currentPanelHeight = getHeight() ;
		for (Shape currentShape: shapes)
			currentShape.setPanelSize(currentPanelWidth,currentPanelHeight );
	}
	public void start() {
		animationThread = new Thread(this);
		animationThread.start();
	}
	public void stop() {
		if (animationThread != null) {
			animationThread = null;
		}
	}
	public void run() {
		Thread myThread = Thread.currentThread();
		while(animationThread==myThread) {
			repaint();
			pause(DELAY);
		}
	}
	private void pause(int milliseconds) {
		try {
			Thread.sleep((long)milliseconds);
		} catch(InterruptedException ie) {}
	}
}
