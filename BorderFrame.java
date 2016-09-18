import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


/*
 * JFrame containing the current rendered image
 * Also creates a separate JFrame control/info panel
 */
public class BorderFrame extends JFrame{
	private ImagePanel image;
	private JFrame controlPanel;
	private JButton reset;
	private JButton save;
	private JButton clipboard;
	private JPanel dataPanel;
	private JPanel savePanel;
	private JTextField xMin,xMax,yMin,yMax,width,height,scale,rand;
	
	/*
	 * Constructor, takes the initial size of the Mandelbrot image
	 */
	public BorderFrame(int w, int h){
		super("Mandelbrot");
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
	
		width = new JTextField(w+"");
		height = new JTextField(h+"");
		
		savePanel = new JPanel(new BorderLayout());
		dataPanel = new JPanel(new GridLayout(0,2));
		
		image = new ImagePanel(w,h,this);
		add(image,BorderLayout.CENTER);
		
		makeControl();
		
		setVisible(true);
		
		repaint();
	}
	
	/*
	 * creates the buttons on the control panel and the control panel frame itself
	 */
	public void makeControl(){
		controlPanel = new JFrame("Mandelbrot Control");
		controlPanel.setLayout(new BorderLayout());
		controlPanel.setVisible(true);
		controlPanel.setResizable(false);
		controlPanel.setSize(controlPanel.getInsets().left+200, controlPanel.getInsets().top+500);
		controlPanel.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		reset = new JButton("Reset");
		reset.setPreferredSize(new Dimension(150,50));
		reset.setFont(new Font("Ariel",Font.PLAIN,40));
		reset.addActionListener(new ResetListener());
		
		save = new JButton("Save");
		save.setPreferredSize(new Dimension(150,50));
		save.setFont(new Font("Ariel",Font.PLAIN,40));
		save.addActionListener(new SaveListener());
		
		clipboard = new JButton("Save Coords");
		clipboard.setPreferredSize(new Dimension(150,50));
		clipboard.setFont(new Font("Ariel",Font.PLAIN,40));
		clipboard.addActionListener(new ClipListener());
		
		savePanel.add(save, BorderLayout.NORTH);
		savePanel.add(clipboard, BorderLayout.SOUTH);
		
		controlPanel.add(reset,BorderLayout.NORTH);
		controlPanel.add(savePanel, BorderLayout.SOUTH);
		
		makeData();
	}
	
	/*
	 * creates and adds all the information about the image
	 */
	public void makeData(){

		
		JLabel xma = new JLabel("X-Max:");
		JLabel xmi = new JLabel("X-Min:");
		JLabel yma = new JLabel("Y-Max:");
		JLabel ymi = new JLabel("Y-Min:");
		JLabel wid = new JLabel("Width:");
		JLabel hei = new JLabel("Height:");
		JLabel sca = new JLabel("Scale:");
		JLabel ran = new JLabel("Points per pixel:");
		
		xMax = new JTextField(0.6+"");
		xMin = new JTextField((0-2.4)+"");
		yMax = new JTextField(1.2+"");
		yMin = new JTextField(-1.2+"");
		scale = new JTextField(2+"");
		rand = new JTextField(1+"");
		
		xMax.setEditable(false);
		xMin.setEditable(false);
		yMax.setEditable(false);
		yMin.setEditable(false);
		width.setEditable(false);
		height.setEditable(false);
		
		scale.addActionListener(new ScaleListener());
		rand.addActionListener(new RandListener());
		
		dataPanel.add(xma);
		dataPanel.add(xMax);
		dataPanel.add(xmi);
		dataPanel.add(xMin);
		dataPanel.add(yma);
		dataPanel.add(yMax);
		dataPanel.add(ymi);
		dataPanel.add(yMin);
		dataPanel.add(wid);
		dataPanel.add(width);
		dataPanel.add(hei);
		dataPanel.add(height);
		dataPanel.add(sca);
		dataPanel.add(scale);
		dataPanel.add(ran);
		dataPanel.add(rand);
		
		controlPanel.add(dataPanel,BorderLayout.CENTER);
		controlPanel.revalidate();
		controlPanel.repaint();
	}
	
	/*
	 * changes the data in the info panel
	 */
	public void setData(double left, double right, double top, double bottom, double w, double h, double s, int r){
		xMax.setText(left+"");
		xMin.setText(right+"");
		yMax.setText(top+"");
		yMin.setText(bottom+"");
		width.setText(w+"");
		height.setText(h+"");
		scale.setText(s+"");
		rand.setText((r+1)+"");
	}
	
	/*
	 * changes the size of the frame and repositions the control/image panel
	 */
	public void setBigness(int width, int height){
		setSize(width+getInsets().left+getInsets().right,height+getInsets().top+getInsets().bottom);
		controlPanel.setLocation(width+getInsets().right+controlPanel.getInsets().left+getX(),getY());
	}
	
	
	/*
	 * The next 5 methods are the action listeners for the buttons and text fields
	 * They are pretty self explanatory 
	 */
	
	class ResetListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			image.reset();
		}
	}
	
	class SaveListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			image.save();
		}
	}
	
	class ScaleListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0){
			image.setScale(Double.parseDouble(scale.getText()));
		}
	}
	
	class ClipListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0){
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Clipboard clipboard = toolkit.getSystemClipboard();
			StringSelection strSel = new StringSelection((int)Double.parseDouble(width.getText()) + ", " + (int)Double.parseDouble(height.getText()) + ", " + xMax.getText() + ", " + xMin.getText() + ", " + yMax.getText() + ", " + yMin.getText());
			clipboard.setContents(strSel, null);
		}
	}
	
	class RandListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0){
			image.setRandom(Integer.parseInt(rand.getText())-1);
		}
	}
}
