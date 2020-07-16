import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class Controller implements Initializable{
	
	public Canvas canvas;
	public PixelWriter pw;
	public GraphicsContext ctx;
	public TextField iterationInput;
	public Button submit;
	public RadioButton mandelb;
	public RadioButton julia;
	public ToggleGroup fractalType;
	public Label currentItr;
	public Label timeTaken;
	
	private int maxIterations = 50;	// default iteration
	private int x, y, n;
	private double a = 0;
	private double b = 0;
	private double xtemp;
	private double xP, yP;
	
	private double map(double n, double start1, double stop1, double start2, double stop2) {
		return ((n-start1)/(stop1-start1))*(stop2-start2)+start2;
	}
	
	public void handleButtonClicked() {
		maxIterations = Integer.parseInt(iterationInput.getText());
	}
	
	public void setFractalParam() {
		RadioButton selected = (RadioButton) fractalType.getSelectedToggle();
		switch (selected.getId()) {
		default:
		case "mandelb":
			a = map(x, 0, canvas.getWidth(), -2, 1);
			b = map(y, 0, canvas.getHeight(), -1.5, 1.5);
			xP = 0;
			yP = 0;
			break;
		case "julia":
			xP = map(x, 0, canvas.getWidth(), -1.5, 1.5);
			yP = map(y, 0, canvas.getHeight(), -1.5, 1.5);
			break;
		}
	}
	
	private void paintFractal() {
		new AnimationTimer() {
			public void handle(long currentNanoTime) {
				currentItr.setText(Integer.toString(maxIterations));
				long startTime = System.currentTimeMillis();
				canvas.addEventFilter(MouseEvent.MOUSE_MOVED, e -> {
					a = map(e.getX(), 0, canvas.getWidth(), -1, 1);
					b = map(e.getY(), 0, canvas.getHeight(), -1, 1);
				});
				for (x = 0; x < canvas.getWidth(); x++) {
					for (y = 0; y < canvas.getHeight(); y++) {
						setFractalParam();
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
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		pw = canvas.getGraphicsContext2D().getPixelWriter();
		ctx = canvas.getGraphicsContext2D();
		paintFractal();
	}
}