import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/*
 * class to display all the data and get input from user
 */
public class ControlPanel extends Application{
	private static GridPane g = new GridPane();
	private static TextField scale;
	private static TextField pixPerPoint;
	private static BorderFrame parent;
	private static Label xmax;
	private static Label xmin;
	private static Label ymax;
	private static Label ymin;
	private static Label width;
	private static Label height;
	private static Button resetButton;
	private static Button saveButton;
	private static Button copyButton;
	@Override
	public void start(Stage primaryStage){
		Scene scene = new Scene(g);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		Platform.setImplicitExit(false);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override
		    public void handle(WindowEvent event) {
		        event.consume();
		    }
		});
		primaryStage.setHeight(215);
		primaryStage.setWidth(255);
		primaryStage.show();
	}
	
	/*
	 * creates and adds all of the data/control components
	 */
	public static void setUp(){
		xmax = new Label();
		xmin = new Label();
		ymax = new Label();
		ymin = new Label();
		width = new Label();
		height = new Label();
		scale = new TextField();
		pixPerPoint = new TextField();
		resetButton = new Button("Reset");
		saveButton = new Button("Save");
		copyButton  = new Button("Copy Coordinates");
		
		scale.setOnAction(event->parent.setScale(Double.parseDouble(scale.getText())));
		pixPerPoint.setOnAction(event->parent.setRand(Integer.parseInt(pixPerPoint.getText())-1));
		resetButton.setOnAction(event->parent.reset());
		saveButton.setOnAction(event->parent.save());
		copyButton.setOnAction(event->parent.copy());
		
		
		GridPane g1 = new GridPane();
		GridPane g2 = new GridPane();
		g1.setHgap(10);
		g2.setHgap(5);
		g1.setPadding(new Insets(0,0,0,5));
		g2.setPadding(new Insets(0,0,0,23));
		g.setVgap(5);
		
		g1.add(new Label("X-Max:"), 0, 0);
		g1.add(new Label("X-Min:"), 0, 1);
		g1.add(new Label("Y-Max:"), 0, 2);
		g1.add(new Label("Y-Min:"), 0, 3);
		g1.add(new Label("Width:"), 0, 4);
		g1.add(new Label("Height:"), 0, 5);
		g1.add(new Label("Scale:"), 0, 6);
		g1.add(new Label("Points per Pixel:"), 0, 7);
		g1.add(xmax, 1, 0);
		g1.add(xmin, 1, 1);
		g1.add(ymax, 1, 2);
		g1.add(ymin, 1, 3);
		g1.add(width, 1, 4);
		g1.add(height, 1, 5);
		g1.add(scale, 1, 6);
		g1.add(pixPerPoint, 1, 7);
		g2.add(resetButton, 0, 0);
		g2.add(saveButton, 1, 0);
		g2.add(copyButton, 2, 0);
		g.add(g1, 0, 0);
		g.add(g2, 0, 1);
	}
	
	/*
	 * changes the data displayed
	 */
	public static void setVars(double xma, double xmi, double yma, double ymi, int wid, int hgt, double sca, int ppp){
		if (xmax==null) setUp();
		xmax.setText(xma+"");
		xmin.setText(xmi+"");
		ymax.setText(yma+"");
		ymin.setText(ymi+"");
		width.setText(wid+"");
		height.setText(hgt+"");
		scale.setText(sca + "");
		pixPerPoint.setText(ppp + "");
	}
	
	/*
	 * sets the parent BorderFrame so that the inputs have somewhere to go
	 */
	public static void setParent(BorderFrame p){
		parent = p;
	}
	
	/*
	 * returns a string of the data
	 */
	public static String getString(){
		return width.getText() + ", " + height.getText() + ", " + xmax.getText() + ", " + xmin.getText() + ", " + ymax.getText() + ", " + ymin.getText();
	}
}
