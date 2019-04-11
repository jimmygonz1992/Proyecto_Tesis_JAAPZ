package ec.com.jaapz.main;

import java.util.Optional;

import ec.com.jaapz.controlador.InicioSesionC;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;



public class LaunchSystem extends Application {
	ControllerHelper helper = new ControllerHelper();
	
	@Override
	public void start(Stage stage) {
		try {
			FXMLLoader root = new FXMLLoader();
			root.setLocation(getClass().getResource("/principal/InicioSesion.fxml"));
			AnchorPane page = (AnchorPane) root.load();
			Scene scene = new Scene(page);
			stage.setScene(scene);
			stage.getIcons().add(new Image("/logo_jaapz.png"));
			stage.setTitle("Inicio de Sesion");
			InicioSesionC inicio = (InicioSesionC) root.getController();
			inicio.setDialogStage(stage);
			stage.setMaximized(true);
			stage.show();
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea salir del sistema?",Context.getInstance().getStage());
					if(result.get() == ButtonType.OK)
						System.exit(0);
					else
						event.consume();
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}