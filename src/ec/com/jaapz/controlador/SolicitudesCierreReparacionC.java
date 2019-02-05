package ec.com.jaapz.controlador;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import ec.com.jaapz.modelo.CuentaCliente;
import ec.com.jaapz.modelo.Planilla;
import ec.com.jaapz.modelo.PlanillaDAO;
import ec.com.jaapz.modelo.PlanillaDetalle;
import ec.com.jaapz.modelo.Reparacion;
import ec.com.jaapz.modelo.ReparacionDAO;
import ec.com.jaapz.modelo.ReparacionDetalle;
import ec.com.jaapz.modelo.Rubro;
import ec.com.jaapz.modelo.RubroDAO;
import ec.com.jaapz.modelo.SolInspeccionRep;
import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import ec.com.jaapz.util.Encriptado;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

public class SolicitudesCierreReparacionC {
	@FXML private TextField txtCodigo;
	@FXML private Button btnBuscar;
	@FXML private TextField txtCodigoMedidor;
	@FXML private TextField txtFecha;
	@FXML private TextField txtReferencia;
	@FXML private TextField txtLongitud;
	@FXML private TextField txtLatitud;
	
	@FXML private TextField txtCedula;
	@FXML private TextField txtTelefono;
	@FXML private TextField txtNombres;
	@FXML private TextField txtContacto;
	@FXML private TextField txtDireccion;
	
	@FXML private TextArea txtNovedadesReportadas;
	
	@FXML private TextField txtCodigoMat;
	@FXML private Button btnBuscarRubro;
	@FXML private TextArea txtDescripcionProd;
	@FXML private TextField txtCantidad;
	@FXML private TextField txtPrecio;
	@FXML private TextField txtStock;
	
	@FXML private TextField txtUsuario;
	@FXML private DatePicker dtpFecha;
	
	@FXML private Button btnAgregar;
	@FXML private Button btnQuitar;
	@FXML private Button btnGrabar;
	@FXML private Button btnNuevo;
	
	@FXML private TextArea txtNovedadesInspeccion;
	
