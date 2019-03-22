package ec.com.jaapz.controlador;

import java.util.List;
import java.util.Optional;

import ec.com.jaapz.modelo.CuentaCliente;
import ec.com.jaapz.modelo.CuentaClienteDAO;
import ec.com.jaapz.modelo.Planilla;
import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class CorteServicioC {
	@FXML private CheckBox chkCorte;
	@FXML private Button btnGrabar;
	@FXML private TableView<CuentaCliente> tvDatos;
	ControllerHelper helper = new ControllerHelper();
	
	CuentaClienteDAO cuentaClienteDao = new CuentaClienteDAO();
	
	public void initialize() {
		try {
			llenarDatos("");
			tvDatos.setEditable(true);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
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
			
			for(CuentaCliente cuenta : listaCuentas) {
				int cont = 0;
				for(Planilla planilla : cuenta.getPlanillas()) {
					if(planilla.getCancelado() != null)
						if(planilla.getCancelado().equals(Constantes.EST_FAC_PENDIENTE))
							cont = cont + 1;
				}
				if(cont > 0) {
					if(cont >= 3)
						datosCuenta.add(cuenta);
				}
					
			}

			//llenar los datos en la tabla
			TableColumn<CuentaCliente, String> idColum = new TableColumn<>("Id");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(40);
			idColum.setCellValueFactory(new PropertyValueFactory<CuentaCliente, String>("idCuenta"));
			
			TableColumn<CuentaCliente, String> cedulaColum = new TableColumn<>("Cédula");
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
			medidorColum.setPrefWidth(110);
			medidorColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CuentaCliente, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CuentaCliente, String> param) {
					String codigoMedidor =  "";
					if(param.getValue().getMedidor() != null)
						codigoMedidor = String.valueOf(param.getValue().getMedidor().getCodigo());
					return new SimpleObjectProperty<String>(codigoMedidor);
				}
			});
			
			TableColumn<CuentaCliente, String> direccionColum = new TableColumn<>("Dirección");
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
					int cont = 0;
					for(Planilla planilla : param.getValue().getPlanillas()) {
						if(planilla.getCancelado() != null)
							if(planilla.getCancelado().equals(Constantes.EST_FAC_PENDIENTE))
								cont = cont + 1;
					}
					return new SimpleObjectProperty<String>(String.valueOf(cont));
				}
			});
			
			TableColumn<CuentaCliente, Boolean> activeColumn = new TableColumn<CuentaCliente, Boolean>("Servicio Cortado");
			activeColumn.setCellValueFactory(new Callback<CellDataFeatures<CuentaCliente, Boolean>, ObservableValue<Boolean>>() {
				@Override
				public ObservableValue<Boolean> call(CellDataFeatures<CuentaCliente, Boolean> param) {
					CuentaCliente val = param.getValue();
					SimpleBooleanProperty booleanProp;
					if(val.getCortado() != null)
						booleanProp = new SimpleBooleanProperty(val.getCortado());
					else
						booleanProp = new SimpleBooleanProperty(false);
					booleanProp.addListener(new ChangeListener<Boolean>() {
						@Override
						public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
								Boolean newValue) {
							val.setCortado(newValue);
						}
					});
					return booleanProp;
				}
			});
			activeColumn.setCellFactory(new Callback<TableColumn<CuentaCliente, Boolean>, 
					TableCell<CuentaCliente, Boolean>>() {
				@Override
				public TableCell<CuentaCliente, Boolean> call(TableColumn<CuentaCliente, Boolean> p) {
					CheckBoxTableCell<CuentaCliente, Boolean> cell = new CheckBoxTableCell<CuentaCliente, Boolean>();
					cell.setAlignment(Pos.CENTER);
					return cell;
				}
			});
			
			tvDatos.getColumns().addAll(medidorColum, cedulaColum, clienteColum, direccionColum, totalColum, activeColumn);
			tvDatos.setItems(datosCuenta);
			
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}

	public void grabar() {
		try {			
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				cuentaClienteDao.getEntityManager().getTransaction().begin();
				for(CuentaCliente cuenta : tvDatos.getItems()) {
					if(cuenta.getCortado() != null)
						cuentaClienteDao.getEntityManager().merge(cuenta);
				}
				cuentaClienteDao.getEntityManager().getTransaction().commit();
				helper.mostrarAlertaInformacion("Datos guardados correctamente!!!", Context.getInstance().getStage());
			}					
		}catch(Exception ex) {
			cuentaClienteDao.getEntityManager().getTransaction().rollback();
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
			System.out.println(ex.getMessage());
		}
	}
}