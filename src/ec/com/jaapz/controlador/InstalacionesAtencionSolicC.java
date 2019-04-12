package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import ec.com.jaapz.modelo.CuentaCliente;
import ec.com.jaapz.modelo.Estado;
import ec.com.jaapz.modelo.Instalacion;
import ec.com.jaapz.modelo.InstalacionDAO;
import ec.com.jaapz.modelo.InstalacionDetalle;
import ec.com.jaapz.modelo.LiquidacionOrdenDAO;
import ec.com.jaapz.modelo.Medidor;
import ec.com.jaapz.modelo.SolInspeccionIn;
import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import ec.com.jaapz.util.Encriptado;
import ec.com.jaapz.util.PrintReport;
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
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Callback;

public class InstalacionesAtencionSolicC {
	@FXML private TextField txtIdSolicitud;
	@FXML private Button btnBuscar;
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
	@FXML private TextArea txtObservaciones;
	
	private @FXML TableView<InstalacionDetalle> tvDatos;
	
	@FXML private Button btnGrabar;
	@FXML private Button btnNuevo;
	@FXML private Button btnImprimir;
	
	ControllerHelper helper = new ControllerHelper();
	Instalacion instalacionSeleccionada = new Instalacion();
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	LiquidacionOrdenDAO liquidacionDao = new LiquidacionOrdenDAO();
	InstalacionDAO instalacionDao = new InstalacionDAO();
	
