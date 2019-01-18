package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ec.com.jaapz.modelo.Estado;
import ec.com.jaapz.modelo.Instalacion;
import ec.com.jaapz.modelo.InstalacionDAO;
import ec.com.jaapz.modelo.InstalacionDetalle;
import ec.com.jaapz.modelo.LiquidacionDetalle;
import ec.com.jaapz.modelo.LiquidacionOrden;
import ec.com.jaapz.modelo.LiquidacionOrdenDAO;
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
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	LiquidacionOrdenDAO liquidacionDao = new LiquidacionOrdenDAO();
	InstalacionDAO instalacionDao = new InstalacionDAO();
	Instalacion instalacion;
	
	@SuppressWarnings("static-access")
	public void initialize() {
		try {
			dtpFecha.setValue(LocalDate.now());
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
				helper.mostrarAlertaAdvertencia("Indique si realizó la instalación", Context.getInstance().getStage());
				cboEstadoInstalacion.requestFocus();
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
				llenarDatosLiquidacion(liquidacionSeleccionada.getIdLiquidacion());
				Context.getInstance().setLiquidaciones(null);
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	public void llenarDatosLiquidacion(Integer idLiquidacion){
		try{		
			List<LiquidacionOrden> listaLiquidacion = new ArrayList<LiquidacionOrden>();
			listaLiquidacion = liquidacionDao.getRecuperaLiquidacionEmitida(idLiquidacion);
			
			for(int i = 0 ; i < listaLiquidacion.size() ; i ++) {
				
				txtIdSolicitud.setText(Integer.toString(listaLiquidacion.get(i).getSolInspeccionIn().getIdSolInspeccion()));
				txtEstadoValor.setText(listaLiquidacion.get(i).getEstadoValor());
				txtFechaSolic.setText(String.valueOf(formateador.format(listaLiquidacion.get(i).getSolInspeccionIn().getFechaIngreso())));
				txtDireccion.setText(listaLiquidacion.get(i).getSolInspeccionIn().getDireccion());
				txtReferencia.setText(listaLiquidacion.get(i).getSolInspeccionIn().getReferencia());
				txtCedula.setText(listaLiquidacion.get(i).getCuentaCliente().getCliente().getCedula());
				txtCliente.setText(listaLiquidacion.get(i).getCuentaCliente().getCliente().getNombre() + " " + listaLiquidacion.get(i).getCuentaCliente().getCliente().getApellido());
				txtUsuarioSolic.setText(String.valueOf(listaLiquidacion.get(i).getUsuarioCrea()));
				txtCodigoMedidor.setText(listaLiquidacion.get(i).getMedidor().getCodigo());
				txtMarca.setText(listaLiquidacion.get(i).getMedidor().getMarca());
				txtModelo.setText(listaLiquidacion.get(i).getMedidor().getModelo());
				txtPrecioMed.setText(String.valueOf(listaLiquidacion.get(i).getMedidor().getPrecio()));
				recuperarDetalleLiquidacion(listaLiquidacion.get(i));
			}
		}catch(Exception e) {
			e.printStackTrace();
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