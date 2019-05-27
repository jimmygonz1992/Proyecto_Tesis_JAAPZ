package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ec.com.jaapz.modelo.SegUsuarioDAO;
import ec.com.jaapz.modelo.SolInspeccionIn;
import ec.com.jaapz.modelo.SolInspeccionInDAO;
import ec.com.jaapz.util.Encriptado;
import ec.com.jaapz.util.PrintReport;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

public class SolicitudesNoFactiblesC {
	@FXML private Button btnImprimir;
	@FXML private TableView<SolInspeccionIn> tvDatos;
	@FXML private TextField txtBuscar;
	SolInspeccionInDAO inspeccionDAO = new SolInspeccionInDAO(); 
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	SegUsuarioDAO usuarioDAO = new SegUsuarioDAO();
	
	public void initialize() {
		try {
			btnImprimir.setStyle("-fx-graphic: url('/imprimir.png');-fx-cursor: hand;");
			llenarDatos("");
			txtBuscar.setOnKeyPressed(new EventHandler<KeyEvent>() { 
				@Override 
				public void handle(KeyEvent ke) { 
					if (ke.getCode().equals(KeyCode.ENTER)) { 
						List<SolInspeccionIn> listaInspecciones = inspeccionDAO.getSolicitudesNoFactibles(txtBuscar.getText());
						ObservableList<SolInspeccionIn> datos = FXCollections.observableArrayList();
						datos.setAll(listaInspecciones);
						tvDatos.setItems(datos);
						tvDatos.refresh();
					} 
				} 
			}); 
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	private void llenarDatos(String patron) {
		try {
			tvDatos.getColumns().clear();
			tvDatos.getItems().clear();
			List<SolInspeccionIn> listaInspecciones = inspeccionDAO.getSolicitudesNoFactibles(patron);
			
			ObservableList<SolInspeccionIn> datos = FXCollections.observableArrayList();
			datos.setAll(listaInspecciones);

			//llenar los datos en la tabla
			TableColumn<SolInspeccionIn, String> usuarioColum = new TableColumn<>("Usuario Responsable");
			usuarioColum.setMinWidth(10);
			usuarioColum.setPrefWidth(120);
			usuarioColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionIn,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionIn, String> param) {
					String usuario = usuarioDAO.getUsuarioById(param.getValue().getIdUsuEncargado()).getUsuario();
					return new SimpleObjectProperty<String>(Encriptado.Desencriptar(usuario));
				}
			});
			
			
			TableColumn<SolInspeccionIn, String> idColum = new TableColumn<>("No. Solicitud");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(100);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionIn,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionIn, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdSolInspeccion()));
				}
			});

			TableColumn<SolInspeccionIn, String> fechaColum = new TableColumn<>("Fecha");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(100);
			fechaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionIn,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionIn, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(formateador.format(param.getValue().getFechaIngreso())));
				}
			});

			TableColumn<SolInspeccionIn, String> clienteColum = new TableColumn<>("Cliente");
			clienteColum.setMinWidth(10);
			clienteColum.setPrefWidth(250);
			clienteColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionIn,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionIn, String> param) {
					String cliente = "";
					cliente = param.getValue().getCliente().getNombre() + " " + param.getValue().getCliente().getApellido();
					return new SimpleObjectProperty<String>(cliente);
				}
			});

			TableColumn<SolInspeccionIn, String> referenciaColum = new TableColumn<>("Referencia");
			referenciaColum.setMinWidth(10);
			referenciaColum.setPrefWidth(350);
			referenciaColum.setCellValueFactory(new PropertyValueFactory<SolInspeccionIn, String>("referencia"));

			TableColumn<SolInspeccionIn, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(90);
			estadoColum.setCellValueFactory(new PropertyValueFactory<SolInspeccionIn, String>("estadoInspeccion"));


			tvDatos.getColumns().addAll(usuarioColum,idColum, fechaColum,clienteColum,referenciaColum,estadoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void imprimirListado() {
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", new Locale("MX"));
			Date fechaDate = new Date();
			String fechaSistema = formateador.format(fechaDate);
			String fecha = dateFormatter("yyyy-MM-dd hh:mm:ss","d 'de' MMMM 'del' yyyy", fechaSistema);
			param.put("FECHA", fecha);
			PrintReport reporte = new PrintReport();
			reporte.crearReporte("/recursos/informes/solicitudesNoFactibles.jasper", inspeccionDAO, param);
			reporte.showReport("Solicitudes no factibles");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public static final Locale LOCALE_MX = new Locale("es", "MX");
	public static String dateFormatter(String inputFormat, String outputFormat, String inputDate){
	      //Define formato default de entrada.   
	      String input = inputFormat.isEmpty()? "yyyy-MM-dd hh:mm:ss" : inputFormat; 
	      //Define formato default de salida.
	      String output = outputFormat.isEmpty()? "d 'de' MMMM 'del' yyyy" : outputFormat;
	    String outputDate = inputDate;
	    try {
	        outputDate = new SimpleDateFormat(output, LOCALE_MX).format(new SimpleDateFormat(input, LOCALE_MX).parse(inputDate));
	    } catch (Exception e) {
	        System.out.println("dateFormatter(): " + e.getMessage());           
	    }
	    return outputDate;
	}
}
