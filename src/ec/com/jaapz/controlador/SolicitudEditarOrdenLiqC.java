package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ec.com.jaapz.modelo.LiquidacionDetalle;
import ec.com.jaapz.modelo.LiquidacionOrden;
import ec.com.jaapz.modelo.LiquidacionOrdenDAO;
import ec.com.jaapz.modelo.Rubro;
import ec.com.jaapz.modelo.RubroDAO;
import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

public class SolicitudEditarOrdenLiqC {
	@FXML private TextField txtCedula;
	@FXML private TextField txtIdLiquidacion;
	@FXML private TextField	txtFechaEmision;
	@FXML private TextField txtIdInspeccion;
	@FXML private TextField txtFechaInspeccion;
	@FXML private TextField	txtNombres;
	@FXML private TextField	txtApellidos;
	@FXML private TextField	txtTelefono;
	@FXML private TextField	txtDireccion;
	@FXML private TextField	txtReferencia;
	@FXML private TextField	txtCodigoMat;
	@FXML private TextArea	txtDescripcionMat;
	@FXML private TextField	txtStockMat;
	@FXML private TextField txtPrecioMat;
	@FXML private TextField	txtCantidadMat;
	@FXML private TextArea	txtObservaciones;
	@FXML private TextField	txtSubtotal;
	@FXML private TextField txtDescuento;
	@FXML private TextField	txtTotal;
	
	@FXML private Button btnBuscarOrdenLiqEmitida;
	@FXML private Button btnBuscarRubro;
	@FXML private Button btnAniadir;
	@FXML private Button btnEliminar;
	@FXML private Button btnGrabar;
	@FXML private Button btnNuevo;
	
	@FXML private TableView<LiquidacionDetalle> tvDatos;
	ControllerHelper helper = new ControllerHelper();
	LiquidacionOrden liquidacionSeleccionada = new LiquidacionOrden();
	Rubro rubroSeleccionado = new Rubro();
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	RubroDAO rubroDAO = new RubroDAO();
	LiquidacionOrdenDAO liquidacionDao =  new LiquidacionOrdenDAO();
	
	public void initialize() {
		btnAniadir.setStyle("-fx-cursor: hand;");
		btnBuscarOrdenLiqEmitida.setStyle("-fx-cursor: hand;");
		btnBuscarRubro.setStyle("-fx-cursor: hand;");
		btnEliminar.setStyle("-fx-cursor: hand;");
		btnGrabar.setStyle("-fx-cursor: hand;");
		btnNuevo.setStyle("-fx-cursor: hand;");
		
		
		bloquear();
		//recuperar Material
		txtCodigoMat.setOnKeyPressed(new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent ke){
				if (ke.getCode().equals(KeyCode.ENTER)){
					if (validarProductoExiste() == false) {
						helper.mostrarAlertaAdvertencia("Elemento no existente!", Context.getInstance().getStage());
						txtCodigoMat.requestFocus();
						limpiarMateriales();
					}else {
						recuperarDatosMat(txtCodigoMat.getText());
						txtCantidadMat.requestFocus();
					}
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
		
		//solo letras mayusculas
		txtCodigoMat.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				String cadena = txtCodigoMat.getText().toUpperCase();
				txtCodigoMat.setText(cadena);
			}
		});
		
