package ec.com.jaapz.controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ec.com.jaapz.modelo.MaterialAdicional;
import ec.com.jaapz.modelo.MaterialAdicionalDAO;
import ec.com.jaapz.modelo.MaterialAdicionalDetalle;
import ec.com.jaapz.modelo.Planilla;
import ec.com.jaapz.modelo.PlanillaDAO;
import ec.com.jaapz.modelo.PlanillaDetalle;
import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Callback;

public class BodegaSalidaAdicionalesC {
	@FXML private Button btnBuscarLiquidCuenta;
	@FXML private TableView<MaterialAdicionalDetalle> tvDatos;
	@FXML private TextField txtDireccion;
	@FXML private TextField txtApellidos;
	@FXML private TextField txtIdLiquid;
	@FXML private Button btnNuevo;
	@FXML private TextField txtCedula;
	@FXML private DatePicker dtpFecha;
	@FXML private TextField txtTotal;
	@FXML private TextField txtUsuario;
	@FXML private TextField txtTelefono;
	@FXML private TextField txtIdCuenta;
	@FXML private TextField txtNombres;
	@FXML private Button btnGrabar;
	@FXML private TextArea txtObservaciones;
	
	
	ControllerHelper helper = new ControllerHelper();
	MaterialAdicional adicionalSeleccionado = new MaterialAdicional();
	MaterialAdicionalDAO materialDAO = new MaterialAdicionalDAO();
	public void initialize() {
		
	}
	
