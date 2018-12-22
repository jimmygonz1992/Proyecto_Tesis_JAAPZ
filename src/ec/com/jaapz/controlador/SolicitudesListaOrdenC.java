package ec.com.jaapz.controlador;

import java.util.List;

import ec.com.jaapz.modelo.SolInspeccionIn;
import ec.com.jaapz.modelo.SolInspeccionInDAO;
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

public class SolicitudesListaOrdenC {
	@FXML TextField txtBuscar;
	@FXML TableView<SolInspeccionIn> tvDatos;
	SolInspeccionInDAO inspeccionDAO = new SolInspeccionInDAO();
	public void initialize() {
		llenarTablaInspecciones("");
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
		try {
			llenarTablaInspecciones(txtBuscar.getText());	
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	private void llenarTablaInspecciones(String patron) {
		try{
			tvDatos.getColumns().clear();
			List<SolInspeccionIn> listaInspecciones;
			
			if(Context.getInstance().getIdPerfil() == 1) {
				listaInspecciones = inspeccionDAO.getListaInspeccion(patron);
			}else {
				listaInspecciones = inspeccionDAO.getListaInspeccionPerfil(patron);
			}
			
			
			ObservableList<SolInspeccionIn> datos = FXCollections.observableArrayList();
			datos.setAll(listaInspecciones);

			//llenar los datos en la tabla
			TableColumn<SolInspeccionIn, String> idColum = new TableColumn<>("Código");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(50);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionIn,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionIn, String> param) {
					String dato = "";
					dato = String.valueOf(param.getValue().getIdSolInspeccion());
					return new SimpleObjectProperty<String>(dato);
				}
			});

			TableColumn<SolInspeccionIn, String> fechaColum = new TableColumn<>("Fecha");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(150);
			fechaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionIn,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionIn, String> param) {
					String dato = "";
					dato = String.valueOf(param.getValue().getFechaIngreso());
					return new SimpleObjectProperty<String>(dato);
				}
			});

			TableColumn<SolInspeccionIn, String> clienteColum = new TableColumn<>("Cliente");
			clienteColum.setMinWidth(10);
			clienteColum.setPrefWidth(250);
			clienteColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionIn,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionIn, String> param) {
					String cliente = "";
					cliente = param.getValue().getCliente().getNombre() + " " + param.getValue().getCliente().getApellido();
					return new SimpleObjectProperty<String>(cliente);
				}
			});

			TableColumn<SolInspeccionIn, String> referenciaColum = new TableColumn<>("Referencia");
			referenciaColum.setMinWidth(10);
			referenciaColum.setPrefWidth(350);
			referenciaColum.setCellValueFactory(new PropertyValueFactory<SolInspeccionIn, String>("referencia"));

			TableColumn<SolInspeccionIn, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(90);
			estadoColum.setCellValueFactory(new PropertyValueFactory<SolInspeccionIn, String>("estadoInspeccion"));


			tvDatos.getColumns().addAll(idColum, fechaColum,clienteColum,referenciaColum,estadoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
}
