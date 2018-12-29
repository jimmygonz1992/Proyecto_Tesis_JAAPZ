package ec.com.jaapz.controlador;

import java.util.ArrayList;
import java.util.List;

import ec.com.jaapz.modelo.ReparacionDetalle;
import ec.com.jaapz.modelo.Rubro;
import ec.com.jaapz.modelo.RubroDAO;
import ec.com.jaapz.modelo.SolInspeccionRep;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

public class SolicitudesCierreReparacionC {
	@FXML private TextField txtCodigo;
	@FXML private Button btnBuscar;
	@FXML private TextField txtCodigoMedidor;
	@FXML private TextField txtFecha;
	@FXML private TextField txtReferencia;
	@FXML private TextField txtLongitud;
	@FXML private TextField txtLatitud;
	
	@FXML private TextField txtCedula;
	@FXML private TextField txtTelefono;
	@FXML private TextField txtNombres;
	@FXML private TextField txtContacto;
	@FXML private TextField txtDireccion;
	
	@FXML private TextArea txtNovedadesReportadas;
	
	@FXML private TextField txtCodigoMat;
	@FXML private Button btnBuscarRubro;
	@FXML private TextArea txtDescripcionProd;
	@FXML private TextField txtCantidad;
	@FXML private TextField txtPrecio;
	@FXML private TextField txtStock;
	
	@FXML private Button btnAgregar;
	@FXML private Button btnQuitar;
	
	@FXML private TextArea txtNovedadesInspeccion;
	
	@FXML TableView<ReparacionDetalle> tvDatosDetalle;
	
	ControllerHelper helper = new ControllerHelper();
	SolInspeccionRep inspeccionRepSeleccionado = new SolInspeccionRep();
	Rubro rubroSeleccionado = new Rubro();
	RubroDAO rubroDao = new RubroDAO();
	
