package ec.com.jaapz.controlador;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import ec.com.jaapz.modelo.Instalacion;
import ec.com.jaapz.modelo.InstalacionDAO;
import ec.com.jaapz.modelo.InstalacionDetalle;
import ec.com.jaapz.modelo.LiquidacionDetalle;
import ec.com.jaapz.modelo.LiquidacionOrden;
import ec.com.jaapz.modelo.Rubro;
import ec.com.jaapz.modelo.RubroDAO;
import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import ec.com.jaapz.util.Encriptado;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class BodegaSalidaRubroInstC {
	@FXML private TextField txtCedula;
	@FXML private TextField txtIdCuenta;
	@FXML private TextField txtNombres;
	@FXML private TextField txtApellidos;
	@FXML private TextField txtDireccion;
	@FXML private TextField txtTelefono;
	@FXML private TextField txtIdLiquid;
	@FXML private DatePicker dtpFecha;
	@FXML private TextField txtUsuario;
	
	@FXML private TextField txtCodigoMedidor;
	@FXML private TextField txtMarca;
	@FXML private TextField txtModelo;
	@FXML private TextField txtPrecioMed;
	
	@FXML private TextField txtSubtotal;
	@FXML private TextField txtDescuento;
	@FXML private TextField txtTotal;
	@FXML private TextArea txtObservaciones;
	@FXML private TextField txtEstadoValorInst;
	
	@FXML private Button btnBuscarLiquidCuenta;
	@FXML private Button btnBuscarLiquidacion;
	@FXML private Button btnEliminar;
	@FXML private Button btnGrabar;
	@FXML private Button btnNuevo;
	
	@FXML private TableView<InstalacionDetalle> tvDatos;
	ControllerHelper helper = new ControllerHelper();
	InstalacionDAO instalacionDao = new InstalacionDAO();
	RubroDAO rubroDAO = new RubroDAO();	
	LiquidacionOrden liquidacionSeleccionada = new LiquidacionOrden();
	
	public void initialize() {
		try {
			Encriptado encriptado = new Encriptado();
			txtUsuario.setText(encriptado.Desencriptar(String.valueOf(Context.getInstance().getUsuariosC().getUsuario())));
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void buscarLiqCuenta() {
		try{
			helper.abrirPantallaModal("/bodega/ListadoLiquidaciones.fxml","Listado de Rubros", Context.getInstance().getStage());
			if (Context.getInstance().getLiquidaciones() != null) {
				liquidacionSeleccionada = Context.getInstance().getLiquidaciones();
				llenarDatosLiquidacion(liquidacionSeleccionada);
				Context.getInstance().setLiquidaciones(null);;
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	void llenarDatosLiquidacion(LiquidacionOrden datoSeleccionado){
		try {
			//txtCodigoMat.setText(String.valueOf(datoSeleccionado.getIdRubro()));
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
			
			if(datoSeleccionado.getCuentaCliente().getCliente().getTelefono() == null)
				txtTelefono.setText("");
			else
				txtTelefono.setText(datoSeleccionado.getCuentaCliente().getCliente().getTelefono());
			
			if(datoSeleccionado.getIdLiquidacion() == null)
				txtIdLiquid.setText("");
			else
				txtIdLiquid.setText(String.valueOf(datoSeleccionado.getIdLiquidacion()));
			
			if(datoSeleccionado.getMedidor().getCodigo() == null)
				txtCodigoMedidor.setText("");
			else
				txtCodigoMedidor.setText(String.valueOf(datoSeleccionado.getMedidor().getCodigo()));
			
			if(datoSeleccionado.getMedidor().getMarca() == null)
				txtMarca.setText("");
			else
				txtMarca.setText(String.valueOf(datoSeleccionado.getMedidor().getMarca()));
			
			if(datoSeleccionado.getMedidor().getModelo() == null)
				txtModelo.setText("");
			else
				txtModelo.setText(String.valueOf(datoSeleccionado.getMedidor().getModelo()));
			
			if(datoSeleccionado.getMedidor().getPrecio() == 0.0)
				txtPrecioMed.setText("");
			else
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
		TableColumn<InstalacionDetalle, String> descripcionColum = new TableColumn<>("Descripci�n");
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
		
		sumarDatos();
	}
	
	public void sumarDatos() {
		try {
			if (tvDatos.getItems().isEmpty()) {
				txtSubtotal.setText("0.0");
				txtTotal.setText("0.0");
			}else {
				double subtotal = 0;
				for(int i=0; i<tvDatos.getItems().size(); i++) {
					//DecimalFormat df = new DecimalFormat ("########.00");
					Double valorSubt = new Double(tvDatos.getItems().get(i).getCantidad()*tvDatos.getItems().get(i).getPrecio());
					subtotal += valorSubt;
					//txtSubtotal.setText(String.valueOf(Double.valueOf(subtotal)));
					txtTotal.setText(String.valueOf(Double.valueOf(subtotal)));
				}
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void buscarLiquidacion() {
		try{
			helper.abrirPantallaModal("/bodega/ListadoLiquidaciones.fxml","Listado de Rubros", Context.getInstance().getStage());
			if (Context.getInstance().getLiquidaciones() != null) {
				liquidacionSeleccionada = Context.getInstance().getLiquidaciones();
				llenarDatosLiquidacion(liquidacionSeleccionada);
				Context.getInstance().setLiquidaciones(null);;
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	public void eliminar() {
		try {
			InstalacionDetalle detalleSeleccionado = tvDatos.getSelectionModel().getSelectedItem();
			tvDatos.getItems().remove(detalleSeleccionado);
			sumarDatos();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	boolean validarDatos() {
		try {
			if(txtCedula.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar C�dula de Cliente", Context.getInstance().getStage());
				txtCedula.requestFocus();
				return false;
			}
			
			if(txtNombres.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar Nombre de Cliente", Context.getInstance().getStage());
				txtNombres.requestFocus();
				return false;
			}
			
			if(txtApellidos.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar Apellido de Cliente", Context.getInstance().getStage());
				txtApellidos.requestFocus();
				return false;
			}
			
			if(txtDireccion.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar Direcci�n de Cliente", Context.getInstance().getStage());
				txtDireccion.requestFocus();
				return false;
			}
			
			if(txtTelefono.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar Tel�fono de Cliente", Context.getInstance().getStage());
				txtTelefono.requestFocus();
				return false;
			}
			
			if(txtIdLiquid.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar ID de Liquidaci�n", Context.getInstance().getStage());
				txtIdLiquid.requestFocus();
				return false;
			}
			
			if(txtCodigoMedidor.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar C�digo de Medidor", Context.getInstance().getStage());
				txtCodigoMedidor.requestFocus();
				return false;
			}
			
			if(txtTotal.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar Valor Total de Instalaci�n", Context.getInstance().getStage());
				txtTotal.requestFocus();
				return false;
			}
						
			if(dtpFecha.getValue().equals(null)) {
				helper.mostrarAlertaAdvertencia("Ingresar Fecha", Context.getInstance().getStage());
				dtpFecha.requestFocus();
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
	
	public void grabar() {
		try {
			if(validarDatos() == false)
				return;
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				Instalacion instalacion = new Instalacion();
				String estado = "A";				
				
				//LiquidacionOrden liqSeleccionado = new LiquidacionOrden();
				//liqSeleccionado = tvDatos.getSelectionModel().getSelectedItem();
				liquidacionSeleccionada.setEstadoOrden(Constantes.EST_APERTURA_REALIZADO);
				//liqSeleccionado.setEstadoOrden("REALIZADO");			
				
				instalacion.setIdInstalacion(null);
				Date date = Date.from(dtpFecha.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
				//Timestamp fecha = new Timestamp(date.getTime());
				instalacion.setCuentaCliente(liquidacionSeleccionada.getCuentaCliente());
				instalacion.setSolInspeccionIn(liquidacionSeleccionada.getSolInspeccionIn());
				instalacion.setFechaInst(date);
				instalacion.setTotal(Double.parseDouble(txtTotal.getText()));
				instalacion.setEstadoInstalacion(Constantes.EST_INSPECCION_PENDIENTE);
				instalacion.setUsuarioCrea(Context.getInstance().getUsuariosC().getIdUsuario());
				instalacion.setEstadoValor(txtEstadoValorInst.getText());
				instalacion.setEstado(estado);
				List<InstalacionDetalle> listaAgregadaRubros = new ArrayList<InstalacionDetalle>();
				for(InstalacionDetalle det : tvDatos.getItems()) {
					det.setIdInstalacionDet(null);
					det.setUsuarioCrea(Context.getInstance().getUsuariosC().getIdUsuario());
					det.setEstado("A");
					det.setInstalacion(instalacion);
					listaAgregadaRubros.add(det);
				}
				
				instalacion.setInstalacionDetalles(listaAgregadaRubros);
				instalacionDao.getEntityManager().getTransaction().begin();
				instalacionDao.getEntityManager().persist(instalacion);
				instalacionDao.getEntityManager().merge(liquidacionSeleccionada);
				//junOrdenPreviaDespachoDao.getEntityManager().merge(actualizaOrdenEstado);
				instalacionDao.getEntityManager().getTransaction().commit();
					
				actualizarListaArticulos();
				
				helper.mostrarAlertaInformacion("Datos Grabados Correctamente", Context.getInstance().getStage());
				nuevo();
				dtpFecha.setValue(null);
				tvDatos.getColumns().clear();
				tvDatos.getItems().clear();
			}
		}catch(Exception ex) {
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
			System.out.println(ex.getMessage());
		}
	}
	
	public void actualizarListaArticulos() {
		if(tvDatos != null) {		
			List<Rubro> listaSalidaRubros = new ArrayList<Rubro>();
			for(InstalacionDetalle detalle: tvDatos.getItems()) {
				listaSalidaRubros.add(detalle.getRubro());
			}
			rubroDAO.getEntityManager().getTransaction().begin();
			for (Rubro rubro : listaSalidaRubros) {
				for(InstalacionDetalle detalle : tvDatos.getItems()) {
					if(rubro.getIdRubro() == detalle.getRubro().getIdRubro())
						rubro.setStock(rubro.getStock() - detalle.getCantidad());
				}
				rubroDAO.getEntityManager().merge(rubro);
			}
			rubroDAO.getEntityManager().getTransaction().commit();
		}
	}
	
	public void nuevo() {
		txtCedula.setText("");
		txtIdCuenta.setText("");
		txtNombres.setText("");
		txtApellidos.setText("");
		txtDireccion.setText("");
		txtTelefono.setText("");
		txtIdLiquid.setText("");
		dtpFecha.setValue(null);
		txtCodigoMedidor.setText("");
		txtMarca.setText("");
		txtModelo.setText("");
		txtPrecioMed.setText("");
		txtTotal.setText("");
		txtObservaciones.setText("");
		tvDatos.getColumns().clear();
		tvDatos.getItems().clear();
	}
}