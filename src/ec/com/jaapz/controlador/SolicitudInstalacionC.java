package ec.com.jaapz.controlador;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import ec.com.jaapz.modelo.Barrio;
import ec.com.jaapz.modelo.BarrioDAO;
import ec.com.jaapz.modelo.Categoria;
import ec.com.jaapz.modelo.CategoriaDAO;
import ec.com.jaapz.modelo.Cliente;
import ec.com.jaapz.modelo.ClienteDAO;
import ec.com.jaapz.modelo.Genero;
import ec.com.jaapz.modelo.SolInspeccionIn;
import ec.com.jaapz.modelo.SolInspeccionInDAO;
import ec.com.jaapz.modelo.TipoSolicitud;
import ec.com.jaapz.modelo.TipoSolicitudDAO;
import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class SolicitudInstalacionC {
	@FXML private Button btnGrabarIns;
    @FXML private TextField txtDireccionIns;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtDireccion;
    @FXML private TextField cboEstadoIns;
    @FXML private ComboBox<Categoria> cboUsoMedidor;
    @FXML private TextField txtCedula;
    @FXML private Button btnNuevoIns;
    @FXML private ComboBox<Genero> cboGenero;
    @FXML private TextField txtReferenciaIns;
    @FXML private TextField txtTelefono;
    @FXML private Button btnBuscarIns;
    @FXML private DatePicker dtpFechaIns;
    @FXML private TextField txtNombres;
    @FXML private TextField txtEstado;
    @FXML private TextField txtContacto;
    @FXML private ComboBox<Barrio> cboBarrio;
    @FXML private TextField txtCorreo;

    BarrioDAO barrioDAO = new BarrioDAO();
    CategoriaDAO categoriaDAO = new CategoriaDAO();
    ClienteDAO clienteDAO = new ClienteDAO();
    ControllerHelper helper = new ControllerHelper();
    Cliente clienteRecuperado;
    Genero[] genero = Genero.values();
    SolInspeccionInDAO inspeccionDAO = new SolInspeccionInDAO();
    TipoSolicitudDAO tipoSolicitudDAO = new TipoSolicitudDAO();
    Cliente clienteSeleccionado = new  Cliente();
    
    public void initialize() {
    	try {
    		dtpFechaIns.setValue(LocalDate.now());
    		llenarCombos();
    		txtCedula.setOnKeyPressed(new EventHandler<KeyEvent>(){
    			@Override
    			public void handle(KeyEvent ke){
    				if (ke.getCode().equals(KeyCode.ENTER)){
    					if (validarCedula(txtCedula.getText()) == false){
    						helper.mostrarAlertaAdvertencia("El número de cedula es incorrecto!", Context.getInstance().getStage());
    						txtCedula.setText("");
    						txtCedula.requestFocus();
    					}else {
    						recuperarDatos(txtCedula.getText());
    						
    						//dtpFechaIns.setValue(null);
    				    	cboUsoMedidor.getSelectionModel().select(-1);
    				    	txtReferenciaIns.setText("");
    				    	txtDireccionIns.setText("");
    				    	txtContacto.setText("");
    				    	cboBarrio.getSelectionModel().select(-1);
    					}
    				}
    			}
    		});
    		//validar solo numeros
			txtCedula.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, 
						String newValue) {
					if (newValue.matches("\\d*")) {
						//int value = Integer.parseInt(newValue);
					} else {
						txtCedula.setText(oldValue);
					}
				}
			});
			//validar solo 10 valores
			txtCedula.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
					if (txtCedula.getText().length() > 10) {
						String s = txtCedula.getText().substring(0, 10);
						txtCedula.setText(s);
					}
				}
			});
			txtTelefono.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, 
						String newValue) {
					if (newValue.matches("\\d*")) {
						//int value = Integer.parseInt(newValue);
					} else {
						txtTelefono.setText(oldValue);
					}
				}
			});
			txtTelefono.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
					if (txtTelefono.getText().length() > 10) {
						String s = txtTelefono.getText().substring(0, 10);
						txtTelefono.setText(s);
					}
				}
			});
			txtContacto.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, 
						String newValue) {
					if (newValue.matches("\\d*")) {
						//int value = Integer.parseInt(newValue);
					} else {
						txtContacto.setText(oldValue);
					}
				}
			});
			txtContacto.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
					if (txtContacto.getText().length() > 10) {
						String s = txtContacto.getText().substring(0, 10);
						txtContacto.setText(s);
					}
				}
			});
			//solo letras mayusculas
			txtNombres.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					// TODO Auto-generated method stub
					String cadena = txtNombres.getText().toUpperCase();
					txtNombres.setText(cadena);
				}
			});
			txtApellidos.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					// TODO Auto-generated method stub
					String cadena = txtApellidos.getText().toUpperCase();
					txtApellidos.setText(cadena);
				}
			});
			txtDireccion.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					// TODO Auto-generated method stub
					String cadena = txtDireccion.getText().toUpperCase();
					txtDireccion.setText(cadena);
				}
			});
			txtReferenciaIns.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					// TODO Auto-generated method stub
					String cadena = txtReferenciaIns.getText().toUpperCase();
					txtReferenciaIns.setText(cadena);
				}
			});
			txtDireccionIns.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					// TODO Auto-generated method stub
					String cadena = txtDireccionIns.getText().toUpperCase();
					txtDireccionIns.setText(cadena);
				}
			});
    	}catch(Exception ex) {
    		System.out.println(ex.getMessage());
    	}
    }
    private void recuperarDatos(String cedula) {
    	try {
    		List<Cliente> listaCliente = new ArrayList<Cliente>();
    		listaCliente = clienteDAO.getListaClientesCedula(cedula);
    		if(listaCliente.size() > 0) {
    			txtNombres.setText(listaCliente.get(0).getNombre());
    			txtApellidos.setText(listaCliente.get(0).getApellido());
    			txtDireccion.setText(listaCliente.get(0).getDireccion());
    			txtTelefono.setText(listaCliente.get(0).getTelefono());
    			txtCorreo.setText(listaCliente.get(0).getEmail());
    			for(Genero g : genero){
    				if(g.toString().equals(listaCliente.get(0).getGenero()))
    					cboGenero.getSelectionModel().select(g);
    			}
    			clienteRecuperado = listaCliente.get(0);
    		}else {
    			helper.mostrarAlertaInformacion("Cliente no registrado.. debe ser registrado", Context.getInstance().getStage());
    			txtNombres.requestFocus();
    			
    			clienteRecuperado = new Cliente();
    		}
    	}catch(Exception ex) {
    		System.out.println(ex.getMessage());
    	}
    }
    private void llenarCombos() {
    	try {
    		cboUsoMedidor.setPromptText("Seleccione uso");
			List<Categoria> listaCategoria = categoriaDAO.getListaCategorias("");
			ObservableList<Categoria> datos = FXCollections.observableArrayList();
			datos.addAll(listaCategoria);
			cboUsoMedidor.setItems(datos);
			
			
			cboBarrio.setPromptText("Seleccione barrio");
			List<Barrio> listaBarrio = barrioDAO.getListaBarriosActivos();
			ObservableList<Barrio> datosBarrios = FXCollections.observableArrayList();
			datosBarrios.addAll(listaBarrio);
			cboBarrio.setItems(datosBarrios);
			
			
			ObservableList<Genero> listaGenero = FXCollections.observableArrayList(Genero.values());
			cboGenero.setItems(listaGenero);
			cboGenero.setPromptText("Seleccione Genero");
    	}catch(Exception ex) {
    		System.out.println(ex.getMessage());
    	}
    }
    public void grabar() {
    	try {
    		if(txtCedula.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar un cliente", Context.getInstance().getStage());
				return;
			}
    		if(cboGenero.getSelectionModel().getSelectedIndex() == -1) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar género del cliente", Context.getInstance().getStage());
				return;
			}
			if(dtpFechaIns.getValue() == null) {
				helper.mostrarAlertaAdvertencia("Debe registrar fecha de la inspección", Context.getInstance().getStage());
				return;
			}
			if(cboUsoMedidor.getSelectionModel().getSelectedIndex() == -1) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar el uso del medidor", Context.getInstance().getStage());
				return;
			}
			if(txtReferenciaIns.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Es necesario registrar referencia domiciliaria", Context.getInstance().getStage());
				return;
			}
			if(txtContacto.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Es recesario registrar número de contacto", Context.getInstance().getStage());
				return;
			}
			
			if(txtDireccionIns.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Es recesario registrar dirección de inspección", Context.getInstance().getStage());
				return;
			}
			
			if(cboBarrio.getSelectionModel().getSelectedIndex() == -1) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar el barrio del cliente a inspeccionar", Context.getInstance().getStage());
				return;
			}
			if(!txtCorreo.getText().equals("")) {
				if(ControllerHelper.validarEmail(txtCorreo.getText()) == false) {
					helper.mostrarAlertaAdvertencia("Correo electrónico no valido", Context.getInstance().getStage());
					return;	
				}
			}
			if (grabarDatos() == true) {
				helper.mostrarAlertaInformacion("Orden de Inspección Emitida con Exito", Context.getInstance().getStage());
			}
			else
				helper.mostrarAlertaError("Error al Emitir Orden de Inspección", Context.getInstance().getStage());
    	}catch(Exception ex) {
    		System.out.println(ex.getMessage());
    	}
    }
    private boolean grabarDatos() {
		try {
			java.util.Date utilDate = new java.util.Date(); 
			long lnMilisegundos = utilDate.getTime();
			java.sql.Time sqlTime = new java.sql.Time(lnMilisegundos);
			
			boolean bandera = false;
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				cargarDatos();
				
				SolInspeccionIn inspeccion = new SolInspeccionIn();
				inspeccion.setEstado("A");
				
				List<TipoSolicitud> tipoSolicitud = tipoSolicitudDAO.getSolById(1);
				System.out.println(tipoSolicitud.size());
				if(tipoSolicitud.size() > 0)
					inspeccion.setTipoSolicitud(tipoSolicitud.get(0));
				
				inspeccion.setEstadoInspeccion(Constantes.EST_INSPECCION_PENDIENTE);
				Date date = Date.from(dtpFechaIns.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
				Timestamp fecha = new Timestamp(date.getTime());
				inspeccion.setFechaIngreso(fecha);
				
				inspeccion.setUsoMedidor(String.valueOf(cboUsoMedidor.getValue()));
				inspeccion.setIdSolInspeccion(null);
				inspeccion.setUsuarioCrea(Context.getInstance().getIdUsuario());
				inspeccion.setReferencia(txtReferenciaIns.getText());
				inspeccion.setDireccion(txtDireccionIns.getText());
				inspeccion.setBarrio(cboBarrio.getSelectionModel().getSelectedItem());
				inspeccion.setTelefonoContacto(txtContacto.getText());
				inspeccion.setHoraIngreso(sqlTime);
				if(clienteRecuperado.getIdCliente() != null) {
					inspeccion.setCliente(clienteRecuperado);
					clienteRecuperado.addSolInspeccionIn(inspeccion);
				}
				else {
					inspeccion.setCliente(clienteRecuperado);
					List<SolInspeccionIn> lista = new ArrayList<SolInspeccionIn>();
					lista.add(inspeccion);
					clienteRecuperado.setSolInspeccionIns(lista);
				}
				
				
				inspeccionDAO.getEntityManager().getTransaction().begin();
				if(clienteRecuperado.getIdCliente() != null)
					inspeccionDAO.getEntityManager().merge(clienteRecuperado);
				else
					inspeccionDAO.getEntityManager().persist(clienteRecuperado);
				inspeccionDAO.getEntityManager().getTransaction().commit();
				clienteRecuperado = null;
				bandera = true;
				nuevo();
			}
			return bandera;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			inspeccionDAO.getEntityManager().getTransaction().rollback();;
			return false;
		}
	}
    private void cargarDatos() {
    	clienteRecuperado.setApellido(txtApellidos.getText());
		clienteRecuperado.setCedula(txtCedula.getText());
		clienteRecuperado.setDireccion(txtDireccion.getText());
		clienteRecuperado.setEstado("A");
		clienteRecuperado.setGenero(cboGenero.getSelectionModel().getSelectedItem().name());
		clienteRecuperado.setNombre(txtNombres.getText());
		clienteRecuperado.setTelefono(txtTelefono.getText());
		clienteRecuperado.setUsuarioCrea(Context.getInstance().getIdUsuario());
		clienteRecuperado.setEmail(txtCorreo.getText());
    	if(clienteRecuperado.getIdCliente() == null) 
    		clienteRecuperado.setIdCliente(null);
    }
    public void nuevo() {
    	clienteRecuperado = null;
    	txtCedula.setText("");
    	txtNombres.setText("");
    	txtApellidos.setText("");
    	txtDireccion.setText("");
    	cboGenero.getSelectionModel().select(-1);
    	txtTelefono.setText("");
    	dtpFechaIns.setValue(null);
    	cboUsoMedidor.getSelectionModel().select(-1);
    	txtReferenciaIns.setText("");
    	txtDireccion.setText("");
    	txtDireccionIns.setText("");
    	txtContacto.setText("");
    	txtCorreo.setText("");
    	cboBarrio.getSelectionModel().select(-1);
    }

    
    public void buscarIns() {
    	try{
			helper.abrirPantallaModal("/clientes/ClientesListaClientes.fxml","Listado de Clientes", Context.getInstance().getStage());
			if (Context.getInstance().getCliente() != null) {
				Cliente datoSeleccionado = Context.getInstance().getCliente();
				clienteSeleccionado = datoSeleccionado;
				txtCedula.setText(clienteSeleccionado.getCedula());
				recuperarDatos(clienteSeleccionado.getCedula());
				Context.getInstance().setCliente(null);
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
    }
    void llenarDatosIns(Cliente datosSeleccionado) {
		try {
			txtTelefono.setText(datosSeleccionado.getTelefono());
			txtCedula.setText(datosSeleccionado.getCedula());
			txtNombres.setText(datosSeleccionado.getNombre());
			txtApellidos.setText(datosSeleccionado.getApellido());
			if(datosSeleccionado.getEstado().equals("A"))
				txtEstado.setText("Activo");
			else
				txtEstado.setText("Inactivo");
			for(Genero g : genero){
				if(g.toString().equals(datosSeleccionado.getGenero()))
					cboGenero.getSelectionModel().select(g);
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
    boolean validarCedula(String cedula) {
		int total = 0;  
		int tamanoLongitudCedula = 10;  
		int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};  
		int numeroProviancias = 24;  
		int tercerdigito = 6;  
		if (cedula.matches("[0-9]*") && cedula.length() == tamanoLongitudCedula) {  
			int provincia = Integer.parseInt(cedula.charAt(0) + "" + cedula.charAt(1));  
			int digitoTres = Integer.parseInt(cedula.charAt(2) + "");  
			if ((provincia > 0 && provincia <= numeroProviancias) && digitoTres < tercerdigito) {  
				int digitoVerificadorRecibido = Integer.parseInt(cedula.charAt(9) + "");  
				for (int i = 0; i < coeficientes.length; i++) {  
					int valor = Integer.parseInt(coeficientes[i] + "") * Integer.parseInt(cedula.charAt(i) + "");  
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
}
