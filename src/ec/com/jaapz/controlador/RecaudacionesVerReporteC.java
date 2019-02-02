package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import ec.com.jaapz.modelo.Factura;
import ec.com.jaapz.modelo.FacturaDAO;
import ec.com.jaapz.util.ControllerHelper;
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

public class RecaudacionesVerReporteC {
	@FXML private DatePicker dtpFechaInicio;
	@FXML private DatePicker dtpFechaFin;
	@FXML private Button btnCargarDatos;
	@FXML private Button btnReporte;
	@FXML private TextField txtTotalRec;
	private @FXML TableView<Factura> tvDatos;
	ControllerHelper helper = new ControllerHelper();
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	
	FacturaDAO facturaDao = new FacturaDAO();
	
	public void initialize(){
		dtpFechaInicio.setValue(LocalDate.now());
		dtpFechaFin.setValue(LocalDate.now());
	}
	
	public void cargarDatos() {
		try {
			Date dateInicio = Date.from(dtpFechaInicio.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
			Date dateFin = Date.from(dtpFechaFin.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
			llenarDatos(dateInicio, dateFin);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void llenarDatos(Date fechaInicio, Date fechaFin) {
		try{
			tvDatos.getColumns().clear();
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
			
			TableColumn<Factura, String> fechaColum = new TableColumn<>("Fecha");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(100);
			fechaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Factura, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Factura, String> param) {
					return new SimpleObjectProperty<String>(formateador.format(param.getValue().getFecha()));
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
			
			tvDatos.getColumns().addAll(idColum, fechaColum, totalColum);
			tvDatos.setItems(datos);
			sumarDatos();
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	public void sumarDatos() {
		try {
			if (tvDatos.getItems().isEmpty()) {
				txtTotalRec.setText("0.0");
			}else {
				double subtotal = 0;
				for(int i=0; i<tvDatos.getItems().size(); i++) {
					Double valorSubt = new Double(tvDatos.getItems().get(i).getTotalFactura());
					subtotal += valorSubt;
					txtTotalRec.setText(String.valueOf(Double.valueOf(subtotal)));
				}
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void verReporte() {
		
	}

}