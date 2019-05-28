package ec.com.jaapz.controlador;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import ec.com.jaapz.modelo.Kardex;
import ec.com.jaapz.modelo.Reparacion;
import ec.com.jaapz.modelo.ReparacionDAO;
import ec.com.jaapz.modelo.ReparacionDetalle;
import ec.com.jaapz.modelo.Rubro;
import ec.com.jaapz.modelo.RubroDAO;
import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import ec.com.jaapz.util.Encriptado;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
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

public class BodegaSalidaRubroRepC {
	@FXML private TextField txtCedula;
	@FXML private TextField txtIdCuenta;
	@FXML private Button btnBuscarInspCuenta;
	@FXML private TextField txtNombres;
	@FXML private TextField txtApellidos;
	@FXML private TextField txtDireccion;
	@FXML private TextField txtReferencia;
	@FXML private TextField txtTelefono;
	@FXML private TextField txtCodigoMedidor;
	
	@FXML private TextField txtIdInspecc;
	@FXML private Button btnBuscarInspeccion;
	@FXML private DatePicker dtpFecha;
	@FXML private TextField txtUsuarioInspeccion;
	@FXML private TextField txtUsuarioCrea;
	@FXML private TextField txtIdReparacion;
	
	@FXML private TextArea txtObservaciones;
	@FXML private TableView<ReparacionDetalle> tvDatos;
	
	@FXML private Button btnEliminar;
	@FXML private Button btnGrabar;
	@FXML private Button btnNuevo;
	
	@FXML private TextField txtTotal;
	
	ControllerHelper helper = new ControllerHelper();
	Reparacion salidaRepSeleccionada = new Reparacion();
	ReparacionDAO reparacionDao = new ReparacionDAO();
	RubroDAO rubroDAO = new RubroDAO();
	
