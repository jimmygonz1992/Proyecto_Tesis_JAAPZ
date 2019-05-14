package ec.com.jaapz.controlador;

import java.util.List;

import ec.com.jaapz.modelo.CuentaCliente;
import ec.com.jaapz.modelo.CuentaClienteDAO;
import ec.com.jaapz.modelo.EmpresaDAO;
import ec.com.jaapz.modelo.Planilla;
import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class CortesVerPendientesC {
	private @FXML Button btnRealizarCorte;
	private @FXML Button btnImprimir;
	private @FXML TextField txtBuscar;
	private @FXML TableView<CuentaCliente> tvDatos;
	CuentaClienteDAO cuentaDao = new CuentaClienteDAO();
	EmpresaDAO empresaDao = new EmpresaDAO();
	int numCorte;
	ControllerHelper helper = new ControllerHelper();
	
	public void initialize(){
		llenarDatos("");
		btnRealizarCorte.setStyle("-fx-cursor: hand;");
		btnImprimir.setStyle("-fx-cursor: hand;");
	}
	
	public void buscarCuenta() {
		llenarDatos(txtBuscar.getText());
	}
	
	@SuppressWarnings("unchecked")
	void llenarDatos(String patron) {
		try{
			tvDatos.getColumns().clear();
			numCorte = empresaDao.getEmpresa().get(0).getCorte();
			List<CuentaCliente> listaCuentas;
			if(Context.getInstance().getIdPerfil() == Constantes.ID_USU_ADMINISTRADOR) {
				listaCuentas = cuentaDao.getListaCuentasCortes(patron);
			}else {
				listaCuentas = cuentaDao.getListaCuentasPendPefil(patron);
			}
			ObservableList<CuentaCliente> datosCuenta = FXCollections.observableArrayList();
			
			for(CuentaCliente cuenta : listaCuentas) {
				int cont = 0;
				for(Planilla planilla : cuenta.getPlanillas()) {
					if(planilla.getCancelado() != null)
						if(planilla.getCancelado().equals(Constantes.EST_FAC_PENDIENTE))
							cont = cont + 1;
				}
				if(cont >= numCorte)
					datosCuenta.add(cuenta);
			}

			//llenar los datos en la tabla
					
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
			
			TableColumn<CuentaCliente, String> direccionColum = new TableColumn<>("Dirección");
			direccionColum.setMinWidth(10);
			direccionColum.setPrefWidth(80);
			direccionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CuentaCliente, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CuentaCliente, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getDireccion()));
				}
			});
			
			TableColumn<CuentaCliente, String> telefonoColum = new TableColumn<>("Teléfono");
			telefonoColum.setMinWidth(10);
			telefonoColum.setPrefWidth(80);
			telefonoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CuentaCliente, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CuentaCliente, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCliente().getTelefono()));
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
			
			tvDatos.getColumns().addAll(cedulaColum, clienteColum, medidorColum, direccionColum, telefonoColum, totalColum);
			tvDatos.setItems(datosCuenta);
			
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	public void realizarCorte() {
		try {
			if(tvDatos.getSelectionModel().getSelectedItem() == null) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar un registro", Context.getInstance().getStage());
				return;
			}
			Context.getInstance().setCuentaCliente(tvDatos.getSelectionModel().getSelectedItem());
			helper.abrirPantallaModalSolicitud("/cortes/RealizarCorteServicio.fxml","Corte del servicio de Agua", Context.getInstance().getStage());
			llenarDatos("");
			Context.getInstance().setCuentaCliente(null);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void imprimir(){
		
	}
}