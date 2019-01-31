package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import ec.com.jaapz.modelo.Estado;
import ec.com.jaapz.modelo.Reparacion;
import ec.com.jaapz.modelo.ReparacionDAO;
import ec.com.jaapz.modelo.ReparacionDetalle;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class ReparacionesAtencionSolicC {
	@FXML private TextField txtIdSolicitud;
	@FXML private Button btnBuscar;
	@FXML private TextField txtFechaSolic;
	@FXML private TextField txtDireccion;
	@FXML private TextField txtReferencia;
	@FXML private TextField txtIdReparacion;
	
	@FXML private TextField txtCedula;
	@FXML private TextField txtCliente;
	@FXML private TextField txtCodigoMedidor;
	@FXML private TextField txtIdCuenta;
	@FXML private TextField txtUsuarioCreaRep;
	@FXML private DatePicker dtpFecha;
	@FXML private ComboBox<Estado> cboEstadoReparacion;
	
	@FXML private TextArea txtObservacionesRep;
	@FXML TableView<ReparacionDetalle> tvDatos;
	
	@FXML private Button btnGrabar;
	@FXML private Button btnNuevo;
	@FXML private Button btnImprimir;
	
	ControllerHelper helper = new ControllerHelper();
	Reparacion reparacionSeleccionada = new Reparacion();
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	ReparacionDAO reparacionDao = new ReparacionDAO();
	
	@SuppressWarnings("static-access")
	public void initialize() {
		try {
			dtpFecha.setValue(LocalDate.now());
			bloquear();
			llenarCombo();
			Encriptado encriptado = new Encriptado();
			btnImprimir.setStyle("-fx-graphic: url('/imprimir.png');-fx-cursor: hand;");
			txtUsuarioCreaRep.setText(encriptado.Desencriptar(String.valueOf(Context.getInstance().getUsuariosC().getUsuario())));
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
			
			//solo letras mayusculas
			txtObservacionesRep.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					// TODO Auto-generated method stub
					String cadena = txtObservacionesRep.getText().toUpperCase();
					txtObservacionesRep.setText(cadena);
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
		txtCodigoMedidor.setEditable(false);
		txtIdCuenta.setEditable(false);
		txtUsuarioCreaRep.setEditable(false);
		txtIdReparacion.setEditable(false);
		txtIdReparacion.setVisible(false);
	}
	
	private void llenarCombo() {
    	try {			
    		ObservableList<Estado> listaEstado = FXCollections.observableArrayList(Estado.values()); 
    		cboEstadoReparacion.setItems(listaEstado);
    		cboEstadoReparacion.setPromptText("Seleccione Estado Reparación");
    		cboEstadoReparacion.setStyle("-fx-background-color: #1ec6ff");
    	}catch(Exception ex) {
    		
    		System.out.println(ex.getMessage());
    	}
    }
	
	public void buscarSolicitudRep() {
		try {
			helper.abrirPantallaModal("/reparaciones/ReparacionesListadoInspRep.fxml","Listado de Órdenes de Reparaciones", Context.getInstance().getStage());
			if (Context.getInstance().getReparaciones() != null) {
				reparacionSeleccionada = Context.getInstance().getReparaciones();
				llenarDatosReparacion(reparacionSeleccionada);
				Context.getInstance().setReparaciones(null);
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings("unused")
	void llenarDatosReparacion(Reparacion datoSeleccionado){
		try {
			if(datoSeleccionado.getIdReparacion() == null)
				txtIdReparacion.setText("");
			else
				txtIdReparacion.setText(String.valueOf(datoSeleccionado.getIdReparacion()));
			
			if(datoSeleccionado.getSolInspeccionRep().getIdSolicitudRep() == null)
				txtIdSolicitud.setText("");
			else
				txtIdSolicitud.setText(String.valueOf(datoSeleccionado.getSolInspeccionRep().getIdSolicitudRep()));
			
			if(datoSeleccionado.getSolInspeccionRep().getFecha() == null)
				txtFechaSolic.setText("");
			else
				txtFechaSolic.setText(String.valueOf(formateador.format(datoSeleccionado.getSolInspeccionRep().getFecha())));
			
			if(datoSeleccionado.getCuentaCliente().getDireccion() == null)
				txtDireccion.setText("");
			else
				txtDireccion.setText(datoSeleccionado.getCuentaCliente().getDireccion());
			
			if(datoSeleccionado.getCuentaCliente().getDireccion() == null)
				txtDireccion.setText("");
			else
				txtDireccion.setText(datoSeleccionado.getCuentaCliente().getDireccion());
			
			if(datoSeleccionado.getReferencia() == null)
				txtReferencia.setText("");
			else
				txtReferencia.setText(datoSeleccionado.getReferencia());
			
			if(datoSeleccionado.getCuentaCliente().getCliente().getCedula() == null)
				txtCedula.setText("");
			else
				txtCedula.setText(datoSeleccionado.getCuentaCliente().getCliente().getCedula());
			
			if(datoSeleccionado.getCuentaCliente().getCliente().getNombre() + " " + datoSeleccionado.getCuentaCliente().getCliente().getApellido() == null)
				txtCliente.setText("");
			else
				txtCliente.setText(datoSeleccionado.getCuentaCliente().getCliente().getNombre() + " " + datoSeleccionado.getCuentaCliente().getCliente().getApellido());

			if(datoSeleccionado.getCuentaCliente().getMedidor().getCodigo() == null)
				txtCodigoMedidor.setText("");
			else
				txtCodigoMedidor.setText(datoSeleccionado.getCuentaCliente().getMedidor().getCodigo());
			
			if(datoSeleccionado.getCuentaCliente().getIdCuenta() == null)
				txtIdCuenta.setText("");
			else
				txtIdCuenta.setText(String.valueOf(datoSeleccionado.getCuentaCliente().getIdCuenta()));
						
			recuperarDetalleReparacion(datoSeleccionado);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void recuperarDetalleReparacion(Reparacion rep) {
		List<ReparacionDetalle> detalle = new ArrayList<ReparacionDetalle>();
		ObservableList<ReparacionDetalle> datos = FXCollections.observableArrayList();
		tvDatos.getColumns().clear();
		tvDatos.getItems().clear();
		for(ReparacionDetalle detallePrevia : rep.getReparacionDetalles()) {
			ReparacionDetalle detAdd = new ReparacionDetalle();
			detAdd.setRubro(detallePrevia.getRubro());
			detAdd.setCantidad(detallePrevia.getCantidad());
			detAdd.setPrecio(detallePrevia.getPrecio());
			detAdd.setSubtotal(detallePrevia.getCantidad()*detallePrevia.getPrecio());
			detalle.add(detAdd);
		}
		datos.setAll(detalle);
		TableColumn<ReparacionDetalle, String> descripcionColum = new TableColumn<>("Descripción");
		descripcionColum.setMinWidth(10);
		descripcionColum.setPrefWidth(200);
		descripcionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ReparacionDetalle, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<ReparacionDetalle, String> param) {
				return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getRubro().getDescripcion()));
			}
		});
		
		TableColumn<ReparacionDetalle, String> cantidadColum = new TableColumn<>("Cantidad");
		cantidadColum.setMinWidth(10);
		cantidadColum.setPrefWidth(90);
		cantidadColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ReparacionDetalle, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<ReparacionDetalle, String> param) {
				return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCantidad()));
			}
		});
		
		TableColumn<ReparacionDetalle, String> precioColum = new TableColumn<>("Precio");
		precioColum.setMinWidth(10);
		precioColum.setPrefWidth(90);
		precioColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ReparacionDetalle, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<ReparacionDetalle, String> param) {
				return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getPrecio()));
			}
		});
		
		TableColumn<ReparacionDetalle, String> totalColum = new TableColumn<>("Total");
		totalColum.setMinWidth(10);
		totalColum.setPrefWidth(90);
		totalColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ReparacionDetalle, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<ReparacionDetalle, String> param) {
				return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCantidad()*param.getValue().getPrecio()));
			}
		});
		
		tvDatos.getColumns().addAll(descripcionColum, cantidadColum, precioColum, totalColum);
		tvDatos.setItems(datos);
	}
	
	public void grabar() {
		try {			
			if(validarDatos() == false)
				return;
		
			if(reparacionSeleccionada == null) {
				reparacionSeleccionada = new Reparacion();
			}
			Date date = Date.from(dtpFecha.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				if (reparacionSeleccionada.getIdReparacion() != null) {
					List<ReparacionDetalle> listaAgregadaRubros = new ArrayList<ReparacionDetalle>();
					for(ReparacionDetalle det : tvDatos.getItems()) {
						det.setIdReparacionDet(null);
						det.setEstado(Constantes.ESTADO_ACTIVO);
						det.setReparacion(reparacionSeleccionada);
						listaAgregadaRubros.add(det);
					}
					java.util.Date utilDate = new java.util.Date(); 
					long lnMilisegundos = utilDate.getTime();
					java.sql.Time sqlTime = new java.sql.Time(lnMilisegundos);
					//empieza la transaccion
					reparacionDao.getEntityManager().getTransaction().begin();
					//reparacionSeleccionada.setReparacionDetalles(listaAgregadaRubros);
					reparacionSeleccionada.setObservcion(txtObservacionesRep.getText());
					
					if(cboEstadoReparacion.getSelectionModel().getSelectedIndex() == 0) { // es una solicitud de inspeccion
						reparacionSeleccionada.setEstadoReparacion(Constantes.EST_INSPECCION_REALIZADO);
					}
					if(cboEstadoReparacion.getSelectionModel().getSelectedIndex() == 1) { // es una solicitud de reparacion
						reparacionSeleccionada.setEstadoReparacion(Constantes.EST_INSPECCION_PENDIENTE);
					}
					
					reparacionSeleccionada.setEstadoReparacion(cboEstadoReparacion.getValue().toString());
					reparacionSeleccionada.setFechaReparacion(date);
					reparacionSeleccionada.setHoraReparacion(sqlTime);
					reparacionSeleccionada.setUsuarioReparacion(Context.getInstance().getUsuariosC().getIdUsuario());
					
					//aqui voy a intentar guardar y tengo q preguntar si es nuevo
					//o sino solo para editar
					
					if(txtIdReparacion.getText().equals("0")) {//inserta nuevo ingreso
						reparacionSeleccionada.getIdReparacion();
						reparacionDao.getEntityManager().persist(reparacionSeleccionada);
					}else {//modifica
						reparacionSeleccionada.setIdReparacion(Integer.parseInt(txtIdReparacion.getText()));
						reparacionDao.getEntityManager().merge(reparacionSeleccionada);
					}
						
					reparacionDao.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Datos Grabados Correctamente", Context.getInstance().getStage());
					nuevo();
					tvDatos.getColumns().clear();
					tvDatos.getItems().clear();
				}/*else {
					List<Integer> integer = new ArrayList<Integer>();
					for (ReparacionDetalle detalle : tvDatos.getItems()) {
						if (detalle.getIdReparacionDet() != null)
							integer.add(detalle.getIdReparacionDet());
					}
					for(ReparacionDetalle det : tvDatos.getItems()) {
						if(det.getIdReparacionDet() == null) {
							det.setIdReparacionDet(null);
							det.setEstado(Constantes.ESTADO_ACTIVO);
							det.setReparacion(reparacionSeleccionada);
							reparacionSeleccionada.getReparacionDetalles().add(det);
						}else {
							for (ReparacionDetalle deta: reparacionSeleccionada.getReparacionDetalles()) {
								if (!integer.contains(deta.getIdReparacionDet())) {
									deta.setEstado(Constantes.ESTADO_INACTIVO);
								}
							}
						}
					}*/
					
					//salidaRepSeleccionada.setReparacionDetalles(listaAgregadaRubros);
		/*			reparacionSeleccionada.setEstadoReparacion(cboEstadoReparacion.getPromptText());
					reparacionSeleccionada.setFechaReparacion(date);
					reparacionSeleccionada.setUsuarioReparacion(Context.getInstance().getUsuariosC().getIdUsuario());
					//aqui voy a intentar guardar y tengo q preguntar si es nuevo
					reparacionDao.getEntityManager().getTransaction().begin();
					reparacionDao.getEntityManager().merge(reparacionSeleccionada);
					reparacionDao.getEntityManager().getTransaction().commit();
						
					helper.mostrarAlertaInformacion("Datos grabados Correctamente", Context.getInstance().getStage());
					nuevo();
					tvDatos.getColumns().clear();
					tvDatos.getItems().clear();
				}
	*/		}
			reparacionSeleccionada = null;
		}catch(Exception ex) {
			reparacionDao.getEntityManager().getTransaction().rollback();
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
			System.out.println(ex.getMessage());
		}
	}
	
	boolean validarDatos() {
		try {
			if(txtIdSolicitud.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("No existe ID de solicitud", Context.getInstance().getStage());
				txtIdSolicitud.requestFocus();
				return false;
			}
			
			if(txtFechaSolic.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("No existe Fecha de solicitud", Context.getInstance().getStage());
				txtFechaSolic.requestFocus();
				return false;
			}
			
			if(txtDireccion.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("No existe dirección de reparación", Context.getInstance().getStage());
				txtDireccion.requestFocus();
				return false;
			}
			
			if(txtReferencia.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingrese alguna referencia domiciliaria", Context.getInstance().getStage());
				txtReferencia.requestFocus();
				return false;
			}
						
			if(txtCedula.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar Cédula de Cliente", Context.getInstance().getStage());
				txtCedula.requestFocus();
				return false;
			}
			
			if(txtCliente.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar Nombres y Apellidos del Cliente", Context.getInstance().getStage());
				txtCliente.requestFocus();
				return false;
			}
			
			if(txtCodigoMedidor.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar Código de Medidor", Context.getInstance().getStage());
				txtCodigoMedidor.requestFocus();
				return false;
			}
			
			if(txtIdCuenta.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("No existe cuenta del Cliente", Context.getInstance().getStage());
				txtIdCuenta.requestFocus();
				return false;
			}
			
			if(txtUsuarioCreaRep.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Usuario de Reparación no existe", Context.getInstance().getStage());
				txtUsuarioCreaRep.requestFocus();
				return false;
			}
			
			if(dtpFecha.getValue().equals(null)) {
				helper.mostrarAlertaAdvertencia("Ingresar Fecha de ejecución de reparación", Context.getInstance().getStage());
				dtpFecha.requestFocus();
				return false;
			}
			
			if(cboEstadoReparacion.getSelectionModel().getSelectedIndex() == -1) {
				helper.mostrarAlertaAdvertencia("Indique si realizó la reparación", Context.getInstance().getStage());
				cboEstadoReparacion.requestFocus();
				return false;
			}
			
			if(txtObservacionesRep.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Describa brevemente trabajo de reparación", Context.getInstance().getStage());
				txtObservacionesRep.requestFocus();
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
	
	public void nuevo() {
		try {
			txtIdSolicitud.setText("");
			txtFechaSolic.setText("");
			txtDireccion.setText("");
			txtReferencia.setText("");
			txtCedula.setText("");
			txtCliente.setText("");
			txtCodigoMedidor.setText("");
			txtIdCuenta.setText("");
			txtObservacionesRep.setText("");
			tvDatos.getItems().clear();
			tvDatos.getColumns().clear();
			cboEstadoReparacion.setPromptText("Seleccione Estado Reparación");
			reparacionSeleccionada = null;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void imprimir() {
		try {
			
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}