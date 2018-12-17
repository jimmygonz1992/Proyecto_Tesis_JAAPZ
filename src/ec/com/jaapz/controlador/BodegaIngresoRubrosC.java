package ec.com.jaapz.controlador;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import ec.com.jaapz.modelo.Ingreso;
import ec.com.jaapz.modelo.IngresoDAO;
import ec.com.jaapz.modelo.IngresoDetalle;
import ec.com.jaapz.modelo.Kardex;
import ec.com.jaapz.modelo.Proveedor;
import ec.com.jaapz.modelo.ProveedorDAO;
import ec.com.jaapz.modelo.Rubro;
import ec.com.jaapz.modelo.RubroDAO;
import ec.com.jaapz.modelo.SegUsuario;
import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import ec.com.jaapz.util.Encriptado;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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

public class BodegaIngresoRubrosC {
	@FXML private DatePicker dtpFecha;
	@FXML private TextField txtNumero;
	@FXML private TextField txtCodigo;
	@FXML private TextField txtCodigoProv;
	@FXML private TextField txtUsuario;
	@FXML private TextField txtSubtotal;
	@FXML private TextField txtDescuento;
	@FXML private TextField txtTotal;
	@FXML private Button btnAñadir;
	@FXML private Button btnEliminar;
	@FXML private Button btnGrabar;
	@FXML private Button btnNuevo;
	@FXML private Button btnBuscarRubro;
	@FXML private TextField txtCodigoMat;
	@FXML private TextArea txtDescripcionMat;
	@FXML private TextField txtCantidadMat;
	@FXML private TextField txtPrecioMat;
	@FXML private TextField txtStockMat;

	@FXML private TextField txtProveedor;
	@FXML private TextField txtRuc;
	@FXML private TextField txtNombresPro;
	@FXML private TextField txtApellidosPro;
	@FXML private TextField txtDireccionPro;
	@FXML private TextField txtTelefonoPro;
	@FXML private TextArea txtObservaciones;

	private @FXML TableView<IngresoDetalle> tvDatos;
	ControllerHelper helper = new ControllerHelper();
	Rubro rubroSeleccionado = new Rubro();
	Proveedor proveedorSeleccionado = new Proveedor();
	SegUsuario usuarioLogueado = new SegUsuario();
	IngresoDAO ingresoDao = new IngresoDAO();
	RubroDAO rubroDAO = new RubroDAO();
	ProveedorDAO proveedorDAO = new ProveedorDAO();
	Ingreso ingreso;

	int idfactura;
	int solorecupera = 0;
	int accion;
	int int_bandera;
	