		//para anadir a la grilla con enter
		txtCantidadMat.setOnKeyPressed(new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent ke){
				if (ke.getCode().equals(KeyCode.ENTER)){
					anadir();
					limpiarMateriales();
					btnBuscarRubro.requestFocus();
				}
			}
		});
		
		//validar solo numeros
		txtCantidadMat.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (newValue.matches("\\d*")) {
					//int value = Integer.parseInt(newValue);
				} else {
					txtCantidadMat.setText(oldValue);
				}
			}
		});
	}
	
	void limpiarMateriales() {
		txtCodigoMat.setText("");
		txtDescripcionMat.setText("");
		txtPrecioMat.setText("");
		txtStockMat.setText("");
		txtCantidadMat.setText("");
	}
	
	//recupera datos del material
	public void recuperarDatosMat(String codigo){
		try{
			List<Rubro> listaRubro = new ArrayList<Rubro>();
			listaRubro = rubroDAO.getRecuperaRubro(codigo);
			for(int i = 0 ; i < listaRubro.size() ; i ++) {
				txtCodigoMat.setText(listaRubro.get(i).getCodigo());
				txtDescripcionMat.setText(listaRubro.get(i).getDescripcion());
				txtStockMat.setText(String.valueOf(listaRubro.get(i).getStock()));
				txtPrecioMat.setText(String.valueOf(listaRubro.get(i).getPrecio()));
							
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
			listaRubros = rubroDAO.getRecuperaRubro(txtCodigoMat.getText());
			if(listaRubros.size() != 0)
				return true;
			else
				return false;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
	void bloquear() {
		txtCedula.setEditable(false);
		txtIdLiquidacion.setEditable(false);
		txtIdInspeccion.setEditable(false);
		txtFechaEmision.setEditable(false);
		txtFechaInspeccion.setEditable(false);
		txtNombres.setEditable(false);
		txtApellidos.setEditable(false);
		txtTelefono.setEditable(false);
		txtDireccion.setEditable(false);
		txtReferencia.setEditable(false);
		txtDescripcionMat.setEditable(false);
		txtStockMat.setEditable(false);
		txtPrecioMat.setEditable(false);
		txtSubtotal.setEditable(false);
		txtDescuento.setEditable(false);
		txtTotal.setEditable(false);
	}
	
	public void buscarRubro(){
		try{
			helper.abrirPantallaModal("/bodega/ListadoRubros.fxml","Listado de Rubros", Context.getInstance().getStage());
			if (Context.getInstance().getRubros() != null) {
				rubroSeleccionado = Context.getInstance().getRubros();
				llenarDatos(rubroSeleccionado);
				Context.getInstance().setRubros(null);
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	void llenarDatos(Rubro datoSeleccionado){
		try {
			if(datoSeleccionado.getCodigo() == null)
				txtCodigoMat.setText("");
			else
				txtCodigoMat.setText(datoSeleccionado.getCodigo());
			
			if(datoSeleccionado.getDescripcion() == null)
				txtDescripcionMat.setText("");
			else
				txtDescripcionMat.setText(datoSeleccionado.getDescripcion());

			if(datoSeleccionado.getStock() == null)
				txtStockMat.setText("");
			else
				txtStockMat.setText(String.valueOf(datoSeleccionado.getStock()));

			txtPrecioMat.setText(String.valueOf(datoSeleccionado.getPrecio()));
			txtCantidadMat.requestFocus();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public void anadir(){
		try {
			if(validarAnadirRubro() == false)
				return;
			ObservableList<LiquidacionDetalle> datos = tvDatos.getItems();
			tvDatos.getColumns().clear();
			LiquidacionDetalle datoAnadir = new LiquidacionDetalle();
			rubroSeleccionado.setPrecio(Double.parseDouble(txtPrecioMat.getText()));
			datoAnadir.setRubro(rubroSeleccionado);
			datoAnadir.setCantidad(Integer.parseInt(txtCantidadMat.getText()));
			datoAnadir.setPrecio(Double.parseDouble(txtPrecioMat.getText()));
			datoAnadir.setTotal((Integer.parseInt(txtCantidadMat.getText()) * Double.parseDouble(txtPrecioMat.getText())));
			datos.add(datoAnadir);

			//llenar los datos en la tabla			
			TableColumn<LiquidacionDetalle, String> descripcionColum = new TableColumn<>("Descripción");
			descripcionColum.setMinWidth(10);
			descripcionColum.setPrefWidth(200);
			descripcionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiquidacionDetalle, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<LiquidacionDetalle, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getRubro().getDescripcion());
				}
			});
			
			TableColumn<LiquidacionDetalle, String> cantidadColum = new TableColumn<>("Cantidad");
			cantidadColum.setMinWidth(10);
			cantidadColum.setPrefWidth(90);
			cantidadColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiquidacionDetalle, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<LiquidacionDetalle, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCantidad()));
				}
			});
			
			TableColumn<LiquidacionDetalle, String> precioColum = new TableColumn<>("Precio");
			precioColum.setMinWidth(10);
			precioColum.setPrefWidth(90);
			precioColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiquidacionDetalle, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<LiquidacionDetalle, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getPrecio()));
				}
			});

			TableColumn<LiquidacionDetalle, String> totalColum = new TableColumn<>("Total");
			totalColum.setMinWidth(10);
			totalColum.setPrefWidth(90);
			totalColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiquidacionDetalle, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<LiquidacionDetalle, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCantidad()*param.getValue().getPrecio()));
				}
			});

			tvDatos.getColumns().addAll(descripcionColum, cantidadColum, precioColum, totalColum);
			tvDatos.setItems(datos);

			sumarDatos();

			rubroSeleccionado = null;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	boolean validarAnadirRubro() {
		try {
			if(txtCantidadMat.getText().equals("")) {
				helper.mostrarAlertaError("Ingresar Cantidad", Context.getInstance().getStage());
				txtCantidadMat.requestFocus();
				return false;
			}
			return true;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		} 
	}
	
	public void sumarDatos() {
		try {
			if (tvDatos.getItems().isEmpty()) {
				txtSubtotal.setText("0.0");
				txtDescuento.setText("0.0");
				txtTotal.setText("0.0");
			}else {
				double subtotal = 0;
				for(int i=0; i<tvDatos.getItems().size(); i++) {
					Double valorSubt = new Double(tvDatos.getItems().get(i).getCantidad()*tvDatos.getItems().get(i).getPrecio());
					subtotal += valorSubt;
					txtSubtotal.setText(String.valueOf(Double.valueOf(subtotal)));
					if (txtDescuento.getText().isEmpty()) {
						txtDescuento.setText("0.0");
					}
					double total = Double.valueOf(txtSubtotal.getText()) - Double.valueOf(txtDescuento.getText());
					txtTotal.setText(String.valueOf(Double.valueOf(total)));
				}
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void eliminar(){
		try {
			LiquidacionDetalle detalleSeleccionado = tvDatos.getSelectionModel().getSelectedItem();
			tvDatos.getItems().remove(detalleSeleccionado);
			sumarDatos();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	boolean validarDatos() {
		try {
			if(txtIdLiquidacion.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Mo existe ID de liquidación", Context.getInstance().getStage());
				txtIdLiquidacion.requestFocus();
				return false;
			}

			if(txtIdInspeccion.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("No existe ID de inspección", Context.getInstance().getStage());
				txtIdInspeccion.requestFocus();
				return false;
			}
			
			if(txtFechaEmision.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("No refleja fecha de emisión de la Orden", Context.getInstance().getStage());
				txtFechaEmision.requestFocus();
				return false;
			}
			
			if(txtFechaInspeccion.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("No refleja fecha de Inspección", Context.getInstance().getStage());
				txtFechaInspeccion.requestFocus();
				return false;
			}
			
			if(txtCedula.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingrese cédula de Cliente", Context.getInstance().getStage());
				txtCedula.requestFocus();
				return false;
			}

			if(txtNombres.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar Nombres del Cliente", Context.getInstance().getStage());
				txtNombres.requestFocus();
				return false;
			}		

			if(txtApellidos.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar Apellidos del Cliente", Context.getInstance().getStage());
				txtApellidos.requestFocus();
				return false;
			}

			if(txtDireccion.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar Dirección del Cliente", Context.getInstance().getStage());
				txtDireccion.requestFocus();
				return false;
			}

			if(txtTelefono.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar Teléfono del Cliente", Context.getInstance().getStage());
				txtTelefono.requestFocus();
				return false;
			}

			if(txtReferencia.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar alguna referencia", Context.getInstance().getStage());
				txtReferencia.requestFocus();
				return false;
			}

			if(tvDatos.getItems().isEmpty()) {
				helper.mostrarAlertaAdvertencia("Ingresar Rubros", Context.getInstance().getStage());
				tvDatos.requestFocus();
				return false;
			}
			return true;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
	public void grabar(){
		try {
			if(validarDatos() == false)
				return;
		
			if(liquidacionSeleccionada == null) {
				liquidacionSeleccionada = new LiquidacionOrden();
			}

			liquidacionSeleccionada.setEstadoOrden(Constantes.EST_INSPECCION_PENDIENTE);
				
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				if (liquidacionSeleccionada.getIdLiquidacion() == null) {
					List<LiquidacionDetalle> listaAgregadaRubros = new ArrayList<LiquidacionDetalle>();
					for(LiquidacionDetalle det : tvDatos.getItems()) {
						det.setIdDetalle(null);
						det.setEstado(Constantes.ESTADO_ACTIVO);
						det.setLiquidacionOrden(liquidacionSeleccionada);
						listaAgregadaRubros.add(det);
					}
					liquidacionSeleccionada.setLiquidacionDetalles(listaAgregadaRubros);
					//empieza la transaccion
					liquidacionDao.getEntityManager().getTransaction().begin();
					
					//aqui voy a intentar guardar y tengo q preguntar si es nuevo
					//o sino solo para editar
					if(txtIdLiquidacion.getText().equals("0")) {//inserta nuevo ingreso
						liquidacionSeleccionada.setIdLiquidacion(null);
						liquidacionDao.getEntityManager().persist(liquidacionSeleccionada);
					}else {//modifica
						liquidacionSeleccionada.setIdLiquidacion(Integer.parseInt(txtIdLiquidacion.getText()));
						liquidacionDao.getEntityManager().merge(liquidacionSeleccionada);
					}
						
					//ingresoDao.getEntityManager().persist(ingreso);				
					liquidacionDao.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Datos Grabados Correctamente", Context.getInstance().getStage());
					nuevo();
					tvDatos.getColumns().clear();
					tvDatos.getItems().clear();
				}else {
					List<Integer> integer = new ArrayList<Integer>();
					for (LiquidacionDetalle detalle : tvDatos.getItems()) {
						if (detalle.getIdDetalle() != null)
							integer.add(detalle.getIdDetalle());
					}
					for(LiquidacionDetalle det : tvDatos.getItems()) {
						if(det.getIdDetalle() == null) {
							det.setIdDetalle(null);
							det.setEstado(Constantes.ESTADO_ACTIVO);
							det.setLiquidacionOrden(liquidacionSeleccionada);
							liquidacionSeleccionada.getLiquidacionDetalles().add(det);
						}else {
							for (LiquidacionDetalle deta: liquidacionSeleccionada.getLiquidacionDetalles()) {
								if (!integer.contains(deta.getIdDetalle())) {
									deta.setEstado(Constantes.ESTADO_INACTIVO);
								}
							}
						}
					}
						
					//elimina material resta stock
					if(tvDatos != null) {
						List<Integer> idActual = new ArrayList<Integer>();
						for(LiquidacionDetalle detalle : tvDatos.getItems()) {
							if(detalle.getIdDetalle() != null)
								idActual.add(detalle.getIdDetalle());
						}
							
						System.out.println("Ingresos actuales " + idActual.size());
						System.out.println("Ingresos en la bd " + liquidacionSeleccionada.getLiquidacionDetalles().size());
							
						for (LiquidacionDetalle det : liquidacionSeleccionada.getLiquidacionDetalles()) {
							if(det.getIdDetalle() != null) {
								if(!idActual.contains(det.getIdDetalle())) {
									System.out.println("Rubro a eliminar " + det.getRubro().getDescripcion());
									Rubro rubroMod = det.getRubro();
									rubroMod.setStock(rubroMod.getStock() - det.getCantidad());
									System.out.println("Elimnia rubro " + rubroMod.getDescripcion());
									rubroDAO.getEntityManager().getTransaction().begin();
									rubroDAO.getEntityManager().merge(rubroMod);
									rubroDAO.getEntityManager().getTransaction().commit();
								}	
							}
						}
					}
					//sumar
					if(tvDatos != null) {
						List<Rubro> listaAgregadaRubros = new ArrayList<Rubro>();
						for(LiquidacionDetalle detalle: tvDatos.getItems()) {
							if(detalle.getIdDetalle() == null)
								listaAgregadaRubros.add(detalle.getRubro());
						}
						for (Rubro rubro : listaAgregadaRubros) {
							for(LiquidacionDetalle detalle : tvDatos.getItems()) {
								if(rubro.getIdRubro() == detalle.getRubro().getIdRubro())
									rubro.setStock(rubro.getStock() + detalle.getCantidad());
							}
							rubroDAO.getEntityManager().getTransaction().begin();
							rubroDAO.getEntityManager().merge(rubro);
							rubroDAO.getEntityManager().getTransaction().commit();
						}
					}
					liquidacionDao.getEntityManager().getTransaction().begin();
					liquidacionDao.getEntityManager().merge(liquidacionSeleccionada);
					liquidacionDao.getEntityManager().getTransaction().commit();
						
					helper.mostrarAlertaInformacion("Datos grabados Correctamente", Context.getInstance().getStage());
					nuevo();
					tvDatos.getColumns().clear();
					tvDatos.getItems().clear();
				}
			}
			liquidacionSeleccionada = null;
		}catch(Exception ex) {
			liquidacionDao.getEntityManager().getTransaction().rollback();
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
			System.out.println(ex.getMessage());
		}
	}
	
	public void nuevo(){
		txtCedula.setText("");
		txtIdLiquidacion.setText("");
		txtIdInspeccion.setText("");
		txtFechaEmision.setText("");
		txtFechaInspeccion.setText("");
		txtNombres.setText("");
		txtApellidos.setText("");
		txtTelefono.setText("");
		txtDireccion.setText("");
		txtReferencia.setText("");
		txtObservaciones.setText("");
		txtSubtotal.setText("");
		txtDescuento.setText("");
		txtTotal.setText("");
		limpiarMateriales();
		rubroSeleccionado = null;
		liquidacionSeleccionada = null;
	}
	
	public void buscarOrdenEmitida(){
		try{
			helper.abrirPantallaModal("/solicitudes/ListadoLiquidacionesEmitidas.fxml","Órdenes Liquidaciones Emitidas", Context.getInstance().getStage());
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
				txtIdLiquidacion.setText(Integer.toString(listaLiquidacion.get(i).getIdLiquidacion()));
				txtIdInspeccion.setText(Integer.toString(listaLiquidacion.get(i).getSolInspeccionIn().getIdSolInspeccion()));
				txtFechaEmision.setText(String.valueOf(formateador.format(listaLiquidacion.get(i).getFecha())));
				txtFechaInspeccion.setText(String.valueOf(formateador.format(listaLiquidacion.get(i).getSolInspeccionIn().getFechaIngreso())));
				txtCedula.setText(listaLiquidacion.get(i).getCuentaCliente().getCliente().getCedula());
				txtNombres.setText(listaLiquidacion.get(i).getCuentaCliente().getCliente().getNombre());
				txtApellidos.setText(listaLiquidacion.get(i).getCuentaCliente().getCliente().getApellido());
				txtDireccion.setText(listaLiquidacion.get(i).getCuentaCliente().getDireccion());
				txtTelefono.setText(listaLiquidacion.get(i).getCuentaCliente().getCliente().getTelefono());
				txtReferencia.setText(listaLiquidacion.get(i).getSolInspeccionIn().getReferencia());
				txtTotal.setText(Double.toString(listaLiquidacion.get(i).getTotal()));
				
				recuperarDetalleLiquidacionEmitida();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void recuperarDetalleLiquidacionEmitida() {
		ObservableList<LiquidacionDetalle> datos = FXCollections.observableArrayList();
		List<LiquidacionDetalle> lista = new ArrayList<LiquidacionDetalle>();

		for (int i = 0 ; i < liquidacionSeleccionada.getLiquidacionDetalles().size(); i++) {
			if (liquidacionSeleccionada.getLiquidacionDetalles().get(i).getEstado().equals(Constantes.ESTADO_INACTIVO))
				lista.add(liquidacionSeleccionada.getLiquidacionDetalles().get(i));
		}
		System.out.println(lista.size() + " Eliminar");
	
		tvDatos.getColumns().clear();
		tvDatos.getItems().clear();
		for(LiquidacionDetalle dd : lista) {
			for(int i = 0 ; i < liquidacionSeleccionada.getLiquidacionDetalles().size(); i ++) {
				if(dd.getIdDetalle() == liquidacionSeleccionada.getLiquidacionDetalles().get(i).getIdDetalle())
					liquidacionSeleccionada.getLiquidacionDetalles().remove(i);
			}
		}

		System.out.println(liquidacionSeleccionada.getLiquidacionDetalles().size() + " Resultado");
		datos.setAll(liquidacionSeleccionada.getLiquidacionDetalles());

		TableColumn<LiquidacionDetalle, String> descripcionColum = new TableColumn<>("Descripción");
		descripcionColum.setMinWidth(10);
		descripcionColum.setPrefWidth(200);
		descripcionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiquidacionDetalle, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<LiquidacionDetalle, String> param) {
				return new SimpleObjectProperty<String>(param.getValue().getRubro().getDescripcion());
			}
		});

		TableColumn<LiquidacionDetalle, String> cantidadColum = new TableColumn<>("Cantidad");
		cantidadColum.setMinWidth(10);
		cantidadColum.setPrefWidth(90);
		cantidadColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiquidacionDetalle, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<LiquidacionDetalle, String> param) {
				return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCantidad()));
			}
		});

		TableColumn<LiquidacionDetalle, String> precioColum = new TableColumn<>("Precio");
		precioColum.setMinWidth(10);
		precioColum.setPrefWidth(90);
		precioColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiquidacionDetalle, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<LiquidacionDetalle, String> param) {
				return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getPrecio()));
			}
		});

		TableColumn<LiquidacionDetalle, String> totalColum = new TableColumn<>("Total");
		totalColum.setMinWidth(10);
		totalColum.setPrefWidth(90);
		totalColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiquidacionDetalle, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<LiquidacionDetalle, String> param) {
				return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCantidad()*param.getValue().getPrecio()));
			}
		});
		tvDatos.getColumns().addAll(descripcionColum, cantidadColum, precioColum, totalColum);
		tvDatos.setItems(datos);
	}
}