	@SuppressWarnings("static-access")
	public void initialize() {
		try {
			btnBuscar.setStyle("-fx-cursor: hand;");
			btnGrabar.setStyle("-fx-cursor: hand;");
			btnNuevo.setStyle("-fx-cursor: hand;");
			cboEstadoInstalacion.setStyle("-fx-cursor: hand;");
			
			dtpFecha.setValue(LocalDate.now());
			bloquear();
			llenarCombo();
			Encriptado encriptado = new Encriptado();
			txtUsuarioCreaInst.setText(encriptado.Desencriptar(String.valueOf(Context.getInstance().getUsuariosC().getUsuario())));
			//validar solo numeros
			txtIdSolicitud.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (newValue.matches("\\d*")) {
						//int value = Integer.parseInt(newValue);
					} else {
						txtIdSolicitud.setText(oldValue);
					}
				}
			});
			
			//solo letras mayusculas
			txtObservaciones.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					// TODO Auto-generated method stub
					String cadena = txtObservaciones.getText().toUpperCase();
					txtObservaciones.setText(cadena);
				}
			});
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	void bloquear() {
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
    		cboEstadoInstalacion.setStyle("-fx-background-color: #1ec6ff");
    	}catch(Exception ex) {	
    		System.out.println(ex.getMessage());
    	}
    }
	
	public void nuevo() {
		txtIdSolicitud.setText("");
		txtFechaSolic.setText("");
		txtDireccion.setText("");
		txtReferencia.setText("");
		txtCedula.setText("");
		txtCliente.setText("");
		txtUsuarioSolic.setText("");
		dtpFecha.setValue(LocalDate.now());
		cboEstadoInstalacion.setPromptText("Seleccione Estado");
		txtCodigoMedidor.setText("");
		txtMarca.setText("");
		txtModelo.setText("");
		txtPrecioMed.setText("");
		txtObservaciones.setText("");
		tvDatos.getItems().clear();
		tvDatos.getColumns().clear();
		instalacionSeleccionada = null;
	}
	
	public void grabar() {
		try {
			//para obtener la hora
			java.util.Date utilDate = new java.util.Date(); 
			long lnMilisegundos = utilDate.getTime();
			java.sql.Time sqlTime = new java.sql.Time(lnMilisegundos);
			if(validarDatos() == false)
				return;
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				Medidor medidor = new Medidor();
				SolInspeccionIn solicitud = new SolInspeccionIn();
				
				solicitud = instalacionSeleccionada.getSolInspeccionIn();
				System.out.println("orden de liquidacion: " + instalacionSeleccionada.getSolInspeccionIn().getLiquidacionOrdens().size());
				//se toma en la posicion 0 xq siempre va a haber una liquidacion por cada solicitud
				medidor = instalacionSeleccionada.getSolInspeccionIn().getLiquidacionOrdens().get(0).getMedidor();
				CuentaCliente cuentaCliente = instalacionSeleccionada.getCuentaCliente();
				cuentaCliente.setMedidor(medidor);
				System.out.println(solicitud.getIdSolInspeccion());
				solicitud.setEstadoSolicitud(Constantes.EST_INSPECCION_REALIZADO);
				
				instalacionSeleccionada.setEstadoInstalacion(Constantes.EST_APERTURA_REALIZADO);
				Date date = Date.from(dtpFecha.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
				instalacionSeleccionada.setFechaInst(date);
				instalacionSeleccionada.setHoraInst(sqlTime);
				instalacionSeleccionada.setEstadoInstalacion(Constantes.EST_INSPECCION_REALIZADO);
				instalacionSeleccionada.setObservaciones(txtObservaciones.getText());
				instalacionSeleccionada.setUsuarioInstalacion(Context.getInstance().getUsuariosC().getIdUsuario());
				
				instalacionDao.getEntityManager().getTransaction().begin();
				instalacionDao.getEntityManager().merge(instalacionSeleccionada);
				instalacionDao.getEntityManager().merge(solicitud);
				instalacionDao.getEntityManager().merge(cuentaCliente);
				instalacionDao.getEntityManager().getTransaction().commit();
				helper.mostrarAlertaInformacion("Datos Grabados Correctamente", Context.getInstance().getStage());
				nuevo();
				tvDatos.getColumns().clear();
				tvDatos.getItems().clear();
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
				helper.mostrarAlertaAdvertencia("Medidor no asignado", Context.getInstance().getStage());
				txtCodigoMedidor.requestFocus();
				return false;
			}
			
			if(txtObservaciones.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingrese alguna observación o novedad", Context.getInstance().getStage());
				txtObservaciones.requestFocus();
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
			helper.abrirPantallaModal("/instalaciones/ListadoOrdenLiquidaciones.fxml","Listado de Órdenes de Liquidación", Context.getInstance().getStage());
			if (Context.getInstance().getInstalacion() != null) {
				instalacionSeleccionada = Context.getInstance().getInstalacion();
				llenarDatosLiquidacion(instalacionSeleccionada);
				Context.getInstance().setInstalacion(null);
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	public void llenarDatosLiquidacion(Instalacion instalacionSel){
		try{		
			txtIdSolicitud.setText(Integer.toString(instalacionSel.getIdInstalacion()));
			txtFechaSolic.setText(String.valueOf(formateador.format(instalacionSel.getFechaSalida())));
			txtDireccion.setText(instalacionSel.getSolInspeccionIn().getDireccion());
			txtReferencia.setText(instalacionSel.getSolInspeccionIn().getReferencia());
			txtCedula.setText(instalacionSel.getCuentaCliente().getCliente().getCedula());
			txtCliente.setText(instalacionSel.getCuentaCliente().getCliente().getNombre() + " " + instalacionSel.getCuentaCliente().getCliente().getApellido());
			txtUsuarioSolic.setText(String.valueOf(instalacionSel.getUsuarioCrea()));
			txtCodigoMedidor.setText(instalacionSel.getSolInspeccionIn().getLiquidacionOrdens().get(0).getMedidor().getCodigo());
			txtMarca.setText(instalacionSel.getSolInspeccionIn().getLiquidacionOrdens().get(0).getMedidor().getMarca());
			txtModelo.setText(instalacionSel.getSolInspeccionIn().getLiquidacionOrdens().get(0).getMedidor().getModelo());
			txtPrecioMed.setText(String.valueOf(instalacionSel.getSolInspeccionIn().getLiquidacionOrdens().get(0).getMedidor().getPrecio()));
			recuperarDetalleLiquidacion(instalacionSel);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void imprimir() {
		try {
		/*	Map<String, Object> param = new HashMap<String, Object>();
			param.put("ID_CUENTA", inspeccionRepSeleccionado.getCuentaCliente().getIdCuenta());
			param.put("ID_INSPECCION", inspeccionRepSeleccionado.getIdSolicitudRep());
			param.put("referencia", inspeccionRepSeleccionado.getReferencia());
			param.put("USUARIO_RESPONSABLE", Encriptado.Desencriptar(Context.getInstance().getUsuariosC().getUsuario()));
			if(inspeccionRepSeleccionado.getCuentaCliente().getCategoria().equals(Constantes.CAT_VIVIENDA))
				param.put("vivienda", "X");
			else
				param.put("vivienda", "");
			if(inspeccionRepSeleccionado.getCuentaCliente().getCategoria().equals(Constantes.CAT_COMERCIAL))
				param.put("comercial", "X");
			else
				param.put("comercial", "");
			param.put("LATITUD", inspeccionRepSeleccionado.getCuentaCliente().getLatitud());
			param.put("LONGITUD", inspeccionRepSeleccionado.getCuentaCliente().getLongitud());
			if(inspeccionRepSeleccionado.getCuentaCliente().getCategoria().equals(Constantes.CAT_ESTABLECIMIENTO))
				param.put("publico", "X");
			else
				param.put("publico", "");

			param.put("fecha_inspeccion", formateador.format(inspeccionRepSeleccionado.getFecha()));

			PrintReport printReport = new PrintReport();
			printReport.crearReporte("/recursos/informes/ficha_inspeccion_reparacion.jasper", reparacionDao, param);
			printReport.showReport("Ficha de Inspección de Reparación");*/
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void recuperarDetalleLiquidacion(Instalacion ins) {
		List<InstalacionDetalle> detalle = new ArrayList<InstalacionDetalle>();
		ObservableList<InstalacionDetalle> datos = FXCollections.observableArrayList();
		tvDatos.getColumns().clear();
		tvDatos.getItems().clear();
		System.out.println(ins.getInstalacionDetalles().size());
		for(InstalacionDetalle detallePrevia : ins.getInstalacionDetalles()) {
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
}