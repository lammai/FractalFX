import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.AnimationTimer;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Screen;

public class Controller implements Initializable{
	
	public Canvas canvas;
	public PixelWriter pw;
	public GraphicsContext ctx;
	public Slider slider;
	public RadioButton mandelb;
	public RadioButton julia;
	public ToggleGroup fractalType;
	public Label timeTaken;
	public ColorPicker colorPicker;
	
	private int maxIterations = 50;	// default iteration
	private int x, y, n;
	private double a = 0;
	private double b = 0;
	private double xtemp;
	private double xP, yP;
	
	private double map(double n, double stop1, double start2, double stop2) {
		return ((n- (double) 0)/(stop1- (double) 0))*(stop2-start2)+start2;
	}

	public void setFractalParam() {
		RadioButton selected = (RadioButton) fractalType.getSelectedToggle();
		switch (selected.getId()) {
			case "mandelb" -> {
				a = map(x, canvas.getWidth(), -2, 1);
				b = map(y, canvas.getHeight(), -1.5, 1.5);
				xP = 0;
				yP = 0;
			}
			case "julia" -> {
				xP = map(x, canvas.getWidth(), -1.5, 1.5);
				yP = map(y, canvas.getHeight(), -1.5, 1.5);
			}
		}
	}
	
	private void paintFractal() {
		new AnimationTimer() {
			public void handle(long currentNanoTime) {
				maxIterations = (int) slider.getValue();
				long startTime = System.currentTimeMillis();
				canvas.addEventFilter(MouseEvent.MOUSE_MOVED, e -> {
					a = map(e.getX(), canvas.getWidth(), -1, 1);
					b = map(e.getY(), canvas.getHeight(), -1, 1);
				});
				for (x = 0; x < canvas.getWidth(); x++) {
					for (y = 0; y < canvas.getHeight(); y++) {
						setFractalParam(); //aX, aY, bX, bY double aX, double aY, double bX, double bY
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
							double red = Math.abs(colorPicker.getValue().getRed() - newC);
							double green = Math.abs(colorPicker.getValue().getGreen() - newC);
							double blue = Math.abs(colorPicker.getValue().getBlue() - newC);
							if(newC > 0.4) c = Color.color(blue, green, red);		// inner color
							else c = Color.color(red, green, Math.abs(blue-0.3));	// outer color
							pw.setColor(x, y, c);
						}
						else {
							pw.setColor(x, y, Color.WHITE);
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
		ctx = canvas.getGraphicsContext2D();
		canvas.setWidth(Screen.getPrimary().getBounds().getHeight()-150);
		canvas.setHeight(Screen.getPrimary().getBounds().getHeight()-150);
		colorPicker.setValue(Color.web("6f2741"));
		paintFractal();
	}
}