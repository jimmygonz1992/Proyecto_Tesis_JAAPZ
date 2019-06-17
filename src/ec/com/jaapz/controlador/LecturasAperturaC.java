package ec.com.jaapz.controlador;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import ec.com.jaapz.modelo.Anio;
import ec.com.jaapz.modelo.AnioDAO;
import ec.com.jaapz.modelo.AperturaLectura;
import ec.com.jaapz.modelo.AperturaLecturaDAO;
import ec.com.jaapz.modelo.CuentaCliente;
import ec.com.jaapz.modelo.CuentaClienteDAO;
import ec.com.jaapz.modelo.Me;
import ec.com.jaapz.modelo.MeDAO;
import ec.com.jaapz.modelo.Planilla;
import ec.com.jaapz.modelo.PlanillaDAO;
import ec.com.jaapz.modelo.PlanillaDetalle;
import ec.com.jaapz.modelo.ResponsableLecturaDAO;
import ec.com.jaapz.modelo.SegUsuarioDAO;
import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

public class LecturasAperturaC {
	@FXML private ComboBox<Me> cboMes;
    @FXML private TableView<AperturaLectura> tvAperturas;
    @FXML private ComboBox<Anio> cboAnio;
    @FXML private Button btnGrabarApertura;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtValor;
    @FXML private RadioButton rbActual;
    @FXML private RadioButton rbTodos;
    
    MeDAO mesDAO = new MeDAO();
	AnioDAO anioDAO = new AnioDAO();
	ControllerHelper helper = new ControllerHelper();
	CuentaClienteDAO cuentaDAO = new CuentaClienteDAO();
	AperturaLecturaDAO aperturaDAO = new AperturaLecturaDAO();
	AperturaLectura aperturaActual = new AperturaLectura();
	PlanillaDAO planillaDAO = new PlanillaDAO();
	SegUsuarioDAO usuarioDAO = new SegUsuarioDAO();
	ResponsableLecturaDAO responsableDAO = new ResponsableLecturaDAO();
	