	public void initialize() {
		try {
			dtpFecha.setValue(LocalDate.now());
			txtUsuarioCrea.setText(Encriptado.Desencriptar(String.valueOf(Context.getInstance().getUsuariosC().getUsuario())));
			bloquear();
			
			btnBuscarInspCuenta.setStyle("-fx-cursor: hand;");
			btnBuscarInspeccion.setStyle("-fx-cursor: hand;");
			btnEliminar.setStyle("-fx-cursor: hand;");
			btnGrabar.setStyle("-fx-cursor: hand;");
			btnNuevo.setStyle("-fx-cursor: hand;");
			
			//validar solo numeros
			txtIdCuenta.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, 
						String newValue) {
					if (newValue.matches("\\d*")) {
						//int value = Integer.parseInt(newValue);
					} else {
						txtIdCuenta.setText(oldValue);
					}
				}
			});
			
			//validar solo numeros
			txtIdInspecc.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, 
						String newValue) {
					if (newValue.matches("\\d*")) {
						//int value = Integer.parseInt(newValue);
					} else {
						txtIdInspecc.setText(oldValue);
					}
				}
			});
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	void bloquear() {
		txtCedula.setEditable(false);
		txtIdCuenta.setEditable(false);
		txtNombres.setEditable(false);
		txtApellidos.setEditable(false);
		txtDireccion.setEditable(false);
		txtReferencia.setEditable(false);
		txtTelefono.setEditable(false);
		txtCodigoMedidor.setEditable(false);
		txtIdInspecc.setEditable(false);
		txtUsuarioInspeccion.setEditable(false);
		txtUsuarioCrea.setEditable(false);
		txtObservaciones.setEditable(false);
		txtTotal.setEditable(false);
		txtIdReparacion.setEditable(false);
		txtIdReparacion.setVisible(false);
	}
	
	public void	buscarInspCuenta() {
		try {
			helper.abrirPantallaModal("/bodega/BodegaListadoSalidaRep.fxml","Listado de Órdenes de Reparaciones", Context.getInstance().getStage());
			if (Context.getInstance().getReparaciones() != null) {
				salidaRepSeleccionada = Context.getInstance().getReparaciones();
				llenarDatosSalidaReparacion(salidaRepSeleccionada.getIdReparacion());
				Context.getInstance().setReparaciones(null);
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void buscarInspeccion() {
		try {
			helper.abrirPantallaModal("/bodega/BodegaListadoSalidaRep.fxml","Listado de Órdenes de Reparaciones", Context.getInstance().getStage());
			if (Context.getInstance().getReparaciones() != null) {
				salidaRepSeleccionada = Context.getInstance().getReparaciones();
				llenarDatosSalidaReparacion(salidaRepSeleccionada.getIdReparacion());
				Context.getInstance().setReparaciones(null);
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void llenarDatosSalidaReparacion(Integer idReparacion){
		try{		
			List<Reparacion> listaReparacion = new ArrayList<Reparacion>();
			listaReparacion = reparacionDao.getRecuperaReparacion(idReparacion);
			
			for(int i = 0 ; i < listaReparacion.size() ; i ++) {
				txtIdReparacion.setText(Integer.toString(listaReparacion.get(i).getIdReparacion()));
				txtIdInspecc.setText(Integer.toString(listaReparacion.get(i).getSolInspeccionRep().getIdSolicitudRep()));
				txtCedula.setText(listaReparacion.get(i).getCuentaCliente().getCliente().getCedula());
				txtIdCuenta.setText(Integer.toString(listaReparacion.get(i).getCuentaCliente().getIdCuenta()));
				txtNombres.setText(listaReparacion.get(i).getCuentaCliente().getCliente().getNombre());
				txtApellidos.setText(listaReparacion.get(i).getCuentaCliente().getCliente().getApellido());
				txtDireccion.setText(listaReparacion.get(i).getCuentaCliente().getDireccion());
				txtReferencia.setText(listaReparacion.get(i).getReferencia());
				txtTelefono.setText(listaReparacion.get(i).getCuentaCliente().getCliente().getTelefono());
				txtCodigoMedidor.setText(listaReparacion.get(i).getCuentaCliente().getMedidor().getCodigo());
				txtUsuarioInspeccion.setText(Integer.toString(listaReparacion.get(i).getUsuarioReparacion()));
				txtObservaciones.setText(listaReparacion.get(i).getObservcion());
				txtTotal.setText(Double.toString(listaReparacion.get(i).getTotal()));
			
				recuperarDetalleReparacion();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void recuperarDetalleReparacion() {
		ObservableList<ReparacionDetalle> datos = FXCollections.observableArrayList();
		List<ReparacionDetalle> lista = new ArrayList<ReparacionDetalle>();

		System.out.println(lista.size() + " Eliminar");
	
		tvDatos.getColumns().clear();
		tvDatos.getItems().clear();
		for(ReparacionDetalle dd : lista) {
			for(int i = 0 ; i < salidaRepSeleccionada.getReparacionDetalles().size(); i ++) {
				if(dd.getIdReparacionDet() == salidaRepSeleccionada.getReparacionDetalles().get(i).getIdReparacionDet())
					salidaRepSeleccionada.getReparacionDetalles().remove(i);
			}
		}

		System.out.println(salidaRepSeleccionada.getReparacionDetalles().size() + " Resultado");
		datos.setAll(salidaRepSeleccionada.getReparacionDetalles());

		TableColumn<ReparacionDetalle, String> descripcionColum = new TableColumn<>("Descripción");
		descripcionColum.setMinWidth(10);
		descripcionColum.setPrefWidth(200);
		descripcionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ReparacionDetalle, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<ReparacionDetalle, String> param) {
				return new SimpleObjectProperty<String>(param.getValue().getRubro().getDescripcion());
			}
		});

		TableColumn<ReparacionDetalle, String> cantidadColum = new TableColumn<>("Cantidad");
		cantidadColum.setMinWidth(10);
		cantidadColum.setPrefWidth(90);
		cantidadColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ReparacionDetalle, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<ReparacionDetalle, String> param) {
				return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCantidad()));
			}
		});

		TableColumn<ReparacionDetalle, String> precioColum = new TableColumn<>("Precio");
		precioColum.setMinWidth(10);
		precioColum.setPrefWidth(90);
		precioColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ReparacionDetalle, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<ReparacionDetalle, String> param) {
				return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getPrecio()));
			}
		});

		TableColumn<ReparacionDetalle, String> totalColum = new TableColumn<>("Total");
		totalColum.setMinWidth(10);
		totalColum.setPrefWidth(90);
		totalColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ReparacionDetalle, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<ReparacionDetalle, String> param) {
				return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCantidad()*param.getValue().getPrecio()));
			}
		});
		tvDatos.getColumns().addAll(descripcionColum, cantidadColum, precioColum, totalColum);
		tvDatos.setItems(datos);
	}
	
	public void eliminar() {
		try {
			
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	boolean validarDatos() {
		try {
			if(txtCedula.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar Cédula de Cliente", Context.getInstance().getStage());
				txtCedula.requestFocus();
				return false;
			}
			
			if(txtIdCuenta.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("No existe cuenta del Cliente", Context.getInstance().getStage());
				txtIdCuenta.requestFocus();
				return false;
			}
			
			if(txtNombres.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar Nombre de Cliente", Context.getInstance().getStage());
				txtNombres.requestFocus();
				return false;
			}
			
			if(txtApellidos.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar Apellido de Cliente", Context.getInstance().getStage());
				txtApellidos.requestFocus();
				return false;
			}
			
			if(txtDireccion.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("No existe Dirección", Context.getInstance().getStage());
				txtDireccion.requestFocus();
				return false;
			}
			
			if(txtReferencia.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingrese alguna referencia", Context.getInstance().getStage());
				txtReferencia.requestFocus();
				return false;
			}
			
			if(txtTelefono.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar Teléfono de Cliente", Context.getInstance().getStage());
				txtTelefono.requestFocus();
				return false;
			}
			
			if(txtIdInspecc.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("No existe identificador de inspección", Context.getInstance().getStage());
				txtIdInspecc.requestFocus();
				return false;
			}
			
			if(txtCodigoMedidor.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar Código de Medidor", Context.getInstance().getStage());
				txtCodigoMedidor.requestFocus();
				return false;
			}
			
			if(txtTotal.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar Valor Total de Instalación", Context.getInstance().getStage());
				txtTotal.requestFocus();
				return false;
			}
						
			if(dtpFecha.getValue().equals(null)) {
				helper.mostrarAlertaAdvertencia("Ingresar Fecha", Context.getInstance().getStage());
				dtpFecha.requestFocus();
				return false;
			}
			
			if(tvDatos.getItems().isEmpty()) {
				helper.mostrarAlertaAdvertencia("No contiene rubros", Context.getInstance().getStage());
				tvDatos.requestFocus();
				return false;
			}
			return true;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
	public void grabar() {
		try {
			if(validarDatos() == false)
				return;
			
			if(validarStockRubro() == true) {
				helper.mostrarAlertaError("Stock insuficiente de materiales", Context.getInstance().getStage());
				return;
			}
			
			if(salidaRepSeleccionada == null) {
				salidaRepSeleccionada = new Reparacion();
			}
			Date date = Date.from(dtpFecha.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				if (salidaRepSeleccionada.getIdReparacion() == null) {
					List<ReparacionDetalle> listaAgregadaRubros = new ArrayList<ReparacionDetalle>();
					for(ReparacionDetalle det : tvDatos.getItems()) {
						det.setIdReparacionDet(null);
						det.setEstado(Constantes.ESTADO_ACTIVO);
						det.setReparacion(salidaRepSeleccionada);
						listaAgregadaRubros.add(det);
					}
					
					//empieza la transaccion
					reparacionDao.getEntityManager().getTransaction().begin();
					salidaRepSeleccionada.setReparacionDetalles(listaAgregadaRubros);
					salidaRepSeleccionada.setEstadoEntrega(Constantes.EST_INSPECCION_REALIZADO);
					salidaRepSeleccionada.setFechaSalida(date);
					salidaRepSeleccionada.setUsuarioCreaSalida(Context.getInstance().getUsuariosC().getIdUsuario());
					//aqui voy a intentar guardar y tengo q preguntar si es nuevo
					//o sino solo para editar
					if(txtIdReparacion.getText().equals("0")) {//inserta nuevo ingreso
						salidaRepSeleccionada.getIdReparacion();
						reparacionDao.getEntityManager().persist(salidaRepSeleccionada);
					}else {//modifica
						salidaRepSeleccionada.setIdReparacion(Integer.parseInt(txtIdReparacion.getText()));
						reparacionDao.getEntityManager().merge(salidaRepSeleccionada);
					}
						
					//ingresoDao.getEntityManager().persist(ingreso);
					reparacionDao.getEntityManager().getTransaction().commit();
					helper.mostrarAlertaInformacion("Datos Grabados Correctamente", Context.getInstance().getStage());
					nuevo();
					tvDatos.getColumns().clear();
					tvDatos.getItems().clear();
				}else {
					List<Integer> integer = new ArrayList<Integer>();
					for (ReparacionDetalle detalle : tvDatos.getItems()) {
						if (detalle.getIdReparacionDet() != null)
							integer.add(detalle.getIdReparacionDet());
					}
					for(ReparacionDetalle det : tvDatos.getItems()) {
						if(det.getIdReparacionDet() == null) {
							det.setIdReparacionDet(null);
							det.setEstado(Constantes.ESTADO_ACTIVO);
							det.setReparacion(salidaRepSeleccionada);
							salidaRepSeleccionada.getReparacionDetalles().add(det);
						}else {
							for (ReparacionDetalle deta: salidaRepSeleccionada.getReparacionDetalles()) {
								if (!integer.contains(deta.getIdReparacionDet())) {
									deta.setEstado(Constantes.ESTADO_INACTIVO);
								}
							}
						}
					}
					
					//elimina material resta stock
					if(tvDatos != null) {
						List<Integer> idActual = new ArrayList<Integer>();
						for(ReparacionDetalle detalle : tvDatos.getItems()) {
							if(detalle.getIdReparacionDet() != null)
								idActual.add(detalle.getIdReparacionDet());
						}
							
						System.out.println("Ingresos actuales " + idActual.size());
						System.out.println("Ingresos en la bd " + salidaRepSeleccionada.getReparacionDetalles().size());
							
						for (ReparacionDetalle det : salidaRepSeleccionada.getReparacionDetalles()) {
							if(det.getIdReparacionDet() != null) {
								if(!idActual.contains(det.getIdReparacionDet())) {
									System.out.println("Rubro a eliminar " + det.getRubro().getDescripcion());
									Rubro rubroMod = det.getRubro();
									rubroMod.setStock(rubroMod.getStock() - det.getCantidad());
									System.out.println("Elimnia rubro " + rubroMod.getDescripcion());
									rubroDAO.getEntityManager().getTransaction().begin();
									rubroDAO.getEntityManager().merge(rubroMod);
									rubroDAO.getEntityManager().getTransaction().commit();
								}	
							}
						}
					}
					//sumar
					if(tvDatos != null) {
						List<Rubro> listaAgregadaRubros = new ArrayList<Rubro>();
						for(ReparacionDetalle detalle: tvDatos.getItems()) {
							if(detalle.getIdReparacionDet() == null)
								listaAgregadaRubros.add(detalle.getRubro());
						}
						for (Rubro rubro : listaAgregadaRubros) {
							for(ReparacionDetalle detalle : tvDatos.getItems()) {
								if(rubro.getIdRubro() == detalle.getRubro().getIdRubro())
									rubro.setStock(rubro.getStock() + detalle.getCantidad());
							}
							rubroDAO.getEntityManager().getTransaction().begin();
							rubroDAO.getEntityManager().merge(rubro);
							rubroDAO.getEntityManager().getTransaction().commit();
						}
					}
					//salidaRepSeleccionada.setReparacionDetalles(listaAgregadaRubros);
					salidaRepSeleccionada.setEstadoEntrega(Constantes.EST_INSPECCION_REALIZADO);
					salidaRepSeleccionada.setFechaSalida(date);
					salidaRepSeleccionada.setUsuarioCreaSalida(Context.getInstance().getUsuariosC().getIdUsuario());
					//aqui voy a intentar guardar y tengo q preguntar si es nuevo
					reparacionDao.getEntityManager().getTransaction().begin();
					reparacionDao.getEntityManager().merge(salidaRepSeleccionada);
					reparacionDao.getEntityManager().getTransaction().commit();
					actualizarListaArticulos();
					grabarKardexSalida();
					helper.mostrarAlertaInformacion("Datos grabados Correctamente", Context.getInstance().getStage());
					nuevo();
					tvDatos.getColumns().clear();
					tvDatos.getItems().clear();
				}
			}
			salidaRepSeleccionada = null;
		}catch(Exception ex) {
			reparacionDao.getEntityManager().getTransaction().rollback();
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
			System.out.println(ex.getMessage());
		}
	}
	
	public void actualizarListaArticulos() {
		if(tvDatos != null) {		
			List<Rubro> listaSalidaRubros = new ArrayList<Rubro>();
			for(ReparacionDetalle detalle: tvDatos.getItems()) {
				listaSalidaRubros.add(detalle.getRubro());
			}
			rubroDAO.getEntityManager().getTransaction().begin();
			for (Rubro rubro : listaSalidaRubros) {
				if(rubro.getIdRubro() != Constantes.ID_MEDIDOR || rubro.getIdRubro() != Constantes.ID_TASA_CONEXION) {
					for(ReparacionDetalle detalle : tvDatos.getItems()) {
						if(rubro.getIdRubro() == detalle.getRubro().getIdRubro())
							rubro.setStock(rubro.getStock() - detalle.getCantidad());
					}
					rubroDAO.getEntityManager().merge(rubro);	
				}
			}
			rubroDAO.getEntityManager().getTransaction().commit();
		}
	}
	
	private boolean validarStockRubro() {
		try {
			boolean bandera = false;
			if(tvDatos != null) {
				for(ReparacionDetalle detalle : tvDatos.getItems()) {
					if(detalle.getRubro().getIdRubro() != Constantes.ID_MEDIDOR || detalle.getRubro().getIdRubro() != Constantes.ID_TASA_CONEXION) {
						Rubro rubro = rubroDAO.getRubroById(detalle.getRubro().getIdRubro());
						if(detalle.getCantidad() > rubro.getStock())
							bandera = true;
					}
				}
			}
			return bandera;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
	private void grabarKardexSalida() {
		try {
			java.util.Date utilDate = new java.util.Date(); 
			
			reparacionDao.getEntityManager().getTransaction().begin();
			for(ReparacionDetalle det : tvDatos.getItems()) {
				Kardex kardex = new Kardex();
				//kardex.setIdKardex(null);
				kardex.setRubro(det.getRubro());
				kardex.setFecha(utilDate);
				kardex.setTipoDocumento("Documento Liquidación #");
				kardex.setNumDocumento(String.valueOf(salidaRepSeleccionada.getIdReparacion()));
				kardex.setDetalleOperacion("Salida de " + det.getRubro().getDescripcion());
				kardex.setCantidad(det.getCantidad());
				kardex.setUnidadMedida("Unidad");
				kardex.setValorUnitario(det.getPrecio());
				kardex.setCostoTotal(det.getCantidad()*det.getPrecio());
				kardex.setTipoMovimiento(Constantes.BODEGA_SALIDA);
				kardex.setEstado(Constantes.ESTADO_ACTIVO);
				reparacionDao.getEntityManager().persist(kardex);
			}
			reparacionDao.getEntityManager().getTransaction().commit();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void nuevo() {
		try {
			txtCedula.setText("");
			txtIdCuenta.setText("");
			txtNombres.setText("");
			txtApellidos.setText("");
			txtDireccion.setText("");
			txtReferencia.setText("");
			txtTelefono.setText("");
			txtCodigoMedidor.setText("");
			txtIdInspecc.setText("");
			dtpFecha.setValue(LocalDate.now());
			txtUsuarioInspeccion.setText("");
			txtObservaciones.setText("");
			tvDatos.getItems().clear();
			tvDatos.getColumns().clear();
			txtTotal.setText("");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}