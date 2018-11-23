package ec.com.jaapz.controlador;

import java.util.List;
import ec.com.jaapz.modelo.CuentaCliente;
import ec.com.jaapz.modelo.CuentaClienteDAO;
import ec.com.jaapz.modelo.Planilla;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class RecaudacionesPlanillasEmitidasC {
	@FXML private TextField txtBuscar;
	@FXML private TableView<CuentaCliente> tvDatos;
	
	//PlanillaDAO planillaDao = new PlanillaDAO();
	CuentaClienteDAO cuentaClienteDao = new CuentaClienteDAO();
	
	public void initialize(){
		try {			
			llenarDatos("");
			tvDatos.setRowFactory(tv -> {
				 TableRow<CuentaCliente> row = new TableRow<>();
		            row.setOnMouseClicked(event -> {
		                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
		                	if(tvDatos.getSelectionModel().getSelectedItem() != null){
		                		Context.getInstance().setCuentaCliente(tvDatos.getSelectionModel().getSelectedItem());
		                		Context.getInstance().getStageModal().close();
		    				}
		                }
		            });
	            return row ;
	        });
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void buscarCliente() {
		llenarDatos(txtBuscar.getText());
	}	
	
	@SuppressWarnings("unchecked")
	void llenarDatos(String patron) {
		try{
			tvDatos.getColumns().clear();
			List<CuentaCliente> listaCuentas;
			if(Context.getInstance().getIdPerfil() == 1) {
				listaCuentas = cuentaClienteDao.getListaCuentaClientes(patron);
			}else {
				listaCuentas = cuentaClienteDao.getListaCuentaClientePerfil(patron);
			}
			ObservableList<CuentaCliente> datosCuenta = FXCollections.observableArrayList();
			datosCuenta.setAll(listaCuentas);

			//llenar los datos en la tabla
			TableColumn<CuentaCliente, String> idColum = new TableColumn<>("Id");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(40);
			idColum.setCellValueFactory(new PropertyValueFactory<CuentaCliente, String>("idCuenta"));
			
			TableColumn<CuentaCliente, String> cedulaColum = new TableColumn<>("C�dula");
			cedulaColum.setMinWidth(10);
			cedulaColum.setPrefWidth(80);
			cedulaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CuentaCliente, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CuentaCliente, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCliente().getCedula()));
				}
			});
			
			TableColumn<CuentaCliente, String> clienteColum = new TableColumn<>("Cliente");
			clienteColum.setMinWidth(10);
			clienteColum.setPrefWidth(80);
			clienteColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CuentaCliente, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CuentaCliente, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCliente().getNombre() + " " + param.getValue().getCliente().getApellido()));
				}
			});
			
			TableColumn<CuentaCliente, String> medidorColum = new TableColumn<>("Medidor");
			medidorColum.setMinWidth(10);
			medidorColum.setPrefWidth(80);
			medidorColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CuentaCliente, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CuentaCliente, String> param) {
					String codigoMedidor =  "";
					if(param.getValue().getMedidor() != null)
						codigoMedidor = String.valueOf(param.getValue().getMedidor().getCodigo());
					return new SimpleObjectProperty<String>(codigoMedidor);
				}
			});
			
			TableColumn<CuentaCliente, String> direccionColum = new TableColumn<>("Direcci�n");
			direccionColum.setMinWidth(10);
			direccionColum.setPrefWidth(80);
			direccionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CuentaCliente, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CuentaCliente, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getDireccion()));
				}
			});
			
			TableColumn<CuentaCliente, String> totalColum = new TableColumn<>("Planillas Vencidas");
			totalColum.setMinWidth(10);
			totalColum.setPrefWidth(80);
			totalColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CuentaCliente, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CuentaCliente, String> param) {
					String numPendiente = "";
					int cont = 0;
					List<Planilla> listPla = param.getValue().getPlanillas();
					for(Planilla planilla : listPla) {
						//if (planilla.getCancelado() == "PENDIENTE")
						if(planilla.getCancelado().equals(Constantes.EST_FAC_PENDIENTE)) 
							cont = cont + 1;
					}
					numPendiente = String.valueOf(cont);
					return new SimpleObjectProperty<String>(numPendiente);
				}
			});
			
			tvDatos.getColumns().addAll(idColum,  cedulaColum, clienteColum, medidorColum, direccionColum, totalColum);
			tvDatos.setItems(datosCuenta);
			
			/*for (int i=0; i<tvDatos.getItems().size(); i++) {
				if (String.valueOf(totalColum) != "0") {
					tvDatos.getItems().remove(i);
				}
			}*/
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
}