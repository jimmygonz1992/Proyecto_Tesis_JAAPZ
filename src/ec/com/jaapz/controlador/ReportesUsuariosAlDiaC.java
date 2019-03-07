package ec.com.jaapz.controlador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ec.com.jaapz.modelo.CuentaCliente;
import ec.com.jaapz.modelo.CuentaClienteDAO;
import ec.com.jaapz.modelo.Planilla;
import ec.com.jaapz.modelo.PlanillaDAO;
import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.Encriptado;
import ec.com.jaapz.util.PrintReport;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class ReportesUsuariosAlDiaC {
	@FXML private Button btnReporte;
	private @FXML TableView<CuentaCliente> tvDatos;
	CuentaClienteDAO cuentaClienteDao = new CuentaClienteDAO();
	PlanillaDAO planillaDao = new PlanillaDAO();
	public void initialize(){
		btnReporte.setStyle("-fx-cursor: hand;");
		llenarDatos("");
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
				if(cont == 0)
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
			
			tvDatos.getColumns().addAll(cedulaColum, clienteColum, medidorColum, direccionColum, telefonoColum);
			tvDatos.setItems(datosCuenta);
			
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	public void verReporte() {
		try {
			PrintReport pr = new PrintReport();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("usuarioCrea", Encriptado.Desencriptar(Context.getInstance().getUsuariosC().getUsuario()));
			pr.crearReporte("/recursos/informes/clientes_al_dia.jasper", cuentaClienteDao, param);
			pr.showReport("Clientes al Día");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}