package ec.com.jaapz.controlador;

import java.util.List;

import ec.com.jaapz.modelo.Anio;
import ec.com.jaapz.modelo.AnioDAO;
import ec.com.jaapz.modelo.CuentaCliente;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
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

	ControllerHelper helper = new ControllerHelper();
	CuentaCliente datoSeleccionado;
	
	AnioDAO anioDAO = new AnioDAO();
	
	public void initialize() {
		cboAnio.setStyle("-fx-cursor: hand;");
		btnBuscar.setStyle("-fx-cursor: hand;");
		btnVisualizar.setStyle("-fx-cursor: hand;");
		cargarCombos();
		btnBuscar.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				try{
					helper.abrirPantallaModal("/reparaciones/ReparacionesListadoCuentas.fxml","Listado de Clientes", Context.getInstance().getStage());
					if (Context.getInstance().getCuentaCliente() != null) {
						datoSeleccionado = Context.getInstance().getCuentaCliente();
						recuperarCliente(datoSeleccionado);
					}
				}catch(Exception ex){
					System.out.println(ex.getMessage());
				}
			}
		});
	}
	private void recuperarCliente(CuentaCliente cuenta) {
		try {
			txtidCuenta.setText(String.valueOf(cuenta.getIdCuenta()));
			if(cuenta.getMedidor() != null)
				txtMedidor.setText(cuenta.getMedidor().getCodigo());
			txtCliente.setText(cuenta.getCliente().getNombre() + " " + cuenta.getCliente().getApellido());
			txtCedula.setText(cuenta.getCliente().getCedula());
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void cargarCombos() {
		try {
			ObservableList<Anio> listaAnios = FXCollections.observableArrayList();
			List<Anio> datAn = anioDAO.getListaAnios();
			listaAnios.addAll(datAn);
			cboAnio.setItems(listaAnios);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
