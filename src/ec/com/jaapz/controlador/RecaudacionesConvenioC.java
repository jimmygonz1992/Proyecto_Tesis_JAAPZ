package ec.com.jaapz.controlador;

import java.util.Collections;

import ec.com.jaapz.modelo.CuentaCliente;
import ec.com.jaapz.modelo.Pago;
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

public class RecaudacionesConvenioC {

	@FXML private Button btnGenerar;
	@FXML private TextField txtNumMeses;
	@FXML private TableView<?> tvDetalleConvenio;
	@FXML private TextField txtMedidor;
	@FXML private TextField txtTotalDeuda;
	@FXML private Button btnLimpiar;
	@FXML private TextField txtidCuenta;
	@FXML private TextField txtCliente;
	@FXML private TextField txtCedula;
	@FXML private Button btnGrabar;
	@FXML private Button btnBuscar;
	@FXML private TableView<Planilla> tvPlanillasImpagas;

	ControllerHelper helper = new ControllerHelper();
	CuentaCliente cuentaSeleccionada = new CuentaCliente();
	
	public void buscarCliente() {
		try{
			helper.abrirPantallaModal("/recaudaciones/RecaudacionesListadoPlanillasEmitidas.fxml","Facturas Sin Cobrar", Context.getInstance().getStage());
			if (Context.getInstance().getCuentaCliente() != null) {
				cuentaSeleccionada = Context.getInstance().getCuentaCliente();
				llenarDatos(cuentaSeleccionada);
				Context.getInstance().setCuentaCliente(null);				
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	@SuppressWarnings({ "unused", "unchecked" })
	void llenarDatos(CuentaCliente datoSeleccionado){
		try {
			tvPlanillasImpagas.getColumns().clear();
			tvPlanillasImpagas.getItems().clear();
			
			if(datoSeleccionado.getMedidor() != null)
				if(datoSeleccionado.getMedidor().getCodigo() == null)
					txtMedidor.setText("");
				else
					txtMedidor.setText(datoSeleccionado.getMedidor().getCodigo());

			if(datoSeleccionado.getIdCuenta() == null)
				txtidCuenta.setText("");
			else
				txtidCuenta.setText(String.valueOf(datoSeleccionado.getIdCuenta()));

			if(datoSeleccionado.getCliente().getCedula() == null)
				txtCedula.setText("");
			else
				txtCedula.setText(datoSeleccionado.getCliente().getCedula());

			if(datoSeleccionado.getCliente().getNombre() + " " + datoSeleccionado.getCliente().getApellido() == null)
				txtCliente.setText("");
			else
				txtCliente.setText(datoSeleccionado.getCliente().getNombre() + " " + datoSeleccionado.getCliente().getApellido());


			System.out.println("Planillas: " + datoSeleccionado.getPlanillas().size());
			
			if (datoSeleccionado.getPlanillas().size() > 0) {
				
				ObservableList<Planilla> planillas = FXCollections.observableArrayList();
				for(Planilla pla : datoSeleccionado.getPlanillas()) {
					if (pla.getCancelado() != null)
						if(pla.getIdentificadorProceso() == null) // quiere decir que no esta pendiente de procesar
							if (pla.getCancelado().equals(Constantes.EST_FAC_PENDIENTE)) 
								planillas.add(pla);
				}
				System.out.println(planillas.size());
				Collections.sort(planillas);

				TableColumn<Planilla, String> idColum = new TableColumn<>("Id");
				idColum.setMinWidth(10);
				idColum.setPrefWidth(80);
				idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Planilla, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Planilla, String> param) {
						return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdPlanilla()));
					}
				});

				TableColumn<Planilla, String> descripcionColum = new TableColumn<>("Descripción");
				descripcionColum.setMinWidth(10);
				descripcionColum.setPrefWidth(80);
				descripcionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Planilla, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Planilla, String> param) {
						return new SimpleObjectProperty<String>("Factura mes de: " + String.valueOf(param.getValue().getAperturaLectura().getMe().getDescripcion()));
					}
				});

				TableColumn<Planilla, String> lecturaAnteriorColum = new TableColumn<>("Lectura Anterior");
				lecturaAnteriorColum.setMinWidth(10);
				lecturaAnteriorColum.setPrefWidth(80);
				lecturaAnteriorColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Planilla, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Planilla, String> param) {
						return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getLecturaAnterior()));
					}
				});

				TableColumn<Planilla, String> lecturaActualColum = new TableColumn<>("Lectura Actual");
				lecturaActualColum.setMinWidth(10);
				lecturaActualColum.setPrefWidth(80);
				lecturaActualColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Planilla, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Planilla, String> param) {
						return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getLecturaActual()));
					}
				});

				TableColumn<Planilla, String> consumoColum = new TableColumn<>("Consumo");
				consumoColum.setMinWidth(10);
				consumoColum.setPrefWidth(80);
				consumoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Planilla, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Planilla, String> param) {
						return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getConsumo()));
					}
				});

				TableColumn<Planilla, String> valorPagoColum = new TableColumn<>("Valor a pagar");
				valorPagoColum.setMinWidth(10);
				valorPagoColum.setPrefWidth(80);
				valorPagoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Planilla, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Planilla, String> param) {
						double total = 0.0;
						for(Pago pa : param.getValue().getPagos()){
							total = total + pa.getValor();
						}
						return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getTotalPagar() - total));
					}
				});

				tvPlanillasImpagas.getColumns().addAll(idColum, lecturaAnteriorColum, lecturaActualColum, consumoColum, valorPagoColum);
				tvPlanillasImpagas.setItems(planillas);
				
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void generarDetalleConvenio() {
		try {

		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void grabarConvenio() {
		try {

		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
