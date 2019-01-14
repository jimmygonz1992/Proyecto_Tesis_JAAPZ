package ec.com.jaapz.controlador;

import java.util.ArrayList;
import java.util.List;

import ec.com.jaapz.modelo.Estado;
import ec.com.jaapz.modelo.Reparacion;
import ec.com.jaapz.modelo.ReparacionDetalle;
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
	
	@SuppressWarnings("static-access")
	public void initialize() {
		try {
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
	}
	
	private void llenarCombo() {
    	try {			
    		ObservableList<Estado> listaEstado = FXCollections.observableArrayList(Estado.values()); 
    		cboEstadoReparacion.setItems(listaEstado);
    		cboEstadoReparacion.setPromptText("Seleccione Estado");
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
			if(datoSeleccionado.getSolInspeccionRep().getIdSolicitudRep() == null)
				txtIdSolicitud.setText("");
			else
				txtIdSolicitud.setText(String.valueOf(datoSeleccionado.getSolInspeccionRep().getIdSolicitudRep()));
			
			if(datoSeleccionado.getSolInspeccionRep().getFecha() == null)
				txtFechaSolic.setText("");
			else
				txtFechaSolic.setText(String.valueOf(datoSeleccionado.getSolInspeccionRep().getFecha()));
			
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
			
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
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
			dtpFecha.setValue(null);
			txtObservacionesRep.setText("");
			tvDatos.getItems().clear();
			tvDatos.getColumns().clear();
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