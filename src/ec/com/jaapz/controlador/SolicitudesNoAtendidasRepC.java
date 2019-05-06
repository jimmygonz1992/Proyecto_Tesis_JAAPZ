package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.List;

import ec.com.jaapz.modelo.SolInspeccionRep;
import ec.com.jaapz.modelo.SolInspeccionRepDAO;
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

public class SolicitudesNoAtendidasRepC {
	SolInspeccionRepDAO reparacionDAO = new SolInspeccionRepDAO();
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	private @FXML TableView<SolInspeccionRep> tvDatos;
	public void initialize(){
		llenarDatos();
		tvDatos.setRowFactory(tv -> {
			TableRow<SolInspeccionRep> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
					if(tvDatos.getSelectionModel().getSelectedItem() != null){
						Context.getInstance().setReparacion(tvDatos.getSelectionModel().getSelectedItem());
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
			List<SolInspeccionRep> listaClientes;
			ObservableList<SolInspeccionRep> datos = FXCollections.observableArrayList();
			listaClientes = reparacionDAO.getSolicitudesNoAtendidas();
			datos.setAll(listaClientes);

			//llenar los datos en la tabla
			TableColumn<SolInspeccionRep, String> idColum = new TableColumn<>("No. solicitud");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(90);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionRep,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionRep, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdSolicitudRep()));
				}
			});
			
			TableColumn<SolInspeccionRep, String> nombresColum = new TableColumn<>("Nombres");
			nombresColum.setMinWidth(10);
			nombresColum.setPrefWidth(300);
			nombresColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionRep,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionRep, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getCuentaCliente().getCliente().getNombre() + " " + param.getValue().getCuentaCliente().getCliente().getApellido());
				}
			});
			TableColumn<SolInspeccionRep, String> fechaColum = new TableColumn<>("Fecha");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(100);
			fechaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionRep,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionRep, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(formateador.format(param.getValue().getFecha())));
				}
			});
			TableColumn<SolInspeccionRep, String> telefonoColum = new TableColumn<>("Telefono");
			telefonoColum.setMinWidth(10);
			telefonoColum.setPrefWidth(100);
			telefonoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionRep,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionRep, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getTelfContacto()));
				}
			});
			
			tvDatos.getColumns().addAll(idColum, nombresColum,fechaColum,telefonoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
}
