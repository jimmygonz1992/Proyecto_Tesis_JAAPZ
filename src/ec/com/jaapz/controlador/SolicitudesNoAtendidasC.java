package ec.com.jaapz.controlador;

import java.util.List;

import ec.com.jaapz.modelo.SolInspeccionIn;
import ec.com.jaapz.modelo.SolInspeccionInDAO;
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

public class SolicitudesNoAtendidasC {
	SolInspeccionInDAO inspeccionDAO = new SolInspeccionInDAO();
	private @FXML TableView<SolInspeccionIn> tvDatos;
	public void initialize(){
		llenarDatos();
		tvDatos.setRowFactory(tv -> {
			TableRow<SolInspeccionIn> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
					if(tvDatos.getSelectionModel().getSelectedItem() != null){
						Context.getInstance().setInspeccion(tvDatos.getSelectionModel().getSelectedItem());
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
			List<SolInspeccionIn> listaClientes;
			ObservableList<SolInspeccionIn> datos = FXCollections.observableArrayList();
			listaClientes = inspeccionDAO.getSolicitudesNoAtendidas();
			datos.setAll(listaClientes);

			//llenar los datos en la tabla
			TableColumn<SolInspeccionIn, String> idColum = new TableColumn<>("No. solicitud");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(90);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionIn,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionIn, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdSolInspeccion()));
				}
			});
			
			TableColumn<SolInspeccionIn, String> nombresColum = new TableColumn<>("Nombres");
			nombresColum.setMinWidth(10);
			nombresColum.setPrefWidth(300);
			nombresColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionIn,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionIn, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getCliente().getNombre() + " " + param.getValue().getCliente().getApellido());
				}
			});
			TableColumn<SolInspeccionIn, String> fechaColum = new TableColumn<>("Fecha");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(100);
			fechaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionIn,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionIn, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getFechaIngreso()));
				}
			});
			TableColumn<SolInspeccionIn, String> telefonoColum = new TableColumn<>("Telefono");
			telefonoColum.setMinWidth(10);
			telefonoColum.setPrefWidth(100);
			telefonoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionIn,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionIn, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getTelefonoContacto()));
				}
			});
			
			tvDatos.getColumns().addAll(idColum, nombresColum,fechaColum,telefonoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
}
