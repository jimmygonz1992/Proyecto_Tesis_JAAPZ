package ec.com.jaapz.controlador;

import java.io.IOException;

import ec.com.jaapz.main.LaunchSystem;
import ec.com.jaapz.util.Context;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class InicioSesionC {
	@FXML private ImageView ivLogo;
    @FXML private AnchorPane apContenido;
    
	public void initialize() throws IOException{
		Image image = new Image("logo2.png");
		ivLogo.setImage(image);
		
		apContenido.getChildren().removeAll();
		FXMLLoader loader = new FXMLLoader(LaunchSystem.class.getResource("/principal/FormInicio.fxml"));
		AnchorPane page=(AnchorPane) loader.load();

		FadeTransition ft = new FadeTransition(Duration.millis(1000));
		ft.setNode(page);
		ft.setFromValue(0.1);
		ft.setToValue(1);
		ft.setCycleCount(1);
		ft.setAutoReverse(false);
		ft.play();
		AnchorPane.setBottomAnchor(page, 00.0);
		AnchorPane.setLeftAnchor(page, 00.0);
		AnchorPane.setTopAnchor(page, 00.0);
		AnchorPane.setRightAnchor(page, 00.0);
		apContenido.getChildren().setAll(page);
		Context.getInstance().setApInicioSesion(apContenido);
	}
}