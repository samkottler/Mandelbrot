import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;


/*
 * A JPanel which contains the current Mandelbrot image
 * Allows the user to select a place to zoom
 */
public class ImagePanel extends JPanel implements MouseListener,MouseMotionListener{
	private BufferedImage image;
	private double xClick=0,yClick=0,x=0,y=0;
	private double width,height;
	private int mouseX = 0, mouseY = 0,moveX=0,moveY=0, rand = 1;
	private double top = 1.2,bottom = -1.2,right = 0.6,left = -2.4;
	private final int MAX_WIDTH,MAX_HEIGHT;
	private double scale = 1;
	private Mandelbrot mand;
	private BorderFrame parent;
	
	/*
	 * Constructor takes the size of the current image and the parent JFrame
	 */
	public ImagePanel(int w, int h, BorderFrame p){
		setSize(w,h);
		addMouseListener(this);
		addMouseMotionListener(this);
		width = w;
		height = h;
		MAX_WIDTH=w;
		MAX_HEIGHT=h;
		parent = p;
		
		reset();
		
		setVisible(true);	
	}
	
	/*
	 * calculates the width and height for the new scale and then recalculates the Mandelbrot image
	 */
	public void setScale(double scale){
		width = width/this.scale*scale;
		height = height/this.scale*scale;
		this.scale = scale;
		mand = new Mandelbrot((int)width,(int)height,right,left,top,bottom,rand);
		mand.generate();
		image = mand.image;
		repaint();
	}
	
	/*
	 * recalculates the Mandelbrot with rand number of random points inside each pixel
	 * the bigger rand is the better the image looks and the longer it take to render
	 */
	public void setRandom(int rand){
		this.rand=rand;
		mand = new Mandelbrot((int)width,(int)height,right,left,top,bottom,rand);
		mand.generate();
		image = mand.image;
		repaint();
	}
	
	/*
	 * Called when the save button is pressed
	 * Creates a file chooser so the user can choose where to save the current image
	 * If the file they choose already exists it asks if they want to replace the existing one
	 */
	public void save(){
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("PNG","PNG"));
		int ret = chooser.showSaveDialog(chooser.getParent());
		if (ret == JFileChooser.APPROVE_OPTION){
			String file = chooser.getSelectedFile().getPath()+".png";
			if (new File(file).exists()){
				Object[] options = {"Yes","No"};
				String[] parts = file.split("\\\\");
				int option = JOptionPane.showOptionDialog(null, "The file " + parts[parts.length-1] + " already exists. Do you want to replace the existing file?", 
						"Confirm save", 
						JOptionPane.YES_NO_OPTION, 
						JOptionPane.WARNING_MESSAGE, 
						null, options, options[1]);						
				if (option == JOptionPane.YES_OPTION)
					mand.save(file);
			}
			else
				mand.save(file);
		}
	}
	
	/*
	 * resets all the bounds to their defaults and recalculates the Mandelbrot
	 */
	public void reset(){
		width = MAX_WIDTH;
		height = MAX_HEIGHT;
		right = 0.6;
		left = -2.4;
		top = 1.2;
		bottom = -1.2;
		scale = 1;
		rand = 0;
		mand = new Mandelbrot(MAX_WIDTH,MAX_HEIGHT,rand);
		mand.generate();
		image = mand.image;
		repaint();
	}
	
	/*
	 * sets the size of the JFrame and all the info in the info panel, then draws the 
	 * Mandelbrot and a white box if the user is currently selecting an area to zoom
	 */
	public void paint(Graphics g){
		parent.setBigness((int)width, (int)height);
		parent.setData(left,right,top,bottom,width,height,scale,rand);
		g.drawImage(image,0,0,this);
		g.setColor(Color.white);
		g.drawRect(mouseX, mouseY, moveX, moveY);
	}
	
	/*
	 * Changes the coordinates for the box that the user is drawing so the white
	 * box will be drawn correctly
	 */
	@Override
	public void mouseDragged(MouseEvent m) {
		int x = m.getX();
		int y = m.getY();
		//System.out.println(x + " " + mouseX + " " + moveX);
		if (Math.abs(x-mouseX)>=Math.abs(x-(mouseX+moveX))){
			moveX=Math.abs(x-mouseX);
		}
		else{
			moveX+=mouseX-x;
			mouseX = x;
		}
		
		if (Math.abs(y-mouseY)>=Math.abs(y-(mouseY+moveY))){
			moveY=Math.abs(y-mouseY);
		}
		else{
			moveY+=mouseY-y;
			mouseY = y;
		}
		repaint();
		
	}

	/*
	 * The next few methods didn't need to be overridden
	 */
	@Override
	public void mouseMoved(MouseEvent m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent m) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * starts drawing a box to zoom in on where the user clicked
	 */
	@Override
	public void mousePressed(MouseEvent m) {
		xClick = (double)(m.getX()-getInsets().left)/width*(right-left)+left;
		yClick = (double)(m.getY()-getInsets().top)/height*(top-bottom) + bottom;
		mouseX = m.getX();
		mouseY = m.getY();
		
	}

	/*
	 * When the user stops dragging the mouse the area highlighted is zoomed in on
	 */
	@Override
	public void mouseReleased(MouseEvent m) {
		x = (double)(m.getX()-getInsets().left)/width*(right-left)+left;
		y = (double)(m.getY()-getInsets().top)/height*(top-bottom)+bottom;
		if(Math.abs(x-xClick)/2.4<Math.abs(y-yClick)/3){
			width = MAX_HEIGHT*scale*(Math.abs(x-xClick)/Math.abs(y-yClick));
			height = MAX_HEIGHT*scale;
		}
		else{
			height = MAX_WIDTH*scale*(Math.abs(y-yClick)/Math.abs(x-xClick));
			width = MAX_WIDTH*scale;
		}
		if (width<292){
			height*=292/width;
			width = 292;
		}
		if (x>xClick){
			right = x;
			left = xClick;
		}
		else{
			right = xClick;
			left = x;
		}
		if (y>yClick){
			top = y;
			bottom = yClick;
		}
		else{
			top = yClick;
			bottom = y;
		}
		//System.out.println(width + "," + height + ","+right+","+left+","+top+","+bottom);
		//System.out.println((width/height)+ " " + (right-left)/(top-bottom));
		mand = new Mandelbrot((int)width,(int)height,right,left,top,bottom,rand);
		mand.generate();
		image = mand.image;
		mouseX=mouseY=moveX=moveY =0;
		repaint();
	}

}
