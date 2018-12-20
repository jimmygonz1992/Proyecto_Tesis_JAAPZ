package ec.com.jaapz.controlador;

import java.sql.Timestamp;
import java.time.LocalDate;
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
import ec.com.jaapz.modelo.LiquidacionOrdenDAO;
import ec.com.jaapz.modelo.Rubro;
import ec.com.jaapz.modelo.RubroDAO;
import ec.com.jaapz.modelo.SegUsuario;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class BodegaSalidaRubrosC {
	@FXML private TextField txtInspeccion;
	@FXML private TextField txtIdLiquidacion;
	@FXML private DatePicker dtpFecha;
	@FXML private TextField txtUsuario;
	@FXML private CheckBox chkHabilitar;
	@FXML private Button btnBuscar;
	@FXML private TextField txtCodigoMat;
	@FXML private TextField txtDescripcionMat;
	@FXML private TextField txtStockMat;
	@FXML private TextField txtPrecioMat;
	@FXML private TextField txtCantidadMat;
	@FXML private TextField txtBuscar;
	@FXML private TextField txtSubtotal;
	@FXML private TextField txtDescuento;
	@FXML private TextField txtTotal;
	@FXML private Button btnAñadir;
	@FXML private Button btnEliminar;
	@FXML private Button btnGrabar;
	@FXML private Button btnNuevo;
	@FXML private TableView<InstalacionDetalle> tvDatos;
	@FXML private TableView<LiquidacionOrden> tvDatosCli;
	
	ControllerHelper helper = new ControllerHelper();
	Rubro rubroSeleccionado = new Rubro();
	SegUsuario usuarioLogueado = new SegUsuario();
	InstalacionDAO instalacionDao = new InstalacionDAO();
	LiquidacionOrdenDAO liquidacionOrdenDao = new LiquidacionOrdenDAO();
	RubroDAO rubroDAO = new RubroDAO();
	
	public void initialize(){
		try {		
			//LiquidacionOrden liquid = new LiquidacionOrden();
			Encriptado encriptado = new Encriptado();
			usuarioLogueado = Context.getInstance().getUsuariosC();
			txtUsuario.setText(encriptado.Desencriptar(String.valueOf(Context.getInstance().getUsuariosC().getUsuario())));
			txtUsuario.setEditable(false);
			txtCodigoMat.setEditable(false);
			txtDescripcionMat.setEditable(false);
			txtPrecioMat.setEditable(false);
			txtStockMat.setEditable(false);
			dtpFecha.setValue(LocalDate.now());
			llenarDatos("");
			deshabilitar();
			tvDatosCli.setRowFactory(tv -> {
	            TableRow<LiquidacionOrden> row = new TableRow<>();
	            row.setOnMouseClicked(event -> {
	                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
	                	if(tvDatosCli.getSelectionModel().getSelectedItem() != null){
	                		LiquidacionOrden liq = tvDatosCli.getSelectionModel().getSelectedItem();
	                		recuperarDetalleLiquidacion(liq);
	    				}
	                }
	            });
	            return row ;
	        });
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void recuperarDetalleLiquidacion(LiquidacionOrden liq) {
		List<InstalacionDetalle> detalle = new ArrayList<InstalacionDetalle>();
		ObservableList<InstalacionDetalle> datos = FXCollections.observableArrayList();
		txtInspeccion.setText(String.valueOf(liq.getSolInspeccionIn().getIdSolInspeccion()));
		txtIdLiquidacion.setText(String.valueOf(liq.getIdLiquidacion()));
		tvDatos.getColumns().clear();
		tvDatos.getItems().clear();
		for(LiquidacionDetalle detallePrevia : liq.getLiquidacionDetalles()) {
			InstalacionDetalle detAdd = new InstalacionDetalle();
			detAdd.setRubro(detallePrevia.getRubro());
			detAdd.setCantidad(detallePrevia.getCantidad());
			detAdd.setPrecio(detallePrevia.getPrecio());
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
		
		sumarDatos();
	}
	
	public void buscarCliente() {
		llenarDatos(txtBuscar.getText());
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
					txtSubtotal.setText(String.valueOf(Double.valueOf(subtotal)));
				}
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	void llenarDatos(String patron) {
		try{
			tvDatosCli.getColumns().clear();
			List<LiquidacionOrden> listaLiquidaciones;
			if(Context.getInstance().getIdPerfil() == 1) {
				listaLiquidaciones = liquidacionOrdenDao.getListaLiquidacionOrden(patron);
			}else {
				listaLiquidaciones = liquidacionOrdenDao.getListaLiquidacionOrdenPerfil(patron);
			}
			
			ObservableList<LiquidacionOrden> datosReq = FXCollections.observableArrayList();
			datosReq.setAll(listaLiquidaciones);

			//llenar los datos en la tabla
			TableColumn<LiquidacionOrden, String> idColum = new TableColumn<>("Nº");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(40);
			idColum.setCellValueFactory(new PropertyValueFactory<LiquidacionOrden, String>("idLiquidacion"));
			
			TableColumn<LiquidacionOrden, String> ordenColum = new TableColumn<>("Inspección");
			ordenColum.setMinWidth(10);
			ordenColum.setPrefWidth(40);
			ordenColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiquidacionOrden, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<LiquidacionOrden, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getSolInspeccionIn().getIdSolInspeccion()));
				}
			});

			TableColumn<LiquidacionOrden, String> fechaOrdenColum = new TableColumn<>("Fecha de emisión");
			fechaOrdenColum.setMinWidth(10);
			fechaOrdenColum.setPrefWidth(80);
			fechaOrdenColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiquidacionOrden, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<LiquidacionOrden, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getFecha()));
				}
			});
			
			TableColumn<LiquidacionOrden, String> fechaInspColum = new TableColumn<>("Fecha de Inspección");
			fechaInspColum.setMinWidth(10);
			fechaInspColum.setPrefWidth(80);
			fechaInspColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiquidacionOrden, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<LiquidacionOrden, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getSolInspeccionIn().getFechaIngreso()));
				}
			});
			
			TableColumn<LiquidacionOrden, String> cedulaColum = new TableColumn<>("Cédula");
			cedulaColum.setMinWidth(10);
			cedulaColum.setPrefWidth(80);
			cedulaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiquidacionOrden, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<LiquidacionOrden, String> param) {
					//return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getJunOrdenPreviaDespacho().getJunInspeccionNuevoCliente().getFecha()));
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCuentaCliente().getCliente().getCedula()));
				}
			});
			
			TableColumn<LiquidacionOrden, String> clienteColum = new TableColumn<>("Cliente");
			clienteColum.setMinWidth(10);
			clienteColum.setPrefWidth(240);
			clienteColum.setCellValueFactory(new PropertyValueFactory<LiquidacionOrden, String>("cuentaCliente"));
			
			TableColumn<LiquidacionOrden, String> direccionColum = new TableColumn<>("Dirección");
			direccionColum.setMinWidth(10);
			direccionColum.setPrefWidth(80);
			direccionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiquidacionOrden, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<LiquidacionOrden, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCuentaCliente().getDireccion()));
				}
			});
			
			TableColumn<LiquidacionOrden, String> referenciaColum = new TableColumn<>("Referencia");
			referenciaColum.setMinWidth(10);
			referenciaColum.setPrefWidth(80);
			referenciaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiquidacionOrden, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<LiquidacionOrden, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getSolInspeccionIn().getReferencia()));
				}
			});
			
			TableColumn<LiquidacionOrden, String> estadoInspColum = new TableColumn<>("Estado Inspección");
			estadoInspColum.setMinWidth(10);
			estadoInspColum.setPrefWidth(80);
			estadoInspColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiquidacionOrden, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<LiquidacionOrden, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getSolInspeccionIn().getEstadoInspeccion()));
				}
			});

			TableColumn<LiquidacionOrden, String> estadoOrdColum = new TableColumn<>("Estado Liquidacion");
			estadoOrdColum.setMinWidth(10);
			estadoOrdColum.setPrefWidth(85);
			estadoOrdColum.setCellValueFactory(new PropertyValueFactory<LiquidacionOrden, String>("estadoOrden"));
			
			tvDatosCli.getColumns().addAll(idColum, ordenColum, fechaOrdenColum, fechaInspColum, cedulaColum, clienteColum, direccionColum, referenciaColum, estadoInspColum, estadoOrdColum);
			tvDatosCli.setItems(datosReq);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	public void habilitarIngreso() {
		try {
			if (chkHabilitar.isSelected()) {
				habilitar();
			}else {
				deshabilitar();
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	} 
	
	void habilitar() {
		btnBuscar.setDisable(false);
		txtCodigoMat.setDisable(false);
		txtDescripcionMat.setDisable(false);
		txtStockMat.setDisable(false);
		txtPrecioMat.setDisable(false);
		txtCantidadMat.setDisable(false);
		btnAñadir.setDisable(false);
	}
	
	void deshabilitar() {
		btnBuscar.setDisable(true);
		txtCodigoMat.setDisable(true);
		txtDescripcionMat.setDisable(true);
		txtStockMat.setDisable(true);
		txtPrecioMat.setDisable(true);
		txtCantidadMat.setDisable(true);
		btnAñadir.setDisable(true);
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
			txtCodigoMat.setText(String.valueOf(datoSeleccionado.getIdRubro()));
			if(datoSeleccionado.getDescripcion() == null)
				txtDescripcionMat.setText("");
			else
				txtDescripcionMat.setText(datoSeleccionado.getDescripcion());

			if(datoSeleccionado.getStock() == null)
				txtStockMat.setText("");
			else
				txtStockMat.setText(String.valueOf(datoSeleccionado.getStock()));

			txtPrecioMat.setText(String.valueOf(datoSeleccionado.getPrecio()));
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
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
				
				LiquidacionOrden liqSeleccionado = new LiquidacionOrden();
				liqSeleccionado = tvDatosCli.getSelectionModel().getSelectedItem();
				liqSeleccionado.setEstadoOrden("REALIZADO");
				
				instalacion.setIdInstalacion(null);
				Date date = Date.from(dtpFecha.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
				Timestamp fecha = new Timestamp(date.getTime());
				instalacion.setFechaInst(fecha);
				instalacion.setUsuarioCrea(Context.getInstance().getUsuariosC().getIdUsuario());
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
				instalacionDao.getEntityManager().merge(liqSeleccionado);
				//junOrdenPreviaDespachoDao.getEntityManager().merge(actualizaOrdenEstado);
				instalacionDao.getEntityManager().getTransaction().commit();
					
				actualizarListaArticulos();
				
				helper.mostrarAlertaInformacion("Datos Grabados Correctamente", Context.getInstance().getStage());
				limpiar();
				dtpFecha.setValue(null);
				tvDatos.getColumns().clear();
				tvDatos.getItems().clear();
				llenarDatos("");
			}
		}catch(Exception ex) {
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
			System.out.println(ex.getMessage());
		}
	}
	
	boolean validarDatos() {
		try {
			if(txtInspeccion.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar Nº Inspección", Context.getInstance().getStage());
				txtInspeccion.requestFocus();
				return false;
			}
			
			if(txtIdLiquidacion.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar Nº Liquidación", Context.getInstance().getStage());
				txtIdLiquidacion.requestFocus();
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
	
	boolean validarAñadirRubro() {
		try {
			if(txtCantidadMat.getText().equals("")) {
				helper.mostrarAlertaError("Ingresar Cantidad", Context.getInstance().getStage());
				//helper.mostrarAlertaAdvertencia("Ingresar Cantidad", Context.getInstance().getStage());
				txtCantidadMat.requestFocus();
				return false;
			}
			return true;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		} 
	}
	
	@SuppressWarnings("unchecked")
	public void añadir() {
		try {
			if(validarAñadirRubro() == false)
				return;
			ObservableList<InstalacionDetalle> datos = tvDatos.getItems();
			tvDatos.getColumns().clear();
			InstalacionDetalle datoAnadir = new InstalacionDetalle();
			rubroSeleccionado.setPrecio(Double.parseDouble(txtPrecioMat.getText()));
			datoAnadir.setRubro(rubroSeleccionado);
			datoAnadir.setCantidad(Integer.parseInt(txtCantidadMat.getText()));
			datoAnadir.setPrecio(Double.parseDouble(txtPrecioMat.getText()));
			datos.add(datoAnadir);

			//llenar los datos en la tabla			
			TableColumn<InstalacionDetalle, String> descripcionColum = new TableColumn<>("Descripción");
			descripcionColum.setMinWidth(10);
			descripcionColum.setPrefWidth(200);
			descripcionColum.setCellValueFactory(new PropertyValueFactory<InstalacionDetalle, String>("rubro"));

			TableColumn<InstalacionDetalle, String> cantidadColum = new TableColumn<>("Cantidad");
			cantidadColum.setMinWidth(10);
			cantidadColum.setPrefWidth(90);
			cantidadColum.setCellValueFactory(new PropertyValueFactory<InstalacionDetalle, String>("cantidad"));

			TableColumn<InstalacionDetalle, String> precioColum = new TableColumn<>("Precio");
			precioColum.setMinWidth(10);
			precioColum.setPrefWidth(90);
			precioColum.setCellValueFactory(new PropertyValueFactory<InstalacionDetalle, String>("precio"));
			
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
			rubroSeleccionado = null;
			limpiar();
		}catch(Exception ex) {
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
	
	public void nuevo() {
		limpiar();
		tvDatos.getColumns().clear();
		tvDatos.getItems().clear();
		deshabilitar();
	}
	
	void limpiar() {
		//txtNumOrden.setText("");
		//txtCodRequer.setText("");
		txtCodigoMat.setText("");
		txtDescripcionMat.setText("");
		txtCantidadMat.setText("");
		txtPrecioMat.setText("");
		txtStockMat.setText("");
	}
}