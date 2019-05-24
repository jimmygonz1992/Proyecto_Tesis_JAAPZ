package ec.com.jaapz.controlador;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import ec.com.jaapz.modelo.CuentaCliente;
import ec.com.jaapz.modelo.Factura;
import ec.com.jaapz.modelo.FacturaDAO;
import ec.com.jaapz.modelo.FacturaDetalle;
import ec.com.jaapz.modelo.Pago;
import ec.com.jaapz.modelo.Planilla;
import ec.com.jaapz.modelo.PlanillaDetalle;
import ec.com.jaapz.modelo.SegUsuario;
import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import ec.com.jaapz.util.Encriptado;
import ec.com.jaapz.util.PrintReport;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class RecaudacionesRegistroCobroC {
	CuentaCliente cuentaSeleccionada = new CuentaCliente();
	Planilla planillaSeleccionada = new Planilla();
	@FXML private DatePicker dtpFecha;
	@FXML private TextField txtNumMedidor;
	@FXML private TextField txtCedula;
	@FXML private TextField txtNumFactura;
	@FXML private TextField txtIdCuenta;
	@FXML private TextField txtCliente;
	@FXML private TextField txtDireccion;
	@FXML private TextField txtTotal;
	@FXML private TextField txtAbono;
	@FXML private TextField txtSaldo;
	@FXML private TextField txtPagaCon;
	@FXML private TextField txtCambio;
	@FXML private Button btnBuscaCuentas;
	@FXML private Button btnCobrar;
	@FXML private Button btnLimpiar;
	@FXML private Button btnAceptar;
	@FXML private CheckBox chkAbonoTotal;
	@FXML private CheckBox chkImprimirComp;
	private @FXML TableView<Planilla> tvDatos;
	private @FXML TableView<PlanillaDetalle> tvDetalle;
	private @FXML TableView<FacturaDetalle> tvDetallePago;
	@FXML private TextField txtNumComp;
	@FXML private TextField txtUsuario;
	@FXML private TextField txtTotalPago;
	FacturaDAO facturaDao = new FacturaDAO();

	ControllerHelper helper = new ControllerHelper();
	DecimalFormat df = new DecimalFormat ("######.00");
	SegUsuario usuarioLogueado = new SegUsuario();

	public void initialize(){
		try {
			//System.out.println(facturaDao.getIdFactura()+1);
			txtNumComp.setText(String.valueOf(facturaDao.getIdFactura()+1));
			btnAceptar.setStyle("-fx-cursor: hand;");
			btnBuscaCuentas.setStyle("-fx-cursor: hand;");
			btnCobrar.setStyle("-fx-cursor: hand;");
			btnLimpiar.setStyle("-fx-cursor: hand;");
			chkImprimirComp.setSelected(true);
			
			Context.getInstance().setCuentaCliente(null);
			noEditable();
			dtpFecha.setValue(LocalDate.now());
			usuarioLogueado = Context.getInstance().getUsuariosC();
			txtUsuario.setText(Encriptado.Desencriptar(String.valueOf(Context.getInstance().getUsuariosC().getUsuario())));

			//solo numeros
			txtIdCuenta.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (newValue.matches("\\d*")) {
						//int value = Integer.parseInt(newValue);
					} else {
						txtIdCuenta.setText(oldValue);
					}
				}
			});
			//numeros con decimales
			txtAbono.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (!newValue.matches("\\d*(\\.\\d*)?")) {
						txtAbono.setText(oldValue);
					}
				}
			});

			//numeros con decimales
			txtPagaCon.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (!newValue.matches("\\d*(\\.\\d*)?")) {
						txtPagaCon.setText(oldValue);
					}
				}
			});

			tvDatos.setRowFactory(tv -> {
				TableRow<Planilla> row = new TableRow<>();
				row.setOnMouseClicked(event -> {
					if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
						if(tvDatos.getSelectionModel().getSelectedItem() != null){
							Planilla planillaSelec = tvDatos.getSelectionModel().getSelectedItem();
							recuperarDetallePlanilla(planillaSelec);
						}
					}
				});
				return row ;
			});
			sumarDatos();

			txtAbono.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					txtAbono.setText(newValue);
					if (txtAbono.getText().equals("")){
						txtSaldo.setText(newValue);
					}else if(Double.parseDouble(txtAbono.getText())<=Double.parseDouble(txtTotal.getText())){
						Double resultado = Double.valueOf(txtTotal.getText())-Double.valueOf(txtAbono.getText());
						txtSaldo.setText(resultado.toString());
					}else {
						txtSaldo.setText("0.0");
					}
				}
			});

			txtPagaCon.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					txtPagaCon.setText(newValue);
					if (txtPagaCon.getText().equals("")){
						txtCambio.setText(newValue);
					}else{
						Double resultado = Double.valueOf(txtPagaCon.getText())-Double.valueOf(txtAbono.getText());
						txtCambio.setText(resultado.toString());
					}					
				}
			});

			//valia presionando enter
			txtAbono.setOnKeyPressed(new EventHandler<KeyEvent>(){
				@Override
				public void handle(KeyEvent ke){
					if (ke.getCode().equals(KeyCode.ENTER)){
						if (Double.parseDouble(txtAbono.getText()) > 0){
							Double resultado = Double.valueOf(txtTotal.getText())-Double.valueOf(txtAbono.getText());
							txtSaldo.setText(String.valueOf(resultado));
							aceptar();
						}
						else {
							helper.mostrarAlertaAdvertencia("Verifique el valor ingresado", Context.getInstance().getStage());
							txtAbono.requestFocus();
						}
					}
				}
			});

			txtPagaCon.setOnKeyPressed(new EventHandler<KeyEvent>(){
				@Override
				public void handle(KeyEvent ke){
					if (ke.getCode().equals(KeyCode.ENTER)){
						if (Double.parseDouble(txtPagaCon.getText()) > 0){
							Double resultado = Double.valueOf(txtPagaCon.getText())-Double.valueOf(txtAbono.getText());
							txtCambio.setText(String.valueOf(resultado));
							aceptar();
						}
						else
							helper.mostrarAlertaAdvertencia("Verifique el valor ingresado", Context.getInstance().getStage());
						txtPagaCon.requestFocus();
					}
				}
			});
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void habilitarAbonoTotal() {
		try {
			if (chkAbonoTotal.isSelected()) {
				habilitar();
			}else {
				deshabilitar();
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	void habilitar() {
		try {
			txtAbono.setText(txtTotal.getText());
			txtPagaCon.requestFocus();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	void deshabilitar() {
		try {
			txtAbono.setText("");
			txtPagaCon.setText("");
			txtAbono.requestFocus();
			tvDetallePago.getItems().clear();
			tvDetallePago.getColumns().clear();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	private void recuperarDetallePlanilla(Planilla planilla) {
		try {
			List<PlanillaDetalle> detalle = new ArrayList<PlanillaDetalle>();
			ObservableList<PlanillaDetalle> datos = FXCollections.observableArrayList();
			tvDetalle.getColumns().clear();
			tvDetalle.getItems().clear();
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
			cantidadColum.setPrefWidth(110);
			cantidadColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlanillaDetalle, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<PlanillaDetalle, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCantidad()));
				}
			});

			TableColumn<PlanillaDetalle, String> descripcionColum = new TableColumn<>("Descripción");
			descripcionColum.setMinWidth(10);
			descripcionColum.setPrefWidth(430);
			descripcionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlanillaDetalle, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<PlanillaDetalle, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getDescripcion()));
				}
			});

			TableColumn<PlanillaDetalle, String> subtotalColum = new TableColumn<>("Subtotal");
			subtotalColum.setMinWidth(10);
			subtotalColum.setPrefWidth(140);
			subtotalColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlanillaDetalle, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<PlanillaDetalle, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getSubtotal()));
				}
			});

			tvDetalle.getColumns().addAll(cantidadColum, descripcionColum, subtotalColum);
			tvDetalle.setItems(datos);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void buscarCuenta() {
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
			tvDatos.getColumns().clear();
			tvDatos.getItems().clear();
			
			if(datoSeleccionado.getMedidor() != null) {
				if(datoSeleccionado.getMedidor().getCodigo() == null)
					txtNumMedidor.setText("Medidor No Asignado");
				else
					txtNumMedidor.setText(datoSeleccionado.getMedidor().getCodigo());
			}

			if(datoSeleccionado.getIdCuenta() == null)
				txtIdCuenta.setText("");
			else
				txtIdCuenta.setText(String.valueOf(datoSeleccionado.getIdCuenta()));

			if(datoSeleccionado.getCliente().getCedula() == null)
				txtCedula.setText("");
			else
				txtCedula.setText(datoSeleccionado.getCliente().getCedula());

			if(datoSeleccionado.getCliente().getNombre() + " " + datoSeleccionado.getCliente().getApellido() == null)
				txtCliente.setText("");
			else
				txtCliente.setText(datoSeleccionado.getCliente().getNombre() + " " + datoSeleccionado.getCliente().getApellido());

			if(datoSeleccionado.getDireccion() == null)
				txtDireccion.setText("");
			else
				txtDireccion.setText(datoSeleccionado.getDireccion());

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
				descripcionColum.setPrefWidth(110);
				descripcionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Planilla, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Planilla, String> param) {
						return new SimpleObjectProperty<String>("Factura mes de: " + String.valueOf(param.getValue().getAperturaLectura().getMe().getDescripcion()));
					}
				});

				TableColumn<Planilla, String> lecturaAnteriorColum = new TableColumn<>("Lectura Anterior");
				lecturaAnteriorColum.setMinWidth(10);
				lecturaAnteriorColum.setPrefWidth(165);
				lecturaAnteriorColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Planilla, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Planilla, String> param) {
						return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getLecturaAnterior()));
					}
				});

				TableColumn<Planilla, String> lecturaActualColum = new TableColumn<>("Lectura Actual");
				lecturaActualColum.setMinWidth(10);
				lecturaActualColum.setPrefWidth(165);
				lecturaActualColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Planilla, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Planilla, String> param) {
						return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getLecturaActual()));
					}
				});

				TableColumn<Planilla, String> consumoColum = new TableColumn<>("Consumo");
				consumoColum.setMinWidth(10);
				consumoColum.setPrefWidth(165);
				consumoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Planilla, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Planilla, String> param) {
						return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getConsumo()));
					}
				});

				TableColumn<Planilla, String> valorPagoColum = new TableColumn<>("Valor a pagar");
				valorPagoColum.setMinWidth(10);
				valorPagoColum.setPrefWidth(165);
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

				tvDatos.getColumns().addAll(lecturaAnteriorColum, lecturaActualColum, consumoColum, valorPagoColum);
				tvDatos.setItems(planillas);
				sumarDatos();
				noEditable();
				txtAbono.requestFocus();
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void sumarDatos() {
		try {
			if (tvDatos.getItems().isEmpty()) {
				//	txtTotal.setText("0.00");
				//	txtSaldo.setText("0.00");
			}else {
				double total = 0;
				for(int i=0; i<tvDatos.getItems().size(); i++) {
					Double valorTotal = new Double(tvDatos.getItems().get(i).getTotalPagar());
					total += valorTotal;
					double totalPagado = 0.0;
					for(Planilla pla : tvDatos.getItems()) {
						for(Pago pa : pla.getPagos()){
							totalPagado = totalPagado + pa.getValor();
						}
					}
					txtTotal.setText(String.valueOf(Double.valueOf(total - totalPagado)));
				}
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	void noEditable() {
		try {
			txtNumMedidor.setEditable(false);
			txtIdCuenta.setEditable(false);
			txtNumComp.setEditable(false);
			txtUsuario.setEditable(false);
			txtTotalPago.setEditable(false);
			txtCedula.setEditable(false);
			txtCliente.setEditable(false);
			txtDireccion.setEditable(false);
			txtTotal.setEditable(false);
			txtSaldo.setEditable(false);
			txtCambio.setEditable(false);
			dtpFecha.setEditable(false);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void cobrar() {
		try {
			if(validarDatos() == false)
				return;
			//validar si el cobro que se va a realizar es de una instalacion... xq si no paga no se le puede instalar el medidor, ademas no puede tener otra factura
			//xq aun no se le instala el medidor y x lo tanto no tiene lecturas registradas
			boolean verificarInstalacion = false;
			boolean verificarPago = false;
			for(Planilla pl : tvDatos.getItems()) {
				if(pl.getIdentInstalacion() != null)
					if(pl.getIdentInstalacion().equals(Constantes.IDENT_INSTALACION))
						verificarInstalacion = true;
			}
			
			if(verificarInstalacion == true) {//si es un pago de una instalacion.. entoces se debe calcular si esta pagando el 60% del valor
				double porcentaje = Double.parseDouble(txtTotal.getText()) * 0.6;
				double totalPago = 0;
				for(FacturaDetalle det : tvDetallePago.getItems())
					totalPago = totalPago + det.getSubtotal();
				if(totalPago < porcentaje)
					verificarPago = true;
			}
			if(verificarPago == true && verificarInstalacion == true) {//si es instalacion y el pago esta por debajo del 60%. manda un error
				helper.mostrarAlertaAdvertencia("El pago mínimo debe ser el 60% del costo de instalación", Context.getInstance().getStage());
				return;
			}
			
			
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				Factura factura = new Factura();
				factura.setIdFactura(null);
				Date date = Date.from(dtpFecha.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
				Timestamp fecha = new Timestamp(date.getTime());
				factura.setFecha(fecha);
				factura.setNumFactura(txtNumComp.getText());
				factura.setCuentaCliente(cuentaSeleccionada);
				factura.setUsuarioCrea(Context.getInstance().getUsuariosC().getIdUsuario());
				factura.setEstado(Constantes.ESTADO_ACTIVO);
				factura.setTotalFactura(Double.parseDouble(txtAbono.getText()));
				List<Planilla> listaPla = new ArrayList<Planilla>();

				List<FacturaDetalle> listaAgregadaPlanillas = new ArrayList<FacturaDetalle>();
				for(FacturaDetalle det : tvDetallePago.getItems()) {
					det.setIdDetalleFac(null);
					if(det.getSubtotal() >= det.getPlanilla().getTotalPagar())
						det.getPlanilla().setCancelado(Constantes.EST_FAC_CANCELADO);
					else {	
						double totalPagado = 0.0;
						for(Pago pa : det.getPlanilla().getPagos()){
							totalPagado = totalPagado + pa.getValor();
						}
						if (det.getSubtotal() >= (det.getPlanilla().getTotalPagar() - totalPagado))
							det.getPlanilla().setCancelado(Constantes.EST_FAC_CANCELADO);
					}
						
					det.setFactura(factura);
					listaPla.add(det.getPlanilla());
					det.setEstado(Constantes.ESTADO_ACTIVO);
					listaAgregadaPlanillas.add(det);
				}
				factura.setFacturaDetalles(listaAgregadaPlanillas);

				facturaDao.getEntityManager().getTransaction().begin();
				facturaDao.getEntityManager().persist(factura);

				for(Planilla pla : listaPla) {
					Pago pagoGrabar = new Pago();
					pagoGrabar.setIdPago(null);
					for(FacturaDetalle det : tvDetallePago.getItems()) {
						if(det.getPlanilla().getIdPlanilla() == pla.getIdPlanilla()) {
							//falta setear tipo de pago
							pagoGrabar.setUsuarioCrea(Context.getInstance().getUsuariosC().getIdUsuario());
							pagoGrabar.setValor(det.getSubtotal());
							pagoGrabar.setFecha(fecha);
							pagoGrabar.setEstado(Constantes.ESTADO_ACTIVO);
						}
					}
					pagoGrabar.setPlanilla(pla);
					pla.addPago(pagoGrabar);
					facturaDao.getEntityManager().merge(pla);
				}

				facturaDao.getEntityManager().getTransaction().commit();

				//actualizarListaRubros();

				helper.mostrarAlertaInformacion("Datos Grabados Correctamente", Context.getInstance().getStage());
				if (chkImprimirComp.isSelected()==true) {
					PrintReport pr = new PrintReport();
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("numComprobante", txtNumComp.getText());
					param.put("cedula", cuentaSeleccionada.getIdCuenta());
					param.put("saldoTotal", Double.valueOf(txtTotal.getText())-Double.valueOf(txtTotalPago.getText()));
					param.put("usuario", Context.getInstance().getUsuariosC().getNombre() + ' ' + Context.getInstance().getUsuariosC().getApellido());
					pr.crearReporte("/recursos/informes/comprobante.jasper", facturaDao, param);
					pr.imprimirReporte();
					pr.showReport("Comprobante de Pago");
				}
				limpiar();
				txtNumComp.setText(String.valueOf(facturaDao.getIdFactura()+1));
				tvDetallePago.getColumns().clear();
				tvDetallePago.getItems().clear();
			}
		}catch(Exception ex) {
			facturaDao.getEntityManager().getTransaction().rollback();
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
			System.out.println(ex.getMessage());
		}
	}

	boolean validarDatos() {
		try {
			if(txtUsuario.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar usuario", Context.getInstance().getStage());
				txtUsuario.requestFocus();
				return false;
			}

			if(txtNumComp.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar Nº Comprobante", Context.getInstance().getStage());
				txtNumComp.requestFocus();
				return false;
			}

			if(dtpFecha.getValue().equals(null)) {
				helper.mostrarAlertaAdvertencia("Ingresar Fecha", Context.getInstance().getStage());
				dtpFecha.requestFocus();
				return false;
			}

			if(tvDetallePago.getItems().isEmpty()) {
				helper.mostrarAlertaAdvertencia("No contiene rubros", Context.getInstance().getStage());
				tvDetallePago.requestFocus();
				return false;
			}
			return true;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	@SuppressWarnings("unchecked")
	public void aceptar() {
		try {
			tvDetallePago.getItems().clear();
			tvDetallePago.getColumns().clear();
			double totalPagar = Double.parseDouble(txtAbono.getText());
			List<Planilla> listaPlanilla = tvDatos.getItems();
			ObservableList<FacturaDetalle> datos = tvDetallePago.getItems();
			FacturaDetalle datoAnadir;

			for(Planilla pla : listaPlanilla) {
				double totalPagado = 0.0;
				if(pla.getPagos() != null) {
					for(Pago pa : pla.getPagos()){
						totalPagado = totalPagado + pa.getValor();
					}
				}
				
				if(totalPagar > (pla.getTotalPagar() - totalPagado)) {
					datoAnadir = new FacturaDetalle();
					datoAnadir.setEstado(Constantes.ESTADO_ACTIVO);
					datoAnadir.setIdDetalleFac(null);
					datoAnadir.setPlanilla(pla);
					//detalleAgregar.setFactura(factura);
					datoAnadir.setSubtotal(pla.getTotalPagar() - totalPagado);
					totalPagar = totalPagar - (pla.getTotalPagar() - totalPagado);
					datos.add(datoAnadir);
				}else if(totalPagar <= pla.getTotalPagar()) {
					datoAnadir = new FacturaDetalle();
					datoAnadir.setEstado(Constantes.ESTADO_ACTIVO);
					datoAnadir.setIdDetalleFac(null);
					datoAnadir.setPlanilla(pla);
					//detalleAgregar.setFactura(factura);
					datoAnadir.setSubtotal(totalPagar);
					totalPagar = 0.0;
					datos.add(datoAnadir);
					break;
				}
			}
			//llenar los datos en la tabla			
			TableColumn<FacturaDetalle, String> descripcionColum = new TableColumn<>("Descripción");
			descripcionColum.setMinWidth(10);
			descripcionColum.setPrefWidth(80);
			descripcionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FacturaDetalle, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<FacturaDetalle, String> param) {
					String descripcion = "";
					if(param.getValue().getPlanilla().getIdentInstalacion() != null) {
						if(param.getValue().getPlanilla().getIdentInstalacion().equals(Constantes.IDENT_INSTALACION))
							descripcion = "Por instalacion de nuevo medidor";
						else if(param.getValue().getPlanilla().getIdentInstalacion().equals(Constantes.IDENT_REPARACION))
							descripcion = "Por reparacion en el servicio";
					}
					else
						descripcion = "Factura mes de: " + String.valueOf(param.getValue().getPlanilla().getAperturaLectura().getMe());
					return new SimpleObjectProperty<String>(descripcion);
				}
			});

			TableColumn<FacturaDetalle, String> idColum = new TableColumn<>("IdPlanilla");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(90);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FacturaDetalle, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<FacturaDetalle, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getPlanilla().getIdPlanilla()));
				}
			});

			TableColumn<FacturaDetalle, String> subtotalColum = new TableColumn<>("Subtotal");
			subtotalColum.setMinWidth(10);
			subtotalColum.setPrefWidth(90);
			subtotalColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FacturaDetalle, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<FacturaDetalle, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getSubtotal()));
				}
			});

			TableColumn<FacturaDetalle, String> totalColum = new TableColumn<>("Estado");
			totalColum.setMinWidth(10);
			totalColum.setPrefWidth(90);
			totalColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FacturaDetalle, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<FacturaDetalle, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getEstado()));
				}
			});

			tvDetallePago.getColumns().addAll(idColum, descripcionColum, subtotalColum, totalColum);
			tvDetallePago.setItems(datos);
			sumarDatosTotalPagar();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void sumarDatosTotalPagar() {
		try {
			if (tvDetallePago.getItems().isEmpty()) {
				//	txtTotal.setText("0.00");
				//	txtSaldo.setText("0.00");
			}else {
				double total = 0;
				for(int i=0; i<tvDetallePago.getItems().size(); i++) {
					Double valorTotal = new Double(tvDetallePago.getItems().get(i).getSubtotal());
					total += valorTotal;
					txtTotalPago.setText(String.valueOf(Double.valueOf(total)));
				}
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void limpiar() {
		try {
			txtNumMedidor.setText("");
			txtIdCuenta.setText("");
			txtCedula.setText("");
			txtCliente.setText("");
			txtDireccion.setText("");
			tvDatos.getColumns().clear();
			tvDatos.getItems().clear();
			tvDetalle.getColumns().clear();
			tvDetalle.getItems().clear();
			tvDetallePago.getColumns().clear();
			tvDetallePago.getItems().clear();
			chkAbonoTotal.setSelected(false);
			txtAbono.setText("");
			txtPagaCon.setText("");
			txtSaldo.setText("");
			txtTotal.setText("");
			txtTotalPago.setText("");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}