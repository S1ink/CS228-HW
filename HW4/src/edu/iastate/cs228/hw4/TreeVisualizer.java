package edu.iastate.cs228.hw4;

import java.util.ArrayList;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.iastate.cs228.hw4.Main.MsgTree;


public class TreeVisualizer {
	private MsgTree rootNode;
	private int TreeDepth;
	
	private VisualNode VSNodeTree;
	private ArrayList<VisualNode> TreeAsArray = new ArrayList<VisualNode>();
	private ArrayList<Connector> Connectors = new ArrayList<Connector>();

	
	// Config for visual nodes
	private int NodeWidth = 50;
	private int NodeHeight = 40;
	private int NodeSpace = 40;
	
	private int wWidth = 1800;
	private int wHeight = 1000;
	
	private int xoffset = 0;
	private int yoffset = 0;
	
	private double scale = 1;

	/**
	 * 
	 * @author Daniel Mitchell
	 * This class is very simmilar to MsgTree but it has extra methods needed to visually
	 * draw the tree.
	 *
	 */
	
	private class VisualNode extends JComponent{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private int offset =0;
		private int row =1;
		private int width;
		private int height;
		//private int depth =1;
		
		private int x,y;
		private int ogX, ogY;
		private Color col = Color.gray;
		
		
		
		public String data;
		public VisualNode Parent, Lch, Rch;
		
		
		/**
		 * 
		 * @param msg The letter the node holds
		 * @param off This tells if its the left or right child 
		 * @param row What row of the tree is it in
		 */
		public VisualNode(String msg, int off, int row) {
			super();
			data = msg;
			offset = off;
			this.row = row;
			this.setWidth(NodeWidth); // *** added
			this.setHeight(NodeHeight); // *** added
			if (this.Parent != null) {
				this.x = Parent.x + ((NodeSpace * Math.max(1,(TreeDepth/this.row)) * offset));
				this.y = Parent.y + NodeHeight;
			}
			else {
				this.x = wWidth/2;
				this.y = 100;
			}
			ogX = this.x;
			ogY = this.y;
		}
		/**
		 * 
		 * @param msg
		 * @param off
		 * @param parent the parent to be assigned to the new node. 
		 * @param row
		 */
		public VisualNode(String msg, int off, VisualNode parent, int row) {
			super();
			data = msg;
			offset = off;
			this.row = row;
			this .Parent = parent;
			this.setWidth(NodeWidth); // *** added
			this.setHeight(NodeHeight); // *** added
			
			//Do some math to kinda determine where to draw this node.
			if (this.Parent != null) {
				this.x = Parent.x + ((NodeSpace * Math.max(1,(TreeDepth/this.row)) * offset));
				this.y = Parent.y + NodeHeight + 20;
				
				if (this.data !="Parent") {
					this.x = Parent.x + (NodeSpace * offset);
				}
			}
			else {
				this.x = wWidth/2;
				this.y = 100;
			}
			ogX = this.x;
			ogY = this.y;
			
			setBorder(BorderFactory.createTitledBorder("Node"));
			
		}
		
		@Override
		public void paintComponent(Graphics g) {
			//Save color from before
			//Color tCol = g.getColor();
			super.paintComponent(g);
			//Draw this Node
			//System.out.println("Paint");
			//Nodefill
			
			g.setColor(col);
			g.fillRoundRect(0, 0,this.width-1, this.height-1, 5, 5);
			//g.fillRoundRect(((this.x - (NodeWidth/2) )+ xoffset) * scale, (this.y + yoffset) *scale, NodeWidth * scale, NodeHeight * scale, 5, 5);
			
			//NodeBorders
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(2));
			g2.setColor(Color.getHSBColor(198, 7, 62));
			g2.drawRoundRect(1, 1,this.width-2, this.height-2, 5, 5);

			//Text
			g.setColor(Color.BLUE);
			g.setFont(new Font("TimesRoman", Font.PLAIN, (int)(14*scale))); 
			g.drawString(data,(int)(5*scale),(int)(25*scale));
			