	public void buscarLiqCuenta() {
		try{
			helper.abrirPantallaModal("/bodega/ListadoAdicionales.fxml","Listado de materiales adicionales", Context.getInstance().getStage());
			if (Context.getInstance().getMaterialAdicional() != null) {
				adicionalSeleccionado = Context.getInstance().getMaterialAdicional();
				llenarDatosLiquidacion(adicionalSeleccionado);
				Context.getInstance().setMaterialAdicional(null);
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	void llenarDatosLiquidacion(MaterialAdicional datoSeleccionado){
		try {
			if(datoSeleccionado.getInstalacion().getCuentaCliente().getCliente().getCedula() == null)
				txtCedula.setText("");
			else
				txtCedula.setText(datoSeleccionado.getInstalacion().getCuentaCliente().getCliente().getCedula());

			if(datoSeleccionado.getInstalacion().getCuentaCliente().getIdCuenta() == null)
				txtIdCuenta.setText("");
			else
				txtIdCuenta.setText(String.valueOf(datoSeleccionado.getInstalacion().getCuentaCliente().getIdCuenta()));
			
			if(datoSeleccionado.getInstalacion().getCuentaCliente().getCliente().getNombre() == null)
				txtNombres.setText("");
			else
				txtNombres.setText(datoSeleccionado.getInstalacion().getCuentaCliente().getCliente().getNombre());
			
			if(datoSeleccionado.getInstalacion().getCuentaCliente().getCliente().getApellido() == null)
				txtApellidos.setText("");
			else
				txtApellidos.setText(datoSeleccionado.getInstalacion().getCuentaCliente().getCliente().getApellido());
			
			if(datoSeleccionado.getInstalacion().getCuentaCliente().getDireccion() == null)
				txtDireccion.setText("");
			else
				txtDireccion.setText(datoSeleccionado.getInstalacion().getCuentaCliente().getDireccion());
			
			if(datoSeleccionado.getInstalacion().getCuentaCliente().getCliente().getTelefono() == null)
				txtTelefono.setText("");
			else
				txtTelefono.setText(datoSeleccionado.getInstalacion().getCuentaCliente().getCliente().getTelefono());
			
			if(datoSeleccionado.getInstalacion().getIdInstalacion() == null)
				txtIdLiquid.setText("");
			else
				txtIdLiquid.setText(String.valueOf(datoSeleccionado.getInstalacion().getIdInstalacion()));
			
			
			recuperarDetalleLiquidacion(datoSeleccionado);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void recuperarDetalleLiquidacion(MaterialAdicional liq) {
		System.out.println(liq.getMaterialAdicionalDetalles().size());
		ObservableList<MaterialAdicionalDetalle> datos = FXCollections.observableArrayList();
		tvDatos.getColumns().clear();
		tvDatos.getItems().clear();
		
		datos.setAll(liq.getMaterialAdicionalDetalles());
		TableColumn<MaterialAdicionalDetalle, String> descripcionColum = new TableColumn<>("Descripción");
		descripcionColum.setMinWidth(10);
		descripcionColum.setPrefWidth(200);
		descripcionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MaterialAdicionalDetalle, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<MaterialAdicionalDetalle, String> param) {
				return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getRubro().getDescripcion()));
			}
		});
		
		TableColumn<MaterialAdicionalDetalle, String> cantidadColum = new TableColumn<>("Cantidad");
		cantidadColum.setMinWidth(10);
		cantidadColum.setPrefWidth(90);
		cantidadColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MaterialAdicionalDetalle, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<MaterialAdicionalDetalle, String> param) {
				return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCantidad()));
			}
		});
		
		TableColumn<MaterialAdicionalDetalle, String> precioColum = new TableColumn<>("Precio");
		precioColum.setMinWidth(10);
		precioColum.setPrefWidth(90);
		precioColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MaterialAdicionalDetalle, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<MaterialAdicionalDetalle, String> param) {
				return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getPrecio()));
			}
		});
		
		TableColumn<MaterialAdicionalDetalle, String> totalColum = new TableColumn<>("Total");
		totalColum.setMinWidth(10);
		totalColum.setPrefWidth(90);
		totalColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<MaterialAdicionalDetalle, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<MaterialAdicionalDetalle, String> param) {
				return new SimpleObjectProperty<String>(String.valueOf(String.format("%.2f", param.getValue().getCantidad()*param.getValue().getPrecio())));
			}
		});
		
		tvDatos.getColumns().addAll(descripcionColum, cantidadColum, precioColum, totalColum);
		tvDatos.setItems(datos);
		
		sumarDatos();
	}
	
	public void sumarDatos() {
		try {
			Double total = 0.0;
			for(MaterialAdicionalDetalle det : adicionalSeleccionado.getMaterialAdicionalDetalles()) {
				total = total + det.getSubtotal();
			}
			txtTotal.setText(String.valueOf(total));
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void grabar() {
		try {
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				//para grabar planilla que existe una reparacion
				PlanillaDAO planillaDAO = new PlanillaDAO();
				List<Planilla> listaPlanilla = planillaDAO.getPlanillaActual(adicionalSeleccionado.getInstalacion().getCuentaCliente().getIdCuenta());
				Planilla planilla = new Planilla();
				if(listaPlanilla.size() > 0)//necesito saber si tiene una planilla en proceso.. si esta la junta en la que se encuentra en proceso
					planilla = listaPlanilla.get(0);
				else {//caso contrario se verifica si existe alguna planilla que no este planillado.. x si acaso
					List<Planilla> noPlanillado = planillaDAO.getNoPlanillado(adicionalSeleccionado.getInstalacion().getCuentaCliente().getIdCuenta());
					if(noPlanillado.size() > 0) {
						planilla = noPlanillado.get(0);
					}else {//caso contrario se crea una nueva planilla.. pero aqui se pone el identificador.. para saber si esta pendiente
						planilla = new Planilla();
						planilla.setIdPlanilla(null);
						planilla.setIdentificadorProceso(Constantes.IDENT_PROCESO);//con esta variable se identifica si se encuentra procesada
						planilla.setCuentaCliente(adicionalSeleccionado.getInstalacion().getCuentaCliente());
						planilla.setEstado(Constantes.ESTADO_ACTIVO);
						System.out.println("crea una nueva planilla");
					}
				}
				
				
				//enlace entre detalle de planilla y planilla
				PlanillaDetalle detallePlanilla = new PlanillaDetalle();
				detallePlanilla.setIdPlanillaDet(null);
				detallePlanilla.setCantidad(1);
				detallePlanilla.setUsuarioCrea(Context.getInstance().getIdUsuario());
				detallePlanilla.setSubtotal(Double.parseDouble(txtTotal.getText()));
				detallePlanilla.setDescripcion("POR MATERIALES ADICIONALES EN REPARACIÓN");
				detallePlanilla.setIdentificadorOperacion(Constantes.IDENT_ADICIONAL);
				detallePlanilla.setEstado(Constantes.ESTADO_ACTIVO);
				detallePlanilla.setCantidad(1);
				
				
				if(planilla.getIdPlanilla() != null) // si es diferente de null es porque si tuvo en proceso una apertura
					planilla.addPlanillaDetalle(detallePlanilla);
				else { //caso contrario se debe de hacer los dos enlaces..
					List<PlanillaDetalle> detalles = new ArrayList<PlanillaDetalle>();
					detallePlanilla.setPlanilla(planilla);
					detalles.add(detallePlanilla);
					planilla.setPlanillaDetalles(detalles);
				}
				
				materialDAO.getEntityManager().getTransaction().begin();
				materialDAO.getEntityManager().merge(planilla);
				materialDAO.getEntityManager().getTransaction().commit();
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	
	public void nuevo() {

	}

}
