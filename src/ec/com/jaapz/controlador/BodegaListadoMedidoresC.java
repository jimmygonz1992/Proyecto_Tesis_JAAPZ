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
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class BodegaListadoMedidoresC {
	private @FXML TableView<Medidor> tvDatos;
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
			MedidorDAO medidorDAO = new MedidorDAO();
			List<Medidor> listaMedidores;
			listaMedidores = medidorDAO.getListaMedidores();
			ObservableList<Medidor> datos = FXCollections.observableArrayList();
			datos.setAll(listaMedidores);

			//llenar los datos en la tabla
			//llenar los datos en la tabla
			TableColumn<Medidor, String> idColum = new TableColumn<>("Id");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(70);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Medidor,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Medidor, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdMedidor()));
				}
			});
			
			TableColumn<Medidor, String> codigoColum = new TableColumn<>("Código");
			codigoColum.setMinWidth(10);
			codigoColum.setPrefWidth(70);
			codigoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Medidor,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Medidor, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCodigo()));
				}
			});
			
			TableColumn<Medidor, String> estadoColum = new TableColumn<>("Estado Medidor");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(70);
			estadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Medidor,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Medidor, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getEstadoMedidor().getDescripcion()));
				}
			});
			
			TableColumn<Medidor, String> precioColum = new TableColumn<>("Precio");
			precioColum.setMinWidth(10);
			precioColum.setPrefWidth(70);
			precioColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Medidor,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Medidor, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getPrecio()));
				}
			});
			
			tvDatos.getColumns().addAll(idColum, codigoColum, estadoColum, precioColum);
			tvDatos.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
}