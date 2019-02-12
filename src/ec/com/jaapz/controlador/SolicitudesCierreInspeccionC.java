package ec.com.jaapz.controlador;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import ec.com.jaapz.correo.Hilo2;
import ec.com.jaapz.modelo.Categoria;
import ec.com.jaapz.modelo.CategoriaDAO;
import ec.com.jaapz.modelo.Cliente;
import ec.com.jaapz.modelo.CuentaCliente;
import ec.com.jaapz.modelo.Factible;
import ec.com.jaapz.modelo.SolInspeccionInDAO;
import ec.com.jaapz.modelo.LiquidacionDetalle;
import ec.com.jaapz.modelo.LiquidacionOrden;
import ec.com.jaapz.modelo.LiquidacionOrdenDAO;
import ec.com.jaapz.modelo.Medidor;
import ec.com.jaapz.modelo.Planilla;
import ec.com.jaapz.modelo.PlanillaDetalle;
import ec.com.jaapz.modelo.PrecioUnitario;
import ec.com.jaapz.modelo.PrecioUnitarioDAO;
import ec.com.jaapz.modelo.Rubro;
import ec.com.jaapz.modelo.SolInspeccionIn;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class SolicitudesCierreInspeccionC {
	@FXML private TextField txtCodigo;
	@FXML private TextField txtDireccion;
	@FXML private TextField txtFecha;
	@FXML private TextField txtReferencia;
	@FXML private TextField txtHabitar;
	@FXML private TextField txtCedula;
	@FXML private TextField txtNombres;
	@FXML private TextField txtTelefono;
	@FXML private TextField txtGenero;
	@FXML private TextField txtLatitud;
	@FXML private TextField txtLongitud;
	@FXML private TextArea txtObservacion;
	@FXML private ComboBox<Factible> cboFactible;
	@FXML private Button btnBuscar;
	@FXML private Button btnGrabar;
	@FXML private Button btnEliminar;
	@FXML private Button btnAgregar;
	@FXML private Button btnQuitar;
	@FXML private Button btnBuscarRubro;
	@FXML private TextField txtTipoRubro;
	@FXML private TextField txtDescripcion;
	@FXML private TextField txtStock;
	@FXML private TextField txtCantidad;
	@FXML private TextField txtPrecio;
	
	//nuevos componentes
	@FXML private TextField txtModelo;
	@FXML private TextField txtMarca;
	@FXML private TextField txtCodigoMedidor;
	@FXML private Button btnBuscarMedidor;
	
	@FXML TableView<LiquidacionDetalle> tvDatosOrdenPrevia;
	
	SolInspeccionIn inspeccionSeleccionado = new SolInspeccionIn();
	ControllerHelper helper = new ControllerHelper();
	LiquidacionOrdenDAO ordenPreviaDAO = new LiquidacionOrdenDAO();
	SolInspeccionInDAO inspeccionDAO = new SolInspeccionInDAO();
	CategoriaDAO categoriaDAO = new CategoriaDAO();
	Rubro rubroSeleccionado;
	DecimalFormat decimales = new DecimalFormat("#0.00");
	PrecioUnitarioDAO preciosDAO = new PrecioUnitarioDAO();
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	
	public void initialize() {
		txtObservacion.setDisable(true);
		limpiarCliente();
		bloquear();
		limpiarOrden();
		llenarCombo();	
		bloquearResultados();
		cboFactible.getSelectionModel().select(Factible.NO_FACTIBLE);
		cboFactible.setDisable(true);
	}
	@SuppressWarnings("unchecked")
	void recuperarDatos(SolInspeccionIn inspeccion) {
		try {
			tvDatosOrdenPrevia.getItems().clear();
			tvDatosOrdenPrevia.getColumns().clear();
			if(inspeccion.getEstadoInspeccion().equals(Constantes.EST_INSPECCION_REALIZADO)) {
				cboFactible.getSelectionModel().select(Factible.NO_FACTIBLE);
				cboFactible.setDisable(true);
			}else
				cboFactible.setDisable(false);
			
			txtCedula.setText(inspeccion.getCliente().getCedula());
			txtDireccion.setText(inspeccion.getCliente().getDireccion());
			txtNombres.setText(inspeccion.getCliente().getNombre() + " " + inspeccion.getCliente().getApellido());
			txtTelefono.setText(inspeccion.getCliente().getTelefono());
			txtGenero.setText(inspeccion.getCliente().getGenero());
			txtCodigo.setText(String.valueOf(inspeccion.getIdSolInspeccion()));
			txtFecha.setText(String.valueOf(formateador.format(inspeccion.getFechaIngreso())));
			txtReferencia.setText(inspeccion.getReferencia());
			txtHabitar.setText(inspeccion.getUsoMedidor());

			//recupera detalle si esta realizado
			if(inspeccion.getEstadoInspeccion().equals(Constantes.EST_INSPECCION_REALIZADO)) {
				//llenar los datos en la tabla
				TableColumn<LiquidacionDetalle, String> cantidadColum = new TableColumn<>("Cantidad");
				cantidadColum.setMinWidth(10);
				cantidadColum.setPrefWidth(90);
				cantidadColum.setCellValueFactory(new PropertyValueFactory<LiquidacionDetalle, String>("cantidad"));

				TableColumn<LiquidacionDetalle, String> descipcionColum = new TableColumn<>("Descripcion");
				descipcionColum.setMinWidth(10);
				descipcionColum.setPrefWidth(250);
				descipcionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiquidacionDetalle,String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<LiquidacionDetalle, String> param) {
						return new SimpleObjectProperty<String>(param.getValue().getRubro().getDescripcion());
					}
				});

				TableColumn<LiquidacionDetalle, String> precioColum = new TableColumn<>("Costo U.");
				precioColum.setMinWidth(10);
				precioColum.setPrefWidth(90);
				precioColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiquidacionDetalle,String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<LiquidacionDetalle, String> param) {
						return new SimpleObjectProperty<String>(String.valueOf(decimales.format(param.getValue().getRubro().getPrecio())));
					}
				});

				TableColumn<LiquidacionDetalle, String> costoColum = new TableColumn<>("Costo Total");
				costoColum.setMinWidth(10);
				costoColum.setPrefWidth(90);
				costoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiquidacionDetalle,String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<LiquidacionDetalle, String> param) {
						return new SimpleObjectProperty<String>(String.valueOf(decimales.format(param.getValue().getTotal())));
					}
				});

				ObservableList<LiquidacionDetalle> datos = FXCollections.observableArrayList();
				datos.setAll(inspeccion.getLiquidacionOrdens().get(0).getLiquidacionDetalles());

				tvDatosOrdenPrevia.getColumns().addAll(cantidadColum,descipcionColum,precioColum,costoColum);
				tvDatosOrdenPrevia.setItems(datos);
			}
			txtObservacion.setDisable(false);
			//tambien recupera la latitud, longitud y observaciones
			txtObservacion.setText(inspeccion.getObservacion());
			if(inspeccion.getLiquidacionOrdens().size() != 0) {
				txtLatitud.setText(inspeccion.getLiquidacionOrdens().get(0).getCuentaCliente().getLatitud());
				txtLongitud.setText(inspeccion.getLiquidacionOrdens().get(0).getCuentaCliente().getLongitud());
			}else {
				txtLatitud.setText("");
				txtLongitud.setText("");
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	void llenarCombo() {
		try{
			ObservableList<Factible> listaFactibilidad = FXCollections.observableArrayList(Factible.values());
			cboFactible.setItems(listaFactibilidad);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	void bloquear() {
		txtCedula.setEditable(false);
		txtDireccion.setEditable(false);
		txtNombres.setEditable(false);
		txtTelefono.setEditable(false);
		txtGenero.setEditable(false);
		txtCodigo.setEditable(false);
		txtFecha.setEditable(false);
		txtReferencia.setEditable(false);
		txtHabitar.setEditable(false);
		txtTipoRubro.setEditable(false);
		txtDescripcion.setEditable(false);
		txtStock.setEditable(false);
		txtPrecio.setEditable(false);
	}
	public void buscarOrden() {
		try{
			helper.abrirPantallaModal("/solicitudes/SolicitudesListaOrdenes.fxml","Listado de Órdenes", Context.getInstance().getStage());
			if (Context.getInstance().getInspeccion() != null) {
				recuperarDatos(Context.getInstance().getInspeccion());
				inspeccionSeleccionado = Context.getInstance().getInspeccion();
				Context.getInstance().setInspeccion(null);
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	public void grabar() {
		try {
			if(validarDatos() == false)
				return;
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				if(cboFactible.getSelectionModel().getSelectedItem().equals(Factible.FACTIBLE)) {//es factible la instalacion del medidor en la vivienda
					if(medidorSeleccionado == null) {
						helper.mostrarAlertaAdvertencia("Debe seleccionar un medidor!!!", Context.getInstance().getStage());
						return;
					}
					double valorTotal = 0.0;
					//declaro los objetos a grabar
					LiquidacionOrden ordenLiquidacion = new LiquidacionOrden();
					CuentaCliente cuentaCliente = new CuentaCliente();
					Categoria categoria = new Categoria();
					List<LiquidacionDetalle> detalle = new ArrayList<LiquidacionDetalle>();
					
					//para obtener la hora
					java.util.Date utilDate = new java.util.Date(); 
					long lnMilisegundos = utilDate.getTime();
					java.sql.Time sqlTime = new java.sql.Time(lnMilisegundos);
					
					medidorSeleccionado.setUsado(true);
					//lleno los datos de la orden de despacho
					ordenLiquidacion.setIdLiquidacion(null);
					ordenLiquidacion.setMedidor(medidorSeleccionado);
					ordenLiquidacion.setEstadoOrden(Constantes.EST_INSPECCION_PENDIENTE);
					ordenLiquidacion.setEstadoInstalacion(Constantes.EST_INSPECCION_PENDIENTE);
					ordenLiquidacion.setEstadoValor(Constantes.EST_INSPECCION_PENDIENTE);
					ordenLiquidacion.setEstado(Constantes.ESTADO_ACTIVO);
					ordenLiquidacion.setHora(sqlTime);
					
					ordenLiquidacion.setUsuarioCrea(Context.getInstance().getIdUsuario());

					Date date = new Date();
					Timestamp fecha = new Timestamp(date.getTime());
					ordenLiquidacion.setFecha(fecha);
					inspeccionSeleccionado.setEstadoInspeccion(Constantes.EST_INSPECCION_REALIZADO);
					//lista de detalle de la orden previa de inspeccion a insertar
					
					for(LiquidacionDetalle det : tvDatosOrdenPrevia.getItems()) {
						det.setIdDetalle(null);
						det.setLiquidacionOrden(ordenLiquidacion);//se le agrega a que orden pertenece ese detalle
						valorTotal = valorTotal + det.getTotal();
						detalle.add(det);
					}
					//tambien se suma el precio del medidor
					valorTotal = valorTotal + medidorSeleccionado.getPrecio();
					//se realiza el enlace con el detalle, ya que anteriormente se le habia asignado el detalle
					ordenLiquidacion.setLiquidacionDetalles(detalle);
					ordenLiquidacion.setTotal(valorTotal);
					ordenLiquidacion.setSolInspeccionIn(inspeccionSeleccionado);

					//tambien hay que asignar la categoria por defecto es la normal
					categoria = categoriaDAO.getListaCatNormal().get(0);//el 0 xq solo es un registro

					Cliente cliente = new Cliente();
					cliente = inspeccionSeleccionado.getCliente();
					cuentaCliente.setIdCuenta(null);
					cuentaCliente.setUsuarioCrea(Context.getInstance().getIdUsuario());
					cuentaCliente.setLatitud(txtLatitud.getText());
					cuentaCliente.setLongitud(txtLongitud.getText());
					cuentaCliente.setCliente(cliente);
					cuentaCliente.setDireccion(inspeccionSeleccionado.getDireccion());
					cuentaCliente.setBarrio(inspeccionSeleccionado.getBarrio());
					cuentaCliente.setCategoria(categoriaDAO.getCategoriaNombre(inspeccionSeleccionado.getUsoMedidor()));
					cuentaCliente.setFechaIngreso(fecha);
					cuentaCliente.setHoraIngreso(sqlTime);
					cuentaCliente.setEstado(Constantes.ESTADO_ACTIVO);
					
					//aqui para agregar la factura del 60% del costo de instalacion
					List<Planilla> listaAdd = new ArrayList<Planilla>();
					Planilla planilla = new Planilla(); // planilla nueva para el cliente
					planilla.setIdPlanilla(null);
					
					planilla.setHora(sqlTime);
					planilla.setFecha(fecha);
					planilla.setConvenio(Constantes.CONVENIO_NO);
					//obtener el consumo del mes anterior
					planilla.setConsumo(0);
					planilla.setConsumoMinimo(0);
					
					planilla.setIdentInstalacion(Constantes.IDENT_INSTALACION); //verdadero cuando es una nueva instalacion.. caso contrario es una planilla normal
					planilla.setLecturaAnterior(0);//son cero en primera instancia
					planilla.setLecturaActual(0);

					planilla.setTotalPagar(valorTotal);
					planilla.setTotalLetras(helper.cantidadConLetra(String.valueOf(valorTotal)));
					planilla.setEstado(Constantes.ESTADO_ACTIVO);
					planilla.setCancelado(Constantes.EST_FAC_PENDIENTE);
					planilla.setUsuarioCrea(Context.getInstance().getIdUsuario());
					listaAdd.add(planilla);
					//enlace entre cliente y planilla
					planilla.setCuentaCliente(cuentaCliente);
					cuentaCliente.setPlanillas(listaAdd);

					//enlace entre detalle de planilla y planilla
					PlanillaDetalle detallePlanilla = new PlanillaDetalle();
					detallePlanilla.setIdPlanillaDet(null);
					detallePlanilla.setCantidad(1);
					detallePlanilla.setUsuarioCrea(Context.getInstance().getIdUsuario());
					detallePlanilla.setSubtotal(valorTotal);
					detallePlanilla.setDescripcion("POR INSTALACIÓN DE NUEVO MEDIDOR");
					detallePlanilla.setIdentificadorOperacion(Constantes.IDENT_INSTALACION);
					detallePlanilla.setEstado(Constantes.ESTADO_ACTIVO);
					detallePlanilla.setCantidad(1);
					detallePlanilla.setPlanilla(planilla);
					List<PlanillaDetalle> det = new ArrayList<PlanillaDetalle>();
					det.add(detallePlanilla);
					planilla.setPlanillaDetalles(det);
					
					
					//enlace entre cuenta cliente y categoria
					cuentaCliente.setCategoria(categoria);
					List<CuentaCliente> listaCuenta = new ArrayList<CuentaCliente>();
					listaCuenta.add(cuentaCliente);
					categoria.setCuentaClientes(listaCuenta);
					
					ordenLiquidacion.setCuentaCliente(cuentaCliente);
					List<LiquidacionOrden> listaOrden = new ArrayList<LiquidacionOrden>();
					listaOrden.add(ordenLiquidacion);
					cuentaCliente.setLiquidacionOrdens(listaOrden);
					//enlace entre orden liquidacion y la inspeccion
					ordenLiquidacion.setSolInspeccionIn(inspeccionSeleccionado);
					inspeccionSeleccionado.getLiquidacionOrdens().add(ordenLiquidacion);

					
					//se procede a grabar los datos
					inspeccionDAO.getEntityManager().getTransaction().begin();
					inspeccionDAO.getEntityManager().merge(inspeccionSeleccionado);
					inspeccionDAO.getEntityManager().merge(medidorSeleccionado);
					
					//Tambien se hace una factura con el 60% del costo de instalacion
					
					inspeccionDAO.getEntityManager().getTransaction().commit();
					
					helper.mostrarAlertaInformacion("Datos Grabados", Context.getInstance().getStage());
					enviarCorreoCliente(valorTotal,cuentaCliente);
					
					limpiarCliente();
					limpiarOrden();
					limpiarObservaciones();
					cboFactible.getSelectionModel().select(Factible.NO_FACTIBLE);
					tvDatosOrdenPrevia.getColumns().clear();
					tvDatosOrdenPrevia.getItems().clear();
					tvDatosOrdenPrevia.getColumns().clear();
					tvDatosOrdenPrevia.getItems().clear();
					
					txtCodigoMedidor.setText("");
					txtModelo.setText("");
					txtMarca.setText("");
					medidorSeleccionado = null;
				}else {
					inspeccionSeleccionado.setEstadoInspeccion(Constantes.EST_INSPECCION_REALIZADO);
					inspeccionSeleccionado.setFactibilidad(cboFactible.getSelectionModel().getSelectedItem().toString());
					//se procede a grabar los daotos
					inspeccionDAO.getEntityManager().getTransaction().begin();
					inspeccionDAO.getEntityManager().merge(inspeccionSeleccionado);
					inspeccionDAO.getEntityManager().getTransaction().commit();
					
					helper.mostrarAlertaInformacion("Datos Grabados", Context.getInstance().getStage());
					limpiarCliente();
					limpiarOrden();
					limpiarObservaciones();
					cboFactible.getSelectionModel().select(Factible.NO_FACTIBLE);
					tvDatosOrdenPrevia.getColumns().clear();
					tvDatosOrdenPrevia.getItems().clear();
					tvDatosOrdenPrevia.getColumns().clear();
					tvDatosOrdenPrevia.getItems().clear();
					
					txtCodigoMedidor.setText("");
					txtModelo.setText("");
					txtMarca.setText("");
					medidorSeleccionado = null;
				}
				
			}
		}catch(Exception ex) {
			inspeccionDAO.getEntityManager().getTransaction().rollback();
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
			System.out.println(ex.getMessage());
		}
	}
	
	boolean validarDatos() {
		try {	
			if (cboFactible.getSelectionModel().getSelectedIndex() == 1) {
				if(txtObservacion.getText().toString().equals("")) {
					helper.mostrarAlertaAdvertencia("Registre observación", Context.getInstance().getStage());
					txtObservacion.requestFocus();
					return false;
				}
				return false;
			}
			return true;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
	private void enviarCorreoCliente(double totalPagar,CuentaCliente cuenta) {
		try {
			if(cuenta.getCliente().getEmail() != null) {
				String adjunto = "";
				String[] adjuntos = adjunto.split(",");
				String asunto;
				String destinatario;
				String mensaje;
				int servidor;
				String[] destinatarios;
				asunto = "Planilla generada por solicitud de instalación de nuevo medidor";
				destinatario = cuenta.getCliente().getEmail();
				destinatarios = destinatario.split(";");
				servidor = 0;
				mensaje = "Se ha generado planilla por solicitud de nuevo medidor por la cantidad de " + totalPagar + ".\n"
						+ "Es obligación del cliente cancelar al menos el 60% del total a pagar, para que se puede efectuar la instalación del \n"
						+ "medidor en el domicilio.. muchas gracias!!!";
				
				Hilo2 miHilo = new Hilo2(adjunto, adjuntos, destinatarios, servidor, destinatario, asunto, mensaje);
				miHilo.enviarCorreoSolicitud();	
				helper.mostrarAlertaInformacion("Mensaje enviado a dirección de correo electrónico del cliente", Context.getInstance().getStage());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void eliminar() {
		try {
			limpiar();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	void limpiar() {
		txtCodigo.setText("");
		txtFecha.setText("");
		txtReferencia.setText("");
		txtHabitar.setText("");
		txtCedula.setText("");
		txtTelefono.setText("");
		txtNombres.setText("");
		txtGenero.setText("");
		txtDireccion.setText("");
		txtCodigoMedidor.setText("");
		txtMarca.setText("");
		txtModelo.setText("");
		txtLatitud.setText("");
		txtLongitud.setText("");
		txtObservacion.setText("");
		txtTipoRubro.setText("");
		txtDescripcion.setText("");
		txtCantidad.setText("");
		txtPrecio.setText("");
		txtStock.setText("");
		tvDatosOrdenPrevia.getItems().clear();
		tvDatosOrdenPrevia.getColumns().clear();
		inspeccionSeleccionado = null;
		rubroSeleccionado = null;
	}
	
	public void cambiarFactibilidad() {
		try {
			if(cboFactible.getSelectionModel().getSelectedItem().equals(Factible.FACTIBLE)) {
				desbloquearResultados();
				txtObservacion.setDisable(false);
				cargarPreciosUnitarios();
			}
			else {
				bloquearResultados();
				tvDatosOrdenPrevia.getColumns().clear();
				tvDatosOrdenPrevia.getItems().clear();
				txtCodigoMedidor.setText("");
				txtModelo.setText("");
				txtMarca.setText("");
				txtObservacion.setText("");
				txtObservacion.setDisable(false);
				medidorSeleccionado = null;
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	private void cargarPreciosUnitarios() {
		try {
			tvDatosOrdenPrevia.getItems().clear();
			tvDatosOrdenPrevia.getColumns().clear();

			List<PrecioUnitario> listaPrecios;
			listaPrecios = preciosDAO.getListaPrecios();
			
			//llenar los datos en la tabla
			TableColumn<LiquidacionDetalle, String> cantidadColum = new TableColumn<>("Cantidad");
			cantidadColum.setMinWidth(10);
			cantidadColum.setPrefWidth(110);
			cantidadColum.setCellValueFactory(new PropertyValueFactory<LiquidacionDetalle, String>("cantidad"));

			TableColumn<LiquidacionDetalle, String> descipcionColum = new TableColumn<>("Descripcion");
			descipcionColum.setMinWidth(10);
			descipcionColum.setPrefWidth(350);
			descipcionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiquidacionDetalle,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<LiquidacionDetalle, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getRubro().getDescripcion());
				}
			});

			ObservableList<LiquidacionDetalle> datos = FXCollections.observableArrayList();
			for(PrecioUnitario p : listaPrecios) {
				LiquidacionDetalle orden = new LiquidacionDetalle();
				orden.setCantidad(p.getCantidad());
				orden.setRubro(p.getRubro());
				orden.setEstado("A");
				orden.setPrecio(p.getRubro().getPrecio());
				orden.setTotal(p.getTotal());
				orden.setIdDetalle(null);
				datos.add(orden);
			}

			tvDatosOrdenPrevia.getColumns().addAll(cantidadColum,descipcionColum);
			tvDatosOrdenPrevia.setItems(datos);

		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void buscarRubro() {
		try{
			helper.abrirPantallaModal("/clientes/ClientesListadoPrecios.fxml","Listado de Precios Unitarios", Context.getInstance().getStage());
			if (Context.getInstance().getRubro() != null) {
				rubroSeleccionado = Context.getInstance().getRubro();
				recuperarDatosRubros();
			}else
				limpiarRubro();
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	void recuperarDatosRubros() {
		try {
			txtStock.setText(String.valueOf(rubroSeleccionado.getStock()));
			txtDescripcion.setText(rubroSeleccionado.getDescripcion());
			txtPrecio.setText(String.valueOf(decimales.format(rubroSeleccionado.getPrecio())));
			txtTipoRubro.setText(rubroSeleccionado.getTipoRubro().getDescripcion());
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	public void agregarPrecioUnitario() {
		try {
			if(txtCantidad.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe Ingresar cantidad!!!", Context.getInstance().getStage());
				return;
			}
			ObservableList<LiquidacionDetalle> datos = tvDatosOrdenPrevia.getItems();
			tvDatosOrdenPrevia.getColumns().clear();
			LiquidacionDetalle datoAnadir = new LiquidacionDetalle();
			datoAnadir.setEstado("A");
			datoAnadir.setIdDetalle(null);
			datoAnadir.setRubro(rubroSeleccionado);
			datoAnadir.setCantidad(Integer.valueOf(txtCantidad.getText()));
			datoAnadir.setPrecio(Double.valueOf(txtCantidad.getText()) * rubroSeleccionado.getPrecio());
			datos.add(datoAnadir);


			//llenar los datos en la tabla
			TableColumn<LiquidacionDetalle, String> cantidadColum = new TableColumn<>("Cantidad");
			cantidadColum.setMinWidth(10);
			cantidadColum.setPrefWidth(110);
			cantidadColum.setCellValueFactory(new PropertyValueFactory<LiquidacionDetalle, String>("cantidad"));

			TableColumn<LiquidacionDetalle, String> descipcionColum = new TableColumn<>("Descripcion");
			descipcionColum.setMinWidth(10);
			descipcionColum.setPrefWidth(350);
			descipcionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LiquidacionDetalle,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<LiquidacionDetalle, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getRubro().getDescripcion());
				}
			});

			tvDatosOrdenPrevia.getColumns().addAll(cantidadColum,descipcionColum);
			tvDatosOrdenPrevia.setItems(datos);
			rubroSeleccionado = null;
			limpiarRubro();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			rubroSeleccionado = null;
			Context.getInstance().setRubro(null);
		}
	}
	public void quitarPrecioUnitario() {
		try {
			LiquidacionDetalle detalleSeleccionado = tvDatosOrdenPrevia.getSelectionModel().getSelectedItem();
			tvDatosOrdenPrevia.getItems().remove(detalleSeleccionado);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void limpiarCliente() {
		txtCedula.setText("");
		txtDireccion.setText("");
		txtNombres.setText("");
		txtTelefono.setText("");
		txtGenero.setText("");
	}
	private void limpiarOrden() {
		txtCodigo.setText("");
		txtFecha.setText("");
		txtReferencia.setText("");
		txtHabitar.setText("");
	}
	private void limpiarRubro() {
		txtStock.setText("");
		txtDescripcion.setText("");
		txtPrecio.setText("");
		txtTipoRubro.setText("");
		txtCantidad.setText("");
	}
	private void limpiarObservaciones() {
		txtObservacion.setText("");
		txtLatitud.setText("");
		txtLongitud.setText("");
	}
	void bloquearResultados() {
		tvDatosOrdenPrevia.setDisable(true);
		btnAgregar.setDisable(true);
		txtCodigoMedidor.setEditable(true);
		btnBuscarMedidor.setDisable(true);
		txtMarca.setEditable(true);
		txtModelo.setEditable(true);
		btnQuitar.setDisable(true);
		txtTipoRubro.setDisable(true);
		txtDescripcion.setDisable(true);
		txtStock.setDisable(true);
		txtCantidad.setDisable(true);
		txtPrecio.setDisable(true);
		btnBuscarRubro.setDisable(true);
		//
		txtCodigoMedidor.setDisable(true);
		txtMarca.setDisable(true);
		txtModelo.setDisable(true);
		txtLongitud.setDisable(true);
		txtLatitud.setDisable(true);
	}
	void desbloquearResultados() {
		tvDatosOrdenPrevia.setDisable(false);
		btnAgregar.setDisable(false);
		btnBuscarMedidor.setDisable(false);
		txtCodigoMedidor.setEditable(false);
		txtMarca.setEditable(false);
		txtModelo.setEditable(false);
		btnQuitar.setDisable(false);
		txtTipoRubro.setDisable(false);
		txtDescripcion.setDisable(false);
		txtStock.setDisable(false);
		txtCantidad.setDisable(false);
		txtPrecio.setDisable(false);
		btnBuscarRubro.setDisable(false);
		//
		txtCodigoMedidor.setDisable(false);
		txtMarca.setDisable(false);
		txtModelo.setDisable(false);
		txtLatitud.setDisable(false);
		txtLongitud.setDisable(false);
	}
	
	Medidor medidorSeleccionado;
	public void buscarMedidor() {
		try{
			helper.abrirPantallaModal("/solicitudes/SolicitudesListaMedidor.fxml","Listado Medidores", Context.getInstance().getStage());
			if (Context.getInstance().getMedidor() != null) {
				medidorSeleccionado = Context.getInstance().getMedidor();
				txtCodigoMedidor.setText(medidorSeleccionado.getCodigo());
				txtModelo.setText(medidorSeleccionado.getModelo());
				txtMarca.setText(medidorSeleccionado.getMarca());
			}else {
				txtCodigoMedidor.setText("");
				txtModelo.setText("");
				txtMarca.setText("");
				medidorSeleccionado = null;
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
}
