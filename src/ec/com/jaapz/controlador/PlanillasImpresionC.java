package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ec.com.jaapz.correo.EnviarCorreo;
import ec.com.jaapz.correo.Hilo2;
import ec.com.jaapz.modelo.AperturaLectura;
import ec.com.jaapz.modelo.Pago;
import ec.com.jaapz.modelo.Planilla;
import ec.com.jaapz.modelo.PlanillaDAO;
import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import ec.com.jaapz.util.GenerarPlanillaJasper;
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
import javafx.scene.control.ButtonType;
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
	PlanillaDAO planillaDAO = new PlanillaDAO();

	public void initialize() {
		try {
			btnBuscarApertura.setStyle("-fx-cursor: hand;");
			btnEnviarCorreo.setStyle("-fx-cursor: hand;");
			btnImprimirEnviar.setStyle("-fx-cursor: hand;");
			btnImprimirPlanilla.setStyle("-fx-cursor: hand;");
			
			btnImprimirPlanilla.setStyle("-fx-graphic: url('/imprimir.png');-fx-cursor: hand;");
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
			TableColumn<Planilla, String> totalColum = new TableColumn<>("Total Deuda");
			totalColum.setMinWidth(10);
			totalColum.setPrefWidth(110);
			totalColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Planilla,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Planilla, String> param) {
					List<Planilla> planillas = planillaDAO.getPlanillaCuenta(param.getValue().getCuentaCliente().getIdCuenta());
					double totalDeuda = 0;
					double abonos = 0;
					for(Planilla pl : planillas) {
						abonos = 0;
						if(pl.getCancelado().equals(Constantes.EST_FAC_PENDIENTE)) {
							for(Pago pago : pl.getPagos()) 
								if(pago.getEstado().equals(Constantes.ESTADO_ACTIVO)) 
									abonos = abonos + pago.getValor();
							totalDeuda = totalDeuda + (pl.getTotalPagar() - abonos);
						}
					}
					return new SimpleObjectProperty<String>(String.valueOf(totalDeuda));
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
								if(pl.getImprime() != null)
									if(pl.getImprime() == true)
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
								if(pl.getEnvia() != null)
									if(pl.getEnvia() == true)
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
			tvDatos.getColumns().addAll(medidorColum,clienteColum,antColum,actColum,totalColum,activeColumn,correoColumn);
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
		try {
			int contador = 0;
			for(Planilla pl : tvDatos.getItems()) {
				if(pl.getImprime() != null)
					if(pl.getImprime() == true) 
						contador = contador + 1;
			}
			if(contador == 0) {
				helper.mostrarAlertaAdvertencia("No hay ningun cliente seleccionado para la impresión de planillas", Context.getInstance().getStage());
				return;
			}
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Se imprimira la planilla de los clientes seleccionados\nDesea continuar?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				GenerarPlanillaJasper planillaJasper;
				for(Planilla pl : tvDatos.getItems()) {
					if(pl.getImprime() != null)
						if(pl.getImprime() == true) {
							planillaJasper = new GenerarPlanillaJasper();
							planillaJasper.crearPlanillaCliente(pl);
						}
				}
				helper.mostrarAlertaInformacion("Planillas impresas correctamente!!!", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	//metodo que sirve solo para el envio de correos de los estados de cuenta de los clientes
	public void enviarCorreo() {
		try {
			
			if(EnviarCorreo.ComprobarConexionInternet() == false) {
				helper.mostrarAlertaError("No se pudo establecer conexion a internet", Context.getInstance().getStage());
				return;
			}
			int contador = 0;
			for(Planilla pl : tvDatos.getItems()) {
				if(pl.getEnvia() != null)
					if(pl.getEnvia() == true) 
						contador = contador + 1;
			}
			if(contador == 0) {
				helper.mostrarAlertaAdvertencia("No hay ningun cliente seleccionado para el envio de correo", Context.getInstance().getStage());
				return;
			}
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Se procedera a enviar los estados de cuenta a los clientes\nel proceso dependera de su conexion a internet\ndesea continuar?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){	
				//primero se envia los correos electronicos a las personas que tienen uno
				GenerarPlanillasPDF planillaPDF;
				for(Planilla pl : tvDatos.getItems()) {
					if(pl.getEnvia() != null)
						if(pl.getEnvia() == true) {
							ivEnviandoMensaje.setVisible(true);
							btnEnviarCorreo.setDisable(true);
							if(pl.getCuentaCliente().getCliente().getEmail() != null) {
								planillaPDF = new GenerarPlanillasPDF();
								String adjunto = planillaPDF.crearEstadoCuenta(pl);
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
								miHilo.enviarCorreo();
							}
						}
				}	
				helper.mostrarAlertaInformacion("Mensajes enviados correctamente!!!", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void imprimirEnviar() {
		try {
			int contador = 0;
			for(Planilla pl : tvDatos.getItems()) {
				if(pl.getImprime() != null)
					if(pl.getImprime() == true) 
						contador = contador + 1;
			}
			for(Planilla pl : tvDatos.getItems()) {
				if(pl.getEnvia() != null)
					if(pl.getEnvia() == true) 
						contador = contador + 1;
			}
			if(contador == 0) {
				helper.mostrarAlertaAdvertencia("No hay ningun cliente seleccionado para la impresión de planilas, ni para envio de correo", Context.getInstance().getStage());
				return;
			}
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Los correos con estados de cuenta se envian primero, luego se imprime la planilla de consumo\nDesea continuar?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){	
				//primero se envia los correos electronicos a las personas que tienen uno
				GenerarPlanillasPDF planillaPDF;
				for(Planilla pl : tvDatos.getItems()) {
					if(pl.getEnvia() != null)
						if(pl.getEnvia() == true) {
							ivEnviandoMensaje.setVisible(true);
							btnEnviarCorreo.setDisable(true);
							if(pl.getCuentaCliente().getCliente().getEmail() != null) {
								planillaPDF = new GenerarPlanillasPDF();
								String adjunto = planillaPDF.crearEstadoCuenta(pl);
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
								miHilo.enviarCorreo();
							}
						}
				}	
				GenerarPlanillaJasper planillaJasper;
				for(Planilla pl : tvDatos.getItems()) {
					if(pl.getImprime() != null)
						if(pl.getImprime() == true) {
							planillaJasper = new GenerarPlanillaJasper();
							planillaJasper.crearPlanillaCliente(pl);
						}
				}
				helper.mostrarAlertaInformacion("Mensajes y Planillas generadas correctamente!!!", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

}
