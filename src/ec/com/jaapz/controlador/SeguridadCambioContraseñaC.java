package ec.com.jaapz.controlador;

import java.util.Optional;

import ec.com.jaapz.modelo.SegUsuario;
import ec.com.jaapz.modelo.SegUsuarioDAO;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import ec.com.jaapz.util.Encriptado;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SeguridadCambioContraseñaC {
	@FXML private TextField txtUsuario;
	@FXML private PasswordField txtContraseñaActual;
	@FXML private PasswordField txtContraseñaNueva;
	@FXML private PasswordField txtConfirmaContraseña;
	@FXML private Button btnGrabar;
	@FXML private Label lblEtiqueta;
	
	ControllerHelper helper = new ControllerHelper();
	
	SegUsuario usuarioLogueado = new SegUsuario();
	SegUsuarioDAO segUsuarioDAO = new SegUsuarioDAO();
	
	public void initialize(){
		try {
			txtContraseñaNueva.requestFocus();
			usuarioLogueado = Context.getInstance().getUsuariosC();
			txtContraseñaActual.setText(Encriptado.Desencriptar(Context.getInstance().getUsuariosC().getClave()));
			txtUsuario.setText(Encriptado.Desencriptar(Context.getInstance().getUsuariosC().getUsuario()));
			txtUsuario.setEditable(false);
			txtContraseñaActual.setEditable(false);

			txtConfirmaContraseña.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(!txtConfirmaContraseña.getText().equals("") && !txtContraseñaNueva.getText().equals(""))
						if(txtConfirmaContraseña.getText().equals(txtContraseñaNueva.getText()))
							lblEtiqueta.setText("Las contraseñas coinciden");
						else
							lblEtiqueta.setText("Las contraseñas no coinciden");
					else
						lblEtiqueta.setText("");
				}
			});
			
			txtContraseñaNueva.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(!txtConfirmaContraseña.getText().equals("") && !txtContraseñaNueva.getText().equals(""))
						if(txtConfirmaContraseña.getText().equals(txtContraseñaNueva.getText()))
							lblEtiqueta.setText("Las contraseñas coinciden");
						else
							lblEtiqueta.setText("Las contraseñas no coinciden");
					else
						lblEtiqueta.setText("");
				}
			});
		}catch (Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	public void grabar(){
		try {
			if(validarDatos() == false){
				return;
			}
			usuarioLogueado.setClave(Encriptado.Encriptar(txtConfirmaContraseña.getText()));
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los datos?", Context.getInstance().getStage());
			if (result.get() == ButtonType.OK) {
				segUsuarioDAO.getEntityManager().getTransaction().begin();
				segUsuarioDAO.getEntityManager().merge(usuarioLogueado);
				segUsuarioDAO.getEntityManager().getTransaction().commit();
				helper.mostrarAlertaConfirmacion("Datos Grabados Correctamente", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
			segUsuarioDAO.getEntityManager().getTransaction().rollback();
			System.out.println(ex.getMessage());
		}
	}
	
	boolean validarDatos() {
		try {
			if(txtContraseñaNueva.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar nueva contraseña", Context.getInstance().getStage());
				txtContraseñaNueva.requestFocus();
				return false;
			}
			if(txtConfirmaContraseña.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Confirme contraseña", Context.getInstance().getStage());
				txtConfirmaContraseña.requestFocus();
				return false;
			}
			if(!txtConfirmaContraseña.getText().equals(txtContraseñaNueva.getText())){
				helper.mostrarAlertaAdvertencia("Las contraseñas no coinciden", Context.getInstance().getStage());
				txtConfirmaContraseña.requestFocus();
				return false;
			}
			return true;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
}