package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ec.com.jaapz.modelo.Egreso;
import ec.com.jaapz.modelo.EgresoDAO;
import ec.com.jaapz.modelo.Factura;
import ec.com.jaapz.modelo.FacturaDAO;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.Encriptado;
import ec.com.jaapz.util.PrintReport;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class CajaVerTotalC {
	@FXML private DatePicker dtpFechaInicio;
	@FXML private DatePicker dtpFechaFin;
	@FXML private TextField txtTotalRec;
	@FXML private TextField txtTotalGastos;
	@FXML private TextField txtTotalCaja;
	@FXML private Button btnCargarDatos;
	@FXML private Button btnReporte;
	private @FXML TableView<Factura> tvDatosIngresos;
	private @FXML TableView<Egreso> tvDatosEgresos;
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	
	Date dateInicio = new Date();
	Date dateFin = new Date();
	Date fechaImpresion = new Date();
	FacturaDAO facturaDao = new FacturaDAO();
	EgresoDAO egresoDao = new EgresoDAO();
	
	public void cargarDatos() {
		try {
			dateInicio = Date.from(dtpFechaInicio.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
			dateFin = Date.from(dtpFechaFin.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
			llenarDatosIngreso(dateInicio, dateFin);
			llenarDatosEgreso(dateInicio, dateFin);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void llenarDatosIngreso(Date fechaInicio, Date fechaFin) {
		try{
			tvDatosIngresos.getColumns().clear();
			List<Factura> listado;
			
			listado = facturaDao.getListaFacturasRecaudadas(fechaInicio, fechaFin);
			
			ObservableList<Factura> datos = FXCollections.observableArrayList();
			datos.setAll(listado);
			
			//llenar los datos en la tabla
			TableColumn<Factura, String> idColum = new TableColumn<>("Nº");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(40);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Factura, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Factura, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdFactura()));
				}
			});
			
			TableColumn<Factura, String> numComprobColum = new TableColumn<>("Nº Comprobante");
			numComprobColum.setMinWidth(10);
			numComprobColum.setPrefWidth(100);
			numComprobColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Factura, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Factura, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getNumFactura());
				}
			});
			
			TableColumn<Factura, String> fechaColum = new TableColumn<>("Fecha");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(100);
			fechaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Factura, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Factura, String> param) {
					return new SimpleObjectProperty<String>(formateador.format(param.getValue().getFecha()));
				}
			});
			
			TableColumn<Factura, String> clienteColum = new TableColumn<>("Cliente");
			clienteColum.setMinWidth(10);
			clienteColum.setPrefWidth(250);
			clienteColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Factura, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Factura, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getCuentaCliente().getCliente().getNombre() + " " + param.getValue().getCuentaCliente().getCliente().getApellido());
				}
			});
			
			TableColumn<Factura, String> numMedidorColum = new TableColumn<>("Nº Medidor");
			numMedidorColum.setMinWidth(10);
			numMedidorColum.setPrefWidth(75);
			numMedidorColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Factura, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Factura, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getCuentaCliente().getMedidor().getCodigo());
				}
			});
			
			TableColumn<Factura, String> direccionColum = new TableColumn<>("Dirección");
			direccionColum.setMinWidth(10);
			direccionColum.setPrefWidth(300);
			direccionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Factura, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Factura, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getCuentaCliente().getCliente().getDireccion());
				}
			});
			
			TableColumn<Factura, String> totalColum = new TableColumn<>("Total");
			totalColum.setMinWidth(10);
			totalColum.setPrefWidth(100);
			totalColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Factura, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Factura, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getTotalFactura()));
				}
			});
			
			tvDatosIngresos.getColumns().addAll(idColum, numComprobColum, fechaColum, clienteColum, numMedidorColum, direccionColum, totalColum);
			tvDatosIngresos.setItems(datos);
			sumarDatosIngreso();
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}	
	
	public void sumarDatosIngreso() {
		try {
			if (tvDatosIngresos.getItems().isEmpty()) {
				txtTotalRec.setText("0.0");
			}else {
				double subtotal = 0;
				for(int i=0; i<tvDatosIngresos.getItems().size(); i++) {
					Double valorSubt = new Double(tvDatosIngresos.getItems().get(i).getTotalFactura());
					subtotal += valorSubt;
					txtTotalRec.setText(String.valueOf(Double.valueOf(subtotal)));
				}
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void llenarDatosEgreso(Date fechaInicio, Date fechaFin) {
		try{
			tvDatosEgresos.getColumns().clear();
			List<Egreso> listadoEgreso;
			
			listadoEgreso = egresoDao.getListaEgresos(fechaInicio, fechaFin);
			
			ObservableList<Egreso> datosEgreso = FXCollections.observableArrayList();
			datosEgreso.setAll(listadoEgreso);
			
			//llenar los datos en la tabla
			TableColumn<Egreso, String> idColum = new TableColumn<>("Nº");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(40);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Egreso, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Egreso, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdEgreso()));
				}
			});
			
			TableColumn<Egreso, String> fechaColum = new TableColumn<>("Fecha");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(100);
			fechaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Egreso, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Egreso, String> param) {
					return new SimpleObjectProperty<String>(formateador.format(param.getValue().getFecha()));
				}
			});
			
			
			TableColumn<Egreso, String> descripcionColum = new TableColumn<>("Descripción");
			descripcionColum.setMinWidth(10);
			descripcionColum.setPrefWidth(75);
			descripcionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Egreso, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Egreso, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getDescripcion());
				}
			});
			
			TableColumn<Egreso, String> observacionesColum = new TableColumn<>("Observaciones");
			observacionesColum.setMinWidth(10);
			observacionesColum.setPrefWidth(300);
			observacionesColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Egreso, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Egreso, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getObservaciones());
				}
			});
			
			TableColumn<Egreso, String> totalColum = new TableColumn<>("Total");
			totalColum.setMinWidth(10);
			totalColum.setPrefWidth(100);
			totalColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Egreso, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Egreso, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getMonto()));
				}
			});
			
			tvDatosEgresos.getColumns().addAll(idColum, fechaColum, descripcionColum, observacionesColum, totalColum);
			tvDatosEgresos.setItems(datosEgreso);
			sumarDatosEgreso();
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	public void sumarDatosEgreso() {
		try {
			if (tvDatosEgresos.getItems().isEmpty()) {
				txtTotalGastos.setText("0.0");
			}else {
				double subtotal = 0;
				for(int i=0; i<tvDatosEgresos.getItems().size(); i++) {
					Double valorSubt = new Double(tvDatosEgresos.getItems().get(i).getMonto());
					subtotal += valorSubt;
					txtTotalGastos.setText(String.valueOf(Double.valueOf(subtotal)));
				}
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void verReporte() {
		try {
				PrintReport pr = new PrintReport();
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("fechaInicio", dateInicio);
				param.put("fechaFin", dateFin);
				param.put("usuarioCrea", Encriptado.Desencriptar(Context.getInstance().getUsuariosC().getUsuario()));
				param.put("fechaImpresion", fechaImpresion);
				pr.crearReporte("/recursos/informes/ver_recaudaciones.jasper", facturaDao, param);
				pr.showReport("Recaudaciones");
			}catch(Exception ex) {
				System.out.println(ex.getMessage());
			}
		}

}