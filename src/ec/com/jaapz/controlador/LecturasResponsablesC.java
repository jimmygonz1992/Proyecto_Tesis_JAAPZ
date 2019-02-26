package ec.com.jaapz.controlador;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import ec.com.jaapz.modelo.AperturaLectura;
import ec.com.jaapz.modelo.Barrio;
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
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class LecturasResponsablesC {

    @FXML private TextField txtMesAperRes;
    @FXML private Button btnBuscarApertura;
    @FXML private Button btnQuitarAsig;
    @FXML private TextField txtFechAperRes;
    @FXML private TabPane tpAsignaciones;
    @FXML private TableView<Barrio> tvNuevosAsig;
    @FXML private TableView<ResponsableLectura> tvAsignados;
    @FXML private Button btnGrabarAsig;
    @FXML private TextField txtAnioAperRes;
    @FXML private TextField txtEstadoApeAsi;
    @FXML private TableView<SegUsuario> tvPersonalLectura;
    @FXML private Tab tpRealizadas;
    @FXML private Button btnAsignar;
    @FXML private Tab tpNuevas;
    @FXML private Button btnImprimirAsig;
    
    SegUsuarioDAO usuarioDAO = new SegUsuarioDAO();
    ControllerHelper helper = new ControllerHelper();
    ResponsableLecturaDAO responsableDAO = new ResponsableLecturaDAO();
    
    public void initialize() {
    	try {
    		btnAsignar.setStyle("-fx-cursor: hand;");
    		btnBuscarApertura.setStyle("-fx-cursor: hand;");
    		btnGrabarAsig.setStyle("-fx-cursor: hand;");
    		btnImprimirAsig.setStyle("-fx-cursor: hand;");
    		btnQuitarAsig.setStyle("-fx-cursor: hand;");
    		
			Context.getInstance().setBarrio(null);
			llenarListaResponsablesLectura();
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
			nombresColum.setCellValueFactory(new PropertyValueFactory<SegUsuario, String>("nombre"));
			TableColumn<SegUsuario, String> apellidosColum = new TableColumn<>("Apellidos");
			apellidosColum.setMinWidth(10);
			apellidosColum.setPrefWidth(100);
			apellidosColum.setCellValueFactory(new PropertyValueFactory<SegUsuario, String>("apellido"));
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
