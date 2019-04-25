package ec.com.jaapz.controlador;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import ec.com.jaapz.main.LaunchSystem;
import ec.com.jaapz.modelo.SegPerfil;
import ec.com.jaapz.modelo.SegUsuario;
import ec.com.jaapz.modelo.SegUsuarioDAO;
import ec.com.jaapz.modelo.SegUsuarioPerfil;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class InicioSeleccionPerfilC {
	@FXML private ImageView ivLogin;
	@FXML private Button btnAceptar;
	@FXML private Button btnCancelar;
	@FXML private Button btnAtras;
	@FXML private ComboBox<SegPerfil> cboPerfil;
	
	SegUsuarioDAO usuarioDAO = new SegUsuarioDAO();
	ControllerHelper helper = new ControllerHelper();
	
	public void initialize() {
		Image image1 = new Image("login.png");
		ivLogin.setImage(image1);
		btnAtras.setStyle("-fx-graphic: url('/atras.png');-fx-cursor: hand;-fx-background-color: transparent");
		buscarPerfilUsuario();
		btnAtras.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				try {
					retroceder();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	private void retroceder() throws IOException {
		Context.getInstance().getApInicioSesion().getChildren().removeAll();
		FXMLLoader loader = new FXMLLoader(LaunchSystem.class.getResource("/principal/FormInicio.fxml"));
		AnchorPane page=(AnchorPane) loader.load();

		FadeTransition ft = new FadeTransition(Duration.millis(800));
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
		Context.getInstance().getApInicioSesion().getChildren().setAll(page);
	}
	private void buscarPerfilUsuario() {
		try {
			ObservableList<SegPerfil> listaPerfil = FXCollections.observableArrayList();
			List<SegUsuario> usuario = usuarioDAO.getUsuarioPerfil(Context.getInstance().getUsuariosC().getUsuario());
			for(SegUsuarioPerfil per : usuario.get(0).getSegUsuarioPerfils()) {
				if(per.getEstado().equals("A"))
					listaPerfil.add(per.getSegPerfil());
			}
			cboPerfil.setItems(listaPerfil);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void aceptar() {
		if(cboPerfil.getSelectionModel().getSelectedItem() == null) {
			helper.mostrarAlertaAdvertencia("Debe seleccionar perfil de usuario", Context.getInstance().getStage());
			return;
		}
		Context.getInstance().setPerfil(cboPerfil.getSelectionModel().getSelectedItem().getDescripcion());
		Context.getInstance().setIdPerfil(cboPerfil.getSelectionModel().getSelectedItem().getIdPerfil());
		helper.abrirPantallaPrincipal("Sistema JAAPZ","/principal/Contenido.fxml");
	}

	
	public void cancelar() {
		Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Realmente Desea Salir?",Context.getInstance().getStagePrincipal());
		if(result.get() == ButtonType.OK)
			System.exit(0);
	}
}
