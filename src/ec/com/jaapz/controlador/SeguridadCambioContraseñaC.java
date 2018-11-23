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

public class SeguridadCambioContrase�aC {
	@FXML private TextField txtUsuario;
	@FXML private PasswordField txtContrase�aActual;
	@FXML private PasswordField txtContrase�aNueva;
	@FXML private PasswordField txtConfirmaContrase�a;
	@FXML private Button btnGrabar;
	@FXML private Label lblEtiqueta;
	
	Encriptado encriptado = new Encriptado();
	ControllerHelper helper = new ControllerHelper();
	
	SegUsuario usuarioLogueado = new SegUsuario();
	SegUsuarioDAO segUsuarioDAO = new SegUsuarioDAO();
	
	public void initialize(){
		try {
			txtContrase�aNueva.requestFocus();
			usuarioLogueado = Context.getInstance().getUsuariosC();
			txtContrase�aActual.setText(encriptado.Desencriptar(Context.getInstance().getUsuariosC().getClave()));
			txtUsuario.setText(encriptado.Desencriptar(Context.getInstance().getUsuariosC().getUsuario()));
			txtUsuario.setEditable(false);
			txtContrase�aActual.setEditable(false);

			txtConfirmaContrase�a.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(!txtConfirmaContrase�a.getText().equals("") && !txtContrase�aNueva.getText().equals(""))
						if(txtConfirmaContrase�a.getText().equals(txtContrase�aNueva.getText()))
							lblEtiqueta.setText("Las contrase�as coinciden");
						else
							lblEtiqueta.setText("Las contrase�as no coinciden");
					else
						lblEtiqueta.setText("");
				}
			});
			
			txtContrase�aNueva.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(!txtConfirmaContrase�a.getText().equals("") && !txtContrase�aNueva.getText().equals(""))
						if(txtConfirmaContrase�a.getText().equals(txtContrase�aNueva.getText()))
							lblEtiqueta.setText("Las contrase�as coinciden");
						else
							lblEtiqueta.setText("Las contrase�as no coinciden");
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
			usuarioLogueado.setClave(encriptado.Encriptar(txtConfirmaContrase�a.getText()));
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
			if(txtContrase�aNueva.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar nueva contrase�a", Context.getInstance().getStage());
				txtContrase�aNueva.requestFocus();
				return false;
			}
			if(txtConfirmaContrase�a.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Confirme contrase�a", Context.getInstance().getStage());
				txtConfirmaContrase�a.requestFocus();
				return false;
			}
			if(!txtConfirmaContrase�a.getText().equals(txtContrase�aNueva.getText())){
				helper.mostrarAlertaAdvertencia("Las contrase�as no coinciden", Context.getInstance().getStage());
				txtConfirmaContrase�a.requestFocus();
				return false;
			}
			return true;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
}