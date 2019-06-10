package ec.com.jaapz.controlador;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import ec.com.jaapz.modelo.IngresoDetalle;
import ec.com.jaapz.modelo.Instalacion;
import ec.com.jaapz.modelo.InstalacionDetalle;
import ec.com.jaapz.modelo.LiquidacionOrden;
import ec.com.jaapz.modelo.Rubro;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
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

public class BodegaMatAdicionalC {

	@FXML private Button btnBuscarLiquidCuenta;
	@FXML private TableView<InstalacionDetalle> tvDatos;
	@FXML private TextField txtDireccion;
	@FXML private TextField txtApellidos;
	@FXML private TextField txtIdInstalacion;
	@FXML private Button btnNuevo;
	@FXML private TextField txtStockMat;
	@FXML private TextField txtPrecioMat;
	@FXML private TextField txtCedula;
	@FXML private DatePicker dtpFechaInstalacion;
	@FXML private TextField txtTotal;
	@FXML private TextField txtCantidadMat;
	@FXML private TextArea txtDescripcionMat;
	@FXML private TextField txtTelefono;
	@FXML private TextField txtCodigoMat;
	@FXML private TextField txtIdCuenta;
	@FXML private TextField txtNombres;
	@FXML private Button btnGrabar;
	@FXML private Button btnA�adir;
	@FXML private TextArea txtObservaciones;
	@FXML private Button btnBuscarRubro;

	ControllerHelper helper = new ControllerHelper();
	Instalacion instalacionSeleccionada;
	DecimalFormat df = new DecimalFormat("0.00");
	
	public void buscarLiqCuenta() {
		try{
			helper.abrirPantallaModal("/bodega/ListadoLiquidaciones.fxml","Listado de �rdenes Liquidaciones", Context.getInstance().getStage());
			if (Context.getInstance().getLiquidaciones() != null) {
				instalacionSeleccionada = Context.getInstance().getInstalacion();
				llenarDatosLiquidacion(instalacionSeleccionada);
				Context.getInstance().setLiquidaciones(null);
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	void llenarDatosLiquidacion(Instalacion datoSeleccionado){
		try {
			if(datoSeleccionado.getIdInstalacion() == null)
				txtIdInstalacion.setText("");
			else
				txtIdInstalacion.setText(String.valueOf(datoSeleccionado.getIdInstalacion()));
			
			if(datoSeleccionado.getFechaInst() == null)
				dtpFechaInstalacion.setValue(LocalDate.now());
			else {
				LocalDate date = datoSeleccionado.getFechaInst().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				dtpFechaInstalacion.setValue(LocalDate.now());
			}
			
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
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void grabar() {

	}

	
	public void nuevo() {

	}

	Rubro rubroSeleccionado = new Rubro();
	public void buscarRubro() {
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
			//txtCodigoMat.setText(String.valueOf(datoSeleccionado.getIdRubro()));
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
	
	public void anadir() {
		try {
			if(validarAnadirRubro() == false)
				return;
			ObservableList<InstalacionDetalle> datos = tvDatos.getItems();
			tvDatos.getColumns().clear();
			InstalacionDetalle datoAnadir = new InstalacionDetalle();
			rubroSeleccionado.setPrecio(Double.parseDouble(txtPrecioMat.getText()));
			datoAnadir.setRubro(rubroSeleccionado);
			datoAnadir.setCantidad(Integer.parseInt(txtCantidadMat.getText()));
			datoAnadir.setPrecio(Double.parseDouble(txtPrecioMat.getText()));
			Double total = Integer.parseInt(txtCantidadMat.getText()) * Double.parseDouble(txtPrecioMat.getText());
			datoAnadir.setSubtotal(total);
			datos.add(datoAnadir);

			//llenar los datos en la tabla			
			/*TableColumn<IngresoDetalle, String> descipcionColum = new TableColumn<>("Descripci�n");
			descipcionColum.setMinWidth(10);
			descipcionColum.setPrefWidth(200);
			descipcionColum.setCellValueFactory(new PropertyValueFactory<IngresoDetalle, String>("rubro"));*/
			
			TableColumn<InstalacionDetalle, String> descipcionColum = new TableColumn<>("Descripci�n");
			descipcionColum.setMinWidth(10);
			descipcionColum.setPrefWidth(200);
			descipcionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<InstalacionDetalle, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<InstalacionDetalle, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getRubro().getDescripcion()));
				}
			});

			/*TableColumn<IngresoDetalle, String> cantidadColum = new TableColumn<>("Cantidad");
			cantidadColum.setMinWidth(10);
			cantidadColum.setPrefWidth(90);
			cantidadColum.setCellValueFactory(new PropertyValueFactory<IngresoDetalle, String>("cantidad"));*/
			
			TableColumn<InstalacionDetalle, String> cantidadColum = new TableColumn<>("Cantidad");
			cantidadColum.setMinWidth(10);
			cantidadColum.setPrefWidth(90);
			cantidadColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<InstalacionDetalle, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<InstalacionDetalle, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCantidad()));
				}
			});

			/*TableColumn<IngresoDetalle, Double> precioColum = new TableColumn<>("Precio");
			precioColum.setMinWidth(10);
			precioColum.setPrefWidth(90);
			precioColum.setCellValueFactory(new PropertyValueFactory<IngresoDetalle, Double>("precio"));*/
			
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
					return new SimpleObjectProperty<String>(String.valueOf(String.format("%.2f", param.getValue().getCantidad()*param.getValue().getPrecio())));
				}
			});
			tvDatos.getColumns().addAll(descipcionColum, cantidadColum, precioColum, totalColum);
			tvDatos.setItems(datos);
			if(tvDatos.getItems().size() > 0) {
				sumarDatos();
			}
			rubroSeleccionado = null;
			limpiar();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	boolean validarAnadirRubro() {
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
	public void sumarDatos() {
		try {
			if (tvDatos.getItems().isEmpty()) {
				txtTotal.setText("0.0");
			}else {
				double total = 0;
				for(InstalacionDetalle det : tvDatos.getItems()) {
					total = total + det.getSubtotal();
				}
				txtTotal.setText(df.format(total).replace(",", "."));
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	void limpiar() {
		txtCodigoMat.setText("");
		txtDescripcionMat.setText("");
		txtCantidadMat.setText("");
		txtPrecioMat.setText("");
		txtStockMat.setText("");
		//proveedorSeleccionado = null;	
	}
}