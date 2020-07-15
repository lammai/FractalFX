import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class Controller implements Initializable{
	
	public Canvas canvas;
	public PixelWriter pw;
	public TextField iterationInput;
	public Button submit;
	public RadioButton mandelb;
	public RadioButton julia;
	public ToggleGroup fractalType;
	public Label currentItr;
	public Label timeTaken;
	private int maxIterations = 30;	// default iteration
	private double ca;				// real component
	private double cb;				// imaginary component
	
	
	private double map(double n, double start1, double stop1, double start2, double stop2) {
		return ((n-start1)/(stop1-start1))*(stop2-start2)+start2;
	}
	
	public void handleButtonClicked() {
		maxIterations = Integer.parseInt(iterationInput.getText());
	}
	
	public void handleRadioClicked() {
		RadioButton selected = (RadioButton) fractalType.getSelectedToggle();
		if (selected.getId().equals("mandelb")) {
			paintMandelbrot();
		}
		else {
			paintJulia();
		}
	}
	
	private void paintMandelbrot() {
		new AnimationTimer() {
			public void handle(long currentNanoTime) {
				currentItr.setText(Integer.toString(maxIterations));
				long startTime = System.currentTimeMillis();
				
				int x, y, n;
				double a = 0;
				double b = 0;
				double xtemp;
				double xP, yP;
				
				for (x = 0; x < canvas.getWidth(); x++) {
					for (y = 0; y < canvas.getHeight(); y++) {
						a = map(x, 0, canvas.getWidth(), -2, 1);
						b = map(y, 0, canvas.getHeight(), -1.5, 1.5);
						
						xP = 0;
						yP = 0;
						
						n = 0;
						while(xP * xP + yP * yP <= 4 && n < maxIterations) {
							xtemp = xP * xP - yP * yP + a;
							yP = 2 * xP * yP + b;
							xP = xtemp;
							
							n++;
						}
						if (n < maxIterations) {
							double newC = ((double)n)/((double)maxIterations);
							Color c;
							if(newC > 0.4) c = Color.color(1, newC, 0);
							else c = Color.color(newC, 0, Math.abs(newC-0.3));
							pw.setColor(x, y, c);
						}
						else {
							pw.setColor(x, y, Color.BLACK);
						}
					}
				}
				
				timeTaken.setText(System.currentTimeMillis() - startTime + " milliseconds");
			}
		}.start();
	}
	
	private void paintJulia() {	// Julia set 
		new AnimationTimer() {
			public void handle(long currentNanoTime) {
				currentItr.setText(Integer.toString(maxIterations));
				
				canvas.addEventFilter(MouseEvent.MOUSE_MOVED, e -> {	// Animate Julia set with
					ca = map(e.getX(), 0, canvas.getWidth(), -1, 1);	// mouse coordinate
					cb = map(e.getY(), 0, canvas.getHeight(), -1, 1);
				});
				
				
				long startTime = System.currentTimeMillis();
				
				int x, y, n;
				double a = 0;
				double b = 0;
				double xtemp;
				
				for (x = 0; x < canvas.getWidth(); x++) {
					for (y = 0; y < canvas.getHeight(); y++) {
						a = map(x, 0, canvas.getWidth(), -1.5, 1.5);
						b = map(y, 0, canvas.getHeight(), -1.5, 1.5);
						
						n = 0;
						while(a * a + b * b <= 4 && n < maxIterations) {
							xtemp = a * a - b * b + ca;
							b = 2 * a * b + cb;
							a = xtemp;
							
							n++;
						}
						if (n < maxIterations) {
							double newC = ((double)n)/((double)maxIterations);
							Color c;
							if(newC > 0.4) c = Color.color(1, newC, 0);
							else c = Color.color(newC, 0, Math.abs(newC-0.3));
							pw.setColor(x, y, c);
						}
						else {
							pw.setColor(x, y, Color.BLACK);
						}
					}
				}
				
				timeTaken.setText(System.currentTimeMillis() - startTime + " milliseconds");
			}
		}.start();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		pw = canvas.getGraphicsContext2D().getPixelWriter();
		paintJulia();
	}
}