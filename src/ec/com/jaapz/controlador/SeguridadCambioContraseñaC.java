package ec.com.jaapz.controlador;

import java.util.Optional;

import ec.com.jaapz.modelo.SegUsuario;
import ec.com.jaapz.modelo.SegUsuarioDAO;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import ec.com.jaapz.util.Encriptado;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SeguridadCambioContrase�aC {
	@FXML private TextField txtUsuario;
	@FXML private PasswordField txtContrase�aActual;
	@FXML private PasswordField txtContrase�aNueva;
	@FXML private PasswordField txtConfirmaContrase�a;
	@FXML private Button btnGrabar;
	
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
			//txtIdUsuario.setText(Integer.toString(Context.getInstance().getIdUsuario()));

			//Context.getInstance().setUsuarios(null);
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
			return true;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
}