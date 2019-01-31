package ec.com.jaapz.controlador;

import java.util.List;

import ec.com.jaapz.modelo.AperturaLectura;
import ec.com.jaapz.modelo.AperturaLecturaDAO;
import ec.com.jaapz.util.Context;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

public class LecturasListadoAperturaC {
	@FXML private TableView<AperturaLectura> tvDatos;
	@FXML private TextField txtBuscar;
	AperturaLecturaDAO aperturaDAO = new AperturaLecturaDAO();
	public void initialize(){
		Context.getInstance().setApertura(null);
		llenarDatos("");
		tvDatos.setRowFactory(tv -> {
			TableRow<AperturaLectura> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
					if(tvDatos.getSelectionModel().getSelectedItem() != null){
						Context.getInstance().setApertura(tvDatos.getSelectionModel().getSelectedItem());
						Context.getInstance().getStageModal().close();
					}
				}
			});
			return row ;
		});
		
	}
	@SuppressWarnings("unchecked")
	void llenarDatos(String busqueda){
		try{
			tvDatos.getItems().clear();
			tvDatos.getColumns().clear();
			List<AperturaLectura> listaApertura;
			ObservableList<AperturaLectura> datos = FXCollections.observableArrayList();
			listaApertura = aperturaDAO.getListaAperturas();
			datos.setAll(listaApertura);

			TableColumn<AperturaLectura, String> mesColum = new TableColumn<>("Mes");
			mesColum.setMinWidth(10);
			mesColum.setPrefWidth(100);
			mesColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AperturaLectura,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<AperturaLectura, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getMe().getDescripcion()));
				}
			});

			TableColumn<AperturaLectura, String> anioColum = new TableColumn<>("Año");
			anioColum.setMinWidth(10);
			anioColum.setPrefWidth(90);
			anioColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AperturaLectura,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<AperturaLectura, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getAnio().getDescripcion()));
				}
			});
			TableColumn<AperturaLectura, String> clienteColum = new TableColumn<>("No. Clientes");
			clienteColum.setMinWidth(10);
			clienteColum.setPrefWidth(90);
			clienteColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AperturaLectura,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<AperturaLectura, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getPlanillas().size()));
				}
			});
			TableColumn<AperturaLectura, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(90);
			estadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AperturaLectura,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<AperturaLectura, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getEstadoApertura());
				}
			});

			tvDatos.getColumns().addAll(anioColum,mesColum,clienteColum,estadoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	public void buscarApertura() {
		try {
			ObservableList<AperturaLectura> datos = FXCollections.observableArrayList();
			List<AperturaLectura> listaApertura = aperturaDAO.getListaAperturasByPatron(txtBuscar.getText().toString());
			datos.setAll(listaApertura);
			tvDatos.setItems(datos);
			tvDatos.refresh();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
