package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ec.com.jaapz.modelo.SolInspeccionRep;
import ec.com.jaapz.modelo.SolInspeccionRepDAO;
import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.Context;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
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

public class SolicitudListaRepC {
	@FXML TextField txtBuscar;
	@FXML TableView<SolInspeccionRep> tvDatos;
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	
	SolInspeccionRepDAO reparacionDAO = new SolInspeccionRepDAO();
	List<SolInspeccionRep> listadoInspecciones = new ArrayList<SolInspeccionRep>();
	
	public void initialize() {
		/*listadoInspecciones = Context.getInstance().getListaInspeccionesRep();
		//poner nuevamente a null
		Context.getInstance().getListaInspeccionesRep().clear();*/
		
		llenarTablaInspecciones("");
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
		
		//solo letras mayusculas
				txtBuscar.textProperty().addListener(new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
						// TODO Auto-generated method stub
						String cadena = txtBuscar.getText().toUpperCase();
						txtBuscar.setText(cadena);
					}
				});
	}
	
	public void buscarCliente() {
		llenarTablaInspecciones(txtBuscar.getText());
	}
	
	@SuppressWarnings("unchecked")
	void llenarTablaInspecciones(String patron) {
		try{
			tvDatos.getColumns().clear();
			boolean bandera;
			List<SolInspeccionRep> listaInspecciones;
			List<SolInspeccionRep> listaAgregar = new ArrayList<SolInspeccionRep>();
			if(Context.getInstance().getIdPerfil() == Constantes.ID_USU_ADMINISTRADOR) {
				listaInspecciones = reparacionDAO.getListaInspeccionPendiente(patron);
			}else {
				listaInspecciones = reparacionDAO.getListaInspeccionPerfilPendiente(patron);
			}
			for(SolInspeccionRep inspeccionAdd : listaInspecciones) {
				bandera = false;
				for(SolInspeccionRep inspeccionLst : listadoInspecciones) {
					if(inspeccionAdd.getIdSolicitudRep() == inspeccionLst.getIdSolicitudRep())
						bandera = true;
				}
				if(bandera == false)
					listaAgregar.add(inspeccionAdd);
			}
			
			ObservableList<SolInspeccionRep> datos = FXCollections.observableArrayList();
			datos.setAll(listaAgregar);

			//llenar los datos en la tabla
			TableColumn<SolInspeccionRep, String> idColum = new TableColumn<>("Código");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(200);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionRep,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionRep, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdSolicitudRep()));
				}
			});
			
			TableColumn<SolInspeccionRep, String> fechaColum = new TableColumn<>("Fecha");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(200);
			fechaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionRep,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionRep, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(formateador.format(param.getValue().getFecha())));
				}
			});

			TableColumn<SolInspeccionRep, String> clienteColum = new TableColumn<>("Cliente");
			clienteColum.setMinWidth(10);
			clienteColum.setPrefWidth(250);
			clienteColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionRep,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionRep, String> param) {
					String cliente = "";
					cliente = param.getValue().getCuentaCliente().getCliente().getNombre() + " " + param.getValue().getCuentaCliente().getCliente().getApellido();
					return new SimpleObjectProperty<String>(cliente);
				}
			});
			
			//falta campo referencia en la base de datos y en el modelo y luego borrar el llenado de abajo
			
			/*TableColumn<SolInspeccionRep, String> referenciaColum = new TableColumn<>("Referencia");
			referenciaColum.setMinWidth(10);
			referenciaColum.setPrefWidth(200);
			referenciaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionRep,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionRep, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getReferencia);
				}
			});*/

			TableColumn<SolInspeccionRep, String> referenciaColum = new TableColumn<>("Referencia");
			referenciaColum.setMinWidth(10);
			referenciaColum.setPrefWidth(350);
			referenciaColum.setCellValueFactory(new PropertyValueFactory<SolInspeccionRep, String>("referencia"));
			
			TableColumn<SolInspeccionRep, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(200);
			estadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionRep,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionRep, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getEstadoInspecRep());
				}
			});

			tvDatos.getColumns().addAll(idColum, fechaColum,clienteColum,referenciaColum,estadoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
}