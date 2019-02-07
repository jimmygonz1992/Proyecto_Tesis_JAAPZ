package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import ec.com.jaapz.modelo.SegUsuario;
import ec.com.jaapz.modelo.SegUsuarioDAO;
import ec.com.jaapz.modelo.SolInspeccionIn;
import ec.com.jaapz.modelo.SolInspeccionInDAO;
import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import ec.com.jaapz.util.Encriptado;
import ec.com.jaapz.util.PrintReport;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class SolicitudAsignacionInsC {
	@FXML private Button btnQuitarAsig;
	@FXML private Tab tpRealizadas;
	@FXML private TabPane tpAsignaciones;
	@FXML private TableView<SegUsuario> tvPersonalAsig;
	@FXML private Tab tpNuevas;
	@FXML TableView<SolInspeccionIn> tvAsignados;
	@FXML TableView<SolInspeccionIn> tvNuevosAsig;
	@FXML private Button btnImprimirAsig;
	@FXML private Button btnGrabarAsig;
	@FXML private Button btnAsignarAsig;
	SegUsuarioDAO usuarioDAO = new SegUsuarioDAO();
	SolInspeccionInDAO inspeccionDAO = new SolInspeccionInDAO();
	List<SolInspeccionIn> listaInspeccionesEliminar = new ArrayList<SolInspeccionIn>();
	
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");

	ControllerHelper helper = new ControllerHelper();
	public void initialize() {
		try {
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
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
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
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SegUsuario,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SegUsuario, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdUsuario()));
				}
			});
			TableColumn<SegUsuario, String> nombresColum = new TableColumn<>("Nombres");
			nombresColum.setMinWidth(10);
			nombresColum.setPrefWidth(200);
			nombresColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SegUsuario,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SegUsuario, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getNombre()));
				}
			});
			TableColumn<SegUsuario, String> apellidosColum = new TableColumn<>("Apellidos");
			apellidosColum.setMinWidth(10);
			apellidosColum.setPrefWidth(200);
			apellidosColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SegUsuario,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SegUsuario, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getApellido()));
				}
			});

			TableColumn<SegUsuario, String> fechColum = new TableColumn<>("Telefono");
			fechColum.setMinWidth(10);
			fechColum.setPrefWidth(100);
			fechColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SegUsuario,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SegUsuario, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getTelefono()));
				}
			});
			tvPersonalAsig.getColumns().addAll(idColum, nombresColum,apellidosColum,fechColum);
			tvPersonalAsig.setItems(datos);
		}catch(Exception ex){
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

	public void imprimirAsignacion() {
		try {
			if(tvAsignados.getSelectionModel().getSelectedItem() != null) {
				SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yy");
				SolInspeccionIn ins = tvAsignados.getSelectionModel().getSelectedItem();
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("ID_CLIENTE", ins.getCliente().getIdCliente());
				param.put("ID_INSPECCION", ins.getIdSolInspeccion());
				param.put("referencia", ins.getReferencia());
				param.put("USUARIO_RESPONSABLE", Encriptado.Desencriptar(tvPersonalAsig.getSelectionModel().getSelectedItem().getUsuario()));
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
				printReport.crearReporte("/recursos/informes/ficha_inspeccion.jasper", usuarioDAO, param);
				printReport.showReport("Ficha de Inspección");
			}else 
				helper.mostrarAlertaError("Debe Seleccionar un Inspección a imprimir la ficha de Inspección", Context.getInstance().getStage());
		}catch(Exception ex) {
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
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionIn,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionIn, String> param) {
					String dato = "";
					dato = String.valueOf(param.getValue().getIdSolInspeccion());
					return new SimpleObjectProperty<String>(dato);
				}
			});

			TableColumn<SolInspeccionIn, String> fechaColum = new TableColumn<>("Fecha");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(150);
			fechaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionIn,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionIn, String> param) {
					String dato = "";
					dato = String.valueOf(formateador.format(param.getValue().getFechaIngreso()));
					return new SimpleObjectProperty<String>(dato);
				}
			});

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
			referenciaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionIn,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionIn, String> param) {
					String dato = "";
					dato = param.getValue().getReferencia();
					return new SimpleObjectProperty<String>(dato);
				}
			});


			TableColumn<SolInspeccionIn, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(90);
			estadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionIn,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionIn, String> param) {
					String dato = "";
					dato = param.getValue().getEstadoInspeccion();
					return new SimpleObjectProperty<String>(dato);
				}
			});


			tvNuevosAsig.getColumns().addAll(idColum, fechaColum,clienteColum,referenciaColum,estadoColum);
			tvNuevosAsig.setItems(datos);

		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void quitarAsignacion() {
		try {

			if(tpRealizadas.isSelected()) {
				if(tvAsignados.getSelectionModel().getSelectedItem().getEstadoInspeccion().equals(Constantes.EST_INSPECCION_REALIZADO)) {
					helper.mostrarAlertaError("No se puede quitar una inspección ya realizada", Context.getInstance().getStage());
					return;
				}else if(tvAsignados.getSelectionModel().getSelectedItem().getEstadoInspeccion().equals(Constantes.EST_INSPECCION_PENDIENTE)) {
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
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionIn,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionIn, String> param) {
					String dato = "";
					dato = String.valueOf(param.getValue().getIdSolInspeccion());
					return new SimpleObjectProperty<String>(dato);
				}
			});

			TableColumn<SolInspeccionIn, String> fechaColum = new TableColumn<>("Fecha");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(150);
			fechaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionIn,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionIn, String> param) {
					String dato = "";
					dato = String.valueOf(formateador.format(param.getValue().getFechaIngreso()));
					return new SimpleObjectProperty<String>(dato);
				}
			});
			

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
			referenciaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionIn,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionIn, String> param) {
					String dato = "";
					dato = param.getValue().getReferencia();
					return new SimpleObjectProperty<String>(dato);
				}
			});

			TableColumn<SolInspeccionIn, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(90);
			estadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionIn,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionIn, String> param) {
					String dato = "";
					dato = param.getValue().getEstadoInspeccion();
					return new SimpleObjectProperty<String>(dato);
				}
			});

			tvAsignados.getColumns().addAll(idColum, fechaColum,clienteColum,referenciaColum,estadoColum);
			tvAsignados.setItems(datos);

		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
}
