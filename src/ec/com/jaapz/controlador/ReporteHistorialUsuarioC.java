package ec.com.jaapz.controlador;

import ec.com.jaapz.modelo.Anio;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class ReporteHistorialUsuarioC {
	@FXML private Button btnImprimir;
	@FXML private TextField txtMedidor;
	@FXML private TextField txtidCuenta;
	@FXML private ComboBox<Anio> cboAnio;
	@FXML private TextField txtCliente;
	@FXML private TextField txtCedula;
	@FXML private Button btnVisualizar;
	@FXML private Button btnBuscar;

	public void initialize() {
		
	}
	public void buscarCliente() {

	}
}
