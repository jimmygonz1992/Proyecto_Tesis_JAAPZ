package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ec.com.jaapz.modelo.AnioDAO;
import ec.com.jaapz.modelo.AperturaLectura;
import ec.com.jaapz.modelo.AperturaLecturaDAO;
import ec.com.jaapz.modelo.MeDAO;
import ec.com.jaapz.modelo.Planilla;
import ec.com.jaapz.modelo.PlanillaDetalle;
import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

public class LecturasIngresoC {
	@FXML private TableView<PlanillaDetalle> tvDatosLecturas;
	@FXML private Button btnGrabar;
	@FXML private TextField txtMes;
	@FXML private TextField txtAnio;
	@FXML private TextField txtFecha;
	@FXML private Button btnBuscarApertura;
	private AperturaLectura aperturaSeleccionada = new AperturaLectura();
	AperturaLecturaDAO aperturaDAO = new AperturaLecturaDAO();
	MeDAO mesDAO = new MeDAO();
	AnioDAO anioDAO = new AnioDAO();
	AperturaLectura aperturaActual = new AperturaLectura();
	ControllerHelper helper = new ControllerHelper();

	public void initialize() {
		try {
			btnBuscarApertura.setStyle("-fx-cursor: hand;");
			btnGrabar.setStyle("-fx-cursor: hand;");
			cargarCiclo();
			cargarClientes();
			tvDatosLecturas.setEditable(true);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	private void cargarClientes() {
		try {
			tvDatosLecturas.getItems().clear();
			tvDatosLecturas.getColumns().clear();
			List<PlanillaDetalle> listaDetalle = new ArrayList<PlanillaDetalle>();
			ObservableList<PlanillaDetalle> datos = FXCollections.observableArrayList();
			for(Planilla planilla : aperturaActual.getPlanillas()) {
				for(PlanillaDetalle detalle : planilla.getPlanillaDetalles()) {
					if(detalle.getIdentificadorOperacion().equals(Constantes.IDENT_LECTURA))
						listaDetalle.add(detalle);
				}
			}
			datos.setAll(listaDetalle);

			//llenar los datos en la tabla
			TableColumn<PlanillaDetalle, String> medidorColum = new TableColumn<>("Cód. Medidor");
			medidorColum.setMinWidth(10);
			medidorColum.setPrefWidth(110);
			medidorColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlanillaDetalle,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<PlanillaDetalle, String> param) {
					String medidor = "";
					if(param.getValue().getPlanilla().getCuentaCliente().getMedidor() != null)
						medidor = param.getValue().getPlanilla().getCuentaCliente().getMedidor().getCodigo();
					else
						medidor = "NO ASIGNADO";
					return new SimpleObjectProperty<String>(medidor);
				}
			});

			TableColumn<PlanillaDetalle, String> clienteColum = new TableColumn<>("Cliente");
			clienteColum.setMinWidth(10);
			clienteColum.setPrefWidth(200);
			clienteColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlanillaDetalle,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<PlanillaDetalle, String> param) {
					String nombre = param.getValue().getPlanilla().getCuentaCliente().getCliente().getNombre();
					String apellido = param.getValue().getPlanilla().getCuentaCliente().getCliente().getApellido();
					return new SimpleObjectProperty<String>(nombre + " " + apellido);
				}
			});

			TableColumn<PlanillaDetalle, String> antColum = new TableColumn<>("Lec.  Anterior");
			antColum.setMinWidth(10);
			antColum.setPrefWidth(90);
			antColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlanillaDetalle,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<PlanillaDetalle, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getPlanilla().getLecturaAnterior()));
				}
			});

			TableColumn<PlanillaDetalle, String> actColum = new TableColumn<>("Lec. Actual");
			actColum.setMinWidth(10);
			actColum.setPrefWidth(90);
			actColum.setCellFactory(TextFieldTableCell.<PlanillaDetalle>forTableColumn());
			actColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlanillaDetalle,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<PlanillaDetalle, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getPlanilla().getLecturaActual()));
				}
			});
			actColum.setOnEditCommit(
					new EventHandler<CellEditEvent<PlanillaDetalle, String>>() {
						@Override
						public void handle(CellEditEvent<PlanillaDetalle, String> t) {
							Integer valor = 0;
							if(isNumeric(t.getNewValue()) == false) {
								helper.mostrarAlertaError("Lectura incorrecta!!", Context.getInstance().getStage());
								valor = Integer.parseInt(t.getOldValue());
							}else {
								Integer anterior = Integer.parseInt(t.getOldValue());
								Integer lecActerior = tvDatosLecturas.getSelectionModel().getSelectedItem().getPlanilla().getLecturaAnterior();
								Integer nuevo = Integer.parseInt(t.getNewValue());
								if(nuevo < lecActerior) {
									valor = anterior;
									helper.mostrarAlertaError("Lectura incorrecta!!", Context.getInstance().getStage());
								}
								else {
									valor = nuevo;
								}
							}

							((PlanillaDetalle) t.getTableView().getItems().get(
									t.getTablePosition().getRow())
									).getPlanilla().setLecturaActual(valor);
							tvDatosLecturas.refresh();
						}
					}
					);
			tvDatosLecturas.getColumns().addAll(medidorColum,clienteColum,antColum,actColum);
			tvDatosLecturas.setItems(datos);

		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public static boolean isNumeric(String cadena) {
		boolean resultado;
		try {
			Integer.parseInt(cadena);
			resultado = true;
		} catch (NumberFormatException excepcion) {
			resultado = false;
		}
		return resultado;
	}
	private void cargarCiclo() {
		try {
			int numApertura = 0;
			AperturaLectura apertura = new AperturaLectura();
			numApertura = aperturaDAO.getListaAperturasEnProceso().size(); 
			if(numApertura > 0) {
				apertura = aperturaDAO.getListaAperturasEnProceso().get(0);
				aperturaActual = apertura;
				txtAnio.setText(apertura.getAnio().getDescripcion());
				txtMes.setText(apertura.getMe().getDescripcion());
				SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yy");
				txtFecha.setText(formateador.format(apertura.getFecha()));
			}else {
				txtAnio.setText("");
				txtMes.setText("");
				txtFecha.setText("");
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void buscarApertura() {
		try {
			helper.abrirPantallaModal("/lecturas/LecturasListaApertura.fxml","Aperturas Realizadas", Context.getInstance().getStage());
			if(Context.getInstance().getApertura() != null) {
				aperturaSeleccionada = Context.getInstance().getApertura();
				Context.getInstance().setApertura(null);
				recuperarAperturaSeleccionada(aperturaSeleccionada);//recupera los datos de la asignacion seleccionada
			}else {
				txtAnio.setText("");
				txtMes.setText("");
				txtFecha.setText("");
				Context.getInstance().setApertura(null);
				tvDatosLecturas.getItems().clear();
				tvDatosLecturas.getColumns().clear();
				aperturaSeleccionada = null;
				aperturaActual = null;
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void recuperarAperturaSeleccionada(AperturaLectura aperturaSeleccionada) {
		try {
			SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yy");
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
	public void grabarLecturas() {
		try {
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				List<PlanillaDetalle> listaDetalle = tvDatosLecturas.getItems();
				aperturaDAO.getEntityManager().getTransaction().begin();
				for(PlanillaDetalle det : listaDetalle) {
					Double categoriaPrecio = det.getPlanilla().getCuentaCliente().getCategoria().getValorM3();
					Integer lecturaActual = det.getPlanilla().getLecturaActual();
					Integer lecturaAnterior = det.getPlanilla().getLecturaAnterior();
					Integer consumo = lecturaActual - lecturaAnterior;
					det.getPlanilla().setUsuarioCrea(Context.getInstance().getIdUsuario());
					det.getPlanilla().setOrigen(Constantes.ORIGEN_ESCRITORIO);
					det.setCantidad(consumo);
					det.getPlanilla().setConsumo(consumo);
					det.setUsuarioCrea(Context.getInstance().getIdUsuario());
					det.getPlanilla().setConsumoMinimo(0);
					det.setSubtotal(consumo * categoriaPrecio);
					det.setEstado(Constantes.ESTADO_ACTIVO);
					aperturaDAO.getEntityManager().merge(det);
				}
				aperturaDAO.getEntityManager().getTransaction().commit();
				helper.mostrarAlertaInformacion("Datos Grabados Correctamente", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			aperturaDAO.getEntityManager().getTransaction().rollback();
		}
	}
}
