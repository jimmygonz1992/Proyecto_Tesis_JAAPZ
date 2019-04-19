package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ec.com.jaapz.modelo.CuentaCliente;
import ec.com.jaapz.modelo.CuentaClienteDAO;
import ec.com.jaapz.modelo.Pago;
import ec.com.jaapz.modelo.Planilla;
import ec.com.jaapz.modelo.PlanillaDetalle;
import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.PrintReport;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class DeudaPorClienteC {
	@FXML private TextField txtBuscar;
	@FXML private Button btnImprimir;

	private @FXML TableView<CuentaCliente> tvDatosCuentas;
	private @FXML TableView<Planilla> tvDatos;
	private @FXML TableView<PlanillaDetalle> tvDatosDetalle;
	CuentaClienteDAO cuentaClienteDao = new CuentaClienteDAO();
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	
	Double val = 0.0;
	
	public void initialize() {
		try {
			llenarDatos("");

			tvDatosCuentas.setRowFactory(tv -> {
				TableRow<CuentaCliente> row = new TableRow<>();
				row.setOnMouseClicked(event -> {
					if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
						if(tvDatosCuentas.getSelectionModel().getSelectedItem() != null){
							CuentaCliente cuentaSelec = tvDatosCuentas.getSelectionModel().getSelectedItem();
							recuperarDetalleCuenta(cuentaSelec);
							tvDatosDetalle.getItems().clear();
							tvDatosDetalle.getColumns().clear();
						}
					}
				});
				return row ;
			});
			
			tvDatos.setRowFactory(tv -> {
				TableRow<Planilla> row = new TableRow<>();
				row.setOnMouseClicked(event -> {
					if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
						if(tvDatos.getSelectionModel().getSelectedItem() != null){
							Planilla planilla = tvDatos.getSelectionModel().getSelectedItem();
							recuperarDetallePlanilla(planilla);
						}
					}
				});
				return row ;
			});
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void recuperarDetallePlanilla(Planilla planilla) {
		try {
			List<PlanillaDetalle> detalle = new ArrayList<PlanillaDetalle>();
			ObservableList<PlanillaDetalle> datos = FXCollections.observableArrayList();
			tvDatosDetalle.getColumns().clear();
			tvDatosDetalle.getItems().clear();
			for(PlanillaDetalle detallePlanilla : planilla.getPlanillaDetalles()) {
				PlanillaDetalle detAdd = new PlanillaDetalle();
				detAdd.setIdPlanillaDet(detallePlanilla.getIdPlanillaDet());
				detAdd.setCantidad(detallePlanilla.getCantidad());
				detAdd.setDescripcion(detallePlanilla.getDescripcion());
				detAdd.setSubtotal(detallePlanilla.getSubtotal());
				detalle.add(detAdd);
			}
			datos.setAll(detalle);
			Collections.sort(datos);

			TableColumn<PlanillaDetalle, String> idColum = new TableColumn<>("Id");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(40);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlanillaDetalle, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<PlanillaDetalle, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdPlanillaDet()));
				}
			});

			TableColumn<PlanillaDetalle, String> cantidadColum = new TableColumn<>("Cantidad");
			cantidadColum.setMinWidth(10);
			cantidadColum.setPrefWidth(50);
			cantidadColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlanillaDetalle, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<PlanillaDetalle, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCantidad()));
				}
			});

			TableColumn<PlanillaDetalle, String> descripcionColum = new TableColumn<>("Descripción");
			descripcionColum.setMinWidth(10);
			descripcionColum.setPrefWidth(400);
			descripcionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlanillaDetalle, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<PlanillaDetalle, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getDescripcion()));
				}
			});

			TableColumn<PlanillaDetalle, String> subtotalColum = new TableColumn<>("Subtotal");
			subtotalColum.setMinWidth(10);
			subtotalColum.setPrefWidth(100);
			subtotalColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlanillaDetalle, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<PlanillaDetalle, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getSubtotal()));
				}
			});

			tvDatosDetalle.getColumns().addAll(idColum, cantidadColum, descripcionColum, subtotalColum);
			tvDatosDetalle.setItems(datos);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void recuperarDetalleCuenta(CuentaCliente cuentaC) {
		try {
			tvDatos.getColumns().clear();
			tvDatos.getItems().clear();
			if(cuentaC.getPlanillas().size()>0) {
				ObservableList<Planilla> datos = FXCollections.observableArrayList();
				for(Planilla pla : cuentaC.getPlanillas()) {
					if (pla.getCancelado() != null)
						if(pla.getIdentificadorProceso() == null) // quiere decir que no esta pendiente de procesar
							if (pla.getCancelado().equals(Constantes.EST_FAC_PENDIENTE)) 
								datos.add(pla);
				}
				Collections.sort(datos);

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

				tvDatos.getColumns().addAll(idColum, lecturaAnteriorColum, lecturaActualColum, consumoColum, valorPagoColum);
				tvDatos.setItems(datos);
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void imprimir() {
		PrintReport pr = new PrintReport();
		Map<String, Object> param = new HashMap<String, Object>();
		CuentaCliente cuentaSelec = tvDatosCuentas.getSelectionModel().getSelectedItem();
		param.put("idCliente", cuentaSelec.getCliente().getIdCliente());
		param.put("cedula", cuentaSelec.getCliente().getCedula());
		pr.crearReporte("/recursos/informes/deudaPorCliente.jasper", cuentaClienteDao, param);
		pr.showReport("Deuda por cada cliente");
	}
	
	public void buscarCliente() {
		llenarDatos(txtBuscar.getText());
	}
	
	@SuppressWarnings("unchecked")
	void llenarDatos(String patron) {
		try{
			tvDatosCuentas.getColumns().clear();
			List<CuentaCliente> listaCuentas;
			listaCuentas = cuentaClienteDao.getListaCuentaClientes(patron);
			ObservableList<CuentaCliente> datosCuenta = FXCollections.observableArrayList();
			
			for(CuentaCliente cuenta : listaCuentas) {
				int cont = 0;
				for(Planilla planilla : cuenta.getPlanillas()) {
					if(planilla.getCancelado() != null)
						if(planilla.getCancelado().equals(Constantes.EST_FAC_PENDIENTE))
							cont = cont + 1;
				}
				if(cont > 0)
					datosCuenta.add(cuenta);
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
			
			TableColumn<CuentaCliente, String> totalValColum = new TableColumn<>("Total Deuda");
			totalValColum.setMinWidth(10);
			totalValColum.setPrefWidth(80);
			totalValColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CuentaCliente, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<CuentaCliente, String> param) {
					double valorPagar = 0.0;
					double totalPagos = 0.0;
					for(Planilla planilla : param.getValue().getPlanillas()) {
						if(planilla.getCancelado() != null) {
							if(planilla.getCancelado().equals(Constantes.EST_FAC_PENDIENTE)) {
								for(Pago pa : planilla.getPagos()) {
									totalPagos = totalPagos + pa.getValor();
								}
								valorPagar = valorPagar + planilla.getTotalPagar();
							}
						}
					}
					return new SimpleObjectProperty<String>(String.valueOf(valorPagar - totalPagos));
				}
			});
			
			tvDatosCuentas.getColumns().addAll(idColum, cedulaColum, clienteColum, medidorColum, direccionColum, totalColum, totalValColum);
			tvDatosCuentas.setItems(datosCuenta);
			
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
}