package ec.com.jaapz.controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ec.com.jaapz.modelo.Estado;
import ec.com.jaapz.modelo.InstalacionDetalle;
import ec.com.jaapz.modelo.LiquidacionDetalle;
import ec.com.jaapz.modelo.LiquidacionOrden;
import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import ec.com.jaapz.util.Encriptado;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class InstalacionesAtencionSolicC {
	@FXML private TextField txtIdSolicitud;
	@FXML private Button btnBuscar;
	@FXML private TextField txtEstadoValor;
	@FXML private TextField txtFechaSolic;
	@FXML private TextField txtDireccion;
	@FXML private TextField txtReferencia;
	
	@FXML private TextField txtCedula;
	@FXML private TextField txtCliente;
	@FXML private TextField txtUsuarioSolic;
	@FXML private DatePicker dtpFecha;
	@FXML private TextField txtUsuarioCreaInst;
	@FXML private ComboBox<Estado> cboEstadoInstalacion;
	
	@FXML private TextField txtCodigoMedidor;
	@FXML private TextField txtMarca;
	@FXML private TextField txtModelo;
	@FXML private TextField txtPrecioMed;
	
	private @FXML TableView<InstalacionDetalle> tvDatos;
	
	@FXML private Button btnGrabar;
	@FXML private Button btnNuevo;
	@FXML private Button btnImprimir;
	
	ControllerHelper helper = new ControllerHelper();
	LiquidacionOrden liquidacionSeleccionada = new LiquidacionOrden();
	
	@SuppressWarnings("static-access")
	public void initialize() {
		try {
			bloquear();
			llenarCombo();
			Encriptado encriptado = new Encriptado();
			btnImprimir.setStyle("-fx-graphic: url('/imprimir.png');-fx-cursor: hand;");
			txtUsuarioCreaInst.setText(encriptado.Desencriptar(String.valueOf(Context.getInstance().getUsuariosC().getUsuario())));
			//validar solo numeros
			txtIdSolicitud.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, 
						String newValue) {
					if (newValue.matches("\\d*")) {
						//int value = Integer.parseInt(newValue);
					} else {
						txtIdSolicitud.setText(oldValue);
					}
				}
			});
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	void bloquear() {
		txtEstadoValor.setEditable(false);
		txtFechaSolic.setEditable(false);
		txtDireccion.setEditable(false);
		txtReferencia.setEditable(false);
		txtCedula.setEditable(false);
		txtCliente.setEditable(false);
		txtUsuarioSolic.setEditable(false);
		txtUsuarioCreaInst.setEditable(false);
		txtCodigoMedidor.setEditable(false);
		txtMarca.setEditable(false);
		txtModelo.setEditable(false);
		txtPrecioMed.setEditable(false);
	}
	
	private void llenarCombo() {
    	try {			
    		ObservableList<Estado> listaEstado = FXCollections.observableArrayList(Estado.values()); 
    		cboEstadoInstalacion.setItems(listaEstado);
    		cboEstadoInstalacion.setPromptText("Seleccione Estado");
    	}catch(Exception ex) {
    		
    		System.out.println(ex.getMessage());
    	}
    }
	
	public void nuevo() {
		txtIdSolicitud.setText("");
		txtEstadoValor.setText("");
		txtFechaSolic.setText("");
		txtDireccion.setText("");
		txtReferencia.setText("");
		txtCedula.setText("");
		txtCliente.setText("");
		txtUsuarioSolic.setText("");
		dtpFecha.setValue(null);
		cboEstadoInstalacion.setPromptText("Seleccione Estado");
		txtCodigoMedidor.setText("");
		txtMarca.setText("");
		txtModelo.setText("");
		txtPrecioMed.setText("");
		tvDatos.getItems().clear();
		tvDatos.getColumns().clear();
		liquidacionSeleccionada = null;
	}
	
	public void grabar() {
		try {
			if(validarDatos() == false)
				return;
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				
				liquidacionSeleccionada.setEstadoInstalacion(Constantes.EST_APERTURA_REALIZADO);
				
				helper.mostrarAlertaInformacion("Datos Grabados Correctamente", Context.getInstance().getStage());
				nuevo();
			}
		}catch(Exception ex) {
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
			System.out.println(ex.getMessage());
		}
	}
	
	boolean validarDatos() {
		try {
			if(txtIdSolicitud.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("No existe # de Solicitud", Context.getInstance().getStage());
				txtIdSolicitud.requestFocus();
				return false;
			}
			
			if(txtEstadoValor.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("No existe Estado del Valor de Orden de Liquidación", Context.getInstance().getStage());
				txtEstadoValor.requestFocus();
				return false;
			}
			
			if(txtFechaSolic.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("No existe fecha de Orden de Liquidación", Context.getInstance().getStage());
				txtFechaSolic.requestFocus();
				return false;
			}
			
			if(txtCedula.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("No existe cédula del cliente", Context.getInstance().getStage());
				txtCedula.requestFocus();
				return false;
			}
			
			if(txtCliente.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("No existe nombres del cliente", Context.getInstance().getStage());
				txtCliente.requestFocus();
				return false;
			}
			
			if(txtUsuarioSolic.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("No existe usuario de registro de orden de liquidación", Context.getInstance().getStage());
				txtUsuarioSolic.requestFocus();
				return false;
			}
			
			if(dtpFecha.getValue().equals(null)) {
				helper.mostrarAlertaAdvertencia("Ingresar fecha de instalación", Context.getInstance().getStage());
				dtpFecha.requestFocus();
				return false;
			}
			
			if(cboEstadoInstalacion.getSelectionModel().getSelectedIndex() == -1) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar tipo de solicitud", Context.getInstance().getStage());
				return false;
			}
			
			if(txtCodigoMedidor.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("No medidor asignado", Context.getInstance().getStage());
				txtCodigoMedidor.requestFocus();
				return false;
			}
			
			if(tvDatos.getItems().isEmpty()) {
				helper.mostrarAlertaAdvertencia("No contiene rubros", Context.getInstance().getStage());
				tvDatos.requestFocus();
				return false;
			}
			return true;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
	public void buscarSolicitudInst() {
		try{
			helper.abrirPantallaModal("/instalaciones/ListadoOrdenLiquidaciones.fxml","Listado de Órdenes de Liquidaciones", Context.getInstance().getStage());
			if (Context.getInstance().getLiquidaciones() != null) {
				liquidacionSeleccionada = Context.getInstance().getLiquidaciones();
				llenarDatosLiquidacion(liquidacionSeleccionada);
				Context.getInstance().setLiquidaciones(null);
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings("unused")
	void llenarDatosLiquidacion(LiquidacionOrden datoSeleccionado){
		try {
			if(datoSeleccionado.getSolInspeccionIn().getIdSolInspeccion() == null)
				txtIdSolicitud.setText("");
			else
				txtIdSolicitud.setText(String.valueOf(datoSeleccionado.getSolInspeccionIn().getIdSolInspeccion()));
			
			if(datoSeleccionado.getEstadoValor() == null)
				txtEstadoValor.setText("");
			else
				txtEstadoValor.setText(datoSeleccionado.getEstadoValor());
			
			if(datoSeleccionado.getFecha() == null)
				txtFechaSolic.setText("");
			else
				txtFechaSolic.setText(String.valueOf(datoSeleccionado.getFecha()));
			
			if(datoSeleccionado.getCuentaCliente().getDireccion() == null)
				txtDireccion.setText("");
			else
				txtDireccion.setText(datoSeleccionado.getCuentaCliente().getDireccion());
			
			if(datoSeleccionado.getSolInspeccionIn().getReferencia() == null)
				txtReferencia.setText("");
			else
				txtReferencia.setText(datoSeleccionado.getSolInspeccionIn().getReferencia());
			
			if(datoSeleccionado.getCuentaCliente().getCliente().getCedula() == null)
				txtCedula.setText("");
			else
				txtCedula.setText(datoSeleccionado.getCuentaCliente().getCliente().getCedula());
			
			if(datoSeleccionado.getCuentaCliente().getCliente().getNombre() + " " + datoSeleccionado.getCuentaCliente().getCliente().getApellido() == null)
				txtCliente.setText("");
			else
				txtCliente.setText(datoSeleccionado.getCuentaCliente().getCliente().getNombre() + " " + datoSeleccionado.getCuentaCliente().getCliente().getApellido());
			
			if(datoSeleccionado.getUsuarioCrea() == null)
				txtUsuarioSolic.setText("");
			else
				txtUsuarioSolic.setText(String.valueOf(datoSeleccionado.getUsuarioCrea()));

			if(datoSeleccionado.getMedidor().getCodigo() == null)
				txtCodigoMedidor.setText("");
			else
				txtCodigoMedidor.setText(datoSeleccionado.getMedidor().getCodigo());
			
			if(datoSeleccionado.getMedidor().getMarca() == null)
				txtMarca.setText("");
			else
				txtMarca.setText(datoSeleccionado.getMedidor().getMarca());
			
			if(datoSeleccionado.getMedidor().getModelo() == null)
				txtModelo.setText("");
			else
				txtModelo.setText(datoSeleccionado.getMedidor().getModelo());
			
			txtPrecioMed.setText(String.valueOf(datoSeleccionado.getMedidor().getPrecio()));
			
			recuperarDetalleLiquidacion(datoSeleccionado);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void recuperarDetalleLiquidacion(LiquidacionOrden liq) {
		List<InstalacionDetalle> detalle = new ArrayList<InstalacionDetalle>();
		ObservableList<InstalacionDetalle> datos = FXCollections.observableArrayList();
		tvDatos.getColumns().clear();
		tvDatos.getItems().clear();
		for(LiquidacionDetalle detallePrevia : liq.getLiquidacionDetalles()) {
			InstalacionDetalle detAdd = new InstalacionDetalle();
			detAdd.setRubro(detallePrevia.getRubro());
			detAdd.setCantidad(detallePrevia.getCantidad());
			detAdd.setPrecio(detallePrevia.getPrecio());
			detAdd.setSubtotal(detallePrevia.getCantidad()*detallePrevia.getPrecio());
			detalle.add(detAdd);
		}
		datos.setAll(detalle);
		TableColumn<InstalacionDetalle, String> descripcionColum = new TableColumn<>("Descripción");
		descripcionColum.setMinWidth(10);
		descripcionColum.setPrefWidth(200);
		descripcionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<InstalacionDetalle, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<InstalacionDetalle, String> param) {
				return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getRubro().getDescripcion()));
			}
		});
		
		TableColumn<InstalacionDetalle, String> cantidadColum = new TableColumn<>("Cantidad");
		cantidadColum.setMinWidth(10);
		cantidadColum.setPrefWidth(90);
		cantidadColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<InstalacionDetalle, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<InstalacionDetalle, String> param) {
				return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCantidad()));
			}
		});
		
		TableColumn<InstalacionDetalle, String> precioColum = new TableColumn<>("Precio");
		precioColum.setMinWidth(10);
		precioColum.setPrefWidth(90);
		precioColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<InstalacionDetalle, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<InstalacionDetalle, String> param) {
				return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getPrecio()));
			}
		});
		
		TableColumn<InstalacionDetalle, String> totalColum = new TableColumn<>("Total");
		totalColum.setMinWidth(10);
		totalColum.setPrefWidth(90);
		totalColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<InstalacionDetalle, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<InstalacionDetalle, String> param) {
				return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCantidad()*param.getValue().getPrecio()));
			}
		});
		
		tvDatos.getColumns().addAll(descripcionColum, cantidadColum, precioColum, totalColum);
		tvDatos.setItems(datos);
	}
	
	public void imprimir() {
		
	}
}