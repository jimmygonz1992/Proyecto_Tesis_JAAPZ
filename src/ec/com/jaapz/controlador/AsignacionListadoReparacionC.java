package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ec.com.jaapz.modelo.Reparacion;
import ec.com.jaapz.modelo.ReparacionDAO;
import ec.com.jaapz.util.Constantes;
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
import javafx.util.Callback;

public class AsignacionListadoReparacionC {
	@FXML TextField txtBuscar;
	@FXML TableView<Reparacion> tvDatos;
	ReparacionDAO reparacionDAO = new ReparacionDAO();
	List<Reparacion> listadoReparaciones = new ArrayList<Reparacion>();
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	
	public void initialize() {
		listadoReparaciones = Context.getInstance().getListaReparaciones();
		//poner nuevamente a null
		Context.getInstance().getListaReparaciones().clear();
		llenarTablaReparaciones("");
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
	
	public void buscarCliente() {
		llenarTablaReparaciones(txtBuscar.getText());
	}
	
	@SuppressWarnings("unchecked")
	void llenarTablaReparaciones(String patron) {
		try{
			tvDatos.getColumns().clear();
			boolean bandera;
			List<Reparacion> listaReparaciones;
			List<Reparacion> listaAgregar = new ArrayList<Reparacion>();
			if(Context.getInstance().getIdPerfil() == Constantes.ID_USU_ADMINISTRADOR) {
				listaReparaciones = reparacionDAO.getListaReparacionPendiente(patron);
			}else {
				listaReparaciones = reparacionDAO.getListaReparacionPerfilPendiente(patron);
			}
			for(Reparacion reparacionAdd : listaReparaciones) {
				bandera = false;
				for(Reparacion reparacionLst : listaReparaciones) {
					if(reparacionAdd.getIdReparacion() == reparacionLst.getIdReparacion())
						bandera = true;
				}
				if(bandera == false)
					listaAgregar.add(reparacionAdd);
			}
			
			ObservableList<Reparacion> datos = FXCollections.observableArrayList();
			datos.setAll(listaAgregar);

			//llenar los datos en la tabla
			TableColumn<Reparacion, String> idColum = new TableColumn<>("Id");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(100);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reparacion,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Reparacion, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdReparacion()));
				}
			});

			TableColumn<Reparacion, String> fechaColum = new TableColumn<>("Fecha");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(100);
			fechaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reparacion,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Reparacion, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(formateador.format(param.getValue().getFechaCierreInspeccion())));
				}
			});

			TableColumn<Reparacion, String> clienteColum = new TableColumn<>("Cliente");
			clienteColum.setMinWidth(10);
			clienteColum.setPrefWidth(250);
			clienteColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reparacion,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Reparacion, String> param) {
					String cliente = "";
					cliente = param.getValue().getCuentaCliente().getCliente().getNombre() + " " + param.getValue().getCuentaCliente().getCliente().getApellido();
					return new SimpleObjectProperty<String>(cliente);
				}
			});
			
			TableColumn<Reparacion, String> referenciaColum = new TableColumn<>("Referencia");
			referenciaColum.setMinWidth(10);
			referenciaColum.setPrefWidth(100);
			referenciaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reparacion,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Reparacion, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getReferencia());
				}
			});
			
			TableColumn<Reparacion, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(100);
			estadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reparacion,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Reparacion, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getEstadoReparacion());
				}
			});

			tvDatos.getColumns().addAll(idColum, fechaColum,clienteColum,referenciaColum,estadoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}	
}