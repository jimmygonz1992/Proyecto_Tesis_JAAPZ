package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ec.com.jaapz.correo.EnviarCorreo;
import ec.com.jaapz.modelo.AperturaLectura;
import ec.com.jaapz.modelo.Planilla;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import ec.com.jaapz.util.GenerarPlanillasPDF;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

public class PlanillasImpresionC {
	@FXML private CheckBox chkImpTodo;
	@FXML private CheckBox chkEnvTodo;
	@FXML private Button btnImprimirPlanilla;
	@FXML private Button btnBuscarApertura;
	@FXML private TableView<Planilla> tvDatos;
	@FXML private Button btnEnviarCorreo;
	@FXML private TextField txtMes;
	@FXML private TextField txtAnio;
	@FXML private Button btnImprimirEnviar;
	@FXML private TextField txtFecha;
	@FXML private ImageView	ivEnviandoMensaje;

	AperturaLectura aperturaActual = new AperturaLectura();
	ControllerHelper helper = new ControllerHelper();
	private AperturaLectura aperturaSeleccionada = new AperturaLectura();

	public void initialize() {
		try {
			tvDatos.setEditable(true);
			Image image = new Image("enviando-mensaje.gif", 400, 200, true, true);
			ivEnviandoMensaje.setImage(image);
			ivEnviandoMensaje.setVisible(false);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void buscarApertura() {
		try {
			helper.abrirPantallaModal("/planillas/PlanillasListaApertura.fxml","Aperturas Realizadas", Context.getInstance().getStage());
			if(Context.getInstance().getApertura() != null) {
				aperturaSeleccionada = Context.getInstance().getApertura();
				Context.getInstance().setApertura(null);
				recuperarAperturaSeleccionada(aperturaSeleccionada);//recupera los datos de la asignacion seleccionada
				chkEnvTodo.setSelected(false);
				chkImpTodo.setSelected(false);
			}else {
				txtAnio.setText("");
				txtMes.setText("");
				txtFecha.setText("");
				Context.getInstance().setApertura(null);
				tvDatos.getItems().clear();
				tvDatos.getColumns().clear();
				aperturaSeleccionada = null;
				aperturaActual = null;
				chkEnvTodo.setSelected(false);
				chkImpTodo.setSelected(false);
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void recuperarAperturaSeleccionada(AperturaLectura aperturaSeleccionada) {
		try {
			SimpleDateFormat formateador = new SimpleDateFormat("dd, MMMMMMMMMM, yyyy");
			txtAnio.setText(String.valueOf(aperturaSeleccionada.getAnio().getDescripcion()));
			txtMes.setText(String.valueOf(aperturaSeleccionada.getMe().getDescripcion()));
			txtFecha.setText(formateador.format(aperturaSeleccionada.getFecha()));
			if(aperturaSeleccionada.getPlanillas().size() != 0) {
				aperturaActual = aperturaSeleccionada;
				cargarClientes();
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	private void cargarClientes() {
		try {
			tvDatos.getItems().clear();
			tvDatos.getColumns().clear();
			List<Planilla> listaDetalle = new ArrayList<Planilla>();
			ObservableList<Planilla> datos = FXCollections.observableArrayList();
			for(Planilla planilla : aperturaActual.getPlanillas()) {
				listaDetalle.add(planilla);
			}
			
			datos.setAll(listaDetalle);

			//llenar los datos en la tabla
			TableColumn<Planilla, String> medidorColum = new TableColumn<>("Cód. Medidor");
			medidorColum.setMinWidth(10);
			medidorColum.setPrefWidth(90);
			medidorColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Planilla,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Planilla, String> param) {
					String medidor = "";
					if(param.getValue().getCuentaCliente().getMedidor() != null)
						medidor = param.getValue().getCuentaCliente().getMedidor().getCodigo();
					else
						medidor = "NO ASIGNADO";
					return new SimpleObjectProperty<String>(medidor);
				}
			});

			TableColumn<Planilla, String> clienteColum = new TableColumn<>("Cliente");
			clienteColum.setMinWidth(10);
			clienteColum.setPrefWidth(200);
			clienteColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Planilla,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Planilla, String> param) {
					String nombre = param.getValue().getCuentaCliente().getCliente().getNombre();
					String apellido = param.getValue().getCuentaCliente().getCliente().getApellido();
					return new SimpleObjectProperty<String>(nombre + " " + apellido);
				}
			});

			TableColumn<Planilla, String> antColum = new TableColumn<>("Lec.  Anterior");
			antColum.setMinWidth(10);
			antColum.setPrefWidth(90);
			antColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Planilla,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Planilla, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getLecturaAnterior()));
				}
			});
			TableColumn<Planilla, String> actColum = new TableColumn<>("Lec. Actual");
			actColum.setMinWidth(10);
			actColum.setPrefWidth(90);
			actColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Planilla,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Planilla, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getLecturaActual()));
				}
			});

			TableColumn<Planilla, Boolean> activeColumn = new TableColumn<Planilla, Boolean>("imprime");
			activeColumn.setCellValueFactory(new Callback<CellDataFeatures<Planilla, Boolean>, ObservableValue<Boolean>>() {
				@Override
				public ObservableValue<Boolean> call(CellDataFeatures<Planilla, Boolean> param) {
					Planilla val = param.getValue();
					SimpleBooleanProperty booleanProp;
					if(val.getImprime() != null)
						booleanProp = new SimpleBooleanProperty(val.getImprime());
					else
						booleanProp = new SimpleBooleanProperty(false);
					booleanProp.addListener(new ChangeListener<Boolean>() {
						@Override
						public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
								Boolean newValue) {
							val.setImprime(newValue);
							int contador = 0;
							for(Planilla pl : tvDatos.getItems()) {
								if(pl.getImprime().equals(true) && pl.getImprime() != null)
									contador = contador + 1;
							}
							if(contador == tvDatos.getItems().size())
								chkImpTodo.setSelected(true);
							else
								chkImpTodo.setSelected(false);
						}
					});
					return booleanProp;
				}
			});
			activeColumn.setCellFactory(new Callback<TableColumn<Planilla, Boolean>, 
					TableCell<Planilla, Boolean>>() {
				@Override
				public TableCell<Planilla, Boolean> call(TableColumn<Planilla, Boolean> p) {
					CheckBoxTableCell<Planilla, Boolean> cell = new CheckBoxTableCell<Planilla, Boolean>();
					cell.setAlignment(Pos.CENTER);
					return cell;
				}
			});
			TableColumn<Planilla, Boolean> correoColumn = new TableColumn<Planilla, Boolean>("Env. Correo");
			correoColumn.setCellValueFactory(new Callback<CellDataFeatures<Planilla, Boolean>, ObservableValue<Boolean>>() {
				@Override
				public ObservableValue<Boolean> call(CellDataFeatures<Planilla, Boolean> param) {
					Planilla val = param.getValue();
					SimpleBooleanProperty booleanProp;
					if(val.getEnvia() != null)
						booleanProp = new SimpleBooleanProperty(val.getEnvia());
					else
						booleanProp = new SimpleBooleanProperty(false);
					booleanProp.addListener(new ChangeListener<Boolean>() {
						@Override
						public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
								Boolean newValue) {
							val.setEnvia(newValue);
							
							int contador = 0;
							for(Planilla pl : tvDatos.getItems()) {
								if(pl.getEnvia().equals(true) && pl.getEnvia() != null)
									contador = contador + 1;
							}
							if(contador == tvDatos.getItems().size())
								chkEnvTodo.setSelected(true);
							else
								chkEnvTodo.setSelected(false);
						}
					});
					return booleanProp;
				}
			});
			correoColumn.setCellFactory(new Callback<TableColumn<Planilla, Boolean>, 
					TableCell<Planilla, Boolean>>() {
				@Override
				public TableCell<Planilla, Boolean> call(TableColumn<Planilla, Boolean> p) {
					CheckBoxTableCell<Planilla, Boolean> cell = new CheckBoxTableCell<Planilla, Boolean>();
					cell.setAlignment(Pos.CENTER);
					return cell;
				}
			});
			tvDatos.getColumns().addAll(medidorColum,clienteColum,antColum,actColum,activeColumn,correoColumn);
			tvDatos.setItems(datos);

		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void imprimirTodo() {
		try {
			if(tvDatos.getItems().size() <= 0)
				return;
			if(aperturaActual == null)
				return;
			
			if(chkImpTodo.isSelected()){
				for(int i = 0 ; i < tvDatos.getItems().size() ; i ++) 
					tvDatos.getItems().get(i).setImprime(true);
			}else {
				for(int i = 0 ; i < tvDatos.getItems().size() ; i ++) 
					tvDatos.getItems().get(i).setImprime(false);
			}
			tvDatos.refresh();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void enviarTodo() {
		try {
			if(tvDatos.getItems().size() <= 0)
				return;
			if(aperturaActual == null)
				return;
			
			if(chkEnvTodo.isSelected()){
				for(int i = 0 ; i < tvDatos.getItems().size() ; i ++) 
					tvDatos.getItems().get(i).setEnvia(true);
			}else {
				for(int i = 0 ; i < tvDatos.getItems().size() ; i ++) 
					tvDatos.getItems().get(i).setEnvia(false);
			}
			tvDatos.refresh();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void imprimirPlanilla() {

	}

	public void enviarCorreo() {
		try {
			
			if(EnviarCorreo.ComprobarConexionInternet() == false) {
				helper.mostrarAlertaError("No se pudo establecer conexion a internet", Context.getInstance().getStage());
				return;
			}
	        
			Context.getInstance().setMensajeEnviado(false);
			for(Planilla pl : tvDatos.getItems()) {
				if(pl.getCuentaCliente().getCliente().getEmail() != null) {
					GenerarPlanillasPDF generador = new GenerarPlanillasPDF();
					generador.crearEstadoCuenta(pl);
					return;
					/*
					ivEnviandoMensaje.setVisible(true);
					btnEnviarCorreo.setDisable(true);
					String adjunto = "D:/consulta.txt";
					String[] adjuntos = adjunto.split(",");
					String asunto;
					String destinatario;
					String mensaje;
					int servidor;
					String medidor;
					String cliente;
					String[] destinatarios;
					asunto = "Estado de cuenta";
					destinatario = pl.getCuentaCliente().getCliente().getEmail();
					destinatarios = destinatario.split(";");
					servidor = 0;
					mensaje = "Estado de cuenta de: " + pl.getCuentaCliente().getCliente().getNombre();
					cliente = pl.getCuentaCliente().getCliente().getNombre();
					if(pl.getCuentaCliente().getMedidor() != null)
						medidor = pl.getCuentaCliente().getMedidor().getCodigo();
					else
						medidor = "SIN CODIGO DE MEDIDOR";
					Hilo2 miHilo = new Hilo2(adjunto, adjuntos, destinatarios, servidor, destinatario, asunto, mensaje,ivEnviandoMensaje,btnEnviarCorreo,
							cliente,medidor);
					miHilo.start();
					*/		
				}
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void imprimirEnviar() {

	}

}
