package ec.com.jaapz.controlador;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import ec.com.jaapz.modelo.EstadoMedidor;
import ec.com.jaapz.modelo.EstadoMedidorDAO;
import ec.com.jaapz.modelo.Ingreso;
import ec.com.jaapz.modelo.IngresoDAO;
import ec.com.jaapz.modelo.IngresoDetalle;
import ec.com.jaapz.modelo.Kardex;
import ec.com.jaapz.modelo.KardexDAO;
import ec.com.jaapz.modelo.Medidor;
import ec.com.jaapz.modelo.MedidorDAO;
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
import javafx.scene.control.TableRow;
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
	MedidorDAO medidorDAO = new MedidorDAO();
	ProveedorDAO proveedorDAO = new ProveedorDAO();
	EstadoMedidorDAO estadoMedidorDAO = new EstadoMedidorDAO();
	KardexDAO kardexDAO = new KardexDAO();
	Ingreso ingreso;

	public void initialize(){
		try {
			btnAñadir.setStyle("-fx-cursor: hand;");
			btnBuscarRubro.setStyle("-fx-cursor: hand;");
			btnEliminar.setStyle("-fx-cursor: hand;");
			btnGrabar.setStyle("-fx-cursor: hand;");
			btnNuevo.setStyle("-fx-cursor: hand;");
			
			int maxLength = 10;
			nuevo();
			usuarioLogueado = Context.getInstance().getUsuariosC();
			txtUsuario.setText(Encriptado.Desencriptar(String.valueOf(Context.getInstance().getUsuariosC().getUsuario())));
			txtUsuario.setEditable(false);
			txtDescripcionMat.setEditable(false);
			txtStockMat.setEditable(false);
			dtpFecha.setValue(LocalDate.now());
			txtRuc.requestFocus();
			//txtCodigo.setVisible(false);
			//txtCodigoProv.setVisible(false);

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

			//validar solo numeros
			txtCantidadMat.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, 
						String newValue) {
					if (newValue.matches("\\d*")) {
						//int value = Integer.parseInt(newValue);
					} else {
						txtCantidadMat.setText(oldValue);
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
							}else {
								recuperarDatos(txtRuc.getText());
								txtNumero.requestFocus();
							}
						}
					}
				}
			});

			//numeros con decimales
			txtPrecioMat.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (!newValue.matches("\\d*(\\.\\d*)?")) {
						txtPrecioMat.setText(oldValue);
					}
				}
			});

			//numeros con decimales
			txtSubtotal.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (!newValue.matches("\\d*(\\.\\d*)?")) {
						txtSubtotal.setText(oldValue);
					}
				}
			});

			//numeros con decimales
			txtDescuento.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (!newValue.matches("\\d*(\\.\\d*)?")) {
						txtDescuento.setText(oldValue);
					}
				}
			});

			//numeros con decimales
			txtTotal.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (!newValue.matches("\\d*(\\.\\d*)?")) {
						txtTotal.setText(oldValue);
					}
				}
			});

			//recuperar factura
			txtNumero.setOnKeyPressed(new EventHandler<KeyEvent>(){
				@Override
				public void handle(KeyEvent ke){
					if (ke.getCode().equals(KeyCode.ENTER)){
						if (validarIngresoExiste() == true) 
							recuperarIngreso(txtNumero.getText());
						else {
							limpiar();
							limpiarProveedor();
							limpiarIngreso();
						}
							
					}
				}
			});

			//solo letras mayusculas
			txtProveedor.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					// TODO Auto-generated method stub
					String cadena = txtProveedor.getText().toUpperCase();
					txtProveedor.setText(cadena);
				}
			});
			
			//solo letras mayusculas
			txtObservaciones.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					// TODO Auto-generated method stub
					String cadena = txtObservaciones.getText().toUpperCase();
					txtObservaciones.setText(cadena);
				}
			});

			//solo letras mayusculas
			txtNombresPro.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					// TODO Auto-generated method stub
					String cadena = txtNombresPro.getText().toUpperCase();
					txtNombresPro.setText(cadena);
				}
			});

			//validar solo letras.... igual se va con puntuaciones
			txtNombresPro.textProperty().addListener(new ChangeListener<String>() {
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (!newValue.matches("\\D*")) {
						txtNombresPro.setText(newValue.replaceAll("[^\\D]", ""));
					}
				}
			});

			//solo letras mayusculas
			txtApellidosPro.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					// TODO Auto-generated method stub
					String cadena = txtApellidosPro.getText().toUpperCase();
					txtApellidosPro.setText(cadena);
				}
			});

			//validar solo letras.... igual se va con puntuaciones
			txtApellidosPro.textProperty().addListener(new ChangeListener<String>() {
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if (!newValue.matches("\\D*")) {
						txtApellidosPro.setText(newValue.replaceAll("[^\\D]", ""));
					}
				}
			});

			//solo letras mayusculas
			txtDireccionPro.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					// TODO Auto-generated method stub
					String cadena = txtDireccionPro.getText().toUpperCase();
					txtDireccionPro.setText(cadena);
				}
			});

			//solo letras mayusculas
			txtNumero.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					// TODO Auto-generated method stub
					String cadena = txtNumero.getText().toUpperCase();
					txtNumero.setText(cadena);
				}
			});

			//solo letras mayusculas
			txtCodigoMat.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					// TODO Auto-generated method stub
					String cadena = txtCodigoMat.getText().toUpperCase();
					txtCodigoMat.setText(cadena);
				}
			});

			//validar solo numeros
			txtTelefonoPro.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, 
						String newValue) {
					if (newValue.matches("\\d*")) {
						//int value = Integer.parseInt(newValue);
					} else {
						txtTelefonoPro.setText(oldValue);
					}
				}
			});

			//validar solo 10 valores
			txtTelefonoPro.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
					if (txtTelefonoPro.getText().length() > maxLength) {
						String s = txtTelefonoPro.getText().substring(0, maxLength);
						txtTelefonoPro.setText(s);
					}
				}
			});

			//recuperar Material
			txtCodigoMat.setOnKeyPressed(new EventHandler<KeyEvent>(){
				@Override
				public void handle(KeyEvent ke){
					if (ke.getCode().equals(KeyCode.ENTER)){
						if (validarProductoExiste() == false) {
							helper.mostrarAlertaAdvertencia("Elemento no existente!", Context.getInstance().getStage());
							txtCodigoMat.requestFocus();
							txtCodigoMat.setText("");
							txtDescripcionMat.setText("");
							txtStockMat.setText("");
							txtPrecioMat.setText("");
						}else {
							recuperarDatosMat(txtCodigoMat.getText());
							txtCantidadMat.requestFocus();
						}
					}
				}
			});
			//para realizar modificaciones en los medidores
			tvDatos.setRowFactory(tv -> {
				TableRow<IngresoDetalle> row = new TableRow<>();
				row.setOnMouseClicked(event -> {
					if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
						if(tvDatos.getSelectionModel().getSelectedItem() != null){
							if(tvDatos.getSelectionModel().getSelectedItem().getRubro().getIdRubro() == Constantes.ID_MEDIDOR) {
								Context.getInstance().setDetalleMedidor(tvDatos.getSelectionModel().getSelectedItem());
								helper.abrirPantallaModal("/bodega/BodegaModificarMedidor.fxml","Listado de Medidores", Context.getInstance().getStage());
								//falta de actualizar la lista cuando se sale de la ventana modal
								recuperarDetalleIngreso();
								Context.getInstance().setDetalleMedidor(null);
							}
						}
					}
				});
				return row ;
			});
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	//recupera datos del material
	public void recuperarDatosMat(String codigo){
		try{
			List<Rubro> listaRubro = new ArrayList<Rubro>();
			listaRubro = rubroDAO.getRecuperaRubro(codigo);
			for(int i = 0 ; i < listaRubro.size() ; i ++) {
				txtCodigoMat.setText(listaRubro.get(i).getCodigo());
				txtDescripcionMat.setText(listaRubro.get(i).getDescripcion());
				txtStockMat.setText(String.valueOf(listaRubro.get(i).getStock()));
				txtPrecioMat.setText(String.valueOf(listaRubro.get(i).getPrecio()));

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
			listaRubros = rubroDAO.getRecuperaRubro(txtCodigoMat.getText());
			if(listaRubros.size() != 0)
				return true;
			else
				return false;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
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
				//txtNumero.setText(listaIngreso.get(i).getNumeroIngreso());
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
		tvDatos.getColumns().addAll(descripcionColum, cantidadColum, precioColum, totalColum);
		tvDatos.setItems(datos);
		//sumarDatos();
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
			if(validarAnadirRubro() == false)
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
			tvDatos.getColumns().addAll(descipcionColum, cantidadColum, precioColum, totalColum);
			tvDatos.setItems(datos);
			//sumarDatos();
			rubroSeleccionado = null;
			limpiar();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	boolean validarAnadirRubro() {
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
			if(tvDatos.getSelectionModel().getSelectedItem() != null){
				if(tvDatos.getSelectionModel().getSelectedItem().getRubro().getIdRubro() == Constantes.ID_MEDIDOR) {
					Context.getInstance().setDetalleMedidor(tvDatos.getSelectionModel().getSelectedItem());
					helper.abrirPantallaModal("/bodega/BodegaModificarMedidor.fxml","Listado de Medidores", Context.getInstance().getStage());
					//falta de actualizar la lista cuando se sale de la ventana modal
					recuperarDetalleIngreso();
				}else {
					IngresoDetalle detalleSeleccionado = tvDatos.getSelectionModel().getSelectedItem();
					tvDatos.getItems().remove(detalleSeleccionado);	
				}
			}
			//sumarDatos();
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
			//para obtener la hora
			java.util.Date utilDate = new java.util.Date();
			long lnMilisegundos = utilDate.getTime();
			java.sql.Time sqlTime = new java.sql.Time(lnMilisegundos);
			if(validarDatos() == false)
				return;
			Date date = Date.from(dtpFecha.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
			//para guardar ingreso
			if(ingreso == null) {//pregunta si el objeto se encuentra en null.. para crear uno nuevo y sera un nuevo ingreso
				ingreso = new Ingreso();
				ingreso.setIdIngreso(null);
				if(proveedorSeleccionado == null) {
					proveedorSeleccionado = new Proveedor();
					proveedorSeleccionado.setIdProveedor(null);
				}
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
				proveedorSeleccionado.setEstado(Constantes.ESTADO_ACTIVO);
			}else {//caso contrario es un ingreso recuperado..
				System.out.println(ingreso.getProveedor().getIdProveedor() + " Id Proveedor");
				ingreso.getProveedor().setNombreComercial(txtProveedor.getText());
				ingreso.getProveedor().setNombres(txtNombresPro.getText());
				ingreso.getProveedor().setApellidos(txtApellidosPro.getText());
				ingreso.getProveedor().setDireccion(txtDireccionPro.getText());
				ingreso.getProveedor().setRuc(txtRuc.getText());
				ingreso.getProveedor().setTelefono(txtTelefonoPro.getText());
				ingreso.getProveedor().setUsuarioCrea(Context.getInstance().getUsuariosC().getIdUsuario());
				ingreso.getProveedor().setFechaCrea(date);
				ingreso.setHora(sqlTime);
				ingreso.getProveedor().setUsuarioModifica(Context.getInstance().getUsuariosC().getIdUsuario());
				ingreso.getProveedor().setFechaModificacion(date);
				ingreso.getProveedor().setEstado(Constantes.ESTADO_ACTIVO);
			}

			//ingreso.setIdIngreso(null);
			ingreso.setFecha(date);
			ingreso.setNumeroIngreso(txtNumero.getText());
			ingreso.setUsuarioCrea(Context.getInstance().getUsuariosC().getIdUsuario());
			ingreso.setHora(sqlTime);
			ingreso.setSubtotal(Double.parseDouble(txtSubtotal.getText()));
			ingreso.setTotal(Double.parseDouble(txtTotal.getText()));
			ingreso.setObservaciones(txtObservaciones.getText());
			ingreso.setEstado(Constantes.ESTADO_ACTIVO);

			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				//es un ingreso nuevoooooo **********************************************************************************************
				if (ingreso.getIdIngreso() == null) {//se realiza un nuevo registro
					List<IngresoDetalle> listaAgregadaRubros = new ArrayList<IngresoDetalle>();
					List<Kardex> listaProductos = new ArrayList<Kardex>();

					for(IngresoDetalle det : tvDatos.getItems()) {
						det.setIdIngresoDet(null);
						det.setEstado(Constantes.ESTADO_ACTIVO);
						det.setIngreso(ingreso);

						//para lo del kardex
						Kardex kardex = new Kardex();
						kardex.setIdKardex(null);
						kardex.setRubro(det.getRubro());
						kardex.setFecha(date);
						kardex.setTipoDocumento("Factura #");
						kardex.setNumDocumento(txtNumero.getText());
						kardex.setDetalleOperacion("Adquisición de " + det.getRubro().getDescripcion());
						kardex.setCantidad(det.getCantidad());
						kardex.setUnidadMedida("Unidad");
						kardex.setValorUnitario(det.getPrecio());
						kardex.setCostoTotal(det.getCantidad()*det.getPrecio());
						kardex.setTipoMovimiento(Constantes.BODEGA_INGRESO);
						kardex.setEstado(Constantes.ESTADO_ACTIVO);		

						listaProductos.add(kardex);
						listaAgregadaRubros.add(det);
					}

					ingreso.setIngresoDetalles(listaAgregadaRubros);
					if(proveedorSeleccionado.getIdProveedor() == null) {
						ingreso.setProveedor(proveedorSeleccionado);
						List<Ingreso> listaIngreso = new ArrayList<Ingreso>();
						listaIngreso.add(ingreso);
						proveedorSeleccionado.setIngresos(listaIngreso);
					}else {
						proveedorSeleccionado.addIngresos(ingreso);
						//ingreso.setProveedor(proveedorSeleccionado);
					}
						
					
					//grabar el ingreso
					ingresoDao.getEntityManager().getTransaction().begin();
					
					if(proveedorSeleccionado.getIdProveedor() == null) 
						ingresoDao.getEntityManager().persist(proveedorSeleccionado);
					else
						ingresoDao.getEntityManager().merge(proveedorSeleccionado);
					
					//ingresoDao.getEntityManager().persist(ingreso);
					for (Kardex kar : listaProductos) ingresoDao.getEntityManager().persist(kar);
					ingresoDao.getEntityManager().getTransaction().commit();
					/*
					ingresoDao.getEntityManager().getTransaction().begin();
					if (txtCodigoProv.getText().equals("0")) { //inserta nuevo proveedor
						proveedorSeleccionado.setIdProveedor(null);
						ingresoDao.getEntityManager().persist(proveedorSeleccionado);
					}else {//la modifica
						proveedorSeleccionado.setIdProveedor(Integer.parseInt(txtCodigoProv.getText()));
						ingresoDao.getEntityManager().merge(proveedorSeleccionado);
					}		
					ingresoDao.getEntityManager().getTransaction().commit();
					*/
					Integer ultimaFactura = 0;
					Ingreso ing = ingresoDao.getUltimoRegistro();
					EstadoMedidor estadoM = estadoMedidorDAO.getEstadoBueno();
					if(ing != null)
						ultimaFactura = ing.getIdIngreso();

					//preguntar si se ingresan medidores y registrarlos en la tbla de medidores
					medidorDAO.getEntityManager().getTransaction().begin();
					for(IngresoDetalle det : tvDatos.getItems()) {
						if(det.getRubro().getIdRubro() == Constantes.ID_MEDIDOR) {//hay registros de medidores
							for(int i = 0 ; i < det.getCantidad() ; i ++) {
								Medidor medidor = new Medidor();
								medidor.setIdMedidor(null);
								medidor.setIdFactura(ultimaFactura);
								medidor.setMarca(det.getRubro().getMarca());
								medidor.setModelo(det.getRubro().getMarca());
								medidor.setEstado(Constantes.ESTADO_ACTIVO);
								medidor.setEstadoMedidor(estadoM);
								medidor.setPrecio(det.getRubro().getPrecio());
								medidor.setUsado(false);
								medidor.setUsuarioCrea(Context.getInstance().getUsuariosC().getIdUsuario());
								medidorDAO.getEntityManager().persist(medidor);
							}
						}
					}
					medidorDAO.getEntityManager().getTransaction().commit();
					actualizarListaRubros();

					helper.mostrarAlertaInformacion("Datos Grabados Correctamente", Context.getInstance().getStage());
					limpiar();
					txtCodigo.setText("0");
					txtNumero.setText("");
					limpiarProveedor();
					txtNumero.setText("");
					txtObservaciones.setText("");
					tvDatos.getColumns().clear();
					tvDatos.getItems().clear();
					
				}else {//es una modificacion ************************************************************************************************
					List<Integer> integer = new ArrayList<Integer>();//un arreglo para guardar los id del detalle q han sido registrado
					for (IngresoDetalle detalle : tvDatos.getItems()) {
						if (detalle.getIdIngresoDet() != null)//guardo los id de los registros ya grabados en la BD
							integer.add(detalle.getIdIngresoDet());
					}
					
					for(IngresoDetalle det : tvDatos.getItems()) {
						if(det.getIdIngresoDet() == null) {//cuando el id es null.. es un nuevo registro de detalle
							det.setIdIngresoDet(null);
							det.setEstado(Constantes.ESTADO_ACTIVO);
							det.setIngreso(ingreso);
							ingreso.getIngresoDetalles().add(det);//y se añade al ingreso
							//se registra en el kardex el ingreso
							//para lo del kardex
							Kardex kardex = new Kardex();
							//kardex.setIdKardex(null);
							kardex.setRubro(det.getRubro());
							kardex.setFecha(date);
							kardex.setTipoDocumento("Factura #");
							kardex.setNumDocumento(txtNumero.getText());
							kardex.setDetalleOperacion("Adquisición de " + det.getRubro().getDescripcion());
							kardex.setCantidad(det.getCantidad());
							kardex.setUnidadMedida("Unidad");
							kardex.setValorUnitario(det.getPrecio());
							kardex.setCostoTotal(det.getCantidad()*det.getPrecio());
							kardex.setTipoMovimiento(Constantes.BODEGA_INGRESO);
							kardex.setEstado(Constantes.ESTADO_ACTIVO);	
							medidorDAO.getEntityManager().getTransaction().begin();
							medidorDAO.getEntityManager().persist(kardex);
							medidorDAO.getEntityManager().getTransaction().commit();	
						}
					}
					//dar de baja a los q se han eliminado
					for (IngresoDetalle deta: ingreso.getIngresoDetalles()) {
						if (!integer.contains(deta.getIdIngresoDet())) { //si el id del detalle recuperado no aparece en la lista.. se le da de baja
							deta.setEstado(Constantes.ESTADO_INACTIVO);
							//tambien se le da de baja el id del kardex del registro
							Kardex kardexEliminar = kardexDAO.getKardexByDocumento(deta.getIngreso().getNumeroIngreso(), deta.getRubro().getIdRubro());
							if(kardexEliminar != null) {
								kardexEliminar.setEstado(Constantes.ESTADO_INACTIVO);
								medidorDAO.getEntityManager().getTransaction().begin();
								medidorDAO.getEntityManager().merge(kardexEliminar);
								medidorDAO.getEntityManager().getTransaction().commit();
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
					txtCodigo.setText("0");
					txtNumero.setText("");
					limpiarProveedor();
					txtNumero.setText("");
					txtObservaciones.setText("");
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
				if(rubro.getIdRubro() != Constantes.ID_MEDIDOR || rubro.getIdRubro() != Constantes.ID_TASA_CONEXION) {
					for(IngresoDetalle detalle : tvDatos.getItems()) {
						if(rubro.getIdRubro() == detalle.getRubro().getIdRubro())
							rubro.setStock(rubro.getStock() + detalle.getCantidad());
					}
					rubroDAO.getEntityManager().merge(rubro);
				}
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
			//txtCodigoMat.setText(String.valueOf(datoSeleccionado.getIdRubro()));
			if(datoSeleccionado.getCodigo() == null)
				txtCodigoMat.setText("");
			else
				txtCodigoMat.setText(datoSeleccionado.getCodigo());

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
		//txtNumero.setText("");
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