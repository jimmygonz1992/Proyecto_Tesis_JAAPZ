package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ec.com.jaapz.modelo.Instalacion;
import ec.com.jaapz.modelo.InstalacionDAO;
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

public class ListadoOrdenesInstalacionesC {
	@FXML TextField txtBuscar;
	@FXML TableView<Instalacion> tvDatos;
	InstalacionDAO instalacionDao = new InstalacionDAO();
	List<Instalacion> listadoInstalaciones = new ArrayList<Instalacion>();
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	
	public void initialize() {
		listadoInstalaciones = Context.getInstance().getListaInstalaciones();
		//poner nuevamente a null
		Context.getInstance().getListaInstalaciones().clear();;
		llenarTablaInstalaciones("");
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
	}
	
	public void buscarCliente() {
		llenarTablaInstalaciones(txtBuscar.getText());
	}
	
	@SuppressWarnings("unchecked")
	void llenarTablaInstalaciones(String patron) {
		try{
			tvDatos.getColumns().clear();
			boolean bandera;
			List<Instalacion> listaInstalaciones;
			List<Instalacion> listaAgregar = new ArrayList<Instalacion>();
			if(Context.getInstance().getIdPerfil() == Constantes.ID_USU_ADMINISTRADOR) {
				listaInstalaciones = instalacionDao.getListaInstalacionPendiente(patron);
			}else {
				listaInstalaciones = instalacionDao.getListaInstalacionPerfilPendiente(patron);
			}
			for(Instalacion instalacionAdd : listaInstalaciones) {
				bandera = false;
				for(Instalacion instalacionLst : listadoInstalaciones) {
					if(instalacionAdd.getIdInstalacion() == instalacionLst.getIdInstalacion())
						bandera = true;
				}
				if(bandera == false)
					listaAgregar.add(instalacionAdd);
			}
			
			ObservableList<Instalacion> datos = FXCollections.observableArrayList();
			datos.setAll(listaAgregar);

			//llenar los datos en la tabla
			TableColumn<Instalacion, String> idColum = new TableColumn<>("Id");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(100);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Instalacion,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Instalacion, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdInstalacion()));
				}
			});

			TableColumn<Instalacion, String> fechaColum = new TableColumn<>("Fecha");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(100);
			fechaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Instalacion,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Instalacion, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(formateador.format(param.getValue().getSolInspeccionIn().getFechaIngreso())));
				}
			});

			TableColumn<Instalacion, String> clienteColum = new TableColumn<>("Cliente");
			clienteColum.setMinWidth(10);
			clienteColum.setPrefWidth(250);
			clienteColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Instalacion,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Instalacion, String> param) {
					String cliente = "";
					cliente = param.getValue().getCuentaCliente().getCliente().getNombre() + " " + param.getValue().getCuentaCliente().getCliente().getApellido();
					return new SimpleObjectProperty<String>(cliente);
				}
			});

			TableColumn<Instalacion, String> referenciaColum = new TableColumn<>("Referencia");
			referenciaColum.setMinWidth(10);
			referenciaColum.setPrefWidth(100);
			referenciaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Instalacion,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Instalacion, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getSolInspeccionIn().getReferencia());
				}
			});

			
			TableColumn<Instalacion, String> estadoColum = new TableColumn<>("Estado Inspección");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(100);
			estadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Instalacion,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Instalacion, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getSolInspeccionIn().getEstadoInspeccion());
				}
			});
			
			tvDatos.getColumns().addAll(idColum, fechaColum,clienteColum,referenciaColum,estadoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
}