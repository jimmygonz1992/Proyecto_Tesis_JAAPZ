package ec.com.jaapz.controlador;

import java.util.ArrayList;
import java.util.List;

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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class BodegaSalidaRubroRepC {
	@FXML private TextField txtCedula;
	@FXML private TextField txtIdCuenta;
	@FXML private Button btnBuscarInspCuenta;
	@FXML private TextField txtNombres;
	@FXML private TextField txtApellidos;
	@FXML private TextField txtDireccion;
	@FXML private TextField txtReferencia;
	@FXML private TextField txtTelefono;
	@FXML private TextField txtCodigoMedidor;
	
	@FXML private TextField txtIdInspecc;
	@FXML private Button btnBuscarInspeccion;
	@FXML private DatePicker dtpFecha;
	@FXML private TextField txtUsuarioInspeccion;
	@FXML private TextField txtUsuarioCrea;
	
	@FXML private TextArea txtObservaciones;
	@FXML private TableView<ReparacionDetalle> tvDatos;
	
	@FXML private Button btnEliminar;
	@FXML private Button btnGrabar;
	@FXML private Button btnNuevo;
	
	@FXML private TextField txtTotal;
	
	ControllerHelper helper = new ControllerHelper();
	Reparacion salidaRepSeleccionada = new Reparacion();
	
	public void initialize() {
		try {
			//validar solo numeros
			txtIdCuenta.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, 
						String newValue) {
					if (newValue.matches("\\d*")) {
						//int value = Integer.parseInt(newValue);
					} else {
						txtIdCuenta.setText(oldValue);
					}
				}
			});
			
			//validar solo numeros
			txtIdInspecc.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, 
						String newValue) {
					if (newValue.matches("\\d*")) {
						//int value = Integer.parseInt(newValue);
					} else {
						txtIdInspecc.setText(oldValue);
					}
				}
			});
			txtUsuarioCrea.setText(Encriptado.Desencriptar(String.valueOf(Context.getInstance().getUsuariosC().getUsuario())));
			bloquear();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	void bloquear() {
		txtCedula.setEditable(false);
		txtNombres.setEditable(false);
		txtApellidos.setEditable(false);
		txtDireccion.setEditable(false);
		txtReferencia.setEditable(false);
		txtTelefono.setEditable(false);
		txtCodigoMedidor.setEditable(false);
		txtUsuarioInspeccion.setEditable(false);
		txtUsuarioCrea.setEditable(false);
		txtObservaciones.setEditable(false);
		txtTotal.setEditable(false);
	}
	
	public void	buscarInspCuenta() {
		try {
			helper.abrirPantallaModal("/bodega/BodegaListadoSalidaRep.fxml","Listado de Órdenes de Reparaciones", Context.getInstance().getStage());
			if (Context.getInstance().getReparaciones() != null) {
				salidaRepSeleccionada = Context.getInstance().getReparaciones();
				llenarDatosSalidaReparacion(salidaRepSeleccionada);
				Context.getInstance().setReparaciones(null);
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void buscarInspeccion() {
		try {
			helper.abrirPantallaModal("/bodega/BodegaListadoSalidaRep.fxml","Listado de Órdenes de Reparaciones", Context.getInstance().getStage());
			if (Context.getInstance().getReparaciones() != null) {
				salidaRepSeleccionada = Context.getInstance().getReparaciones();
				llenarDatosSalidaReparacion(salidaRepSeleccionada);
				Context.getInstance().setReparaciones(null);
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	void llenarDatosSalidaReparacion(Reparacion datoSeleccionado){
		try {
			if(datoSeleccionado.getCuentaCliente().getCliente().getCedula() == null)
				txtCedula.setText("");
			else
				txtCedula.setText(datoSeleccionado.getCuentaCliente().getCliente().getCedula());

			if(datoSeleccionado.getCuentaCliente().getIdCuenta() == null)
				txtIdCuenta.setText("");
			else
				txtIdCuenta.setText(String.valueOf(datoSeleccionado.getCuentaCliente().getIdCuenta()));
			
			if(datoSeleccionado.getCuentaCliente().getCliente().getNombre() == null)
				txtNombres.setText("");
			else
				txtNombres.setText(datoSeleccionado.getCuentaCliente().getCliente().getNombre());
			
			if(datoSeleccionado.getCuentaCliente().getCliente().getApellido() == null)
				txtApellidos.setText("");
			else
				txtApellidos.setText(datoSeleccionado.getCuentaCliente().getCliente().getApellido());
			
			if(datoSeleccionado.getCuentaCliente().getDireccion() == null)
				txtDireccion.setText("");
			else
				txtDireccion.setText(datoSeleccionado.getCuentaCliente().getDireccion());
			
			if(datoSeleccionado.getReferencia() == null)
				txtReferencia.setText("");
			else
				txtReferencia.setText(datoSeleccionado.getReferencia());
			
			if(datoSeleccionado.getCuentaCliente().getCliente().getTelefono() == null)
				txtTelefono.setText("");
			else
				txtTelefono.setText(datoSeleccionado.getCuentaCliente().getCliente().getTelefono());
			
			if(datoSeleccionado.getCuentaCliente().getMedidor().getCodigo() == null)
				txtCodigoMedidor.setText("");
			else
				txtCodigoMedidor.setText(datoSeleccionado.getCuentaCliente().getMedidor().getCodigo());
			
			if(datoSeleccionado.getSolInspeccionRep().getIdSolicitudRep() == null)
				txtIdInspecc.setText("");
			else
				txtIdInspecc.setText(String.valueOf(datoSeleccionado.getSolInspeccionRep().getIdSolicitudRep()));
			
			if(datoSeleccionado.getSolInspeccionRep().getUsuarioCrea() == null)
				txtUsuarioInspeccion.setText("");
			else
				txtUsuarioInspeccion.setText(String.valueOf(datoSeleccionado.getSolInspeccionRep().getUsuarioCrea()));
			
			if(datoSeleccionado.getSolInspeccionRep().getObservacion() == null)
				txtObservaciones.setText("");
			else
				txtObservaciones.setText(datoSeleccionado.getSolInspeccionRep().getObservacion());
			
			txtTotal.setText(String.valueOf(datoSeleccionado.getTotal()));
			
			recuperarDetalleSalidaReparacion(datoSeleccionado);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void recuperarDetalleSalidaReparacion(Reparacion rep) {
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
	
	public void eliminar() {
		try {
			
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void grabar() {
		try {
			
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void nuevo() {
		try {
			txtCedula.setText("");
			txtIdCuenta.setText("");
			txtNombres.setText("");
			txtApellidos.setText("");
			txtDireccion.setText("");
			txtReferencia.setText("");
			txtTelefono.setText("");
			txtCodigoMedidor.setText("");
			txtIdInspecc.setText("");
			dtpFecha.setValue(null);
			txtUsuarioInspeccion.setText("");
			txtObservaciones.setText("");
			tvDatos.getItems().clear();
			tvDatos.getColumns().clear();
			txtTotal.setText("");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}