package ec.com.jaapz.controlador;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import ec.com.jaapz.modelo.LiquidacionDetalle;
import ec.com.jaapz.modelo.LiquidacionOrden;
import ec.com.jaapz.modelo.Rubro;
import ec.com.jaapz.modelo.RubroDAO;
import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import ec.com.jaapz.util.PrintReport;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class VerOrdenLiquidacionC {
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
	//@FXML private TextField	txtCodigoMat;
	//@FXML private TextArea	txtDescripcionMat;
	//@FXML private TextField	txtStockMat;
	//@FXML private TextField txtPrecioMat;
	//@FXML private TextField	txtCantidadMat;
	//@FXML private TextArea	txtObservaciones;
	@FXML private TextField	txtTotal;
	
	@FXML private Button btnImprimir;

	/*@FXML private Button btnBuscarRubro;
	@FXML private Button btnAniadir;
	@FXML private Button btnEliminar;
	@FXML private Button btnGrabar;*/

	@FXML private TableView<LiquidacionDetalle> tvDatos;
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	DecimalFormat decimales = new DecimalFormat("#0.00");
	ControllerHelper helper = new ControllerHelper();
	LiquidacionOrden liquidacionSeleccionado = new LiquidacionOrden();
	Rubro rubroSeleccionado = new Rubro();
	RubroDAO rubroDAO = new RubroDAO();

	public void initialize() {
		btnImprimir.setStyle("-fx-graphic: url('/imprimir.png');-fx-cursor: hand;");
		/*txtCodigoMat.setOnKeyPressed(new EventHandler<KeyEvent>(){
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
		});*/

		//solo letras mayusculas
	/*	txtObservaciones.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				String cadena = txtObservaciones.getText().toUpperCase();
				txtObservaciones.setText(cadena);
			}
		});*/

		//solo letras mayusculas
		/*txtCodigoMat.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				String cadena = txtCodigoMat.getText().toUpperCase();
				txtCodigoMat.setText(cadena);
			}
		});*/

		//validar solo numeros
		/*txtCantidadMat.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (newValue.matches("\\d*")) {
					//int value = Integer.parseInt(newValue);
				} else {
					txtCantidadMat.setText(oldValue);
				}
			}
		});*/

		//para anadir a la grilla con enter
		/*txtCantidadMat.setOnKeyPressed(new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent ke){
				if (ke.getCode().equals(KeyCode.ENTER)){
					anadir();
					limpiarMateriales();
					btnBuscarRubro.requestFocus();
				}
			}
		});*/

		//cargar dato recuperado
		recuperarDatos(Context.getInstance().getLiquidaciones());
		liquidacionSeleccionado = Context.getInstance().getLiquidaciones();
	}

	/*void limpiarMateriales() {
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
	}*/

	@SuppressWarnings("unchecked")
	void recuperarDatos(LiquidacionOrden liquidacion) {
		try {
			tvDatos.getItems().clear();
			tvDatos.getColumns().clear();

			txtCedula.setText(liquidacion.getCuentaCliente().getCliente().getCedula());
			txtIdLiquidacion.setText(String.valueOf(liquidacion.getIdLiquidacion()));
			txtFechaEmision.setText(String.valueOf(formateador.format(liquidacion.getFecha())));
			txtIdInspeccion.setText(String.valueOf(liquidacion.getSolInspeccionIn().getIdSolInspeccion()));
			txtFechaInspeccion.setText(String.valueOf(formateador.format(liquidacion.getSolInspeccionIn().getFechaIngreso())));
			txtNombres.setText(liquidacion.getCuentaCliente().getCliente().getNombre());
			txtApellidos.setText(liquidacion.getCuentaCliente().getCliente().getApellido());
			txtTelefono.setText(liquidacion.getSolInspeccionIn().getTelefonoContacto());
			txtDireccion.setText(liquidacion.getCuentaCliente().getDireccion());
			txtReferencia.setText(liquidacion.getSolInspeccionIn().getReferencia());
			//txtObservaciones.setText(liquidacion.getSolInspeccionIn().getObservacion());
			txtTotal.setText(Double.toString(liquidacion.getTotal()));

			//recupera detalle si esta realizado
			if(liquidacion.getSolInspeccionIn().getEstadoInspeccion().equals(Constantes.EST_INSPECCION_REALIZADO)) {
				//llenar los datos en la tabla
				TableColumn<LiquidacionDetalle, String> cantidadColum = new TableColumn<>("Cantidad");
				cantidadColum.setMinWidth(10);
				cantidadColum.setPrefWidth(90);
				cantidadColum.setCellValueFactory(new PropertyValueFactory<LiquidacionDetalle, String>("cantidad"));

				TableColumn<LiquidacionDetalle, String> descipcionColum = new TableColumn<>("Descripcion");
				descipcionColum.setMinWidth(10);
				descipcionColum.setPrefWidth(250);
				descipcionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiquidacionDetalle,String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<LiquidacionDetalle, String> param) {
						return new SimpleObjectProperty<String>(param.getValue().getRubro().getDescripcion());
					}
				});

				TableColumn<LiquidacionDetalle, String> precioColum = new TableColumn<>("Costo U.");
				precioColum.setMinWidth(10);
				precioColum.setPrefWidth(90);
				precioColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiquidacionDetalle,String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<LiquidacionDetalle, String> param) {
						return new SimpleObjectProperty<String>(String.valueOf(decimales.format(param.getValue().getRubro().getPrecio())));
					}
				});

				TableColumn<LiquidacionDetalle, String> costoColum = new TableColumn<>("Costo Total");
				costoColum.setMinWidth(10);
				costoColum.setPrefWidth(90);
				costoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiquidacionDetalle,String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<LiquidacionDetalle, String> param) {
						return new SimpleObjectProperty<String>(String.valueOf(decimales.format(param.getValue().getTotal())));
					}
				});

				ObservableList<LiquidacionDetalle> datos = FXCollections.observableArrayList();
				datos.setAll(liquidacion.getLiquidacionDetalles());

				tvDatos.getColumns().addAll(cantidadColum,descipcionColum,precioColum,costoColum);
				tvDatos.setItems(datos);
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	/*public void buscarRubro(){
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
	}*/

	/*void llenarDatos(Rubro datoSeleccionado){
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
	}*/

	/*@SuppressWarnings("unchecked")
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
	}*/

	/*boolean validarAnadirRubro() {
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
	}*/

	public void sumarDatos() {
		try {
			if (tvDatos.getItems().isEmpty()) {
				txtTotal.setText("0.0");
			}else {
				double subtotal = 0;
				for(int i=0; i<tvDatos.getItems().size(); i++) {
					Double valorSubt = new Double(tvDatos.getItems().get(i).getCantidad()*tvDatos.getItems().get(i).getPrecio());
					subtotal += valorSubt;
					txtTotal.setText(String.valueOf(Double.valueOf(subtotal)));
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

	public void grabar() {
		try {

		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void imprimir() {
		try {
			PrintReport printReport = new PrintReport();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("usuario", Context.getInstance().getUsuariosC().getNombre() + ' ' + Context.getInstance().getUsuariosC().getApellido());
			param.put("idLiquid", Integer.parseInt(txtIdLiquidacion.getText()));
			printReport.crearReporte("/recursos/informes/VerOrdLiquidacion.jasper", rubroDAO, param);
			printReport.showReport("Orden de Liquidación");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}