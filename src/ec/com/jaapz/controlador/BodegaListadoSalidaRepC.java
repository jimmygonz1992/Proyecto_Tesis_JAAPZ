package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.List;

import ec.com.jaapz.modelo.Reparacion;
import ec.com.jaapz.modelo.ReparacionDAO;
import ec.com.jaapz.util.Context;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class BodegaListadoSalidaRepC {
	
	@FXML private TextField txtBuscar;
	@FXML private TableView<Reparacion> tvDatos;
	ReparacionDAO reparacionDao = new ReparacionDAO();
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	
	public void initialize() {
		llenarDatos("");
		tvDatos.setRowFactory(tv -> {
            TableRow<Reparacion> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                	if(tvDatos.getSelectionModel().getSelectedItem() != null){
                		Context.getInstance().setReparaciones(tvDatos.getSelectionModel().getSelectedItem());
    					Context.getInstance().getStageModal().close();
    				}
                }
            });
            return row ;
        });
	}
	
	public void buscarLiquidCuenta() {
		llenarDatos(txtBuscar.getText());
	}
	
	@SuppressWarnings("unchecked")
	void llenarDatos(String patron) {
		try{
			tvDatos.getColumns().clear();
			tvDatos.getItems().clear();
			List<Reparacion> listaSalidaReparaciones;
			if(Context.getInstance().getIdPerfil() == 1) {
				listaSalidaReparaciones = reparacionDao.getListadoSalidaReparaciones(patron);
			}else {
				listaSalidaReparaciones = reparacionDao.getListaSalidaReparacionesPerfil(patron);
			}
			
			ObservableList<Reparacion> datosReq = FXCollections.observableArrayList();
			datosReq.setAll(listaSalidaReparaciones);

			//llenar los datos en la tabla
			TableColumn<Reparacion, String> idColum = new TableColumn<>("N�");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(40);
			idColum.setCellValueFactory(new PropertyValueFactory<Reparacion, String>("idReparacion"));
			
			TableColumn<Reparacion, String> ordenColum = new TableColumn<>("Inspecci�n");
			ordenColum.setMinWidth(10);
			ordenColum.setPrefWidth(40);
			ordenColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reparacion, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Reparacion, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getSolInspeccionRep().getIdSolicitudRep()));
				}
			});

			TableColumn<Reparacion, String> fechaOrdenColum = new TableColumn<>("Fecha de inspecci�n");
			fechaOrdenColum.setMinWidth(10);
			fechaOrdenColum.setPrefWidth(80);
			fechaOrdenColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reparacion, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Reparacion, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(formateador.format(param.getValue().getSolInspeccionRep().getFecha())));
				}
			});
					
			TableColumn<Reparacion, String> cedulaColum = new TableColumn<>("C�dula");
			cedulaColum.setMinWidth(10);
			cedulaColum.setPrefWidth(80);
			cedulaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reparacion, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Reparacion, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getCuentaCliente().getCliente().getCedula());
				}
			});
			
			TableColumn<Reparacion, String> clienteColum = new TableColumn<>("Cliente");
			clienteColum.setMinWidth(10);
			clienteColum.setPrefWidth(80);
			clienteColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reparacion, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Reparacion, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getCuentaCliente().getCliente().getNombre() + " " + param.getValue().getCuentaCliente().getCliente().getApellido());
				}
			});
			
			
			TableColumn<Reparacion, String> direccionColum = new TableColumn<>("Direcci�n");
			direccionColum.setMinWidth(10);
			direccionColum.setPrefWidth(80);
			direccionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reparacion, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Reparacion, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getCuentaCliente().getDireccion());
				}
			});
			
			TableColumn<Reparacion, String> referenciaColum = new TableColumn<>("Referencia");
			referenciaColum.setMinWidth(10);
			referenciaColum.setPrefWidth(80);
			referenciaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reparacion, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Reparacion, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getReferencia());
				}
			});
			
			TableColumn<Reparacion, String> estadoInspColum = new TableColumn<>("Estado Inspecci�n");
			estadoInspColum.setMinWidth(10);
			estadoInspColum.setPrefWidth(80);
			estadoInspColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reparacion, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Reparacion, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getSolInspeccionRep().getEstadoInspecRep());
				}
			});
			
			TableColumn<Reparacion, String> estadoEntregaInspColum = new TableColumn<>("Estado Entrega");
			estadoEntregaInspColum.setMinWidth(10);
			estadoEntregaInspColum.setPrefWidth(80);
			estadoEntregaInspColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reparacion, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Reparacion, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getEstadoEntrega());
				}
			});
			
			
			tvDatos.getColumns().addAll(idColum, ordenColum, fechaOrdenColum, cedulaColum, clienteColum, direccionColum, referenciaColum, estadoInspColum, estadoEntregaInspColum);
			tvDatos.setItems(datosReq);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
}