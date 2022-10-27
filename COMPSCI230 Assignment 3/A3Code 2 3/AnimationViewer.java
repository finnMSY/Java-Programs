/*
 * ==========================================================================================
 * AnimationViewer.java : Moves shapes around on the screen according to different paths.
 * It is the main drawing area where shapes are added and manipulated.
 * YOUR UPI: fmas592
 * YOUR NAME: Finn Massey
 * ==========================================================================================
 */

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.tree.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.event.ListDataListener;
import java.lang.reflect.Field;

class AnimationViewer extends JComponent implements TreeModel, Runnable {
	private Thread animationThread = null;		// the thread for animation
	private static int DELAY = 120;				 // the current animation speed
	ArrayList<Shape> shapes = new ArrayList<Shape>(); //create the ArrayList to store shapes
	private ShapeType currentShapeType=Shape.DEFAULT_SHAPETYPE; // the current shape type,
	private PathType currentPathType=Shape.DEFAULT_PATHTYPE;	// the current path type
	private Color currentColor=Shape.DEFAULT_COLOR; // the current fill colour of a shape
	private int currentPanelWidth=Shape.DEFAULT_PANEL_WIDTH, currentPanelHeight = Shape.DEFAULT_PANEL_HEIGHT, currentWidth=Shape.DEFAULT_WIDTH, currentHeight=Shape.DEFAULT_HEIGHT;
	private NestedShape root;
	private final ArrayList<TreeModelListener> treeModelListeners = new ArrayList<TreeModelListener>();


	public AnimationViewer() {
		start();
		addMouseListener(new MyMouseAdapter());
		root = new NestedShape(currentPanelWidth, currentPanelHeight);
	}
	protected void createNewShape(int x, int y) {
		switch (currentShapeType) {
		  case RECTANGLE: {
			shapes.add( new RectangleShape(x, y,currentWidth,currentHeight,currentPanelWidth,currentPanelHeight,currentColor,currentPathType));
			break;
		  } case OVAL: {
			shapes.add( new OvalShape(x, y,currentWidth,currentHeight,currentPanelWidth,currentPanelHeight,currentColor,currentPathType));
			break;
		  }
		}
	  }

	public NestedShape getRoot() {
		return root;
	}

	public boolean isLeaf(Object node) {
		try {
			NestedShape leaf = (NestedShape) node;
			return false;
		}
		catch (Exception e) {
			return true;
		}
	}

	public boolean isRoot(Shape selectedNode) {
		return root == selectedNode;
	}

	public Object getChild(Object parent, int index) {
		if (!isLeaf(parent)) {
			return ((NestedShape)parent).getInnerShapeAt(index);
		}
		else {
			return null;
		}
	}

	public int getChildCount(Object parent) {
		if (!isLeaf(parent)) {
			return ((NestedShape)parent).getSize();
		}
		else {
			return 0;
		}
	}

	public int getIndexOfChild(Object parent, Object child) {
		if (!isLeaf(parent)) {
			return ((NestedShape)parent).indexOf((Shape)child);
		}
		else {
			return -1;
		}
	}

	public void addTreeModelListener(final TreeModelListener tml) {
		treeModelListeners.add(tml);
	}

	public void removeTreeModelListener(final TreeModelListener tml) {
		treeModelListeners.remove(tml);
	}

	public void valueForPathChanged(TreePath path, Object newValue) {

	}

	public void fireTreeNodesInserted(Object source, Object[] path,int[] childIndices,Object[] children) {
		final TreeModelEvent tme = new TreeModelEvent(source, path, childIndices, children);
		for (final TreeModelListener tml : treeModelListeners) {
			tml.treeNodesInserted(tme);
		}
	}

	public void insertNodeInto(Shape newChild, NestedShape parent) {
		int[] ints = new int[1];
		ints[0] = parent.indexOf(newChild);
		Object[] objects =  parent.getAllInnerShapes().toArray();

		fireTreeNodesInserted(this, parent.getPath(), ints, objects);
	}

	public void addShapeNode(NestedShape selectedNode) {
		if (root == selectedNode) {
			Shape shape = selectedNode.createInnerShape(0, 0, currentWidth, currentHeight, currentColor, currentPathType, currentShapeType);
			insertNodeInto(shape, selectedNode);
		}
		else {
			Shape shape = selectedNode.createInnerShape(0, 0, currentWidth / 2, currentHeight / 2, currentColor, currentPathType, currentShapeType);
			insertNodeInto(shape, selectedNode);
		}
	}

	public void fireTreeNodesRemoved(Object source, Object[] path, int[] childIndices, Object[] children) {
		final TreeModelEvent tme = new TreeModelEvent(source, path, childIndices, children);
		for (final TreeModelListener tml : treeModelListeners) {
			tml.treeNodesRemoved(tme);
		}
	}

	public void removeNodeFromParent(Shape selectedNode) {
		NestedShape parent = selectedNode.getParent();
		int index = parent.indexOf(selectedNode);
		parent.removeInnerShape(selectedNode);
		fireTreeNodesRemoved(this, parent.getPath(), new int[]{index}, new Object[]{selectedNode});
	}

	class MyMouseAdapter extends MouseAdapter {
		public void mouseClicked( MouseEvent e ) {
			boolean found = false;
			for (Shape currentShape: root.getAllInnerShapes())
				if ( currentShape.contains( e.getPoint()) ) { // if the mousepoint is within a shape, then set the shape to be selected/deselected
					currentShape.setSelected( ! currentShape.isSelected() );
					found = true;
				}
			if (!found){
				Shape shape = root.createInnerShape(e.getPoint().x, e.getPoint().y, currentWidth, currentHeight, currentColor, currentPathType, currentShapeType);
				insertNodeInto(shape, root);
			}
		}
	}
	public void setCurrentColor(Color bc) {
		currentColor = bc;
		for (Shape currentShape: root.getAllInnerShapes())
			if ( currentShape.isSelected())
				currentShape.setColor(currentColor);
	}
	public final void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Shape currentShape: root.getAllInnerShapes()) {
			currentShape.move();
			currentShape.draw(g);
			currentShape.drawHandles(g);
		}
	}
	public void resetMarginSize() {
		currentPanelWidth = getWidth();
		currentPanelHeight = getHeight() ;
		for (Shape currentShape: root.getAllInnerShapes())
			currentShape.setPanelSize(currentPanelWidth,currentPanelHeight );
	}
	// you don't need to make any changes after this line ______________
	public void setCurrentShapeType(ShapeType value) { currentShapeType = value; }
	public void setCurrentPathType(PathType value) { currentPathType = value; }
	public ShapeType getCurrentShapeType() { return currentShapeType; }
	public PathType getCurrentPathType() { return currentPathType; }
	public int getCurrentWidth() { return currentWidth; }
	public int getCurrentHeight() { return currentHeight; }
	public Color getCurrentColor() { return currentColor; }
	public void update(Graphics g){ paint(g); }
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
