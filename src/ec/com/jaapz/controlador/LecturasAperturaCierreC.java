package ec.com.jaapz.controlador;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import ec.com.jaapz.modelo.Anio;
import ec.com.jaapz.modelo.AnioDAO;
import ec.com.jaapz.modelo.AperturaLectura;
import ec.com.jaapz.modelo.AperturaLecturaDAO;
import ec.com.jaapz.modelo.Barrio;
import ec.com.jaapz.modelo.CuentaCliente;
import ec.com.jaapz.modelo.CuentaClienteDAO;
import ec.com.jaapz.modelo.Me;
import ec.com.jaapz.modelo.MeDAO;
import ec.com.jaapz.modelo.Planilla;
import ec.com.jaapz.modelo.PlanillaDAO;
import ec.com.jaapz.modelo.PlanillaDetalle;
import ec.com.jaapz.modelo.ResponsableLectura;
import ec.com.jaapz.modelo.ResponsableLecturaDAO;
import ec.com.jaapz.modelo.SegUsuario;
import ec.com.jaapz.modelo.SegUsuarioDAO;
import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class LecturasAperturaCierreC {

	@FXML private ComboBox<Me> cboMes;
	@FXML private TextField txtTotalClientes;
	@FXML private Button btnCerrarCiclo;
	@FXML private Button btnQuitarAsig;
	@FXML private TextField txtMes;
	@FXML private TabPane tpAsignaciones;
	@FXML private TableView<Barrio> tvNuevosAsig;
	@FXML private Button btnGrabarApertura;
	@FXML private TableView<ResponsableLectura> tvAsignados;
	@FXML private Button btnGrabarAsig;
	@FXML private Button btnAsignar;
	@FXML private TextField txtClientesRegistrados;
	@FXML private TableView<AperturaLectura> tvAperturas;
	@FXML private TableView<AperturaLectura> tvApRealizadas;
	@FXML private TableView<SegUsuario> tvPersonalLectura;
	@FXML private Tab tpRealizadas;
	@FXML private TextField txtAnio;
	@FXML private Tab tpNuevas;
	@FXML private ComboBox<Anio> cboAnio;
	@FXML private TextField txtClientesFaltantes;
	@FXML private Button btnImprimirAsig;
	@FXML private TextField txtFecha;

	@FXML private Button btnResultados;

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
			Context.getInstance().setBarrio(null);
			cargarCombos();
			recuperarDatos();
			obtenerCicloActual();
			cargarDatosAperturasRealizadas();
			llenarListaResponsablesLectura();
			tvApRealizadas.setOnMouseClicked(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					cargarAperturaSeleccionada(tvApRealizadas.getSelectionModel().getSelectedItem());
				}
			});
			tvPersonalLectura.setOnMouseClicked(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					if(aperturaSeleccionada != null) {
						recuperarAsignacionesResponsables(aperturaSeleccionada,tvPersonalLectura.getSelectionModel().getSelectedItem());// recupera los datos de los responsables que se ebcuebtran en esa apertura
						tvNuevosAsig.getItems().clear();
						tvNuevosAsig.getColumns().clear();
						listaResponsableEliminar.clear();
					}else {
						tvNuevosAsig.getItems().clear();
						tvNuevosAsig.getColumns().clear();
					}
				}
			});
			if(tpRealizadas.isSelected()) {
				btnAsignar.setDisable(true);
				btnImprimirAsig.setDisable(true);
				tpNuevas.setDisable(true);
				btnGrabarAsig.setDisable(true);
				btnQuitarAsig.setDisable(true);
			}
			tpNuevas.setOnSelectionChanged(new EventHandler<Event>() {
				@Override
				public void handle(Event arg0) {
					if(tpNuevas.isSelected()) {
						btnAsignar.setDisable(false);
						btnImprimirAsig.setDisable(true);
						listaResponsableEliminar.clear();
						if(aperturaSeleccionada != null) {
							recuperarAsignacionesResponsables(aperturaSeleccionada,tvPersonalLectura.getSelectionModel().getSelectedItem());// recupera los datos de los responsables que se ebcuebtran en esa apertura	
						}else {
							tvNuevosAsig.getItems().clear();
							tvNuevosAsig.getColumns().clear();
						}
						
					}
				}
			});
			tpRealizadas.setOnSelectionChanged(new EventHandler<Event>() {
				@Override
				public void handle(Event arg0) {
					if(tpRealizadas.isSelected()) {
						btnAsignar.setDisable(true);
						btnImprimirAsig.setDisable(false);
					}
				}
			});
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

			TableColumn<AperturaLectura, String> mesColum = new TableColumn<>("Mes");
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

			tvAperturas.getColumns().addAll(idColum,mesColum,anioColum,clienteColum,estadoColum);
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
			ObservableList<Me> listaMeses = FXCollections.observableArrayList();
			List<Me> datMes = mesDAO.getListaMeses();
			listaMeses.addAll(datMes);
			cboMes.setItems(listaMeses);

			ObservableList<Anio> listaAnios = FXCollections.observableArrayList();
			List<Anio> datAn = anioDAO.getListaAnios();
			listaAnios.setAll(datAn);
			cboAnio.setItems(listaAnios);
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
				aperturaGrabar.setEstado("A");
				aperturaGrabar.setEstadoApertura(Constantes.EST_APERTURA_PROCESO);
				Date date = new Date();
				Timestamp fecha = new Timestamp(date.getTime());
				aperturaGrabar.setFecha(fecha);
				aperturaGrabar.setUsuarioCrea(Context.getInstance().getIdUsuario());
				aperturaGrabar.setAnio(cboAnio.getSelectionModel().getSelectedItem());
				aperturaGrabar.setIdApertura(null);
				aperturaGrabar.setMe(cboMes.getSelectionModel().getSelectedItem());

				//aperturar todos los clientes 
				List<CuentaCliente> listaCuentasActivas = new ArrayList<CuentaCliente>();
				listaCuentasActivas = cuentaDAO.getListaCuentasActivas();
				aperturaDAO.getEntityManager().getTransaction().begin();
				//recorrer las cuentas para asignar las aperturas
				for(CuentaCliente cuentas : listaCuentasActivas) {
					List<Planilla> listaAdd = new ArrayList<Planilla>();
					Planilla planilla = new Planilla(); // planilla nueva para todos los clientes
					planilla.setIdPlanilla(null);
					planilla.setFecha(fecha);
					planilla.setConvenio(Constantes.CONVENIO_NO);
					//obtener el consumo del mes anterior
					planilla.setConsumo(0);
					planilla.setConsumoMinimo(0);
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
					planilla.setAperturaLectura(aperturaGrabar);
					listaAdd.add(planilla);
					aperturaGrabar.setPlanillas(listaAdd);
					//enlace entre cliente y planilla
					planilla.setCuentaCliente(cuentas);
					cuentas.setPlanillas(listaAdd);

					//enlace entre detalle de planilla y planilla
					PlanillaDetalle detallePlanilla = new PlanillaDetalle();
					detallePlanilla.setIdPlanillaDet(null);
					detallePlanilla.setDescripcion("Por consumo del mes de: " + cboMes.getSelectionModel().getSelectedItem().getDescripcion() + " del año: " + cboAnio.getSelectionModel().getSelectedItem().getDescripcion());
					detallePlanilla.setEstado("A");
					detallePlanilla.setCantidad(0);
					detallePlanilla.setPlanilla(planilla);
					List<PlanillaDetalle> det = new ArrayList<PlanillaDetalle>();
					det.add(detallePlanilla);
					planilla.setPlanillaDetalles(det);

					aperturaDAO.getEntityManager().persist(aperturaGrabar);
				}
				aperturaDAO.getEntityManager().getTransaction().commit();
				helper.mostrarAlertaInformacion("Datos Grabados Correctamente", Context.getInstance().getStage());
				recuperarDatos();
				cargarDatosAperturasRealizadas();
				obtenerCicloActual();
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
	/*
	 * Controlador para el cierre de ciclo -----------------------------------------------------------------------------------------------------------------
	 */
	private void obtenerCicloActual() {
		try {
			SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yy");
			int numeroTomados = 0;
			int numApertura = 0;
			AperturaLectura apertura = new AperturaLectura();
			numApertura = aperturaDAO.getListaAperturasEnProceso().size(); 
			if(numApertura > 0) {
				apertura = aperturaDAO.getListaAperturasEnProceso().get(0);
				aperturaActual = apertura;
				txtAnio.setText(String.valueOf(apertura.getAnio().getDescripcion()));
				txtMes.setText(String.valueOf(apertura.getMe().getDescripcion()));
				txtFecha.setText(formateador.format(apertura.getFecha()));

				txtTotalClientes.setText(String.valueOf(apertura.getPlanillas().size()));
				for(Planilla planilla : apertura.getPlanillas()) {
					if(planilla.getConsumo() > 0)
						numeroTomados = numeroTomados + 1;
				}
				txtClientesRegistrados.setText(String.valueOf(numeroTomados));
				txtClientesFaltantes.setText(String.valueOf(apertura.getPlanillas().size() - numeroTomados));	
			}else {
				aperturaActual = null;
				txtAnio.setText("");
				txtMes.setText("");
				txtFecha.setText("");
				txtTotalClientes.setText("");
				txtClientesRegistrados.setText("");
				txtClientesFaltantes.setText("");
			}

		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	private void cargarDatosAperturasRealizadas() {
		try {
			tvApRealizadas.getItems().clear();
			tvApRealizadas.getColumns().clear();
			List<AperturaLectura> listaPrecios;
			ObservableList<AperturaLectura> datos = FXCollections.observableArrayList();
			listaPrecios = aperturaDAO.getListaAperturas();
			datos.setAll(listaPrecios);

			TableColumn<AperturaLectura, String> mesColum = new TableColumn<>("Mes");
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

			tvApRealizadas.getColumns().addAll(anioColum,mesColum,clienteColum,estadoColum);
			tvApRealizadas.setItems(datos);

		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void cargarAperturaSeleccionada(AperturaLectura apertura) {
		try {
			SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yy");
			aperturaActual = apertura;
			int numeroTomados = 0;
			txtAnio.setText(String.valueOf(apertura.getAnio().getDescripcion()));
			txtMes.setText(String.valueOf(apertura.getMe().getDescripcion()));
			txtFecha.setText(formateador.format(apertura.getFecha()));

			txtTotalClientes.setText(String.valueOf(apertura.getPlanillas().size()));
			for(Planilla planilla : apertura.getPlanillas()) {
				if(planilla.getConsumo() > 0)
					numeroTomados = numeroTomados + 1;
			}
			txtClientesRegistrados.setText(String.valueOf(numeroTomados));
			txtClientesFaltantes.setText(String.valueOf(apertura.getPlanillas().size() - numeroTomados));

			if(apertura.getEstadoApertura().equals(Constantes.EST_APERTURA_PROCESO))
				btnCerrarCiclo.setDisable(false);
			else
				btnCerrarCiclo.setDisable(true);    		
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	public void cerrarCiclo(ActionEvent event) {
		try {
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Se procedera a cerrar el proceso de facturación, desea continuar??",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				if (validarCierreCiclo() == false) {
					Optional<ButtonType> resultado = helper.mostrarAlertaConfirmacion("Existen inconvenientes en el proceso de lecturas, desea continuar??",Context.getInstance().getStage());
					if(resultado.get() == ButtonType.CANCEL){
						return;
					}
				}
				aperturaActual.setEstadoApertura(Constantes.EST_INSPECCION_REALIZADO);
				List<PlanillaDetalle> planillaDetalle;
				double total = 0.0;
				//tambien se necesita hacer los calculos para el total de la deuda
				for(int i = 0 ; i < aperturaActual.getPlanillas().size() ; i ++) {
					total = 0.0;
					planillaDetalle = aperturaActual.getPlanillas().get(i).getPlanillaDetalles(); 
					for(PlanillaDetalle det : planillaDetalle) {
						total = total + det.getSubtotal();
					}
					aperturaActual.getPlanillas().get(i).setTotalPagar(total);

					long iPart = (long) total;
					double fPart = total - iPart;
					String numLetras = "";
					numLetras = "(" + helper.cantidadConLetra(String.valueOf(iPart)).toUpperCase() + " DÓLARES " + (long) (fPart * 100) + "/100)"; 
					System.out.println(numLetras);
					aperturaActual.getPlanillas().get(i).setTotalLetras(numLetras);
					aperturaActual.getPlanillas().get(i).setCancelado(Constantes.EST_FAC_PENDIENTE);
				}
				
				aperturaDAO.getEntityManager().getTransaction().begin();
				aperturaDAO.getEntityManager().merge(aperturaActual);
				aperturaDAO.getEntityManager().getTransaction().commit();
				helper.mostrarAlertaInformacion("Cierre ejecutado con exito!!!!", Context.getInstance().getStage());
				aperturaActual = null;
				recuperarDatos();
				obtenerCicloActual();
				cargarDatosAperturasRealizadas();
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			aperturaDAO.getEntityManager().getTransaction().rollback();
		}
	}
	
	
	private boolean validarCierreCiclo() {
		try {
			boolean bandera = false;
			//boolean banValidaciones = false;
			for (Planilla planilla : aperturaActual.getPlanillas()) {
				if(planilla.getConsumo() == 0) {
					return false;
				}
			}
			bandera = true;
			return bandera;
		}catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	public void visualizarResultados() {
		try {
			helper.abrirPantallaModal("/lecturas/LecturasResultados.fxml","Resultados de la Apertura", Context.getInstance().getStage());

		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	/*
	 * controladores para la vista de asignaciones de responsables de lecturas
	 */
	@FXML private Button btnBuscarApertura;
	@FXML private TextField txtAnioAperRes;
	@FXML private TextField txtFechAperRes;
	@FXML private TextField txtMesAperRes;
	@FXML private TextField txtEstadoApeAsi;
	private AperturaLectura aperturaSeleccionada = new AperturaLectura();
	@SuppressWarnings("unchecked")
	private void llenarListaResponsablesLectura() {
		try{
			tvPersonalLectura.getColumns().clear();
			tvPersonalLectura.getItems().clear();
			List<SegUsuario> listaClientes;
			ObservableList<SegUsuario> datos = FXCollections.observableArrayList();
			listaClientes = usuarioDAO.getListaUsuariosLectura();
			datos.setAll(listaClientes);

			//llenar los datos en la tabla
			TableColumn<SegUsuario, String> idColum = new TableColumn<>("Código");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(90);
			idColum.setCellValueFactory(new PropertyValueFactory<SegUsuario, String>("idUsuario"));
			TableColumn<SegUsuario, String> nombresColum = new TableColumn<>("Nombres");
			nombresColum.setMinWidth(10);
			nombresColum.setPrefWidth(200);
			nombresColum.setCellValueFactory(new PropertyValueFactory<SegUsuario, String>("nombres"));
			TableColumn<SegUsuario, String> apellidosColum = new TableColumn<>("Apellidos");
			apellidosColum.setMinWidth(10);
			apellidosColum.setPrefWidth(100);
			apellidosColum.setCellValueFactory(new PropertyValueFactory<SegUsuario, String>("apellidos"));
			TableColumn<SegUsuario, String> fechColum = new TableColumn<>("Telefono");
			fechColum.setMinWidth(10);
			fechColum.setPrefWidth(100);
			fechColum.setCellValueFactory(new PropertyValueFactory<SegUsuario, String>("telefono"));
			tvPersonalLectura.getColumns().addAll(idColum, nombresColum,apellidosColum,fechColum);
			tvPersonalLectura.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	public void buscarApertura(){
		try {
			helper.abrirPantallaModal("/lecturas/LecturasListaApertura.fxml","Aperturas Realizadas", Context.getInstance().getStage());
			listaResponsableEliminar.clear();
			if(Context.getInstance().getApertura() != null) {
				aperturaSeleccionada = Context.getInstance().getApertura();
				Context.getInstance().setApertura(null);
				btnAsignar.setDisable(true);
				recuperarAperturaSeleccionada(aperturaSeleccionada);//recupera los datos de la asignacion seleccionada
			}else {
				txtAnioAperRes.setText("");
				txtMesAperRes.setText("");
				txtFechAperRes.setText("");
				txtEstadoApeAsi.setText("");
				Context.getInstance().setApertura(null);
				aperturaSeleccionada = null;
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void recuperarAperturaSeleccionada(AperturaLectura aperturaSeleccionada) {
		try {
			SingleSelectionModel<Tab> selectionModel = tpAsignaciones.getSelectionModel();
			selectionModel.select(tpRealizadas);
			tvNuevosAsig.getItems().clear();
			tvNuevosAsig.getColumns().clear();
			tvAsignados.getItems().clear();
			tvAsignados.getColumns().clear();
			SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yy");
			txtAnioAperRes.setText(String.valueOf(aperturaSeleccionada.getAnio().getDescripcion()));
			txtMesAperRes.setText(String.valueOf(aperturaSeleccionada.getMe().getDescripcion()));
			txtFechAperRes.setText(formateador.format(aperturaSeleccionada.getFecha()));
			txtEstadoApeAsi.setText(aperturaSeleccionada.getEstadoApertura());
			if(aperturaSeleccionada.getEstadoApertura().equals(Constantes.EST_INSPECCION_REALIZADO)) {
				btnGrabarAsig.setDisable(true);
				btnQuitarAsig.setDisable(true);
				btnAsignar.setDisable(true);
				tpNuevas.setDisable(true);
				
			}else {
				btnImprimirAsig.setDisable(false);
				btnGrabarAsig.setDisable(false);
				btnQuitarAsig.setDisable(false);
				tpNuevas.setDisable(false);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void recuperarAsignacionesResponsables(AperturaLectura aperturaSeleccionada,SegUsuario usuarioSeleccionado) {
		try {
			List<ResponsableLectura> listaResponsable = new ArrayList<ResponsableLectura>();
			listaResponsable = responsableDAO.getListaResponsableApertura(aperturaSeleccionada.getIdApertura(),usuarioSeleccionado.getIdUsuario());

			tvAsignados.getItems().clear();
			tvAsignados.getColumns().clear();
			ObservableList<ResponsableLectura> datos = FXCollections.observableArrayList();
			datos.setAll(listaResponsable);
			//llenar los datos en la tabla
			TableColumn<ResponsableLectura, String> idColum = new TableColumn<>("Código");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(50);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ResponsableLectura,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<ResponsableLectura, String> param) {
					String id = "";
					id = String.valueOf(param.getValue().getIdResponsable());
					return new SimpleObjectProperty<String>(id);
				}
			});

			TableColumn<ResponsableLectura, String> clienteColum = new TableColumn<>("Barrio");
			clienteColum.setMinWidth(10);
			clienteColum.setPrefWidth(200);
			clienteColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ResponsableLectura,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<ResponsableLectura, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getBarrio().getNombre());
				}
			});

			TableColumn<ResponsableLectura, String> referenciaColum = new TableColumn<>("Estado");
			referenciaColum.setMinWidth(10);
			referenciaColum.setPrefWidth(130);
			referenciaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ResponsableLectura,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<ResponsableLectura, String> param) {
					String estado = "";
					if(param.getValue().getEstado().equals(Constantes.ESTADO_ACTIVO))
						estado = "Activo";
					else
						estado = "Inactivo";
					return new SimpleObjectProperty<String>(estado);
				}
			});

			tvAsignados.getColumns().addAll(idColum,clienteColum,referenciaColum);
			tvAsignados.setItems(datos);

		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public void asignarBarrios() {
		try {
			if(tvPersonalLectura.getSelectionModel().getSelectedItem() == null) {
				helper.mostrarAlertaError("Debe Seleccionar un Responsable de Lectura", Context.getInstance().getStage());
				return;
			}
			//pasar por parametro la lista de Barrios a realzar para ir disminuyendo en el listado
			Context.getInstance().getListaInspecciones();
			Context.getInstance().setListaBarrios(tvNuevosAsig.getItems());
			//ademas paso por parametro a la sigueinte pantalla la apertura seleccionada para ver cuales son los barrios q aun faltan de asignar
			Context.getInstance().setApertura(aperturaSeleccionada);
			helper.abrirPantallaModal("/lecturas/LecturasBarrios.fxml","Listado de Barrios", Context.getInstance().getStage());
			if (Context.getInstance().getBarrio() != null) {
				Barrio barrioAgregar = Context.getInstance().getBarrio();
				agregarBarrio(barrioAgregar);
				Context.getInstance().setBarrio(null);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	private void agregarBarrio(Barrio barrioAgregar) {
		try {
			ObservableList<Barrio> datos = FXCollections.observableArrayList();
			datos.setAll(tvNuevosAsig.getItems());
			datos.add(barrioAgregar);

			tvNuevosAsig.getItems().clear();
			tvNuevosAsig.getColumns().clear();

			//llenar los datos en la tabla
			TableColumn<Barrio, String> idColum = new TableColumn<>("Código");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(70);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Barrio,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Barrio, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdBarrio()));
				}
			});
			TableColumn<Barrio, String> barrioColum = new TableColumn<>("Nombre");
			barrioColum.setMinWidth(10);
			barrioColum.setPrefWidth(150);
			barrioColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Barrio,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Barrio, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getNombre());
				}
			});

			TableColumn<Barrio, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(90);
			estadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Barrio,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Barrio, String> param) {
					String estado = "";
					if(param.getValue().getEstado().equals(Constantes.ESTADO_ACTIVO))
						estado = "Activo";
					else
						estado = "Inactivo";
					return new SimpleObjectProperty<String>(estado);
				}
			});


			tvNuevosAsig.getColumns().addAll(idColum, barrioColum,estadoColum);
			tvNuevosAsig.setItems(datos);

		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	List<ResponsableLectura> listaResponsableEliminar = new ArrayList<ResponsableLectura>();
	public void quitarBarrios() {
		try {
			if(tpRealizadas.isSelected()) {
				if(txtEstadoApeAsi.getText().equals(Constantes.EST_APERTURA_REALIZADO)) {
					System.out.println("realizado");
					helper.mostrarAlertaError("No se puede quitar el barrio de una apertura ya cerrada", Context.getInstance().getStage());
					return;
				}else if(txtEstadoApeAsi.getText().equals(Constantes.EST_APERTURA_PROCESO)) {
					System.out.println("pendiente");
					ResponsableLectura insEliminar = tvAsignados.getSelectionModel().getSelectedItem();
					listaResponsableEliminar.add(insEliminar);
					tvAsignados.getItems().remove(tvAsignados.getSelectionModel().getSelectedItem());
				}
			}
			if(tpNuevas.isSelected()) {
				Barrio ordenQuitar = tvNuevosAsig.getSelectionModel().getSelectedItem();
				if(ordenQuitar == null) {
					helper.mostrarAlertaError("Debe Seleccionar una Asignación", Context.getInstance().getStage());
					return;
				}
				tvNuevosAsig.getItems().remove(ordenQuitar);
				if (tvNuevosAsig.getItems().size() == 0) {
					tvNuevosAsig.getItems().clear();
					tvNuevosAsig.getColumns().clear();
				}	
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void grabarAsig() {
		try {
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				if(tpRealizadas.isSelected()) {
					responsableDAO.getEntityManager().getTransaction().begin();
					for(ResponsableLectura responsableGrabar : listaResponsableEliminar) {
						responsableGrabar.setEstado(Constantes.ESTADO_INACTIVO);
						responsableDAO.getEntityManager().merge(responsableGrabar);
					}
					responsableDAO.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Datos grabados!!", Context.getInstance().getStage());
					recuperarAsignacionesResponsables(aperturaSeleccionada,tvPersonalLectura.getSelectionModel().getSelectedItem());
					tvNuevosAsig.getItems().clear();
					tvNuevosAsig.getColumns().clear();
				}
				if(tpNuevas.isSelected()) {
					responsableDAO.getEntityManager().getTransaction().begin();
					//grabar al responsable con su barrio a tomar lectura
					ResponsableLectura responsabilidadGrabar;
					for(Barrio barrioAsignar : tvNuevosAsig.getItems()) {
						responsabilidadGrabar = new ResponsableLectura();
						responsabilidadGrabar.setIdResponsable(null);
						//solo se hace un enlace xq el barrio y el responsable ya estan grabados en la base de datos
						//si se ingresaran todos como nuevos registros, se hace el doble enlace
						responsabilidadGrabar.setBarrio(barrioAsignar);
						responsabilidadGrabar.setSegUsuario(tvPersonalLectura.getSelectionModel().getSelectedItem());
						responsabilidadGrabar.setAperturaLectura(aperturaSeleccionada);
						responsabilidadGrabar.setUsuarioCrea(Context.getInstance().getIdUsuario());
						Date date = new Date();
						Timestamp fecha = new Timestamp(date.getTime());
						responsabilidadGrabar.setFecha(fecha);
						responsabilidadGrabar.setEstado(Constantes.ESTADO_ACTIVO);
						responsableDAO.getEntityManager().persist(responsabilidadGrabar);
					}
					responsableDAO.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Datos grabados!!", Context.getInstance().getStage());
					recuperarAsignacionesResponsables(aperturaSeleccionada,tvPersonalLectura.getSelectionModel().getSelectedItem());
					tvNuevosAsig.getItems().clear();
					tvNuevosAsig.getColumns().clear();
				}
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			responsableDAO.getEntityManager().getTransaction().rollback();
		}
	}

	public void imprimirAsignaciones() {

	}
}
