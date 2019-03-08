package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ec.com.jaapz.modelo.Pago;
import ec.com.jaapz.modelo.Planilla;
import ec.com.jaapz.modelo.PlanillaDAO;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
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

public class RecaudacionesDeudaGeneralC {
	@FXML private DatePicker dtpFechaInicio;
	@FXML private DatePicker dtpFechaFin;
	@FXML private Button btnCargarDatos;
	@FXML private Button btnReporte;
	@FXML private TextField txtTotalDeuda;
	private @FXML TableView<Planilla> tvDatos;
	
	ControllerHelper helper = new ControllerHelper();
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	
	PlanillaDAO planillaDao = new PlanillaDAO();
	Date dateInicio = new Date();
	Date dateFin = new Date();
	Date fechaImpresion = new Date(); 
	
	public void initialize(){
		btnCargarDatos.setStyle("-fx-cursor: hand;");
		btnReporte.setStyle("-fx-cursor: hand;");
		
		txtTotalDeuda.setEditable(false);
		dtpFechaInicio.setValue(LocalDate.now());
		dtpFechaFin.setValue(LocalDate.now());
	}
	
	public void cargarDatos() {
		try {
			dateInicio = Date.from(dtpFechaInicio.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
			dateFin = Date.from(dtpFechaFin.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
			llenarDatos(dateInicio, dateFin);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void llenarDatos(Date fechaInicio, Date fechaFin) {
		try{
			tvDatos.getColumns().clear();
			List<Planilla> listado;
			
			listado = planillaDao.getListaPlanillaDeudaGeneral(fechaInicio, fechaFin);
			
			ObservableList<Planilla> datos = FXCollections.observableArrayList();
			datos.setAll(listado);
			
			//llenar los datos en la tabla
			TableColumn<Planilla, String> idColum = new TableColumn<>("Nº Planilla");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(100);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Planilla,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Planilla, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdPlanilla()));
				}
			});

			TableColumn<Planilla, String> fechaColum = new TableColumn<>("Fecha");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(100);
			fechaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Planilla,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Planilla, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(formateador.format(param.getValue().getFecha())));
				}
			});
			
			TableColumn<Planilla, String> cedulaColum = new TableColumn<>("Cédula");
			cedulaColum.setMinWidth(10);
			cedulaColum.setPrefWidth(100);
			cedulaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Planilla,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Planilla, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getCuentaCliente().getCliente().getCedula());
				}
			});

			TableColumn<Planilla, String> clienteColum = new TableColumn<>("Cliente");
			clienteColum.setMinWidth(10);
			clienteColum.setPrefWidth(250);
			clienteColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Planilla,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Planilla, String> param) {
					String cliente = "";
					cliente = param.getValue().getCuentaCliente().getCliente().getNombre() + " " + param.getValue().getCuentaCliente().getCliente().getApellido();
					return new SimpleObjectProperty<String>(cliente);
				}
			});
			
			TableColumn<Planilla, String> direccionColum = new TableColumn<>("Dirección");
			direccionColum.setMinWidth(10);
			direccionColum.setPrefWidth(100);
			direccionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Planilla,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Planilla, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getCuentaCliente().getDireccion());
				}
			});
			
			TableColumn<Planilla, String> telefonoColum = new TableColumn<>("Teléfono");
			telefonoColum.setMinWidth(10);
			telefonoColum.setPrefWidth(100);
			telefonoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Planilla,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Planilla, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getCuentaCliente().getCliente().getTelefono());
				}
			});
			
			TableColumn<Planilla, String> valorPagoColum = new TableColumn<>("Valor pendiente");
			valorPagoColum.setMinWidth(10);
			valorPagoColum.setPrefWidth(80);
			valorPagoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Planilla, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Planilla, String> param) {
					double total = 0.0;
					for(Pago pa : param.getValue().getPagos()){
						total = total + pa.getValor();
					}
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getTotalPagar() - total));
				}
			});
			tvDatos.getColumns().addAll(idColum, fechaColum, cedulaColum, clienteColum, direccionColum, telefonoColum, valorPagoColum);
			tvDatos.setItems(datos);
			sumarDatos();
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	public void sumarDatos() {
		try {
			if (tvDatos.getItems().isEmpty()) {
				txtTotalDeuda.setText("0.00");
			}else {
				double total = 0;
				for(int i=0; i<tvDatos.getItems().size(); i++) {
					Double valorTotal = new Double(tvDatos.getItems().get(i).getTotalPagar());
					total += valorTotal;
					double totalPagado = 0.0;
					for(Planilla pla : tvDatos.getItems()) {
						for(Pago pa : pla.getPagos()){
							totalPagado = totalPagado + pa.getValor();
						}
					}
					txtTotalDeuda.setText(String.valueOf(Double.valueOf(total - totalPagado)));
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
			param.put("fecha_inicio", dateInicio);
			param.put("fecha_fin", dateFin);
			param.put("usuario_crea", Encriptado.Desencriptar(Context.getInstance().getUsuariosC().getUsuario()));
			pr.crearReporte("/recursos/informes/deuda_general.jasper", planillaDao, param);
			pr.showReport("Deuda de Clientes");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}