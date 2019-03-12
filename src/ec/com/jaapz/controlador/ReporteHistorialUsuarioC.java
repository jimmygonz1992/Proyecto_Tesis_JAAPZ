package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ec.com.jaapz.modelo.Anio;
import ec.com.jaapz.modelo.AnioDAO;
import ec.com.jaapz.modelo.CuentaCliente;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import ec.com.jaapz.util.PrintReport;
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
		
		btnVisualizar.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				if(datoSeleccionado == null) {
					helper.mostrarAlertaAdvertencia("Debe seleccionar un cliente", Context.getInstance().getStage());
					return;
				}
				if(datoSeleccionado.getIdCuenta() == null) {
					helper.mostrarAlertaAdvertencia("Debe seleccionar un cliente", Context.getInstance().getStage());
					return;
				}
				if(cboAnio.getSelectionModel().getSelectedItem() == null) {
					helper.mostrarAlertaAdvertencia("Debe seleccionar un año", Context.getInstance().getStage());
					return;
				}
				SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", new Locale("MX"));
				Date fechaDate = new Date();
				String fechaSistema = formateador.format(fechaDate);
				String fecha = dateFormatter("yyyy-MM-dd hh:mm:ss","d 'de' MMMM 'del' yyyy", fechaSistema);
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("CLIENTE", datoSeleccionado.getCliente().getCedula() + "      " + datoSeleccionado.getCliente().getNombre() + " " + datoSeleccionado.getCliente().getApellido());
				param.put("FECHA", fecha);
				param.put("ANIO", cboAnio.getSelectionModel().getSelectedItem().getDescripcion());
				param.put("ID_CUENTA", datoSeleccionado.getIdCuenta());
				param.put("ID_ANIO", cboAnio.getSelectionModel().getSelectedItem().getIdAnio());
				PrintReport reporte = new PrintReport();
				reporte.crearReporte("/recursos/informes/historialUsuario.jasper", anioDAO, param);
				reporte.showReport("Historial de usuario");
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
	public static final Locale LOCALE_MX = new Locale("es", "MX");
	public static String dateFormatter(String inputFormat, String outputFormat, String inputDate){
	      //Define formato default de entrada.   
	      String input = inputFormat.isEmpty()? "yyyy-MM-dd hh:mm:ss" : inputFormat; 
	      //Define formato default de salida.
	      String output = outputFormat.isEmpty()? "d 'de' MMMM 'del' yyyy" : outputFormat;
	    String outputDate = inputDate;
	    try {
	        outputDate = new SimpleDateFormat(output, LOCALE_MX).format(new SimpleDateFormat(input, LOCALE_MX).parse(inputDate));
	    } catch (Exception e) {
	        System.out.println("dateFormatter(): " + e.getMessage());           
	    }
	    return outputDate;
	}
}
