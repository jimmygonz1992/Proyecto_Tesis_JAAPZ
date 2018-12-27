package ec.com.jaapz.controlador;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import ec.com.jaapz.modelo.Barrio;
import ec.com.jaapz.modelo.BarrioDAO;
import ec.com.jaapz.modelo.Categoria;
import ec.com.jaapz.modelo.CategoriaDAO;
import ec.com.jaapz.modelo.Cliente;
import ec.com.jaapz.modelo.ClienteDAO;
import ec.com.jaapz.modelo.Genero;
import ec.com.jaapz.modelo.SolInspeccionInDAO;
import ec.com.jaapz.modelo.SegUsuario;
import ec.com.jaapz.modelo.SegUsuarioDAO;
import ec.com.jaapz.modelo.SolInspeccionIn;
import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import ec.com.jaapz.util.PrintReport;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class ClientesInspeccionC {
	Tooltip toolTip;
	//controles de Registro de Clientes
	@FXML private ComboBox<Barrio> cboBarrio;
	@FXML private TableView<Cliente> tvDatosClientes;
	@FXML private TextField txtBuscar;
	@FXML private Button btnEditar;
	@FXML private Button btnImprimir;
	@FXML private Button btnEliminar;
	@FXML private Button btnNuevo;
	//controladores de Generar inspeccion
	@FXML private ComboBox<Categoria> cboUsoMedidor;
	@FXML private TextField cboEstadoIns;
	@FXML private Button btnNuevoIns;
	@FXML private TextField txtReferenciaIns;
	@FXML private TextField txtEstadoIns;
	@FXML private TextField txtTelefonoIns;
	@FXML private Button btnGrabarIns;
	@FXML private TextField txtApellidosIns;
	@FXML private TextField txtIdClienteIns;    
	@FXML private Button btnBuscarIns;
	@FXML private TextField txtGeneroIns;
	@FXML private TextField txtNombresIns;
	@FXML private TextField txtCedulaIns;
	@FXML private DatePicker dtpFechaIns;

	//controladore de Asignar Responsables
	@FXML TableView<SegUsuario> tvPersonalAsig;
	@FXML Button btnGrabarAsig;
	@FXML Button btnNuevoAsig;
	@FXML Tab tpRealizadas;
	@FXML Tab tpNuevas;
	@FXML TableView<SolInspeccionIn> tvAsignados;
	@FXML TableView<SolInspeccionIn> tvNuevosAsig;
	@FXML Button btnImprimirAsig;
	@FXML Button btnAsignarAsig;
	@FXML Button btnQuitarAsig;
	@FXML TabPane tpAsignaciones;

	//variables globales
	Genero[] genero = Genero.values();
	ControllerHelper helper = new ControllerHelper();
	ClienteDAO clienteDAO = new ClienteDAO();
	SegUsuarioDAO usuarioDAO = new SegUsuarioDAO();
	SolInspeccionInDAO inspeccionDAO = new SolInspeccionInDAO();
	BarrioDAO barrioDAO = new BarrioDAO();
	CategoriaDAO categoriaDAO = new CategoriaDAO();
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * Operaciones de la vista de registro de clientes
	 *++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@FXML
	void initialize() {

		toolTip = new Tooltip("Buscar Cliente para emitir una orden de Inspección");
		btnBuscarIns.setTooltip(toolTip);
		btnEditar.setStyle("-fx-graphic: url('/editar.png');-fx-cursor: hand;");
		btnImprimir.setStyle("-fx-graphic: url('/imprimir.png');-fx-cursor: hand;");
		btnEliminar.setStyle("-fx-graphic: url('/eliminar.png');-fx-cursor: hand;");
		btnNuevo.setStyle("-fx-graphic: url('/nuevo.png');-fx-cursor: hand;");


		btnEditarInspeccion.setStyle("-fx-graphic: url('/editar.png');-fx-cursor: hand;");
		btnImprimirInspeccion.setStyle("-fx-graphic: url('/imprimir.png');-fx-cursor: hand;");
		btnEliminarInspeccion.setStyle("-fx-graphic: url('/eliminar.png');-fx-cursor: hand;");

		llenarComboBarrio();
		llenarDatos("");
		llenarListaResponsables();
		if(tpRealizadas.isSelected())
			bloquearBotonesAsignacion();

		tpNuevas.setOnSelectionChanged(new EventHandler<Event>() {
			@Override
			public void handle(Event arg0) {
				if(tpNuevas.isSelected()) {
					desbloquearBotonesAsignacion();
					listaInspeccionesEliminar.clear();
					recuperarPersonalInspeccion(tvPersonalAsig.getSelectionModel().getSelectedItem());
					
				}
			}
		});
		tpRealizadas.setOnSelectionChanged(new EventHandler<Event>() {
			@Override
			public void handle(Event arg0) {
				if(tpRealizadas.isSelected()) {
					bloquearBotonesAsignacion();
				}
			}
		});


		txtBuscar.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				String cadena = txtBuscar.getText().toUpperCase();
				txtBuscar.setText(cadena);
			}
		});
		tvDatosClientes.setRowFactory(tv -> {
			TableRow<Cliente> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
					if(tvDatosClientes.getSelectionModel().getSelectedItem() != null){
						Context.getInstance().setCliente(tvDatosClientes.getSelectionModel().getSelectedItem());
						helper.abrirPantallaModal("/clientes/ClientesEditar.fxml","Datos del Cliente", Context.getInstance().getStage());
						llenarDatos("");
					}
				}
			});
			return row ;
		});
		tvDatosInspecciones.setRowFactory(tv -> {
			TableRow<SolInspeccionIn> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (! row.isEmpty()) ) {

					if(tvDatosInspecciones.getSelectionModel().getSelectedItem() != null) {
						if(tvDatosInspecciones.getSelectionModel().getSelectedItem().getEstadoInspeccion().equals("REALIZADO")) {
							helper.mostrarAlertaError("No se puede editar una INSPECCIÓN ya realizada", Context.getInstance().getStage());
							return;
						}
						Context.getInstance().setInspeccion(tvDatosInspecciones.getSelectionModel().getSelectedItem());
						helper.abrirPantallaModal("/clientes/ClientesInspeccionEditar.fxml","Datos del Inspección", Context.getInstance().getStage());
						recuperarInspeccionesRealizadas("");
					}else {
						helper.mostrarAlertaError("Debe Seleccionar un Inspección a Editar", Context.getInstance().getStage());
					}

				}
			});
			return row ;
		});
		/*
		 * initialize para la inspeccion
		 */
		limpiarIns();
		bloquearIns();
		llenarCombosIns();
		cboEstadoIns.setText(Constantes.EST_INSPECCION_REALIZADO);
		cboEstadoIns.setEditable(false);
		cboUsoMedidor.getSelectionModel().selectFirst();
		//solo letras mayusculas
		txtReferenciaIns.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				String cadena = txtReferenciaIns.getText().toUpperCase();
				txtReferenciaIns.setText(cadena);
			}
		});
		/*
		 * initialize para las asignaciones
		 */
		
		tvPersonalAsig.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event arg0) {
				recuperarPersonalInspeccion(tvPersonalAsig.getSelectionModel().getSelectedItem());
			}
		});
		tvAsignados.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				if(tvAsignados.getSelectionModel().getSelectedItem().getEstadoInspeccion().equals(Constantes.EST_INSPECCION_REALIZADO)) 
					btnQuitarAsig.setDisable(true);
				else
					btnQuitarAsig.setDisable(false);
			}
		});
		cboBarrio.getSelectionModel().select(-1);
		cboUsoMedidor.getSelectionModel().select(-1);
		/*
		 * initialize para las inspecciones realizadas
		 */
		recuperarInspeccionesRealizadas("");
	}
	@FXML
	void editarCliente(ActionEvent event) {
		try {
			if(tvDatosClientes.getSelectionModel().getSelectedItem() != null) {
				Context.getInstance().setCliente(tvDatosClientes.getSelectionModel().getSelectedItem());
				helper.abrirPantallaModal("/clientes/ClientesEditar.fxml","Datos del Cliente", Context.getInstance().getStage());
				llenarDatos("");
			}else {
				helper.mostrarAlertaError("Debe Seleccionar un Cliente a Editar", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	@FXML
	void eliminarCliente(ActionEvent event) {
		try {
			if(tvDatosClientes.getSelectionModel().getSelectedItem() != null) {
				Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Se procederá a dar de baja al cliente.."
						+ "\nNombre: "
						+ tvDatosClientes.getSelectionModel().getSelectedItem().getNombre() + " " + tvDatosClientes.getSelectionModel().getSelectedItem().getApellido() 
						+ "\nCédula: "
						+ tvDatosClientes.getSelectionModel().getSelectedItem().getCedula()
						+ "\n\nDesea Continuar?",Context.getInstance().getStage());
				if(result.get() == ButtonType.OK){

					Cliente clienteSeleccionada = new Cliente();
					clienteSeleccionada = tvDatosClientes.getSelectionModel().getSelectedItem();
					clienteSeleccionada.setEstado("E");
					clienteDAO.getEntityManager().getTransaction().begin();
					clienteDAO.getEntityManager().merge(clienteSeleccionada);
					clienteDAO.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Cliente Dado de Baja", Context.getInstance().getStage());
					llenarDatos("");
				}
			}else {
				helper.mostrarAlertaError("Debe Seleccionar un Cliente a Dar de Baja", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	@FXML
	void imprimirListado(ActionEvent event) {
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("ID_CLIENTE", 7);
			PrintReport printReport = new PrintReport();
			printReport.crearReporte("/recursos/informes/lista_cliente.jasper", clienteDAO, param);
			printReport.showReport("Listado de Clientes");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@FXML
	void nuevoCliente(ActionEvent event) {
		try {
			Context.getInstance().setCliente(null);
			helper.abrirPantallaModal("/clientes/ClientesEditar.fxml","Datos del Cliente", Context.getInstance().getStage());
			llenarDatos("");

		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	void llenarDatos(String busqueda){
		try{
			tvDatosClientes.getColumns().clear();
			tvDatosClientes.getItems().clear();
			List<Cliente> listaClientes;
			ObservableList<Cliente> datos = FXCollections.observableArrayList();
			listaClientes = clienteDAO.getListaClientesPatron(busqueda);
			datos.setAll(listaClientes);

			//llenar los datos en la tabla
			TableColumn<Cliente, String> cedColum = new TableColumn<>("Cédula");
			cedColum.setMinWidth(10);
			cedColum.setPrefWidth(90);
			cedColum.setCellValueFactory(new PropertyValueFactory<Cliente, String>("cedula"));
			TableColumn<Cliente, String> nombresColum = new TableColumn<>("Nombres");
			nombresColum.setMinWidth(10);
			nombresColum.setPrefWidth(200);
			nombresColum.setCellValueFactory(new PropertyValueFactory<Cliente, String>("nombres"));
			TableColumn<Cliente, String> apellidosColum = new TableColumn<>("Apellidos");
			apellidosColum.setMinWidth(10);
			apellidosColum.setPrefWidth(200);
			apellidosColum.setCellValueFactory(new PropertyValueFactory<Cliente, String>("apellidos"));
			TableColumn<Cliente, String> generoColum = new TableColumn<>("Género");
			generoColum.setMinWidth(10);
			generoColum.setPrefWidth(100);
			generoColum.setCellValueFactory(new PropertyValueFactory<Cliente, String>("genero"));
			TableColumn<Cliente, String> fechColum = new TableColumn<>("Telefono");
			fechColum.setMinWidth(10);
			fechColum.setPrefWidth(100);
			fechColum.setCellValueFactory(new PropertyValueFactory<Cliente, String>("telefono"));
			TableColumn<Cliente, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(90);
			estadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Cliente,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Cliente, String> param) {
					String estado = "";
					if(param.getValue().getEstado().equals("A"))
						estado = "Activo";
					else if(param.getValue().getEstado().equals("I"))
						estado = "Inactivo";

					return new SimpleObjectProperty<String>(estado);
				}
			});
			tvDatosClientes.getColumns().addAll(cedColum, nombresColum,apellidosColum,generoColum,fechColum,estadoColum);
			tvDatosClientes.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}

	public void buscarCliente() {
		llenarDatos(txtBuscar.getText().toString());
	}
	/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 *operaciones de la vista de generar inspecciones 
	 +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	//variables globales
	Cliente clienteSeleccionado = new  Cliente();
	void llenarCombosIns() {
		try{
			cboUsoMedidor.setPromptText("Seleccione Uso Medidor");
			List<Categoria> listaCategoria;
			listaCategoria = categoriaDAO.getListaCategorias("");
			ObservableList<Categoria> datos = FXCollections.observableArrayList();

			datos.addAll(listaCategoria);
			cboUsoMedidor.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	private void llenarComboBarrio(){
		try{
			cboBarrio.setPromptText("Seleccione Barrio");
			List<Barrio> listaPerfiles;
			listaPerfiles = barrioDAO.getListaBarriosActivos();
			ObservableList<Barrio> datos = FXCollections.observableArrayList();

			datos.addAll(listaPerfiles);
			cboBarrio.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	void limpiarIns() {
		txtTelefonoIns.setText("");
		txtCedulaIns.setText("");
		txtNombresIns.setText("");
		txtApellidosIns.setText("");
		txtIdClienteIns.setText("0");
		txtEstadoIns.setText("");
		txtGeneroIns.setText("");
		txtReferenciaIns.setText("");
		cboBarrio.getSelectionModel().select(-1);
		cboUsoMedidor.getSelectionModel().select(-1);
		dtpFechaIns.setValue(null);
	}
	void bloquearIns() {
		txtTelefonoIns.setEditable(false);
		txtCedulaIns.setEditable(false);
		txtNombresIns.setEditable(false);
		txtApellidosIns.setEditable(false);
		txtIdClienteIns.setEditable(false);
		txtEstadoIns.setEditable(false);
		txtGeneroIns.setEditable(false);
	}

	@FXML
	void buscarIns(ActionEvent event) {
		try{
			helper.abrirPantallaModal("/clientes/ClientesListaClientes.fxml","Listado de Clientes", Context.getInstance().getStage());
			if (Context.getInstance().getCliente() != null) {
				Cliente datoSeleccionado = Context.getInstance().getCliente();
				clienteSeleccionado = datoSeleccionado;
				llenarDatosIns(datoSeleccionado);
				Context.getInstance().setCliente(null);
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	void llenarDatosIns(Cliente datosSeleccionado) {
		try {
			txtTelefonoIns.setText(datosSeleccionado.getTelefono());
			txtCedulaIns.setText(datosSeleccionado.getCedula());
			txtNombresIns.setText(datosSeleccionado.getNombre());
			txtApellidosIns.setText(datosSeleccionado.getApellido());
			txtIdClienteIns.setText(String.valueOf(datosSeleccionado.getIdCliente()));
			if(datosSeleccionado.getEstado().equals("A"))
				txtEstadoIns.setText("Activo");
			else
				txtEstadoIns.setText("Inactivo");
			txtGeneroIns.setText(datosSeleccionado.getGenero());
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@FXML
	void grabarIns(ActionEvent event) {
		try {
			if(txtIdClienteIns.getText().equals("0")) {
				helper.mostrarAlertaAdvertencia("Debe Seleccionar un Cliente", Context.getInstance().getStage());
				return;
			}
			if(dtpFechaIns.getValue() == null) {
				helper.mostrarAlertaAdvertencia("Debe registrar Fecha de la Inspección", Context.getInstance().getStage());
				return;
			}
			if(cboUsoMedidor.getSelectionModel().getSelectedIndex() == -1) {
				helper.mostrarAlertaAdvertencia("Debe Seleccionar el uso del medidor", Context.getInstance().getStage());
				return;
			}
			if(txtReferenciaIns.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Es Necesario Registrar Referencia Domiciliaria", Context.getInstance().getStage());
				return;
			}
			if(cboBarrio.getSelectionModel().getSelectedIndex() == -1) {
				helper.mostrarAlertaAdvertencia("Debe Seleccionar el Barrio del Cliente", Context.getInstance().getStage());
				return;
			}
			if (grabarDatos() == true) {
				helper.mostrarAlertaInformacion("Orden de Inspección Emitida con Exito", Context.getInstance().getStage());
				recuperarInspeccionesRealizadas("");
			}
			else
				helper.mostrarAlertaError("Error al Emitir Orden de Inspección", Context.getInstance().getStage());
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private boolean grabarDatos() {
		try {
			boolean bandera = false;
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				SolInspeccionIn inspeccion = new SolInspeccionIn();
				inspeccion.setEstado("A");
				inspeccion.setEstadoInspeccion(cboEstadoIns.getText());
				Date date = Date.from(dtpFechaIns.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
				Timestamp fecha = new Timestamp(date.getTime());
				inspeccion.setFechaIngreso(fecha);
				inspeccion.setCliente(clienteSeleccionado);
				inspeccion.setUsoMedidor(String.valueOf(cboUsoMedidor.getValue()));
				inspeccion.setIdSolInspeccion(null);
				inspeccion.setUsuarioCrea(Context.getInstance().getIdUsuario());
				inspeccion.setReferencia(txtReferenciaIns.getText());
				inspeccion.setBarrio(cboBarrio.getSelectionModel().getSelectedItem());
				inspeccionDAO.getEntityManager().getTransaction().begin();
				inspeccionDAO.getEntityManager().persist(inspeccion);
				inspeccionDAO.getEntityManager().getTransaction().commit();
				bandera = true;
				clienteSeleccionado = null;
				limpiarIns();
			}
			return bandera;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			inspeccionDAO.getEntityManager().getTransaction().rollback();;
			return false;
		}
	}
	@FXML
	void nuevoIns(ActionEvent event) {
		limpiarIns();
	}
	/*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 *operaciones de la vista de asignaciones 
	 +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	//variables globales
	private void bloquearBotonesAsignacion() {
		try {
			btnAsignarAsig.setDisable(true);
			btnQuitarAsig.setDisable(true);
			btnImprimirAsig.setDisable(false);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	private void llenarListaResponsables() {
		try{
			tvPersonalAsig.getColumns().clear();
			tvPersonalAsig.getItems().clear();
			List<SegUsuario> listaClientes;
			ObservableList<SegUsuario> datos = FXCollections.observableArrayList();
			listaClientes = usuarioDAO.getListaUsuariosInspeccion();
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
			apellidosColum.setPrefWidth(200);
			apellidosColum.setCellValueFactory(new PropertyValueFactory<SegUsuario, String>("apellidos"));
			TableColumn<SegUsuario, String> fechColum = new TableColumn<>("Telefono");
			fechColum.setMinWidth(10);
			fechColum.setPrefWidth(100);
			fechColum.setCellValueFactory(new PropertyValueFactory<SegUsuario, String>("telefono"));
			tvPersonalAsig.getColumns().addAll(idColum, nombresColum,apellidosColum,fechColum);
			tvPersonalAsig.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	public void buscarOrdenEmitida() {
		try {
			if(tvPersonalAsig.getSelectionModel().getSelectedItem() == null) {
				helper.mostrarAlertaError("Debe Seleccionar un Responsable de Inspección", Context.getInstance().getStage());
				return;
			}
			//pasar por parametro la lista de inspecciones a realzar para ir aminorando en el listado
			Context.getInstance().setListaInspecciones(tvNuevosAsig.getItems());
			helper.abrirPantallaModal("/clientes/ClientesOrdenPendiente.fxml","Listado de Órdenes de Inspección", Context.getInstance().getStage());
			if (Context.getInstance().getInspeccion() != null) {
				SolInspeccionIn ordenAgregar = Context.getInstance().getInspeccion();
				agregarOrden(ordenAgregar);
				Context.getInstance().setInspeccion(null);
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	private void agregarOrden(SolInspeccionIn ordenAgregar) {
		try {
			ObservableList<SolInspeccionIn> datos = FXCollections.observableArrayList();
			datos.setAll(tvNuevosAsig.getItems());
			datos.add(ordenAgregar);

			tvNuevosAsig.getItems().clear();
			tvNuevosAsig.getColumns().clear();

			//llenar los datos en la tabla
			TableColumn<SolInspeccionIn, String> idColum = new TableColumn<>("Código");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(50);
			idColum.setCellValueFactory(new PropertyValueFactory<SolInspeccionIn, String>("idInspeccion"));

			TableColumn<SolInspeccionIn, String> fechaColum = new TableColumn<>("Fecha");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(150);
			fechaColum.setCellValueFactory(new PropertyValueFactory<SolInspeccionIn, String>("fecha"));

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


			tvNuevosAsig.getColumns().addAll(idColum, fechaColum,clienteColum,referenciaColum,estadoColum);
			tvNuevosAsig.setItems(datos);

		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	/*
	 * falta progrmaar
	 * 
	 */
	public void imprimirAsignacion() {
		try {
			System.out.println("listado a eliminar: " + listaInspeccionesEliminar.size());
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	List<SolInspeccionIn> listaInspeccionesEliminar = new ArrayList<SolInspeccionIn>();
	public void quitarAsignacion() {
		try {
			
			if(tpRealizadas.isSelected()) {
				if(tvAsignados.getSelectionModel().getSelectedItem().getEstadoInspeccion().equals(Constantes.EST_INSPECCION_REALIZADO)) {
					System.out.println("realizado");
					helper.mostrarAlertaError("No se puede quitar una inspección ya realizada", Context.getInstance().getStage());
					return;
				}else if(tvAsignados.getSelectionModel().getSelectedItem().getEstadoInspeccion().equals(Constantes.EST_INSPECCION_PENDIENTE)) {
					System.out.println("pendiente");
					SolInspeccionIn insEliminar = tvAsignados.getSelectionModel().getSelectedItem();
					listaInspeccionesEliminar.add(insEliminar);
					tvAsignados.getItems().remove(tvAsignados.getSelectionModel().getSelectedItem());
				}
			}
			if(tpNuevas.isSelected()) {
				SolInspeccionIn ordenQuitar = tvNuevosAsig.getSelectionModel().getSelectedItem();
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
	private void desbloquearBotonesAsignacion() {
		try {
			btnAsignarAsig.setDisable(false);
			btnQuitarAsig.setDisable(false);
			btnImprimirAsig.setDisable(true);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void grabarAsig() {
		try {
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				if(tpRealizadas.isSelected()) {
					inspeccionDAO.getEntityManager().getTransaction().begin();
					for(SolInspeccionIn inspeccionGrabar : listaInspeccionesEliminar) {
						inspeccionGrabar.setIdUsuEncargado(null);
						inspeccionDAO.getEntityManager().merge(inspeccionGrabar);
					}
					inspeccionDAO.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Datos grabados!!", Context.getInstance().getStage());
					recuperarPersonalInspeccion(tvPersonalAsig.getSelectionModel().getSelectedItem());
					listaInspeccionesEliminar.clear();
					tvNuevosAsig.getItems().clear();
					tvNuevosAsig.getColumns().clear();
				}
				if(tpNuevas.isSelected()) {
					inspeccionDAO.getEntityManager().getTransaction().begin();
					for(SolInspeccionIn inspeccionGrabar : tvNuevosAsig.getItems()) {
						inspeccionGrabar.setIdUsuEncargado(tvPersonalAsig.getSelectionModel().getSelectedItem().getIdUsuario());
						inspeccionDAO.getEntityManager().merge(inspeccionGrabar);
					}
					inspeccionDAO.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Datos grabados!!", Context.getInstance().getStage());
					recuperarPersonalInspeccion(tvPersonalAsig.getSelectionModel().getSelectedItem());
					tvNuevosAsig.getItems().clear();
					tvNuevosAsig.getColumns().clear();
				}
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			inspeccionDAO.getEntityManager().getTransaction().rollback();
		}
	}
	/*
	 * Recupera el listado de las inspecciones realizadas y sin realizar pero asignadas al usuario
	 */
	@SuppressWarnings("unchecked")
	private void recuperarPersonalInspeccion(SegUsuario personalInspeccion) {
		try {
			List<SolInspeccionIn> listadoInspecciones = inspeccionDAO.getListaInspeccionAsignada(personalInspeccion.getIdUsuario());
			tvAsignados.getItems().clear();
			tvAsignados.getColumns().clear();
			ObservableList<SolInspeccionIn> datos = FXCollections.observableArrayList();
			datos.setAll(listadoInspecciones);
			//llenar los datos en la tabla
			TableColumn<SolInspeccionIn, String> idColum = new TableColumn<>("Código");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(50);
			idColum.setCellValueFactory(new PropertyValueFactory<SolInspeccionIn, String>("idInspeccion"));

			TableColumn<SolInspeccionIn, String> fechaColum = new TableColumn<>("Fecha");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(150);
			fechaColum.setCellValueFactory(new PropertyValueFactory<SolInspeccionIn, String>("fecha"));

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

			tvAsignados.getColumns().addAll(idColum, fechaColum,clienteColum,referenciaColum,estadoColum);
			tvAsignados.setItems(datos);

		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	/*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 *operaciones de la vista de inspeeciones realizadas
	 +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
	@FXML private TableView<SolInspeccionIn> tvDatosInspecciones;
	@FXML private Button btnEditarInspeccion;
	@FXML private TextField txtBuscarInspeecion;
	@FXML private Button btnEliminarInspeccion;
	@FXML private Button btnImprimirInspeccion;
	@SuppressWarnings("unchecked")
	private void recuperarInspeccionesRealizadas(String criterio) {
		try{
			tvDatosInspecciones.getColumns().clear();
			tvDatosInspecciones.getItems().clear();
			List<SolInspeccionIn> listaInspecciones;
			listaInspecciones = inspeccionDAO.getListaAllInspeccion(criterio);
			ObservableList<SolInspeccionIn> datos = FXCollections.observableArrayList();
			datos.setAll(listaInspecciones);

			//llenar los datos en la tabla
			TableColumn<SolInspeccionIn, String> idColum = new TableColumn<>("Cédula");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(90);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionIn,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionIn, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getCliente().getCedula());
				}
			});

			TableColumn<SolInspeccionIn, String> fechaColum = new TableColumn<>("Fecha");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(100);
			fechaColum.setCellValueFactory(new PropertyValueFactory<SolInspeccionIn, String>("fecha"));

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

			TableColumn<SolInspeccionIn, String> estadoColum = new TableColumn<>("Est. de Inspección");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(115);
			estadoColum.setCellValueFactory(new PropertyValueFactory<SolInspeccionIn, String>("estadoInspeccion"));


			tvDatosInspecciones.getColumns().addAll(idColum, fechaColum,clienteColum,referenciaColum,estadoColum);
			tvDatosInspecciones.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	public void editarInspeccion() {
		try {
			if(tvDatosInspecciones.getSelectionModel().getSelectedItem() != null) {
				if(tvDatosInspecciones.getSelectionModel().getSelectedItem().getEstadoInspeccion().equals(Constantes.EST_INSPECCION_REALIZADO)) {
					helper.mostrarAlertaError("No se puede editar una INSPECCIÓN ya realizada", Context.getInstance().getStage());
					return;
				}
				Context.getInstance().setInspeccion(tvDatosInspecciones.getSelectionModel().getSelectedItem());
				helper.abrirPantallaModal("/clientes/ClientesInspeccionEditar.fxml","Datos del Inspección", Context.getInstance().getStage());
				recuperarInspeccionesRealizadas("");
			}else {
				helper.mostrarAlertaError("Debe Seleccionar un Inspección a Editar", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void eliminarInspeccion() {
		try {
			if(tvDatosInspecciones.getSelectionModel().getSelectedItem() != null) {
				if(tvDatosInspecciones.getSelectionModel().getSelectedItem().getEstadoInspeccion().equals(Constantes.EST_INSPECCION_REALIZADO)){
					helper.mostrarAlertaError("No se puede dar de baja a una INSPECCIÓN ya realizada", Context.getInstance().getStage());
					return;
				}
				Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Se procederá a dar de baja a la Inspección generada al Cliente.."
						+ "\nNombre: "
						+ tvDatosInspecciones.getSelectionModel().getSelectedItem().getCliente().getNombre() + " " + tvDatosInspecciones.getSelectionModel().getSelectedItem().getCliente().getApellido() 
						+ "\nCédula: "
						+ tvDatosInspecciones.getSelectionModel().getSelectedItem().getCliente().getCedula()
						+ " Fecha: "
						+ tvDatosInspecciones.getSelectionModel().getSelectedItem().getFechaIngreso()
						+ "\n\nDesea Continuar?",Context.getInstance().getStage());
				if(result.get() == ButtonType.OK){
					SolInspeccionIn inspeccionSeleccionada = new SolInspeccionIn();
					inspeccionSeleccionada = tvDatosInspecciones.getSelectionModel().getSelectedItem();
					inspeccionSeleccionada.setEstado("I");
					inspeccionDAO.getEntityManager().getTransaction().begin();
					inspeccionDAO.getEntityManager().merge(inspeccionSeleccionada);
					inspeccionDAO.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Inspección Dado de Baja", Context.getInstance().getStage());
					recuperarInspeccionesRealizadas("");
				}
			}else {
				helper.mostrarAlertaError("Debe Seleccionar un Inspección a Dar de Baja", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void imprimirInspeccion() {
		try {
			if(tvDatosInspecciones.getSelectionModel().getSelectedItem() != null) {
				SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yy");
				SolInspeccionIn ins = tvDatosInspecciones.getSelectionModel().getSelectedItem();
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("ID_CLIENTE", ins.getCliente().getIdCliente());
				param.put("referencia", ins.getReferencia());

				if(ins.getUsoMedidor().equals(Constantes.CAT_VIVIENDA))
					param.put("vivienda", "X");
				else
					param.put("vivienda", "");

				if(ins.getUsoMedidor().equals(Constantes.CAT_COMERCIAL))
					param.put("comercial", "X");
				else
					param.put("comercial", "");

				if(ins.getUsoMedidor().equals(Constantes.CAT_ESTABLECIMIENTO))
					param.put("publico", "X");
				else
					param.put("publico", "");

				param.put("fecha_inspeccion", formateador.format(ins.getFechaIngreso()));

				if(ins.getFechaAprobacion() != null)
					param.put("fecha_aprobacion", formateador.format(ins.getFechaAprobacion()));
				else
					param.put("fecha_aprobacion", "");

				if(ins.getFactibilidad() == null) {
					param.put("reprobado", "");
					param.put("aprobado", "");
				}else if(ins.getFactibilidad().equals(Constantes.EST_NO_FACTIBLE)) {
					param.put("reprobado", "X");
					param.put("aprobado", "");
				}
				else if(ins.getFactibilidad().equals(Constantes.EST_FACTIBLE)) {
					param.put("aprobado", "X");
					param.put("reprobado", "");
				}
				PrintReport printReport = new PrintReport();
				printReport.crearReporte("/recursos/informes/ficha_inspeccion.jasper", clienteDAO, param);
				printReport.showReport("Ficha de Inspección");
			}else {
				helper.mostrarAlertaError("Debe Seleccionar un Inspección a imprimir la ficha de Inspección", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}