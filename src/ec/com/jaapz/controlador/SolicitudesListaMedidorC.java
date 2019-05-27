package ec.com.jaapz.controlador;

import java.util.List;

import ec.com.jaapz.modelo.Medidor;
import ec.com.jaapz.modelo.MedidorDAO;
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
import javafx.util.Callback;

public class SolicitudesListaMedidorC {
	MedidorDAO medidorDAO = new MedidorDAO();
	@FXML private TableView<Medidor> tvDatos;
	public void initialize(){
		llenarDatos();
		tvDatos.setRowFactory(tv -> {
			TableRow<Medidor> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
					if(tvDatos.getSelectionModel().getSelectedItem() != null){
						Context.getInstance().setMedidor(tvDatos.getSelectionModel().getSelectedItem());
						Context.getInstance().getStageModal().close();
					}
				}
			});
			return row ;
		});
		
	}
	@SuppressWarnings("unchecked")
	void llenarDatos(){
		try{
			tvDatos.getColumns().clear();
			tvDatos.getItems().clear();
			List<Medidor> listaMedidores;
			ObservableList<Medidor> datos = FXCollections.observableArrayList();
			listaMedidores = medidorDAO.getListaMedidores();
			datos.setAll(listaMedidores);

			//llenar los datos en la tabla
			TableColumn<Medidor, String> idColum = new TableColumn<>("Código");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(150);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Medidor,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Medidor, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getCodigo());
				}
			});
			
			TableColumn<Medidor, String> nombresColum = new TableColumn<>("Marca");
			nombresColum.setMinWidth(10);
			nombresColum.setPrefWidth(250);
			nombresColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Medidor,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Medidor, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getMarca());
				}
			});
			
			TableColumn<Medidor, String> apellidosColum = new TableColumn<>("Modelo");
			apellidosColum.setMinWidth(10);
			apellidosColum.setPrefWidth(250);
			apellidosColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Medidor,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Medidor, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getModelo());
				}
			});
			
			TableColumn<Medidor, String> generoColum = new TableColumn<>("Estado");
			generoColum.setMinWidth(10);
			generoColum.setPrefWidth(100);
			generoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Medidor,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Medidor, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getEstadoMedidor().getDescripcion());
				}
			});
			tvDatos.getColumns().addAll(idColum, nombresColum,apellidosColum,generoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
}
