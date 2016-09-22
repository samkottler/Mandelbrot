import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;


/*
 * JFrame containing the current rendered image
 * Also creates a separate JFrame control/info panel
 */
public class BorderFrame extends JFrame{
	private ImagePanel image;
	
	/*
	 * Constructor, takes the initial size of the Mandelbrot image
	 */
	public BorderFrame(int w, int h){
		super("Mandelbrot");
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		
		ControlPanel.setParent(this);
		
		image = new ImagePanel(w,h,this);
		add(image,BorderLayout.CENTER);
		
		setVisible(true);
		Application.launch(ControlPanel.class, new String[]{});
		
		repaint();
	}
	
	/*
	 * changes the data in the info panel
	 */
	public void setData(double left, double right, double top, double bottom, double w, double h, double s, int r){
		try{
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					ControlPanel.setVars(left, right, top, bottom, (int)w, (int)h, s, r+1);
				}
			});
		} catch (IllegalStateException e){}
	}
	
	/*
	 * changes the size of the frame and repositions the control/image panel
	 */
	public void setBigness(int width, int height){
		setSize(width+getInsets().left+getInsets().right,height+getInsets().top+getInsets().bottom);
	}
	
	public void setScale(double sca){
		image.setScale(sca);
	}
	
	public void setRand(int rand){
		image.setRandom(rand);
	}
	
	public void reset(){
		image.reset();
	}
	
	public void save(){
		image.save();
	}
	
	public void copy(){
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Clipboard clipboard = toolkit.getSystemClipboard();
		StringSelection strSel = new StringSelection(ControlPanel.getString());
		clipboard.setContents(strSel, null);
	}
}