	public void initialize() {
		txtCodigo.setEditable(false);
		txtCodigoMedidor.setEditable(false);
		txtFecha.setEditable(false);
		txtReferencia.setEditable(false);
		txtLatitud.setEditable(false);
		txtLongitud.setEditable(false);
		txtCedula.setEditable(false);
		txtTelefono.setEditable(false);
		txtContacto.setEditable(false);
		txtNombres.setEditable(false);
		txtDireccion.setEditable(false);
		txtNovedadesReportadas.setEditable(false);
		txtDescripcionProd.setEditable(false);
		txtStock.setEditable(false);
		txtPrecio.setEditable(false);
		
		//recuperar Material
		txtCodigoMat.setOnKeyPressed(new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent ke){
				if (ke.getCode().equals(KeyCode.ENTER)){
					if (validarProductoExiste() == false) {
						helper.mostrarAlertaAdvertencia("Elemento no existente!", Context.getInstance().getStage());
						txtCodigoMat.requestFocus();
						txtCodigoMat.setText("");
						txtDescripcionProd.setText("");
						txtStock.setText("");
						txtPrecio.setText("");
					}else {
						recuperarDatos(txtCodigoMat.getText());
						txtCantidad.requestFocus();
					}
				}
			}
		});
		
		//validar solo numeros
		txtCantidad.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (newValue.matches("\\d*")) {
					//int value = Integer.parseInt(newValue);
				} else {
					txtCantidad.setText(oldValue);
				}
			}
		});
		
		//solo letras mayusculas
		txtNovedadesInspeccion.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				String cadena = txtNovedadesInspeccion.getText().toUpperCase();
				txtNovedadesInspeccion.setText(cadena);
			}
		});
		
		//para anadir a la grilla con enter
		txtCantidad.setOnKeyPressed(new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent ke){
				if (ke.getCode().equals(KeyCode.ENTER)){
					agregarMaterial();
					btnBuscarRubro.requestFocus();
				}
			}
		});
	}
	
	//recupera datos del proveedor
		public void recuperarDatos(String codigo){
			try{
				List<Rubro> listaRubro = new ArrayList<Rubro>();
				listaRubro = rubroDao.getRecuperaRubro(codigo);
				for(int i = 0 ; i < listaRubro.size() ; i ++) {
					txtCodigoMat.setText(listaRubro.get(i).getCodigo());
					txtDescripcionProd.setText(listaRubro.get(i).getDescripcion());
					txtStock.setText(String.valueOf(listaRubro.get(i).getStock()));
					txtPrecio.setText(String.valueOf(listaRubro.get(i).getPrecio()));
					
					rubroSeleccionado = listaRubro.get(i);
				}
				if (listaRubro.size() == 0)
					rubroSeleccionado = new Rubro();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	
	boolean validarProductoExiste() {
		try {
			List<Rubro> listaRubros;
			listaRubros = rubroDao.getRecuperaRubro(txtCodigoMat.getText());
			if(listaRubros.size() != 0)
				return true;
			else
				return false;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
	public void nuevo() {
		
	}
	
	public void grabar() {
		
	}
	
	public void buscarSolicitudRep() {
		try{
			helper.abrirPantallaModal("/solicitudes/SolicitudListaReparaciones.fxml","Listado de Solicitudes de Reparación", Context.getInstance().getStage());
			if (Context.getInstance().getReparacion() != null) {
				recuperarDatos(Context.getInstance().getReparacion());
				inspeccionRepSeleccionado = Context.getInstance().getReparacion();
				Context.getInstance().setReparacion(null);
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	void recuperarDatos(SolInspeccionRep inspRep) {
		try {			
			txtCodigo.setText(String.valueOf(inspRep.getIdSolicitudRep()));
			txtCodigoMedidor.setText(inspRep.getCuentaCliente().getMedidor().getCodigo());
			txtFecha.setText(String.valueOf(inspRep.getFecha()));
			//falta Campo Referencia
			//txtReferencia.setText(inspRep.getReferencia);
			txtLatitud.setText(inspRep.getCuentaCliente().getLatitud());
			txtLongitud.setText(inspRep.getCuentaCliente().getLongitud());
			txtCedula.setText(inspRep.getCuentaCliente().getCliente().getCedula());
			//falta Campo telefono de contacto
			//txtContacto.setText(inspRep.getCuentaCliente().getC);
			txtDireccion.setText(inspRep.getCuentaCliente().getDireccion());
			txtNombres.setText(inspRep.getCuentaCliente().getCliente().getNombre() + " " + inspRep.getCuentaCliente().getCliente().getApellido());
			txtNovedadesReportadas.setText(inspRep.getObservacion());
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void buscarRubro() {
		try{
			helper.abrirPantallaModal("/bodega/ListadoRubros.fxml","Listado de Rubros", Context.getInstance().getStage());
			if (Context.getInstance().getRubros() != null) {
				rubroSeleccionado = Context.getInstance().getRubros();
				llenarDatosRubro(rubroSeleccionado);
				Context.getInstance().setRubros(null);
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	void llenarDatosRubro(Rubro datoSeleccionado){
		try {
			if(datoSeleccionado.getCodigo() == null)
				txtCodigoMat.setText("");
			else
				txtCodigoMat.setText(datoSeleccionado.getCodigo());
			
			if(datoSeleccionado.getDescripcion() == null)
				txtDescripcionProd.setText("");
			else
				txtDescripcionProd.setText(datoSeleccionado.getDescripcion());

			if(datoSeleccionado.getStock() == null)
				txtStock.setText("");
			else
				txtStock.setText(String.valueOf(datoSeleccionado.getStock()));

			txtPrecio.setText(String.valueOf(datoSeleccionado.getPrecio()));
			txtCantidad.requestFocus();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	boolean validarAnadirRubro() {
		try {
			if(txtCantidad.getText().equals("")) {
				helper.mostrarAlertaError("Ingresar Cantidad", Context.getInstance().getStage());
				//helper.mostrarAlertaAdvertencia("Ingresar Cantidad", Context.getInstance().getStage());
				txtCantidad.requestFocus();
				return false;
			}
			return true;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		} 
	}
	
	@SuppressWarnings("unchecked")
	public void agregarMaterial() {
		try {
			if(validarAnadirRubro() == false)
				return;
			ObservableList<ReparacionDetalle> datos = tvDatosDetalle.getItems();
			tvDatosDetalle.getColumns().clear();
			ReparacionDetalle datoAnadir = new ReparacionDetalle();
			rubroSeleccionado.setPrecio(Double.parseDouble(txtPrecio.getText()));
			datoAnadir.setRubro(rubroSeleccionado);
			datoAnadir.setCantidad(Integer.parseInt(txtCantidad.getText()));
			datoAnadir.setPrecio(Double.parseDouble(txtPrecio.getText()));
			datoAnadir.setSubtotal((Integer.parseInt(txtCantidad.getText()) * Double.parseDouble(txtPrecio.getText())));
			datos.add(datoAnadir);

			//llenar los datos en la tabla			
			TableColumn<ReparacionDetalle, String> descipcionColum = new TableColumn<>("Descripción");
			descipcionColum.setMinWidth(10);
			descipcionColum.setPrefWidth(200);
			descipcionColum.setCellValueFactory(new PropertyValueFactory<ReparacionDetalle, String>("rubro"));

			TableColumn<ReparacionDetalle, String> cantidadColum = new TableColumn<>("Cantidad");
			cantidadColum.setMinWidth(10);
			cantidadColum.setPrefWidth(90);
			cantidadColum.setCellValueFactory(new PropertyValueFactory<ReparacionDetalle, String>("cantidad"));

			TableColumn<ReparacionDetalle, String> precioColum = new TableColumn<>("Precio");
			precioColum.setMinWidth(10);
			precioColum.setPrefWidth(90);
			precioColum.setCellValueFactory(new PropertyValueFactory<ReparacionDetalle, String>("precio"));

			TableColumn<ReparacionDetalle, String> totalColum = new TableColumn<>("Total");
			totalColum.setMinWidth(10);
			totalColum.setPrefWidth(90);
			totalColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ReparacionDetalle, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<ReparacionDetalle, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCantidad()*param.getValue().getPrecio()));
				}
			});

			tvDatosDetalle.getColumns().addAll(descipcionColum, cantidadColum, precioColum, totalColum);
			tvDatosDetalle.setItems(datos);

			//sumarDatos();

			rubroSeleccionado = null;
			limpiarTextosProd();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	void limpiarTextosProd() {
		txtCodigo.setText("");
		txtDescripcionProd.setText("");
		txtCantidad.setText("");
		txtPrecio.setText("");
		txtStock.setText("");	
	}
	
	public void quitarMaterial() {
		try {
			ReparacionDetalle detalleSeleccionado = tvDatosDetalle.getSelectionModel().getSelectedItem();
			tvDatosDetalle.getItems().remove(detalleSeleccionado);
			//sumarDatos();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}