	public void initialize() {
		try {
			btnGrabarApertura.setStyle("-fx-cursor: hand;");
			cboAnio.setStyle("-fx-cursor: hand;");
			cboMes.setStyle("-fx-cursor: hand;");
			txtCantidad.setText("0");
			txtValor.setText("0");
			txtCantidad.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (newValue.matches("\\d*")) {
						//int value = Integer.parseInt(newValue);
					} else {
						txtCantidad.setText(oldValue);
					}
				}
			});
			txtValor.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (newValue.matches("\\d*")) {
						//int value = Integer.parseInt(newValue);
					} else {
						txtCantidad.setText(oldValue);
					}
				}
			});
			rbActual.setSelected(true);
			cargarCombos();
			recuperarDatos();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	void recuperarDatos() {
		try {
			tvAperturas.getItems().clear();
			tvAperturas.getColumns().clear();
			List<AperturaLectura> listaPrecios;
			ObservableList<AperturaLectura> datos = FXCollections.observableArrayList();
			listaPrecios = aperturaDAO.getListaAperturas();
			datos.setAll(listaPrecios);

			//llenar los datos en la tabla
			TableColumn<AperturaLectura, String> idColum = new TableColumn<>("Id Apertura");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(80);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AperturaLectura,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<AperturaLectura, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdApertura()));
				}
			});

			TableColumn<AperturaLectura, String> mesColum = new TableColumn<>("Mekkkks");
			mesColum.setMinWidth(10);
			mesColum.setPrefWidth(100);
			mesColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AperturaLectura,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<AperturaLectura, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getMe().getDescripcion()));
				}
			});

			TableColumn<AperturaLectura, String> anioColum = new TableColumn<>("Año");
			anioColum.setMinWidth(10);
			anioColum.setPrefWidth(90);
			anioColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AperturaLectura,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<AperturaLectura, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getAnio().getDescripcion()));
				}
			});
			TableColumn<AperturaLectura, String> clienteColum = new TableColumn<>("No. Clientes");
			clienteColum.setMinWidth(10);
			clienteColum.setPrefWidth(90);
			clienteColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AperturaLectura,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<AperturaLectura, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getPlanillas().size()));
				}
			});
			TableColumn<AperturaLectura, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(90);
			estadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AperturaLectura,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<AperturaLectura, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getEstadoApertura());
				}
			});

			tvAperturas.getColumns().addAll(anioColum,mesColum,clienteColum,estadoColum);
			tvAperturas.setItems(datos);

		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	/*
	 * controlador de la vista de Inicio de Ciclo de Facturacion
	 */
	private void cargarCombos() {
		try {
			ObservableList<Anio> listaAnios = FXCollections.observableArrayList();
			List<Anio> datAn = anioDAO.getListaAnios();
			listaAnios.add(datAn.get(0));
			cboAnio.setItems(listaAnios);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void buscarMeses() {
		try {
			cboMes.getItems().clear();
			ObservableList<Me> listaMeses = FXCollections.observableArrayList();
			List<Me> datMes = mesDAO.getListaMeses();
			
			List<AperturaLectura> listadoMes = aperturaDAO.getAperturaByAnio(cboAnio.getSelectionModel().getSelectedItem().getIdAnio());
			for(int i = 0 ; i < listadoMes.size() ; i ++) {
				for(int j = 0 ; j < datMes.size() ; j ++) {
					if(listadoMes.get(i).getMe().getIdMes() == datMes.get(j).getIdMes())
						datMes.remove(j);
				}
			}
			listaMeses.add(datMes.get(0));
			cboMes.setItems(listaMeses);
			
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	//Grabar aperturas de facturacion realizando algunas validaciones
	//faltan validaciones ------------------------------

	public void grabarApertura(ActionEvent event) {
		try {
			if(validarApertura() == true)
				return;
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				//declarar el objeto a grabar
				AperturaLectura aperturaGrabar = new AperturaLectura();
				aperturaGrabar.setEstado(Constantes.ESTADO_ACTIVO);
				aperturaGrabar.setEstadoApertura(Constantes.EST_APERTURA_PROCESO);
				Date date = new Date();
				Timestamp fecha = new Timestamp(date.getTime());
				aperturaGrabar.setFecha(fecha);
				aperturaGrabar.setUsuarioCrea(Context.getInstance().getIdUsuario());
				aperturaGrabar.setAnio(cboAnio.getSelectionModel().getSelectedItem());
				aperturaGrabar.setCantidadMetros(Integer.parseInt(txtCantidad.getText()));
				aperturaGrabar.setCostoMetros(Double.parseDouble(txtValor.getText()));
				aperturaGrabar.setIdApertura(null);
				aperturaGrabar.setMe(cboMes.getSelectionModel().getSelectedItem());
				aperturaDAO.getEntityManager().getTransaction().begin();
				aperturaDAO.getEntityManager().persist(aperturaGrabar);
				aperturaDAO.getEntityManager().getTransaction().commit();
				
				//aperturar todos los clientes 
				List<CuentaCliente> listaCuentasActivas = cuentaDAO.getListaCuentasActivas();
				System.out.println("Cuentas activas: " + listaCuentasActivas.size());
				
				List<AperturaLectura> aperturaProceso = aperturaDAO.getListaAperturasEnProceso();
				
				if(aperturaProceso.size() > 0) {
					//recorrer las cuentas para asignar las aperturas
					for(CuentaCliente cuentas : listaCuentasActivas) {
						if(cuentas.getMedidor() != null) {
							aperturaDAO.getEntityManager().getTransaction().begin();
							List<Planilla> noPlanillado = planillaDAO.getNoPlanillado(cuentas.getIdCuenta());
							
							if(noPlanillado.size() > 0) {//existe una planilla de otro proceso
								
								Planilla planilla = noPlanillado.get(0); // se llena los datos de la planilla ya generada
								System.out.println("Planilla ya generada: " + planilla.getIdPlanilla());
								planilla.setFecha(fecha);
								planilla.setEnvia(false);
								planilla.setImprime(false);
								planilla.setConvenio(Constantes.CONVENIO_NO);
								
								//obtener el consumo del mes anterior
								planilla.setConsumo(0);
								planilla.setConsumoMinimo(0);
								List<Planilla> listaPlanillasCuenta = new ArrayList<Planilla>(); //lista que guarda las planillas de la cuenta del cliente, para saber
																								//las lecturas anteriores
								listaPlanillasCuenta = planillaDAO.getPlanillaCuenta(cuentas.getIdCuenta());
								
								if(listaPlanillasCuenta.size() > 0) {//aqui es en la posicion 
									planilla.setLecturaAnterior(listaPlanillasCuenta.get(0).getLecturaActual());//la lectura anterior y la lectura actual de la planilla
									planilla.setLecturaActual(listaPlanillasCuenta.get(0).getLecturaActual());//son iguales de la lectura actual de la planilla anterior
								}else {
									planilla.setLecturaAnterior(0);//Caso contrario los dos son cero
									planilla.setLecturaActual(0);
								}
								
								planilla.setIdentificadorProceso(null);
								planilla.setEstado(Constantes.ESTADO_ACTIVO);
								//enlace entre planilla y apertura
								planilla.setAperturaLectura(aperturaProceso.get(0));
								
								//enlace entre detalle de planilla y planilla
								PlanillaDetalle detallePlanilla = new PlanillaDetalle();
								detallePlanilla.setIdPlanillaDet(null);
								detallePlanilla.setDescripcion("Por consumo del mes de: " + cboMes.getSelectionModel().getSelectedItem().getDescripcion() + " del año: " + cboAnio.getSelectionModel().getSelectedItem().getDescripcion());
								detallePlanilla.setEstado(Constantes.ESTADO_ACTIVO);
								detallePlanilla.setIdentificadorOperacion(Constantes.IDENT_LECTURA);
								detallePlanilla.setCantidad(0);
								detallePlanilla.setPlanilla(planilla);
								
								planilla.addPlanillaDetalle(detallePlanilla);

								aperturaDAO.getEntityManager().merge(planilla);
								
							}else {
								
								List<Planilla> listaAdd = new ArrayList<Planilla>();
								Planilla planilla = new Planilla(); // planilla nueva para todos los clientes
								planilla.setIdPlanilla(null);
								planilla.setFecha(fecha);
								planilla.setConvenio(Constantes.CONVENIO_NO);
								//obtener el consumo del mes anterior
								planilla.setConsumo(0);
								planilla.setConsumoMinimo(0);
								planilla.setEnvia(false);
								planilla.setImprime(false);
								List<Planilla> listaPlanillasCuenta = new ArrayList<Planilla>(); //lista que guarda las planillas de la cuenta del cliente
								listaPlanillasCuenta = planillaDAO.getPlanillaCuenta(cuentas.getIdCuenta());
								if(listaPlanillasCuenta.size() != 0) {
									planilla.setLecturaAnterior(listaPlanillasCuenta.get(0).getLecturaActual());//la lectura anterior y la lectura actual de la planilla
									planilla.setLecturaActual(listaPlanillasCuenta.get(0).getLecturaActual());//son iguales de la lectura actual de la planilla anterior
								}else {
									planilla.setLecturaAnterior(0);//Caso contrario los dos son cero
									planilla.setLecturaActual(0);
								}

								planilla.setEstado(Constantes.ESTADO_ACTIVO);
								//enlace entre planilla y apertura
								planilla.setAperturaLectura(aperturaProceso.get(0));
								
								//enlace entre cliente y planilla
								planilla.setCuentaCliente(cuentas);
								cuentas.setPlanillas(listaAdd);

								//enlace entre detalle de planilla y planilla
								PlanillaDetalle detallePlanilla = new PlanillaDetalle();
								detallePlanilla.setIdPlanillaDet(null);
								detallePlanilla.setDescripcion("Por consumo del mes de: " + cboMes.getSelectionModel().getSelectedItem().getDescripcion() + " del año: " + cboAnio.getSelectionModel().getSelectedItem().getDescripcion());
								detallePlanilla.setEstado(Constantes.ESTADO_ACTIVO);
								detallePlanilla.setCantidad(0);
								detallePlanilla.setIdentificadorOperacion(Constantes.IDENT_LECTURA);
								detallePlanilla.setPlanilla(planilla);
								List<PlanillaDetalle> det = new ArrayList<PlanillaDetalle>();
								det.add(detallePlanilla);
								planilla.setPlanillaDetalles(det);

								aperturaDAO.getEntityManager().persist(planilla);	
								
							}
							aperturaDAO.getEntityManager().getTransaction().commit();
						}
					}
					helper.mostrarAlertaInformacion("Datos Grabados Correctamente", Context.getInstance().getStage());
					recuperarDatos();
					cboAnio.getItems().clear();
					cboMes.getItems().clear();
					cargarCombos();
				}
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			aperturaDAO.getEntityManager().getTransaction().rollback();
		}
	}
	private boolean validarApertura() {
		try {
			boolean bandera = false;
			if(cboAnio.getSelectionModel().getSelectedIndex() == -1) {
				helper.mostrarAlertaAdvertencia("Debe Seleccionar el año a aperturar", Context.getInstance().getStage());
				bandera = true;
				return bandera;
			}
			if(cboMes.getSelectionModel().getSelectedIndex() == -1) {
				helper.mostrarAlertaAdvertencia("Debe Seleccionar el mes a aperturar", Context.getInstance().getStage());
				bandera = true;
				return bandera;
			}
			List<AperturaLectura> listaApertura = tvAperturas.getItems();
			for(AperturaLectura apertura : listaApertura) {
				if(apertura.getAnio().getIdAnio() == cboAnio.getSelectionModel().getSelectedItem().getIdAnio() 
						&& apertura.getMe().getIdMes() == cboMes.getSelectionModel().getSelectedItem().getIdMes()) {
					bandera = true;
					helper.mostrarAlertaAdvertencia("Apertura ya realizada", Context.getInstance().getStage());
					return bandera;
				}
			}
			for(AperturaLectura apertura : listaApertura) {
				if(apertura.getEstadoApertura().equals(Constantes.EST_APERTURA_PROCESO)) {
					bandera = true;
					helper.mostrarAlertaAdvertencia("Existe una apertura en proceso \nCierre la apertura antes de emitir una nueva", Context.getInstance().getStage());
					return bandera;
				}

			}
			return bandera;
		}catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	public void accionarActual(){
		rbActual.setSelected(true);
		rbTodos.setSelected(false);
		List<Anio> datAn = anioDAO.getListaAnios();
		Anio anio = datAn.get(0);
		
		List<AperturaLectura> listaPrecios;
		ObservableList<AperturaLectura> datos = FXCollections.observableArrayList();
		listaPrecios = aperturaDAO.getAperturaByAnio(anio.getIdAnio());
		datos.setAll(listaPrecios);
		tvAperturas.setItems(datos);
		tvAperturas.refresh();
	}
	public void accionarTodos() {
		rbActual.setSelected(false);
		rbTodos.setSelected(true);
		
		List<AperturaLectura> listaPrecios;
		ObservableList<AperturaLectura> datos = FXCollections.observableArrayList();
		listaPrecios = aperturaDAO.getListaAperturas();
		datos.setAll(listaPrecios);
		tvAperturas.setItems(datos);
		tvAperturas.refresh();
	}
}