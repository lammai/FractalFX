import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("layout.fxml"));
		
		// Get screen height
		double screenHeight = Screen.getPrimary().getBounds().getHeight();
		
		primaryStage.setTitle("FractalFX");
		primaryStage.setScene(new Scene(root, screenHeight-150, screenHeight-114));
		primaryStage.show();
	}
}