			//connectors
			//if (this.Parent != null) {
			//	int parentx = Parent.x ;
			//	int parenty = Parent.y;
			//	g.setColor(Color.MAGENTA);
			//	g.drawLine(parentx, parenty, 0, 0);
			//}
			
			//return color to what it was
			//g.setColor(tCol);
		}
		
		public void SetColor(Color c) {
			this.col = c;
		}
		
		public void SetColorRecUp(Color c) {
			this.col = c;
			if (this.Parent != null) {
				this.Parent.SetColorRecUp(c);
			}
		}
		
		@Override
		public Dimension getPreferredSize() {
			  return new Dimension(NodeWidth, NodeHeight);
			}	
		@Override
		public Point getLocation() {
			return new Point(this.x,this.y);
		}
		public void setLocation(int x, int y) {
			this.x = x;
			this.y = y;
		}
		public int getX() {
			return this.x;
		}
		public int getY() {
			return this.y;
		}
		public int getWidth() {
			return width;
		}
		public void setWidth(int width) {
			this.width = width;
		}
		public int getHeight() {
			return height;
		}
		public void setHeight(int height) {
			this.height = height;
		}
		
	}
	
	private class Connector extends JComponent {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private VisualNode VN1;
		private VisualNode VN2;
		private int width;
		private int height;
		boolean isLeft;
		
		private Point top;
		private Point bot;
		//private int depth =1;
		
		private int x,y;
		
		public Connector(VisualNode VN1, VisualNode VN2,boolean left) {
			super();
			this.VN1 = VN1;
			this.VN2 = VN2;
			this.isLeft = left;
			
			top = VN1.getLocation();
			top.y -= NodeHeight;
			bot = VN2.getLocation();
			
			this.setWidth(Math.abs(VN1.getX() - VN2.getX()));
			this.setHeight(Math.abs((VN1.getY()-NodeHeight) - VN2.getY()));
			this.x = Math.min(VN1.getX(), VN2.getX());
			this.y = Math.min(VN1.getY(), VN2.getY());
			//System.out.println("New Connector:" + this.x +", " + this.y +"WidthHeight" + this.width +", "+ this.height);
			
		}
		
		public void resize() {
			this.setWidth(Math.abs((VN1.getX()+((int)(NodeWidth*scale))/2) - VN2.getX()));
			this.setHeight(Math.abs((VN1.getY() + ((int)(NodeHeight * scale))) - VN2.getY()));
			this.x = Math.min(VN1.getX() + ((int)(NodeWidth*scale))/2, VN2.getX());
			this.y = Math.min(VN1.getY() + ((int)(NodeHeight * scale)), VN2.getY());
			
			if(VN1.getX() > VN2.getX()) {
				isLeft = true;
			}
			else {
				isLeft = false;
				this.setWidth(Math.abs((VN1.getX()) - VN2.getX()));

			}
			
			//System.out.println("RESIZE: wxh: " + this.width + "," + this.height + " xy: " + this.x + ", " + this.y);
		}
		private void setHeight(int abs) {
			// TODO Auto-generated method stub
			this.height = abs;
		}
		private void setWidth(int val) {
			// TODO Auto-generated method stub
			this.width = val;
		}
		@Override
		public void paintComponent(Graphics g) {
			g.setPaintMode();
			super.paintComponent(g);
			//System.out.println("PRINT LINE");
			//g.setColor(Color.orange);
			//g.drawRect(0, 0, width, height);
			if (VN2.col != Color.GRAY) {
				g.setColor(Color.getHSBColor(40, 86, 79));
			}
			else {
				g.setColor(Color.MAGENTA);
			}
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(2));
			if(isLeft) {
				//g.drawString("L", 0, 10);
				g2.drawLine(0+(int)(NodeWidth*scale)/2, height, width, 0);
			}
			else {
				g2.drawLine(0,0,width,height);
			}
		}
		public Dimension getPreferredSize() {
			  return new Dimension(this.width, this.height);
			}	
		public Point getLocation() {
			return new Point(this.x,this.y);
		}
		public void setLocation(int x, int y) {
			this.x = x;
			this.y = y;
		}
		public int getX() {
			return this.x;
		}
		public int getY() {
			return this.y;
		}
		public int getWidth() {
			return width;
		}
		public int getHeight() {
			return height;
		}
	}
	
	/*
	 * Private class that handles the Java AWT graphics, the stuff of nightmares, truly. 
	 */
	private class MyFrame extends JPanel implements MouseMotionListener{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private Point click;
		private VisualNode RootTree; 
		
		public MyFrame(VisualNode root) {
			//super();
	        setBackground(Color.DARK_GRAY);
			RootTree = root;
			
			
			//setDoubleBuffered(true);
			//setPreferredSize(new Dimension(wWidth, wHeight));
			setSize(wWidth, wHeight);
			//setFocusable(true);		
			
			//setVisible(true);
			
			//add event listeners for keys and mouse
			addMouseMotionListener(this);
			
			this.addMouseListener(new MouseAdapter(){
	            @Override
	            public void mousePressed(MouseEvent e) {
	                click = e.getLocationOnScreen();
	                Point  cl2 = e.getPoint();
	                //System.out.println("ah");
	                VisualNode temp = null;
	                for (VisualNode j : TreeAsArray) {
	                	Rectangle r = new Rectangle(j.getX(),j.getY(),j.getWidth(),j.getHeight());
	                	if(r.contains(cl2)) {
	                		temp = j;
	                	}
	                	//repaint();
	                }
	                if(temp != null) {
	                	for (VisualNode j : TreeAsArray) {
		                	j.SetColor(Color.GRAY);
	                	}
                		temp.SetColorRecUp(Color.getHSBColor(168, 90, 60));
	                }
	                repaint();
	                
	            }
	        });
			

		}
		
		//main paint 
		@Override
		public void paintComponents(Graphics g) {
			
			//Back
			super.paintComponents(g);
			//g.drawString("AAAAAAAAHHHHHH", 100, 100);
			
		}
		
		
		public void repaint() {
			super.repaint();
			//this.paint(this.getGraphics());
		}
			
		
		@Override
		public void mouseDragged(MouseEvent e) {
			//System.out.println("MOUSEDRAG");
			xoffset = e.getX() - click.x;
			yoffset = e.getY() - click.y;
			click = e.getPoint();
			
			for(Component i : TreeAsArray) {
				i.setLocation(i.getX() + xoffset, i.getY() +yoffset);
			}
			for(Component j : Connectors) {
				j.setLocation(j.getX() + xoffset, j.getY() +yoffset);
			}
			
			repaint();
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	/*
	 * Node to use as the root of the tree.
	 */
	public TreeVisualizer(MsgTree node) {
		rootNode = node;
		TreeDepth = findDepth();
		buildVis();
	}
	
	/*
	 * Builds the tree of visual nodes
	 */
	public void buildVis() {
		
		
		VSNodeTree = new VisualNode(rootNode.item + "Root", 0,1);
		VSNodeTree.setLocation(wWidth/2, 100);
		//VSNodeTree.setAlignmentX(wWidth/2);
		//VSNodeTree.setAlignmentX(wHeight);

		recVisBuilder(rootNode, 0, VSNodeTree);
		
		boolean t = true;
		while (t) {
			t= collDetect();
			//t = false;
		}
		for (Connector i : Connectors) {
			i.resize();
		}
		
		
	}
	
	/*
	 * Half baked collision detection so that we can nudge nodes around if they get too intimate.
	 */
	private boolean collDetect() {
		boolean found = false;
		for(VisualNode n : TreeAsArray) {
			for(VisualNode j: TreeAsArray) {
				if (n.row == j.row) {
					if (n != j) {
						if (Math.abs(n.getX() - j.getX()) < NodeWidth-5) {
							n.x -= ((NodeWidth -Math.abs(n.x - j.x)) * n.offset);
							j.x -= ((NodeWidth -Math.abs(n.x - j.x)) * j.offset);
							found = true;
							n.ogX = n.x;
							j.ogX = j.x;

						}
					}
				}
			}
		}
		return found;
	}
	
	public int findDepth() {
		int maxDepth = recDepth(rootNode, 0);
		TreeDepth = maxDepth;
		
		return maxDepth;
	}
	
	public int findDepth(MsgTree node) {
		int maxDepth = recDepth(node, 0);
		
		return maxDepth;
	}
	/*
	 * Recursive way to find the depth of the tree
	 */
	private int recDepth(MsgTree node, int depth) {
		int left = depth, right = depth;
		
		//System.out.println("RecDepth at" + depth);
		
		if (node.left != null) {
			left = recDepth(node.left, depth +1);
		}
		if(node.right != null) {
			right = recDepth(node.right, depth +1);
		}
		
		if (right > left) {
			return right;
		}
		else {
			return left;
		}
	}
	
	/*
	 * Recursively build a VN tree from MSGtree.
	 */
	private void recVisBuilder(MsgTree node, int depth, VisualNode VN){
		
		TreeAsArray.add(VN);
		
		if(node.left != null) {
			if ((int) node.left.item == 0) {
				VN.Lch = new VisualNode("Parent", -1,VN, depth +1);
			}
			else {
				if (node.left.item == '\n') {
					VN.Lch = new VisualNode("(newLine)", -1,VN, depth +1);
				}
				else if (node.left.item == ' ') {
					VN.Lch = new VisualNode("(space)", -1,VN, depth +1);
				}
				else {
					VN.Lch = new VisualNode("" +node.left.item, -1,VN, depth +1);

				}
			}
			Connectors.add(new Connector(VN,VN.Lch,true));
			recVisBuilder(node.left, depth +1 , VN.Lch);
			
		}
		if(node.right != null) {
			if ((int) node.right.item == 0) {
				VN.Rch = new VisualNode("Parent", 1, VN, depth+1);
			}
			else {
				if (node.right.item == '\n') {
					VN.Rch = new VisualNode("(newLine)", 1,VN, depth +1);
				}
				else if (node.right.item == ' ') {
					VN.Rch = new VisualNode("(space)", 1,VN, depth +1);
				}
				else {
					VN.Rch = new VisualNode("" +node.right.item, 1,VN, depth +1);
				}
			}
			Connectors.add(new Connector(VN,VN.Rch, false));
			recVisBuilder(node.right, depth +1 , VN.Rch);
			
		}
		
	}
	
	
	/*
	 * Open a window and draw tree
	 */
	public void Display() {
		JPanel MF = new MyFrame(VSNodeTree);
		MF.setLayout(null);
		for(VisualNode i : TreeAsArray) {
			MF.add(i);
		}
		for(Connector j : Connectors) {
			//System.out.println("COnn");
			MF.add(j);
		}
		//System.out.println(MF.getComponentCount());
		JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(wWidth, wHeight);
        jFrame.add(MF);
        jFrame.setTitle("NodeTree Viewer");
        jFrame.addKeyListener(new KeyListener(){

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
			
			}

			@Override
			public void keyPressed(KeyEvent e) {
				//System.out.println(e.getKeyCode());
				if (e.getKeyCode() ==45) {
					scale -= 0.1;
					xoffset = (int)(xoffset * scale);
					yoffset = (int)(yoffset * scale);

					for (VisualNode i : TreeAsArray) {
						i.setWidth((int)(NodeWidth * scale));
						i.setHeight((int)(NodeHeight * scale));
						i.setPreferredSize(i.getSize());
						i.setLocation((int)(i.ogX * scale),(int)( i.ogY * scale));
					}
					for (Connector i : Connectors) {
						i.resize();
					}
					jFrame.revalidate();
					jFrame.repaint();
				}
				else if(e.getKeyCode() == 61) {
					scale += 0.1;
					xoffset = (int)(xoffset * scale);
					yoffset = (int)(yoffset * scale);

					for (VisualNode i : TreeAsArray) {
						
						i.setWidth((int)(NodeWidth * scale));
						i.setHeight((int)(NodeHeight * scale));
						i.setPreferredSize(i.getSize());
						i.setLocation((int)(i.ogX * scale),(int)( i.ogY * scale));

						//System.out.println("PLUS");
						for (Connector j : Connectors) {
							j.resize();
						}
					}
					jFrame.revalidate();
					jFrame.repaint();
				}
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
        jFrame.setVisible(true);
	}
	
	
}
