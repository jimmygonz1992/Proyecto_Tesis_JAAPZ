package ec.com.jaapz.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class EgresosC {
	@FXML private DatePicker dtpFecha;
	@FXML private TextArea txtDescripcion;
	@FXML private TextField txtValorMonto;
	@FXML private TextArea txtObservaciones;
	@FXML private TextField txtUsuario;
	
	@FXML private Button btnGrabar;
	@FXML private Button btnNuevo;
	
	public void initialize(){
		btnGrabar.setStyle("-fx-cursor: hand;");
		btnNuevo.setStyle("-fx-cursor: hand;");
		
	}
	
	public void grabar() {
		
	}
	
	public void nuevo() {
		
	}
}