	public void initialize(){
		try {
			Encriptado encriptado = new Encriptado();
			usuarioLogueado = Context.getInstance().getUsuariosC();
			txtUsuario.setText(encriptado.Desencriptar(String.valueOf(Context.getInstance().getUsuariosC().getUsuario())));
			txtUsuario.setEditable(false);
			txtCodigoMat.setEditable(false);
			txtDescripcionMat.setEditable(false);
			txtStockMat.setEditable(false);
			//dtpFecha.setValue(LocalDate.now());
			txtRuc.requestFocus();
			
			//validar solo numeros
			txtRuc.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, 
						String newValue) {
					if (newValue.matches("\\d*")) {
						//int value = Integer.parseInt(newValue);
					} else {
						txtRuc.setText(oldValue);
					}
				}
			});
			
			txtDescuento.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					txtDescuento.setText(newValue);
					if (txtDescuento.getText().equals("")){
						txtTotal.setText(newValue);
					}else if(Double.parseDouble(txtDescuento.getText()) <= Double.parseDouble(txtSubtotal.getText())){
						Double resultado = Double.valueOf(txtSubtotal.getText()) - Double.valueOf(txtDescuento.getText());
						txtTotal.setText(resultado.toString());
					}else{
						helper.mostrarAlertaAdvertencia("El valor ingresado es superior al Subtotal!.. Vuelva a ingresar!!", Context.getInstance().getStage());
						txtDescuento.setText("");
						txtDescuento.requestFocus();
					}				
				}
			});
			//para anadir a la grilla con enter
			txtCantidadMat.setOnKeyPressed(new EventHandler<KeyEvent>(){
				@Override
				public void handle(KeyEvent ke){
					if (ke.getCode().equals(KeyCode.ENTER)){
						anadir();
						btnBuscarRubro.requestFocus();
					}
				}
			});
			
			//recuperar Proveedor
			txtRuc.setOnKeyPressed(new EventHandler<KeyEvent>(){
				@Override
				public void handle(KeyEvent ke){
					if (ke.getCode().equals(KeyCode.ENTER)){
						if (validarRucPersonaNatural(txtRuc.getText()) == false){
							helper.mostrarAlertaAdvertencia("El número de RUC es incorrecto!", Context.getInstance().getStage());
							txtRuc.setText("");
							txtRuc.requestFocus();	
						}else {
							if (validarProveedorExiste() == false) {
								helper.mostrarAlertaAdvertencia("RUC no existente.. Debe llenar todos los datos!", Context.getInstance().getStage());
								proveedorSeleccionado = new Proveedor();
							}else {
								recuperarDatos(txtRuc.getText());
								txtNumero.requestFocus();
							}
						}
					}
				}
			});
			
			//recuperar factura
			txtNumero.setOnKeyPressed(new EventHandler<KeyEvent>(){
				@Override
				public void handle(KeyEvent ke){
					if (ke.getCode().equals(KeyCode.ENTER)){
						if (validarIngresoExiste() == true) {
							recuperarIngreso(txtNumero.getText());
							//proveedorSeleccionado = new Proveedor();
						}
					}
				}
			});
			
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	boolean validarIngresoExiste() {
		try {
			List<Ingreso> listaIngresos;
			listaIngresos = ingresoDao.getRecuperaIngreso(txtNumero.getText());
			if(listaIngresos.size() != 0)
				return true;
			else
				return false;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}

	public void recuperarIngreso(String numIngreso){
		try{		
			List<Ingreso> listaIngreso = new ArrayList<Ingreso>();
			listaIngreso = ingresoDao.getRecuperaIngreso(numIngreso);
			for(int i = 0 ; i < listaIngreso.size() ; i ++) {
				LocalDate date = listaIngreso.get(i).getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				
				
				txtCodigo.setText(Integer.toString(listaIngreso.get(i).getIdIngreso()));
				txtCodigoProv.setText(Integer.toString(listaIngreso.get(i).getProveedor().getIdProveedor()));
				txtRuc.setText(listaIngreso.get(i).getProveedor().getRuc());
				txtProveedor.setText(listaIngreso.get(i).getProveedor().getNombreComercial());
				txtNombresPro.setText(listaIngreso.get(i).getProveedor().getNombres());
				txtApellidosPro.setText(listaIngreso.get(i).getProveedor().getApellidos());
				txtDireccionPro.setText(listaIngreso.get(i).getProveedor().getDireccion());
				txtTelefonoPro.setText(listaIngreso.get(i).getProveedor().getTelefono());
				txtNumero.setText(listaIngreso.get(i).getNumeroIngreso());
				dtpFecha.setValue(date);
				
				
				txtSubtotal.setText(Double.toString(listaIngreso.get(i).getSubtotal()));
				//txtDescuento.setText(Double.toString(listaIngreso.get(i).getTotal()));
				txtTotal.setText(Double.toString(listaIngreso.get(i).getTotal()));		

				ingreso = listaIngreso.get(i);
				recuperarDetalleIngreso();

				//proveedorSeleccionado = listaProveedor.get(i);
			}
			//if (listaProveedor.size() == 0)
			//proveedorSeleccionado = new Proveedor();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void recuperarDetalleIngreso() {
		ObservableList<IngresoDetalle> datos = FXCollections.observableArrayList();
		List<IngresoDetalle> lista = new ArrayList<IngresoDetalle>();

		for (int i = 0 ; i < ingreso.getIngresoDetalles().size() ; i++) {
			if (ingreso.getIngresoDetalles().get(i).getEstado().equals("I"))
				lista.add(ingreso.getIngresoDetalles().get(i));
		}
		System.out.println(lista.size() + " Eliminar");
	
		tvDatos.getColumns().clear();
		tvDatos.getItems().clear();
		for(IngresoDetalle dd : lista) {
			for(int i = 0 ; i < ingreso.getIngresoDetalles().size() ; i ++) {
				if(dd.getIdIngresoDet() == ingreso.getIngresoDetalles().get(i).getIdIngresoDet())
					ingreso.getIngresoDetalles().remove(i);
			}
		}

		System.out.println(ingreso.getIngresoDetalles().size() + " Resultado");
		datos.setAll(ingreso.getIngresoDetalles());

		TableColumn<IngresoDetalle, String> descripcionColum = new TableColumn<>("Descripción");
		descripcionColum.setMinWidth(10);
		descripcionColum.setPrefWidth(200);
		descripcionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<IngresoDetalle, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<IngresoDetalle, String> param) {
				return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getRubro().getDescripcion()));
			}
		});

		TableColumn<IngresoDetalle, String> cantidadColum = new TableColumn<>("Cantidad");
		cantidadColum.setMinWidth(10);
		cantidadColum.setPrefWidth(90);
		cantidadColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<IngresoDetalle, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<IngresoDetalle, String> param) {
				return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCantidad()));
			}
		});

		TableColumn<IngresoDetalle, String> precioColum = new TableColumn<>("Precio");
		precioColum.setMinWidth(10);
		precioColum.setPrefWidth(90);
		precioColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<IngresoDetalle, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<IngresoDetalle, String> param) {
				return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getPrecio()));
			}
		});

		TableColumn<IngresoDetalle, String> totalColum = new TableColumn<>("Total");
		totalColum.setMinWidth(10);
		totalColum.setPrefWidth(90);
		totalColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<IngresoDetalle, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<IngresoDetalle, String> param) {
				return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCantidad()*param.getValue().getPrecio()));
			}
		});

		accion = 2;
		tvDatos.getColumns().addAll(descripcionColum, cantidadColum, precioColum, totalColum);
		tvDatos.setItems(datos);

		sumarDatos();
	}

	boolean validarProveedorExiste() {
		try {
			List<Proveedor> listaProveedores;
			listaProveedores = proveedorDAO.getRecuperaProveedor(txtRuc.getText());
			if(listaProveedores.size() != 0)
				return true;
			else
				return false;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
	//recupera datos del proveedor
	public void recuperarDatos(String ruc){
		try{
			List<Proveedor> listaProveedor = new ArrayList<Proveedor>();
			listaProveedor = proveedorDAO.getRecuperaProveedor(ruc);
			for(int i = 0 ; i < listaProveedor.size() ; i ++) {
				txtCodigoProv.setText(Integer.toString(listaProveedor.get(i).getIdProveedor()));
				txtProveedor.setText(listaProveedor.get(i).getNombreComercial());
				txtRuc.setText(listaProveedor.get(i).getRuc());
				txtNombresPro.setText(listaProveedor.get(i).getNombres());
				txtApellidosPro.setText(listaProveedor.get(i).getApellidos());
				txtDireccionPro.setText(listaProveedor.get(i).getDireccion());
				txtTelefonoPro.setText(listaProveedor.get(i).getTelefono());
				
				proveedorSeleccionado = listaProveedor.get(i);
			}
			if (listaProveedor.size() == 0)
				proveedorSeleccionado = new Proveedor();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void anadir() {
		try {
			if(validarAñadirRubro() == false)
				return;
			ObservableList<IngresoDetalle> datos = tvDatos.getItems();
			tvDatos.getColumns().clear();
			IngresoDetalle datoAnadir = new IngresoDetalle();
			rubroSeleccionado.setPrecio(Double.parseDouble(txtPrecioMat.getText()));
			datoAnadir.setRubro(rubroSeleccionado);
			datoAnadir.setCantidad(Integer.parseInt(txtCantidadMat.getText()));
			datoAnadir.setPrecio(Double.parseDouble(txtPrecioMat.getText()));
			datoAnadir.setTotal((Integer.parseInt(txtCantidadMat.getText()) * Double.parseDouble(txtPrecioMat.getText())));
			datos.add(datoAnadir);

			//llenar los datos en la tabla			
			TableColumn<IngresoDetalle, String> descipcionColum = new TableColumn<>("Descripción");
			descipcionColum.setMinWidth(10);
			descipcionColum.setPrefWidth(200);
			descipcionColum.setCellValueFactory(new PropertyValueFactory<IngresoDetalle, String>("rubro"));

			TableColumn<IngresoDetalle, String> cantidadColum = new TableColumn<>("Cantidad");
			cantidadColum.setMinWidth(10);
			cantidadColum.setPrefWidth(90);
			cantidadColum.setCellValueFactory(new PropertyValueFactory<IngresoDetalle, String>("cantidad"));

			TableColumn<IngresoDetalle, String> precioColum = new TableColumn<>("Precio");
			precioColum.setMinWidth(10);
			precioColum.setPrefWidth(90);
			precioColum.setCellValueFactory(new PropertyValueFactory<IngresoDetalle, String>("precio"));

			TableColumn<IngresoDetalle, String> totalColum = new TableColumn<>("Total");
			totalColum.setMinWidth(10);
			totalColum.setPrefWidth(90);
			totalColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<IngresoDetalle, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<IngresoDetalle, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getCantidad()*param.getValue().getPrecio()));
				}
			});

			accion = 1;
			int_bandera = 0;
			tvDatos.getColumns().addAll(descipcionColum, cantidadColum, precioColum, totalColum);
			tvDatos.setItems(datos);

			sumarDatos();

			rubroSeleccionado = null;
			limpiar();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	boolean validarAñadirRubro() {
		try {
			if(txtCantidadMat.getText().equals("")) {
				helper.mostrarAlertaError("Ingresar Cantidad", Context.getInstance().getStage());
				//helper.mostrarAlertaAdvertencia("Ingresar Cantidad", Context.getInstance().getStage());
				txtCantidadMat.requestFocus();
				return false;
			}
			return true;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		} 
	}

	public void eliminar() {
		try {
			IngresoDetalle detalleSeleccionado = tvDatos.getSelectionModel().getSelectedItem();
			tvDatos.getItems().remove(detalleSeleccionado);
			sumarDatos();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void sumarDatos() {
		try {
			if (tvDatos.getItems().isEmpty()) {
				txtSubtotal.setText("0.0");
				txtDescuento.setText("0.0");
				txtTotal.setText("0.0");
			}else {
				double subtotal = 0;
				for(int i=0; i<tvDatos.getItems().size(); i++) {
					//DecimalFormat df = new DecimalFormat ("########.00");
					Double valorSubt = new Double(tvDatos.getItems().get(i).getCantidad()*tvDatos.getItems().get(i).getPrecio());
					subtotal += valorSubt;
					txtSubtotal.setText(String.valueOf(Double.valueOf(subtotal)));
					if (txtDescuento.getText().isEmpty()) {
						txtDescuento.setText("0.0");
					}
					double total = Double.valueOf(txtSubtotal.getText()) - Double.valueOf(txtDescuento.getText());
					txtTotal.setText(String.valueOf(Double.valueOf(total)));
				}
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	//aqui voy a intentar modificar el grabar
	public void grabar() {
	try {
			if(validarDatos() == false)
				return;
			String estado = "A";
			Date date = Date.from(dtpFecha.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
			//para guardar ingreso	
			if(ingreso == null) {
				ingreso = new Ingreso();
				
				proveedorSeleccionado.setNombreComercial(txtProveedor.getText());
				proveedorSeleccionado.setNombres(txtNombresPro.getText());
				proveedorSeleccionado.setApellidos(txtApellidosPro.getText());
				proveedorSeleccionado.setDireccion(txtDireccionPro.getText());
				proveedorSeleccionado.setRuc(txtRuc.getText());
				proveedorSeleccionado.setTelefono(txtTelefonoPro.getText());
				proveedorSeleccionado.setUsuarioCrea(Context.getInstance().getUsuariosC().getIdUsuario());
				proveedorSeleccionado.setFechaCrea(date);
				proveedorSeleccionado.setUsuarioModifica(Context.getInstance().getUsuariosC().getIdUsuario());
				proveedorSeleccionado.setFechaModificacion(date);
				proveedorSeleccionado.setEstado("A");
				
				ingreso.setProveedor(proveedorSeleccionado);
			}else {
				System.out.println(ingreso.getProveedor().getIdProveedor() + " Id Proveedor");
				ingreso.getProveedor().setNombreComercial(txtProveedor.getText());
				ingreso.getProveedor().setNombres(txtNombresPro.getText());
				ingreso.getProveedor().setApellidos(txtApellidosPro.getText());
				ingreso.getProveedor().setDireccion(txtDireccionPro.getText());
				ingreso.getProveedor().setRuc(txtRuc.getText());
				ingreso.getProveedor().setTelefono(txtTelefonoPro.getText());
				ingreso.getProveedor().setUsuarioCrea(Context.getInstance().getUsuariosC().getIdUsuario());
				ingreso.getProveedor().setFechaCrea(date);
				ingreso.getProveedor().setUsuarioModifica(Context.getInstance().getUsuariosC().getIdUsuario());
				ingreso.getProveedor().setFechaModificacion(date);
				ingreso.getProveedor().setEstado("A");
			}


			//ingreso.setIdIngreso(null);
			ingreso.setFecha(date);
			ingreso.setNumeroIngreso(txtNumero.getText());
			ingreso.setUsuarioCrea(Context.getInstance().getUsuariosC().getIdUsuario());
			ingreso.setSubtotal(Double.parseDouble(txtSubtotal.getText()));
			ingreso.setTotal(Double.parseDouble(txtTotal.getText()));
			ingreso.setEstado(estado);


			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				if (ingreso.getIdIngreso() == null) {
					List<IngresoDetalle> listaAgregadaRubros = new ArrayList<IngresoDetalle>();
					List<Kardex> listaProductos = new ArrayList<Kardex>();
					for(IngresoDetalle det : tvDatos.getItems()) {
						det.setIdIngresoDet(null);
						det.setEstado("A");
						det.setIngreso(ingreso);

						//para lo del kardex
						Kardex kardex = new Kardex();
						//kardex.setIdKardex(null);
						kardex.setRubro(det.getRubro());
						kardex.setFecha(date);
						kardex.setTipoDocumento("Factura #");
						kardex.setNumDocumento(txtNumero.getText());
						kardex.setDetalleOperacion(null);
						kardex.setCantidad(det.getCantidad());
						kardex.setUnidadMedida("Unidad");
						kardex.setValorUnitario(det.getPrecio());
						kardex.setCostoTotal(det.getCantidad()*det.getPrecio());
						kardex.setTipoMovimiento("ING");
						kardex.setEstado("A");								
						listaProductos.add(kardex);
						listaAgregadaRubros.add(det);
					}
					ingreso.setIngresoDetalles(listaAgregadaRubros);
					//empieza la transaccion
					ingresoDao.getEntityManager().getTransaction().begin();

					//aqui voy a intentar guardar y tengo q preguntar si es nuevo
					//o sino solo para editar
					if(txtCodigo.getText().equals("0")) {//inserta nuevo ingreso
						ingreso.setIdIngreso(null);
						ingresoDao.getEntityManager().persist(ingreso);
						for (Kardex kar : listaProductos) {
							ingresoDao.getEntityManager().persist(kar);	
						}
					}else {//modifica
						ingreso.setIdIngreso(Integer.parseInt(txtCodigo.getText()));
						ingresoDao.getEntityManager().merge(ingreso);
						for (Kardex kar : listaProductos) {
							ingresoDao.getEntityManager().merge(kar);	
						}
					}

					if (txtCodigoProv.getText().equals("0")) {// inserta nuevo proveedor
						proveedorSeleccionado.setIdProveedor(null);
						ingresoDao.getEntityManager().persist(proveedorSeleccionado);
					}
					//ingresoDao.getEntityManager().persist(ingreso);				
					ingresoDao.getEntityManager().getTransaction().commit();

					actualizarListaRubros();

					helper.mostrarAlertaInformacion("Datos Grabados Correctamente", Context.getInstance().getStage());
					limpiar();
					limpiarProveedor();
					txtNumero.setText("");
					tvDatos.getColumns().clear();
					tvDatos.getItems().clear();
				}else {
					List<Integer> integer = new ArrayList<Integer>();
					for (IngresoDetalle detalle : tvDatos.getItems()) {
						if (detalle.getIdIngresoDet() != null)
							integer.add(detalle.getIdIngresoDet());
					}
					for(IngresoDetalle det : tvDatos.getItems()) {
						if(det.getIdIngresoDet() == null) {
							det.setIdIngresoDet(null);
							det.setEstado("A");
							det.setIngreso(ingreso);
							ingreso.getIngresoDetalles().add(det);
						}else {
							for (IngresoDetalle deta: ingreso.getIngresoDetalles()) {
								if (!integer.contains(deta.getIdIngresoDet())) {
									deta.setEstado("I");
								}
							}
						}
					}
					
					
					//elimina material resta stock
					if(tvDatos != null) {
						List<Integer> idActual = new ArrayList<Integer>();
						for(IngresoDetalle detalle : tvDatos.getItems()) {
							if(detalle.getIdIngresoDet() != null)
								idActual.add(detalle.getIdIngresoDet());
						}
						
						System.out.println("Ingresos actuales " + idActual.size());
						System.out.println("Ingresos en la bd " + ingreso.getIngresoDetalles().size());
						
						for (IngresoDetalle det : ingreso.getIngresoDetalles()) {
							if(det.getIdIngresoDet() != null) {
								if(!idActual.contains(det.getIdIngresoDet())) {
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
						for(IngresoDetalle detalle: tvDatos.getItems()) {
							if(detalle.getIdIngresoDet() == null)
								listaAgregadaRubros.add(detalle.getRubro());
						}
						for (Rubro rubro : listaAgregadaRubros) {
							for(IngresoDetalle detalle : tvDatos.getItems()) {
								if(rubro.getIdRubro() == detalle.getRubro().getIdRubro())
									rubro.setStock(rubro.getStock() + detalle.getCantidad());
							}
							rubroDAO.getEntityManager().getTransaction().begin();
							rubroDAO.getEntityManager().merge(rubro);
							rubroDAO.getEntityManager().getTransaction().commit();
						}
					}
					
					ingresoDao.getEntityManager().getTransaction().begin();
					ingresoDao.getEntityManager().merge(ingreso);
					ingresoDao.getEntityManager().getTransaction().commit();
					
					helper.mostrarAlertaInformacion("Datos grabados Correctamente", Context.getInstance().getStage());
					limpiar();
					limpiarProveedor();
					txtNumero.setText("");
					tvDatos.getColumns().clear();
					tvDatos.getItems().clear();
				}

			}
			ingreso = null;
			proveedorSeleccionado = null;
		}catch(Exception ex) {
			ingresoDao.getEntityManager().getTransaction().rollback();
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
			System.out.println(ex.getMessage());
		}
	}


	//graba solo lo primero los ingresos y el detalle
	/*public void grabar() {
		try {
			if(validarDatos() == false)
				return;
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				Ingreso ingreso = new Ingreso();
				String estado = "A";		
				ingreso.setIdIngreso(null);
				Date date = Date.from(dtpFecha.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
				Timestamp fecha = new Timestamp(date.getTime());
				ingreso.setFecha(fecha);
				ingreso.setNumeroIngreso(txtNumero.getText());
				ingreso.setUsuarioCrea(Context.getInstance().getUsuariosC().getIdUsuario());
				ingreso.setEstado(estado);
				List<IngresoDetalle> listaAgregadaRubros = new ArrayList<IngresoDetalle>();
				for(IngresoDetalle det : tvDatos.getItems()) {
					det.setIdIngresoDet(null);
					det.setEstado("A");
					det.setIngreso(ingreso);
					listaAgregadaRubros.add(det);
				}

				ingreso.setIngresoDetalles(listaAgregadaRubros);
				ingresoDao.getEntityManager().getTransaction().begin();
				ingresoDao.getEntityManager().persist(ingreso);				
				ingresoDao.getEntityManager().getTransaction().commit();

				actualizarListaRubros();

				helper.mostrarAlertaInformacion("Datos Grabados Correctamente", Context.getInstance().getStage());
				limpiar();
				txtNumero.setText("");
				tvDatos.getColumns().clear();
				tvDatos.getItems().clear();
			}
		}catch(Exception ex) {
			ingresoDao.getEntityManager().getTransaction().rollback();
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
			System.out.println(ex.getMessage());
		}
	}*/

	//Intento grabar todo solo graba hasta kardex
	/*public void grabar() {
		try {
			if(validarDatos() == false)
				return;
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				if (idfactura == 0) {
					if(buscaFactura() == true) {
						helper.mostrarAlertaAdvertencia("Número de factura ya existe", Context.getInstance().getStage());
						return;
					}
				}			
				//funcion grabar informacion
				if (grabarFacIngreso() == false) {
					helper.mostrarAlertaError("Error al guardar datos", Context.getInstance().getStage());
				}else {
					helper.mostrarAlertaInformacion("Datos Grabados Correctamente", Context.getInstance().getStage());
				}
			}
			
		}catch(Exception ex) {
			//ingresoDao.getEntityManager().getTransaction().rollback();
			//helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
			System.out.println(ex.getMessage());
		}
	}
	
	boolean grabarFacIngreso() {
		try {
			Date date = Date.from(dtpFecha.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
			Timestamp fecha = new Timestamp(date.getTime());
			String estado = "A";
			Ingreso ingreso = new Ingreso();
			Proveedor proveedor = new Proveedor();
			//inicia transaccion
					
			proveedor.setNombreComercial(txtProveedor.getText());
			proveedor.setNombres(txtNombresPro.getText());
			proveedor.setApellidos(txtApellidosPro.getText());
			proveedor.setDireccion(txtDireccionPro.getText());
			proveedor.setRuc(txtRuc.getText());
			proveedor.setTelefono(txtTelefonoPro.getText());
			proveedor.setUsuarioCrea(Context.getInstance().getUsuariosC().getIdUsuario());
			proveedor.setFechaCrea(fecha);
			proveedor.setUsuarioModifica(Context.getInstance().getUsuariosC().getIdUsuario());
			proveedor.setFechaModificacion(fecha);
			proveedor.setEstado("A");
			
			//proveedorSeleccionado.setIdProveedor(Integer.parseInt(txtCodigoProv.getText()));
			ingreso.setProveedor(proveedor);
			ingreso.setNumeroIngreso(txtNumero.getText());
			ingreso.setUsuarioCrea(Context.getInstance().getUsuariosC().getIdUsuario());
			ingreso.setFecha(fecha);
			ingreso.setSubtotal(Double.parseDouble(txtSubtotal.getText()));
			ingreso.setTotal(Double.parseDouble(txtTotal.getText()));
			ingreso.setEstado(estado);
			
			List<IngresoDetalle> listaAgregadaRubros = new ArrayList<IngresoDetalle>();
			for(IngresoDetalle det : tvDatos.getItems()) {
				det.setIdIngresoDet(null);
				det.setEstado("A");
				det.setIngreso(ingreso);
				listaAgregadaRubros.add(det);
			}
			
			ingreso.setIngresoDetalles(listaAgregadaRubros);
			ingresoDao.getEntityManager().getTransaction().begin();
			
			if(txtCodigoProv.getText().equals("0")) {//inserta
				proveedor.setIdProveedor(null);
				ingresoDao.getEntityManager().persist(proveedor);
			}else {//modifica
				proveedor.setIdProveedor(Integer.parseInt(txtCodigoProv.getText()));
				ingresoDao.getEntityManager().merge(proveedor);
				Ingreso ingreso = new Ingreso();
				List<Kardex> listaProductos = new ArrayList<Kardex>();

				String estado = "A";		
				ingreso.setIdIngreso(null);
				Date date = Date.from(dtpFecha.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
				Timestamp fecha = new Timestamp(date.getTime());
				ingreso.setFecha(fecha);
				ingreso.setProveedor(proveedorSeleccionado);
				ingreso.setSubtotal(Double.parseDouble(txtSubtotal.getText()));
				ingreso.setTotal(Double.parseDouble(txtTotal.getText()));
				ingreso.setNumeroIngreso(txtNumero.getText());
				ingreso.setUsuarioCrea(Context.getInstance().getUsuariosC().getIdUsuario());
				ingreso.setEstado(estado);

				List<IngresoDetalle> listaAgregadaRubros = new ArrayList<IngresoDetalle>();
				for(IngresoDetalle det : tvDatos.getItems()) {
					det.setIdIngresoDet(null);
					det.setEstado("A");
					det.setIngreso(ingreso);

					Kardex kardex = new Kardex();
					kardex.setIdKardex(null);
					kardex.setRubro(det.getRubro());
					kardex.setFecha(fecha);
					kardex.setTipoDocumento("Factura #");
					kardex.setNumDocumento(txtNumero.getText());
					kardex.setDetalleOperacion(null);
					kardex.setCantidad(det.getCantidad());
					kardex.setUnidadMedida("Unidad");
					kardex.setValorUnitario(det.getPrecio());
					kardex.setCostoTotal(det.getCantidad()*det.getPrecio());
					kardex.setTipoMovimiento("ING");
					kardex.setEstado("A");

					listaProductos.add(kardex);
					listaAgregadaRubros.add(det);
				}

				System.out.println("hola");
				proveedorSeleccionado.setNombreComercial(txtProveedor.getText());
				proveedorSeleccionado.setNombres(txtNombresPro.getText());
				proveedorSeleccionado.setApellidos(txtApellidosPro.getText());
				proveedorSeleccionado.setDireccion(txtDireccionPro.getText());
				proveedorSeleccionado.setRuc(txtRuc.getText());
				proveedorSeleccionado.setTelefono(txtTelefonoPro.getText());
				proveedorSeleccionado.setUsuarioCrea(Context.getInstance().getUsuariosC().getIdUsuario());
				proveedorSeleccionado.setFechaCrea(fecha);
				proveedorSeleccionado.setUsuarioModifica(Context.getInstance().getUsuariosC().getIdUsuario());
				proveedorSeleccionado.setFechaModificacion(fecha);
				proveedorSeleccionado.setEstado("A");

				if(proveedorSeleccionado.getIdProveedor() != null) {
					ingreso.setProveedor(proveedorSeleccionado);
				}else {
					proveedorSeleccionado.setIdProveedor(null);
					List<Ingreso> lista = new ArrayList<Ingreso>();
					lista.add(ingreso);
					proveedorSeleccionado.setIngresos(lista);
					ingreso.setProveedor(proveedorSeleccionado);

				}

				ingreso.setIngresoDetalles(listaAgregadaRubros);
				ingresoDao.getEntityManager().getTransaction().begin();
				ingresoDao.getEntityManager().persist(proveedorSeleccionado);		

				for (Kardex kar : listaProductos) {
					ingresoDao.getEntityManager().persist(kar);	
				}

				ingresoDao.getEntityManager().getTransaction().commit();

				actualizarListaRubros();

				helper.mostrarAlertaInformacion("Datos Grabados Correctamente", Context.getInstance().getStage());
				limpiar();
				txtNumero.setText("");
				tvDatos.getColumns().clear();
				tvDatos.getItems().clear();
			}
			
			if(txtCodigo.getText().equals("0")) {//inserta
				ingreso.setIdIngreso(null);
				ingresoDao.getEntityManager().persist(ingreso);
			}else {//modifica
				ingreso.setIdIngreso(Integer.parseInt(txtCodigo.getText()));
				ingresoDao.getEntityManager().merge(ingreso);
			}
			ingresoDao.getEntityManager().getTransaction().commit();
			helper.mostrarAlertaInformacion("Datos Grabados Correctamente", Context.getInstance().getStage());
			return true;		
		}catch(Exception ex) {
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
			ingresoDao.getEntityManager().getTransaction().rollback();
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
	boolean buscaFactura() {
		try {
			List<Ingreso> listaIngresos;
			listaIngresos = ingresoDao.getBuscaFactura(txtNumero.getText(), Integer.parseInt(txtCodigoProv.getText()));
			if(listaIngresos.size() != 0)
				return true;
			else
				return false;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}*/

	boolean validarDatos() {
		try {
			if(txtRuc.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar RUC del Proveedor", Context.getInstance().getStage());
				txtRuc.requestFocus();
				return false;
			}

			if(txtProveedor.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar Nombre Comercial del Proveedor", Context.getInstance().getStage());
				txtProveedor.requestFocus();
				return false;
			}

			if(txtNombresPro.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar Nombres del Proveedor", Context.getInstance().getStage());
				txtNombresPro.requestFocus();
				return false;
			}

			if(txtApellidosPro.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar Apellidos del Proveedor", Context.getInstance().getStage());
				txtApellidosPro.requestFocus();
				return false;
			}

			if(txtDireccionPro.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar Dirección del Proveedor", Context.getInstance().getStage());
				txtDireccionPro.requestFocus();
				return false;
			}

			if(txtTelefonoPro.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar Teléfono del Proveedor", Context.getInstance().getStage());
				txtTelefonoPro.requestFocus();
				return false;
			}

			if(txtNumero.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Ingresar Nº Factura de Compra", Context.getInstance().getStage());
				txtNumero.requestFocus();
				return false;
			}		

			if(dtpFecha.getValue().equals(null)) {
				helper.mostrarAlertaAdvertencia("Ingresar Fecha", Context.getInstance().getStage());
				dtpFecha.requestFocus();
				return false;
			}

			if(tvDatos.getItems().isEmpty()) {
				helper.mostrarAlertaAdvertencia("Ingresar Rubros", Context.getInstance().getStage());
				tvDatos.requestFocus();
				return false;
			}
			return true;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}

	//funcion previa para guardar
	public void actualizarListaRubros() {
		if(tvDatos != null) {		
			List<Rubro> listaAgregadaRubros = new ArrayList<Rubro>();
			for(IngresoDetalle detalle: tvDatos.getItems()) {
				listaAgregadaRubros.add(detalle.getRubro());
			}
			rubroDAO.getEntityManager().getTransaction().begin();
			for (Rubro rubro : listaAgregadaRubros) {
				for(IngresoDetalle detalle : tvDatos.getItems()) {
					if(rubro.getIdRubro() == detalle.getRubro().getIdRubro())
						rubro.setStock(rubro.getStock() + detalle.getCantidad());
				}
				rubroDAO.getEntityManager().merge(rubro);
			}
			rubroDAO.getEntityManager().getTransaction().commit();
		}
	}

	public void nuevo() {
		limpiar();
		limpiarProveedor();
		txtCodigo.setText("0");
		txtNumero.setText("");
		txtSubtotal.setText("");
		txtDescuento.setText("");
		txtTotal.setText("");
		tvDatos.getColumns().clear();
		tvDatos.getItems().clear();
		limpiarIngreso();
		ingreso = null;
		proveedorSeleccionado = null;
		rubroSeleccionado = null;
	}	

	public void buscarRubro() {
		try{
			helper.abrirPantallaModal("/bodega/ListadoRubros.fxml","Listado de Rubros", Context.getInstance().getStage());
			if (Context.getInstance().getRubros() != null) {
				rubroSeleccionado = Context.getInstance().getRubros();
				llenarDatos(rubroSeleccionado);
				Context.getInstance().setRubros(null);
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}

	void llenarDatos(Rubro datoSeleccionado){
		try {
			txtCodigoMat.setText(String.valueOf(datoSeleccionado.getIdRubro()));
			if(datoSeleccionado.getDescripcion() == null)
				txtDescripcionMat.setText("");
			else
				txtDescripcionMat.setText(datoSeleccionado.getDescripcion());

			if(datoSeleccionado.getStock() == null)
				txtStockMat.setText("");
			else
				txtStockMat.setText(String.valueOf(datoSeleccionado.getStock()));

			txtPrecioMat.setText(String.valueOf(datoSeleccionado.getPrecio()));
			txtCantidadMat.requestFocus();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	void limpiar() {
		txtCodigoMat.setText("");
		txtDescripcionMat.setText("");
		txtCantidadMat.setText("");
		txtPrecioMat.setText("");
		txtStockMat.setText("");
		//proveedorSeleccionado = null;	
	}

	void limpiarProveedor() {
		txtCodigoProv.setText("0");
		txtProveedor.setText("");
		txtRuc.setText("");
		txtNombresPro.setText("");
		txtApellidosPro.setText("");
		txtDireccionPro.setText("");
		txtTelefonoPro.setText("");
		//proveedorSeleccionado = null;
	}

	void limpiarIngreso() {
		txtCodigo.setText("0");
		dtpFecha.setAccessibleText(null);
		txtNumero.setText("");
		txtSubtotal.setText("");
		txtDescuento.setText("");
		txtTotal.setText("");
		tvDatos.getColumns().clear();
		tvDatos.getItems().clear();
	}

	public boolean validarRucPersonaNatural(String numero) {
		try {
			validarInicial(numero, 13);
			validarCodigoProvincia(numero.substring(0, 2));
			validarTercerDigito(String.valueOf(numero.charAt(2)), Constantes.getTipoRucNatural());
			validarCodigoEstablecimiento(numero.substring(10, 13));
			validarCedula(numero.substring(0, 9));
		} catch (Exception ex) {
			limpiarProveedor();
			System.out.println(ex.getMessage());
			return false; 
		}

		return true;
	}

	protected boolean validarInicial(String numero, int caracteres){   
		if(txtRuc.getText().equals("")) {
			helper.mostrarAlertaAdvertencia("Ingresar RUC", Context.getInstance().getStage());
			limpiarProveedor();
			txtRuc.requestFocus();
			return false;
		}

		if (numero.length() != caracteres) {
			helper.mostrarAlertaAdvertencia("Valor debe tener " + caracteres + " caracteres", Context.getInstance().getStage());
			limpiarProveedor();
			txtRuc.setText("");
			txtRuc.requestFocus();
			return false;
		}

		return true;
	}

	protected boolean validarCodigoProvincia(String numero){
		if (Integer.parseInt(numero) < 0 || Integer.parseInt(numero) > 24) {
			helper.mostrarAlertaAdvertencia("RUC Inválido", Context.getInstance().getStage());
			limpiarProveedor();
			txtRuc.setText("");
			txtRuc.requestFocus();
			return false;
		}
		return true;
	}

	protected boolean validarCodigoEstablecimiento(String numero){
		if (Integer.parseInt(numero) < 1) {
			helper.mostrarAlertaAdvertencia("RUC Inválido", Context.getInstance().getStage());
			limpiarProveedor();
			txtRuc.setText("");
			txtRuc.requestFocus();
			return false;
		}
		return true;
	}

	boolean validarCedula(String ruc) {
		int total = 0;  
		int tamanoLongitudCedula = 13;  
		int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};  
		int numeroProviancias = 24;  
		int tercerdigito = 6;  
		if (ruc.matches("[0-9]*") && ruc.length() == tamanoLongitudCedula) {  
			int provincia = Integer.parseInt(ruc.charAt(0) + "" + ruc.charAt(1));  
			int digitoTres = Integer.parseInt(ruc.charAt(2) + "");  
			if ((provincia > 0 && provincia <= numeroProviancias) && digitoTres < tercerdigito) {  
				int digitoVerificadorRecibido = Integer.parseInt(ruc.charAt(9) + "");  
				for (int i = 0; i < coeficientes.length; i++) {  
					int valor = Integer.parseInt(coeficientes[i] + "") * Integer.parseInt(ruc.charAt(i) + "");  
					total = valor >= 10 ? total + (valor - 9) : total + valor;  
				}  
				int digitoVerificadorObtenido = total >= 10 ? (total % 10) != 0 ? 10 - (total % 10) : (total % 10) : total;  
				if (digitoVerificadorObtenido == digitoVerificadorRecibido) {  
					return true;  
				}  
			}
			return false;
		}
		return false;		  
	}

	protected boolean validarTercerDigito(String numero, Integer tipo){
		switch (tipo) {
		case 1:
			if (Integer.parseInt(numero) < 0 || Integer.parseInt(numero) > 5) {
				helper.mostrarAlertaAdvertencia("RUC Inválido", Context.getInstance().getStage());
			}
			break;
		case 2:
			if (Integer.parseInt(numero) != 9) {
				helper.mostrarAlertaAdvertencia("RUC Inválido", Context.getInstance().getStage());
			}
			break;

		case 3:
			if (Integer.parseInt(numero) != 6) {
				helper.mostrarAlertaAdvertencia("RUC Inválido", Context.getInstance().getStage());
			}
			break;
		default:
			helper.mostrarAlertaAdvertencia("RUC Inválido", Context.getInstance().getStage());
		}
		return true;
	}
}