	@FXML TableView<ReparacionDetalle> tvDatosDetalle;
	@FXML private TextField txtIdSolicRep;
	@FXML private TextField txtIdCuenta;
	@FXML private TextField txtSubtotal;
	@FXML private TextField txtTotal;
	
	
	ControllerHelper helper = new ControllerHelper();
	SolInspeccionRep inspeccionRepSeleccionado = new SolInspeccionRep();
	SolInspeccionRep inspeccionRepRecuperado;
	CuentaCliente cuentaRecuperada = new CuentaCliente();
	Rubro rubroSeleccionado = new Rubro();
	RubroDAO rubroDao = new RubroDAO();
	ReparacionDAO reparacionDao = new ReparacionDAO();
	
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	
	public void initialize() {
		try {
			dtpFecha.setValue(LocalDate.now());
			bloquear();			
			txtUsuario.setText(Encriptado.Desencriptar(String.valueOf(Context.getInstance().getUsuariosC().getUsuario())));
			
			//recuperar Material
			txtCodigoMat.setOnKeyPressed(new EventHandler<KeyEvent>(){
				@Override
				public void handle(KeyEvent ke){
					if (ke.getCode().equals(KeyCode.ENTER)){
						if (validarProductoExiste() == false) {
							helper.mostrarAlertaAdvertencia("Elemento no existente!", Context.getInstance().getStage());
							txtCodigoMat.requestFocus();
							txtCodigoMat.setText("");
							txtDescripcionProd.setText("");
							txtStock.setText("");
							txtPrecio.setText("");
						}else {
							recuperarDatos(txtCodigoMat.getText());
							txtCantidad.requestFocus();
						}
					}
				}
			});
			
			//validar solo numeros
			txtCantidad.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, 
						String newValue) {
					if (newValue.matches("\\d*")) {
						//int value = Integer.parseInt(newValue);
					} else {
						txtCantidad.setText(oldValue);
					}
				}
			});
			
			//solo letras mayusculas
			txtNovedadesInspeccion.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					// TODO Auto-generated method stub
					String cadena = txtNovedadesInspeccion.getText().toUpperCase();
					txtNovedadesInspeccion.setText(cadena);
				}
			});
			
			//para anadir a la grilla con enter
			txtCantidad.setOnKeyPressed(new EventHandler<KeyEvent>(){
				@Override
				public void handle(KeyEvent ke){
					if (ke.getCode().equals(KeyCode.ENTER)){
						agregarMaterial();
						btnBuscarRubro.requestFocus();
					}
				}
			});
			
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	void bloquear() {
		txtCodigo.setEditable(false);
		txtCodigoMedidor.setEditable(false);
		txtFecha.setEditable(false);
		txtReferencia.setEditable(false);
		txtLatitud.setEditable(false);
		txtLongitud.setEditable(false);
		txtCedula.setEditable(false);
		txtTelefono.setEditable(false);
		txtContacto.setEditable(false);
		txtNombres.setEditable(false);
		txtDireccion.setEditable(false);
		txtNovedadesReportadas.setEditable(false);
		txtDescripcionProd.setEditable(false);
		txtStock.setEditable(false);
		txtPrecio.setEditable(false);
		txtIdSolicRep.setVisible(false);
		txtIdCuenta.setVisible(false);
		txtIdSolicRep.setEditable(false);
		txtIdCuenta.setEditable(false);
		txtUsuario.setEditable(false);
		txtSubtotal.setEditable(false);
		txtTotal.setEditable(false);
		txtSubtotal.setVisible(false);
		txtTotal.setVisible(false);
	}
	
	//recupera datos del material
	public void recuperarDatos(String codigo){
		try{
			List<Rubro> listaRubro = new ArrayList<Rubro>();
			listaRubro = rubroDao.getRecuperaRubro(codigo);
			for(int i = 0 ; i < listaRubro.size() ; i ++) {
				txtCodigoMat.setText(listaRubro.get(i).getCodigo());
				txtDescripcionProd.setText(listaRubro.get(i).getDescripcion());
				txtStock.setText(String.valueOf(listaRubro.get(i).getStock()));
				txtPrecio.setText(String.valueOf(listaRubro.get(i).getPrecio()));
					
				rubroSeleccionado = listaRubro.get(i);
			}
			if (listaRubro.size() == 0)
					rubroSeleccionado = new Rubro();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	boolean validarProductoExiste() {
		try {
			List<Rubro> listaRubros;
			listaRubros = rubroDao.getRecuperaRubro(txtCodigoMat.getText());
			if(listaRubros.size() != 0)
				return true;
			else
				return false;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
	public void nuevo() {
		txtCodigo.setText("");
		txtCodigoMedidor.setText("");
		txtFecha.setText("");
		txtReferencia.setText("");
		txtLatitud.setText("");
		txtLongitud.setText("");
		txtCedula.setText("");
		txtNombres.setText("");
		txtTelefono.setText("");
		txtContacto.setText("");
		txtDireccion.setText("");
		txtSubtotal.setText("");
		txtTotal.setText("");
		txtNovedadesReportadas.setText("");
		dtpFecha.setValue(null);
		txtNovedadesInspeccion.setText("");
		tvDatosDetalle.getColumns().clear();
		tvDatosDetalle.getItems().clear();
	}
	
	public void grabar() {
		try {
			if(validarDatos() == false)
				return;
			inspeccionRepSeleccionado.setIdSolicitudRep(Integer.parseInt(txtIdSolicRep.getText()));
			cuentaRecuperada.setIdCuenta(inspeccionRepSeleccionado.getCuentaCliente().getIdCuenta());
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){				
				double valorTotal = 0.0;
				Reparacion reparacion = new Reparacion();
				
				reparacion.setIdReparacion(null);
				Date date = Date.from(dtpFecha.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
				Timestamp fecha = new Timestamp(date.getTime());
				reparacion.setFechaCierreInspeccion(fecha);
				reparacion.setCuentaCliente(cuentaRecuperada);
				reparacion.setSolInspeccionRep(inspeccionRepSeleccionado);
				reparacion.setEstado(Constantes.ESTADO_ACTIVO);
				reparacion.setEstadoValor(Constantes.EST_FAC_PENDIENTE);
				reparacion.setEstadoReparacion(Constantes.EST_FAC_PENDIENTE);
				reparacion.setEstadoEntrega(Constantes.EST_FAC_PENDIENTE);
				reparacion.setObservcion(txtNovedadesInspeccion.getText());
				reparacion.setReferencia(txtReferencia.getText());
				reparacion.setUsuarioCrea(Context.getInstance().getUsuariosC().getIdUsuario());
				reparacion.setSubtotal(Double.parseDouble(txtSubtotal.getText()));
				reparacion.setTotal(Double.parseDouble(txtTotal.getText()));
				inspeccionRepSeleccionado.setEstadoInspecRep(Constantes.EST_INSPECCION_REALIZADO);
				
				List<ReparacionDetalle> listaAgregadaRubros = new ArrayList<ReparacionDetalle>();
				
				for(ReparacionDetalle det : tvDatosDetalle.getItems()) {
					det.setIdReparacionDet(null);
					det.setEstado(Constantes.ESTADO_ACTIVO);
					det.setReparacion(reparacion);
					det.setUsuarioCrea(Context.getInstance().getUsuariosC().getIdUsuario());
					det.setFechaCrea(fecha);
					valorTotal = valorTotal + (det.getPrecio()*det.getCantidad());
					listaAgregadaRubros.add(det);
				}
				
				reparacion.setReparacionDetalles(listaAgregadaRubros);
				
				//para grabar planilla que existe una reparacion
				PlanillaDAO planillaDAO = new PlanillaDAO();
				List<Planilla> listaPlanilla = planillaDAO.getPlanillaActual(cuentaRecuperada.getIdCuenta());
				Planilla planilla = new Planilla();
				if(listaPlanilla.size() > 0)//necesito saber si tiene una planilla en proceso.. si esta la junta en la que se encuentra en proceso
					planilla = listaPlanilla.get(0);
				else {//caso contrario se verifica si existe alguna planilla que no este planillado.. x si acaso
					List<Planilla> noPlanillado = planillaDAO.getNoPlanillado(cuentaRecuperada.getIdCuenta());
					if(noPlanillado.size() > 0) {
						planilla = noPlanillado.get(0);
					}else {//caso contrario se crea una nueva planilla.. pero aqui se pone el identificador.. para saber si esta pendiente
						planilla = new Planilla();
						planilla.setIdPlanilla(null);
						planilla.setIdentificadorProceso(Constantes.IDENT_PROCESO);//con esta variable se identifica si se encuentra procesada
						planilla.setCuentaCliente(cuentaRecuperada);
						planilla.setEstado(Constantes.ESTADO_ACTIVO);
					}
				}
				//enlace entre detalle de planilla y planilla
				PlanillaDetalle detallePlanilla = new PlanillaDetalle();
				detallePlanilla.setIdPlanillaDet(null);
				detallePlanilla.setCantidad(1);
				detallePlanilla.setUsuarioCrea(Context.getInstance().getIdUsuario());
				detallePlanilla.setSubtotal(valorTotal);
				detallePlanilla.setDescripcion("POR REPARACION EN EL SERVIO DE AGUA");
				detallePlanilla.setIdentificadorOperacion(Constantes.IDENT_REPARACION);
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
				
				reparacionDao.getEntityManager().getTransaction().begin();
				reparacionDao.getEntityManager().persist(reparacion);
				if(planilla.getIdPlanilla() != null) //se modifica
					reparacionDao.getEntityManager().merge(planilla);
				else//se inserta uno nuevo
					reparacionDao.getEntityManager().persist(planilla);
				reparacionDao.getEntityManager().merge(inspeccionRepSeleccionado);
				reparacionDao.getEntityManager().getTransaction().commit();
					
				helper.mostrarAlertaInformacion("Datos Grabados Correctamente", Context.getInstance().getStage());
				nuevo();
			}
		}catch(Exception ex) {
			reparacionDao.getEntityManager().getTransaction().rollback();
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
			System.out.println(ex.getMessage());
		}
	}
	
	//para sumar el subtotal y total de los materiales q se van en una instalación
	public void sumarDatos() {
		try {
			if (tvDatosDetalle.getItems().isEmpty()) {
				txtSubtotal.setText("0.0");
				txtTotal.setText("0.0");
			}else {
				double subtotal = 0;
				for(int i=0; i<tvDatosDetalle.getItems().size(); i++) {
					//DecimalFormat df = new DecimalFormat ("########.00");
					Double valorSubt = new Double(tvDatosDetalle.getItems().get(i).getCantidad()*tvDatosDetalle.getItems().get(i).getPrecio());
					subtotal += valorSubt;
					txtSubtotal.setText(String.valueOf(Double.valueOf(subtotal)));
					
					//total si es q se considera descuento sino va normal
					//double total = Double.valueOf(txtSubtotal.getText()) - Double.valueOf(txtDescuento.getText());
					double total = Double.valueOf(txtSubtotal.getText());
					txtTotal.setText(String.valueOf(Double.valueOf(total)));
				}
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	boolean validarDatos() {
		try {
			
			if(dtpFecha.getValue().equals(null)) {
				helper.mostrarAlertaAdvertencia("Escoja una fecha", Context.getInstance().getStage());
				dtpFecha.requestFocus();
				return false;
			}

			if(tvDatosDetalle.getItems().isEmpty()) {
				helper.mostrarAlertaAdvertencia("Ingresar Rubros", Context.getInstance().getStage());
				tvDatosDetalle.requestFocus();
				return false;
			}
			/*
			//valida si esq hay un ciclo en proceso
			if(aperturaDAO.validarApertura() == false) {
				helper.mostrarAlertaAdvertencia("No existe un ciclo iniciado.. por lo que no se puede procesar el pago", Context.getInstance().getStage());
				return false;
			}
			*/
			return true;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
	public void buscarSolicitudRep() {
		try{
			helper.abrirPantallaModal("/solicitudes/SolicitudListaReparaciones.fxml","Listado de Solicitudes de Reparación", Context.getInstance().getStage());
			if (Context.getInstance().getReparacion() != null) {
				recuperarDatos(Context.getInstance().getReparacion());
				inspeccionRepSeleccionado = Context.getInstance().getReparacion();
				Context.getInstance().setReparacion(null);
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	void recuperarDatos(SolInspeccionRep inspRep) {
		try {			
			txtCodigo.setText(String.valueOf(inspRep.getIdSolicitudRep()));
			txtCodigoMedidor.setText(inspRep.getCuentaCliente().getMedidor().getCodigo());
			txtFecha.setText(String.valueOf(formateador.format(inspRep.getFecha())));
			txtIdCuenta.setText(String.valueOf(inspRep.getCuentaCliente().getIdCuenta()));
			txtIdSolicRep.setText(String.valueOf(inspRep.getIdSolicitudRep()));
			txtReferencia.setText(inspRep.getReferencia());
			txtLatitud.setText(inspRep.getCuentaCliente().getLatitud());
			txtLongitud.setText(inspRep.getCuentaCliente().getLongitud());
			txtCedula.setText(inspRep.getCuentaCliente().getCliente().getCedula());
			txtContacto.setText(inspRep.getTelfContacto());
			txtDireccion.setText(inspRep.getCuentaCliente().getDireccion());
			txtNombres.setText(inspRep.getCuentaCliente().getCliente().getNombre() + " " + inspRep.getCuentaCliente().getCliente().getApellido());
			txtNovedadesReportadas.setText(inspRep.getObservacion());
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void buscarRubro() {
		try{
			helper.abrirPantallaModal("/bodega/ListadoRubros.fxml","Listado de Rubros", Context.getInstance().getStage());
			if (Context.getInstance().getRubros() != null) {
				rubroSeleccionado = Context.getInstance().getRubros();
				llenarDatosRubro(rubroSeleccionado);
				Context.getInstance().setRubros(null);
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	void llenarDatosRubro(Rubro datoSeleccionado){
		try {
			if(datoSeleccionado.getCodigo() == null)
				txtCodigoMat.setText("");
			else
				txtCodigoMat.setText(datoSeleccionado.getCodigo());
			
			if(datoSeleccionado.getDescripcion() == null)
				txtDescripcionProd.setText("");
			else
				txtDescripcionProd.setText(datoSeleccionado.getDescripcion());

			if(datoSeleccionado.getStock() == null)
				txtStock.setText("");
			else
				txtStock.setText(String.valueOf(datoSeleccionado.getStock()));

			txtPrecio.setText(String.valueOf(datoSeleccionado.getPrecio()));
			txtCantidad.requestFocus();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	boolean validarAnadirRubro() {
		try {
			if(txtCantidad.getText().equals("")) {
				helper.mostrarAlertaError("Ingresar Cantidad", Context.getInstance().getStage());
				//helper.mostrarAlertaAdvertencia("Ingresar Cantidad", Context.getInstance().getStage());
				txtCantidad.requestFocus();
				return false;
			}
			return true;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		} 
	}
	
	@SuppressWarnings("unchecked")
	public void agregarMaterial() {
		try {
			if(validarAnadirRubro() == false)
				return;
			ObservableList<ReparacionDetalle> datos = tvDatosDetalle.getItems();
			tvDatosDetalle.getColumns().clear();
			ReparacionDetalle datoAnadir = new ReparacionDetalle();
			rubroSeleccionado.setPrecio(Double.parseDouble(txtPrecio.getText()));
			datoAnadir.setRubro(rubroSeleccionado);
			datoAnadir.setCantidad(Integer.parseInt(txtCantidad.getText()));
			datoAnadir.setPrecio(Double.parseDouble(txtPrecio.getText()));
			datoAnadir.setSubtotal((Integer.parseInt(txtCantidad.getText()) * Double.parseDouble(txtPrecio.getText())));
			datos.add(datoAnadir);

			//llenar los datos en la tabla			
			TableColumn<ReparacionDetalle, String> descipcionColum = new TableColumn<>("Descripción");
			descipcionColum.setMinWidth(10);
			descipcionColum.setPrefWidth(200);
			descipcionColum.setCellValueFactory(new PropertyValueFactory<ReparacionDetalle, String>("rubro"));

			TableColumn<ReparacionDetalle, String> cantidadColum = new TableColumn<>("Cantidad");
			cantidadColum.setMinWidth(10);
			cantidadColum.setPrefWidth(90);
			cantidadColum.setCellValueFactory(new PropertyValueFactory<ReparacionDetalle, String>("cantidad"));

			TableColumn<ReparacionDetalle, String> precioColum = new TableColumn<>("Precio");
			precioColum.setMinWidth(10);
			precioColum.setPrefWidth(90);
			precioColum.setCellValueFactory(new PropertyValueFactory<ReparacionDetalle, String>("precio"));

			TableColumn<ReparacionDetalle, String> totalColum = new TableColumn<>("Total");
			totalColum.setMinWidth(10);
			totalColum.setPrefWidth(90);
			totalColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ReparacionDetalle, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<ReparacionDetalle, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCantidad()*param.getValue().getPrecio()));
				}
			});

			tvDatosDetalle.getColumns().addAll(descipcionColum, cantidadColum, precioColum, totalColum);
			tvDatosDetalle.setItems(datos);

			sumarDatos();

			rubroSeleccionado = null;
			limpiarTextosProd();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	void limpiarTextosProd() {
		txtCodigoMat.setText("");
		txtDescripcionProd.setText("");
		txtCantidad.setText("");
		txtPrecio.setText("");
		txtStock.setText("");	
	}
	
	public void quitarMaterial() {
		try {
			ReparacionDetalle detalleSeleccionado = tvDatosDetalle.getSelectionModel().getSelectedItem();
			tvDatosDetalle.getItems().remove(detalleSeleccionado);
			sumarDatos();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}