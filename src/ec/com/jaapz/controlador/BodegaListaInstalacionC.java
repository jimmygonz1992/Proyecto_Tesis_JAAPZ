package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.List;

import ec.com.jaapz.modelo.Instalacion;
import ec.com.jaapz.modelo.InstalacionDAO;
import ec.com.jaapz.util.Context;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

public class BodegaListaInstalacionC {
	@FXML private TableView<Instalacion> tvDatos;
	@FXML private TextField txtBuscar;
	InstalacionDAO instalacionDAO = new InstalacionDAO();
	public void initialize() {
		try {
			llenarListaInstalacion("");
			txtBuscar.setOnKeyPressed(new EventHandler<KeyEvent>(){
				@Override
				public void handle(KeyEvent ke){
					if (ke.getCode().equals(KeyCode.ENTER)){
						llenarListaInstalacion(txtBuscar.getText());
					}
				}
			});
			tvDatos.setRowFactory(tv -> {
	            TableRow<Instalacion> row = new TableRow<>();
	            row.setOnMouseClicked(event -> {
	                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
	                	if(tvDatos.getSelectionModel().getSelectedItem() != null){
	                		Context.getInstance().setInstalacion(tvDatos.getSelectionModel().getSelectedItem());
	    					Context.getInstance().getStageModal().close();
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
	private void llenarListaInstalacion(String codigo) {
		try {
			SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
			tvDatos.getColumns().clear();
			InstalacionDAO instalacionDAO = new InstalacionDAO();
			List<Instalacion> listaInstalacion = instalacionDAO.getListaInstalacionPendiente(codigo);
			ObservableList<Instalacion> datos = FXCollections.observableArrayList();

			datos.setAll(listaInstalacion);
			
			//llenar los datos en la tabla
			TableColumn<Instalacion, String> idColum = new TableColumn<>("No. de Instalación");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(50);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Instalacion,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Instalacion, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdInstalacion()));
				}
			});
			
			TableColumn<Instalacion, String> ingresoColum = new TableColumn<>("Cliente");
			ingresoColum.setMinWidth(10);
			ingresoColum.setPrefWidth(320);
			ingresoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Instalacion,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Instalacion, String> param) {
					String cliente = param.getValue().getCuentaCliente().getCliente().getNombre() + " " + param.getValue().getCuentaCliente().getCliente().getNombre();
					return new SimpleObjectProperty<String>(cliente);
				}
			});
			
			TableColumn<Instalacion, String> fechaColum = new TableColumn<>("Fecha");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(150);
			fechaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Instalacion,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Instalacion, String> param) {
					return new SimpleObjectProperty<String>(formateador.format(param.getValue().getFechaInst()));
				}
			});
			
			tvDatos.getColumns().addAll(ingresoColum, fechaColum);
			tvDatos.setItems(datos);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void buscarLiquidCuenta() {

	}
	
}
