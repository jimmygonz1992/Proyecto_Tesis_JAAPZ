package ec.com.jaapz.controlador;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import ec.com.jaapz.main.LaunchSystem;
import ec.com.jaapz.modelo.SegUsuario;
import ec.com.jaapz.modelo.SegUsuarioDAO;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import ec.com.jaapz.util.Encriptado;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class InicioFormInicioC {
	Tooltip toolTip;
    @FXML private ImageView ivLogin;
    @FXML private Button btnAceptar;
    @FXML private Button btnCancelar;
    //@FXML private ComboBox<SegPerfil> cboPerfil;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtClave;

	
	ControllerHelper helper = new ControllerHelper();
	SegUsuarioDAO usuarioDAO = new SegUsuarioDAO();
	
    public void initialize() {
    	btnAceptar.setStyle("-fx-cursor: hand;");
		btnCancelar.setStyle("-fx-cursor: hand;");
		//cboPerfil.setStyle("-fx-cursor: hand;");
		
		
		toolTip = new Tooltip("Ingrese Nombre de usuario");
		txtUsuario.setTooltip(toolTip);
		toolTip = new Tooltip("Ingrese Clave del usuario");
		txtClave.setTooltip(toolTip);
		toolTip = new Tooltip("Aceptar");
		btnAceptar.setTooltip(toolTip);
		toolTip = new Tooltip("Cancelar");
		btnCancelar.setTooltip(toolTip);
		
		Image image1 = new Image("login.png");
		ivLogin.setImage(image1);
		
		txtUsuario.setText("sa");
		txtClave.setText("sa");
    }
    
    boolean validarDatos() {
		boolean bandera = false;
		if(txtUsuario.getText().equals("")) {
			helper.mostrarAlertaAdvertencia("Debe de Ingresar Usuario", Context.getInstance().getStage());
			txtUsuario.requestFocus();
			return false;
		}
		if(txtClave.getText().equals("")) {
			helper.mostrarAlertaAdvertencia("Debe de Ingresar Clave", Context.getInstance().getStage());
			txtClave.requestFocus();
			return false;
		}
		bandera = true;
		return bandera;
	}
    
    public void aceptar() throws IOException {
    	if(validarDatos() == false)
			return;
		
		List<SegUsuario> usuario;
		usuario = usuarioDAO.getUsuario(Encriptado.Encriptar(txtUsuario.getText()),Encriptado.Encriptar(txtClave.getText()));
		if(usuario.size() == 1){
			Context.getInstance().setUsuariosC(usuario.get(0));
			
			Context.getInstance().setUsuario(Encriptado.Desencriptar(usuario.get(0).getUsuario()));
			Context.getInstance().setIdUsuario(usuario.get(0).getIdUsuario());
			

			Context.getInstance().getApInicioSesion().getChildren().removeAll();
			FXMLLoader loader = new FXMLLoader(LaunchSystem.class.getResource("/principal/FrmSeleccionPerfil.fxml"));
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
			Context.getInstance().getApInicioSesion().getChildren().setAll(page);
		}
		else{
			helper.mostrarAlertaError("Clave o Usuario Incorrecto!!!",Context.getInstance().getStagePrincipal());
		}
    }

    public void cancelar() {
    	Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Realmente Desea Salir?",Context.getInstance().getStagePrincipal());
		if(result.get() == ButtonType.OK)
			System.exit(0);
    }
    /*
	private void buscarPerfilUsuario() {
		try {
			ObservableList<SegPerfil> listaPerfil = FXCollections.observableArrayList();
			List<SegUsuario> usuario = usuarioDAO.getUsuarioPerfil(Encriptado.Encriptar(txtUsuario.getText()));
			for(SegUsuarioPerfil per : usuario.get(0).getSegUsuarioPerfils()) {
				if(per.getEstado().equals("A"))
					listaPerfil.add(per.getSegPerfil());
			}
			cboPerfil.setItems(listaPerfil);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
		*/
}
