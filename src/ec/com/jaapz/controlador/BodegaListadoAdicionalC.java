package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.List;

import ec.com.jaapz.modelo.InstalacionDAO;
import ec.com.jaapz.modelo.MaterialAdicional;
import ec.com.jaapz.modelo.MaterialAdicionalDAO;
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

public class BodegaListadoAdicionalC {
	@FXML private TableView<MaterialAdicional> tvDatos;
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
	            TableRow<MaterialAdicional> row = new TableRow<>();
	            row.setOnMouseClicked(event -> {
	                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
	                	if(tvDatos.getSelectionModel().getSelectedItem() != null){
	                		Context.getInstance().setMaterialAdicional(tvDatos.getSelectionModel().getSelectedItem());
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
			MaterialAdicionalDAO instalacionDAO = new MaterialAdicionalDAO();
			List<MaterialAdicional> listaInstalacion = instalacionDAO.getListaAdicionales(codigo);
			ObservableList<MaterialAdicional> datos = FXCollections.observableArrayList();

			datos.setAll(listaInstalacion);
			
			//llenar los datos en la tabla
			TableColumn<MaterialAdicional, String> idColum = new TableColumn<>("No.");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(50);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MaterialAdicional,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<MaterialAdicional, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdMatAdicional()));
				}
			});
			
			
			TableColumn<MaterialAdicional, String> idIns = new TableColumn<>("No. Ins");
			idIns.setMinWidth(10);
			idIns.setPrefWidth(50);
			idIns.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MaterialAdicional,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<MaterialAdicional, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getInstalacion().getIdInstalacion()));
				}
			});
			
			TableColumn<MaterialAdicional, String> ingresoColum = new TableColumn<>("Cliente");
			ingresoColum.setMinWidth(10);
			ingresoColum.setPrefWidth(320);
			ingresoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MaterialAdicional,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<MaterialAdicional, String> param) {
					String cliente = param.getValue().getInstalacion().getCuentaCliente().getCliente().getNombre() + " " + param.getValue().getInstalacion().getCuentaCliente().getCliente().getNombre();
					return new SimpleObjectProperty<String>(cliente);
				}
			});
			
			
			
			tvDatos.getColumns().addAll(idColum,idIns, ingresoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void buscarLiquidCuenta() {

	}
}
