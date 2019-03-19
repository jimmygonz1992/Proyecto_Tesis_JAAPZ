package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.Optional;

import ec.com.jaapz.modelo.Barrio;
import ec.com.jaapz.modelo.Categoria;
import ec.com.jaapz.modelo.Cliente;
import ec.com.jaapz.modelo.CuentaCliente;
import ec.com.jaapz.modelo.CuentaClienteDAO;
import ec.com.jaapz.modelo.Genero;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class ClientesJuntaC {
	@FXML TextField txtTelefono;
	@FXML TextField txtCedula;
	@FXML TextField txtNombres;
	@FXML TextField txtApellidos;
	@FXML TextField txtIdCliente;
	@FXML ComboBox<Genero> cboGenero;
	@FXML TextField txtDireccionDomicilio;
	@FXML TextField txtCodigoCuenta;
	@FXML TextField txtEmail;
	@FXML TextField txtFechaIngreso;
	@FXML TextField txtIdCategoria;
	@FXML TextField txtDescripcionCategoria;
	@FXML TextField txtValorm3;
	@FXML TextField txtIdBarrio;
	@FXML TextField txtNombreBarrio;
	@FXML TextField txtDescripcionBarrio;
	@FXML TextField txtIdMedidor;
	@FXML TextField txtCodigoMedidor;
	@FXML TextField txtDetallesMedidor;
	@FXML TextField txtLatitud;
	@FXML TextField txtLongitud;
	@FXML Button btnGrabar;
	@FXML Button btnSalir;
	@FXML Button btnBuscarCategoria;
	@FXML Button btnBuscarBarrio;

	ControllerHelper helper = new ControllerHelper();
	CuentaCliente cuentaSeleccionada = new CuentaCliente();
	Barrio barrioSeleccionado = new Barrio();
	Categoria categoriaSeleccionado = new Categoria();
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	Genero[] genero = Genero.values();
	Cliente clienteSeleccionado;
	
	CuentaClienteDAO cuentaDAO = new CuentaClienteDAO();
	public void initialize() {
		btnBuscarBarrio.setStyle("-fx-cursor: hand;");
		btnBuscarCategoria.setStyle("-fx-cursor: hand;");
		btnGrabar.setStyle("-fx-cursor: hand;");
		btnSalir.setStyle("-fx-cursor: hand;");
		cboGenero.setStyle("-fx-cursor: hand;");
		
		
		cuentaSeleccionada = Context.getInstance().getCuentaCliente();
		clienteSeleccionado = cuentaSeleccionada.getCliente();
		Context.getInstance().setCuentaCliente(null);
		llenarCombos();
		recuperarDatos(cuentaSeleccionada);
	}
	private void llenarCombos() {
    	try {
			ObservableList<Genero> listaGenero = FXCollections.observableArrayList(Genero.values());
			cboGenero.setItems(listaGenero);
			cboGenero.setPromptText("Seleccione Genero");
    	}catch(Exception ex) {
    		System.out.println(ex.getMessage());
    	}
    }
	private void recuperarDatos(CuentaCliente cuentaCliente) {
		try {
			//datos del cliente
			txtIdCliente.setText(String.valueOf(cuentaCliente.getCliente().getIdCliente()));
			txtCedula.setText(cuentaCliente.getCliente().getCedula());
			txtNombres.setText(cuentaCliente.getCliente().getNombre());
			txtApellidos.setText(cuentaCliente.getCliente().getApellido());
			txtTelefono.setText(cuentaCliente.getCliente().getTelefono());
			txtEmail.setText(cuentaCliente.getCliente().getEmail());
			if(cuentaCliente.getCliente().getGenero() != null) {
				for(Genero g : genero){
					if(g.toString().equals(cuentaCliente.getCliente().getGenero().toString()))
						cboGenero.getSelectionModel().select(g);
				}
			}
			//clienteRecuperado = listaCliente.get(0);
			
			//datos de la cuenta
			txtCodigoCuenta.setText(String.valueOf(cuentaCliente.getIdCuenta()));
			if(cuentaCliente.getFechaIngreso() != null)
				txtFechaIngreso.setText(formateador.format(cuentaCliente.getFechaIngreso()));
			//Datos de categoria
			if(cuentaCliente.getCategoria() != null) {
				categoriaSeleccionado = cuentaCliente.getCategoria();
				txtIdCategoria.setText(String.valueOf(cuentaCliente.getCategoria().getIdCategoria()));
				txtDescripcionCategoria.setText(cuentaCliente.getCategoria().getDescripcion());
				txtValorm3.setText(String.valueOf(cuentaCliente.getCategoria().getValorM3()));	
			}else {
				categoriaSeleccionado = null;
				txtIdCategoria.setText("");
				txtDescripcionCategoria.setText("");
				txtValorm3.setText("");
			}
			//datos del barrio
			if(cuentaCliente.getBarrio() != null) {
				barrioSeleccionado = cuentaCliente.getBarrio();
				txtIdBarrio.setText(String.valueOf(cuentaCliente.getBarrio().getIdBarrio()));
				txtNombreBarrio.setText(cuentaCliente.getBarrio().getNombre());
				txtDescripcionBarrio.setText(cuentaCliente.getBarrio().getDescripcion());	
			}else {
				barrioSeleccionado = null;
				txtIdBarrio.setText("");
				txtNombreBarrio.setText("");
				txtDescripcionBarrio.setText("");
			}
			//Datos del Medidor
			if(cuentaCliente.getMedidor() != null) {
				txtIdMedidor.setText(String.valueOf(cuentaCliente.getMedidor().getIdMedidor()));
				txtCodigoMedidor.setText(cuentaCliente.getMedidor().getCodigo());
				txtDetallesMedidor.setText(cuentaCliente.getMedidor().getModelo() + " " + cuentaCliente.getMedidor().getMarca());
			}else {
				txtIdMedidor.setText("");
				txtCodigoMedidor.setText("");
				txtDetallesMedidor.setText("");
			}
			
			//otros datos
			txtLatitud.setText(String.valueOf(cuentaCliente.getLatitud()));
			txtLongitud.setText(String.valueOf(cuentaCliente.getLongitud()));
			//otrosss
			txtDireccionDomicilio.setText(cuentaCliente.getDireccion());
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void grabar() {
		try {
			if(txtCodigoCuenta.getText().equals("")) {
				helper.mostrarAlertaError("Debe Seleccionar Cuenta de Cliente", Context.getInstance().getStage());
				return;
			}
			
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				//actualizar los datos personales tbn
				clienteSeleccionado.setApellido(txtApellidos.getText());
				clienteSeleccionado.setNombre(txtNombres.getText());
				clienteSeleccionado.setCedula(txtCedula.getText());
				clienteSeleccionado.setEmail(txtEmail.getText());
				clienteSeleccionado.setTelefono(txtTelefono.getText());
				clienteSeleccionado.setGenero(cboGenero.getSelectionModel().getSelectedItem().name());
				//enlazar con el barrio
				if (barrioSeleccionado != null) {
					barrioSeleccionado.addCuentaCliente(cuentaSeleccionada);
					cuentaSeleccionada.setBarrio(barrioSeleccionado);
				}
				//enlazar con la categoria
				if(categoriaSeleccionado != null) {
					categoriaSeleccionado.addCuentaCliente(cuentaSeleccionada);
					cuentaSeleccionada.setCategoria(categoriaSeleccionado);
				}
				cuentaSeleccionada.setDireccion(txtDireccionDomicilio.getText());
				cuentaSeleccionada.setEstado("A");
				if(!txtLatitud.getText().isEmpty())
					cuentaSeleccionada.setLatitud(Double.parseDouble(txtLatitud.getText()));
				if(!txtLongitud.getText().isEmpty())
					cuentaSeleccionada.setLongitud(Double.parseDouble(txtLongitud.getText()));
				
				cuentaDAO.getEntityManager().getTransaction().begin();
				cuentaDAO.getEntityManager().merge(cuentaSeleccionada);
				cuentaDAO.getEntityManager().merge(clienteSeleccionado);
				cuentaDAO.getEntityManager().getTransaction().commit();
				helper.mostrarAlertaInformacion("Datos Grabados con Exito", Context.getInstance().getStage());
			}
			
		}catch(Exception ex) {
			cuentaDAO.getEntityManager().getTransaction().rollback();
			System.out.println(ex.getMessage());
		}
	}
	public void salir() {
		Context.getInstance().getStageModal().close();
	}
	
	public void buscarCategoria() {
		try{
			helper.abrirPantallaModal("/clientes/ClientesListaCategoria.fxml","Listado de Categorías", Context.getInstance().getStage());
			if (Context.getInstance().getCategoria() != null) {
				categoriaSeleccionado = Context.getInstance().getCategoria();
				recuperarCategoria(Context.getInstance().getCategoria());
				Context.getInstance().setCategoria(null);
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	private void recuperarCategoria(Categoria categoria) {
		try {
			if(categoria != null) {
				txtIdCategoria.setText(String.valueOf(categoria.getIdCategoria()));
				txtDescripcionCategoria.setText(categoria.getDescripcion());
				txtValorm3.setText(String.valueOf(categoria.getValorM3()));	
			}else {
				txtIdCategoria.setText("");
				txtDescripcionCategoria.setText("");
				txtValorm3.setText("");
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void buscarBarrio() {
		try{
			helper.abrirPantallaModal("/clientes/ClientesBarrios.fxml","Listado de Barrios", Context.getInstance().getStage());
			if (Context.getInstance().getBarrio() != null) {
				barrioSeleccionado = Context.getInstance().getBarrio();
				recuperarBarrio(Context.getInstance().getBarrio());
				Context.getInstance().setBarrio(null);
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	private void recuperarBarrio(Barrio barrio) {
		try {
			if(barrio != null) {
				txtIdBarrio.setText(String.valueOf(barrio.getIdBarrio()));
				txtNombreBarrio.setText(barrio.getNombre());
				txtDescripcionBarrio.setText(barrio.getDescripcion());	
			}else {
				txtIdBarrio.setText("");
				txtNombreBarrio.setText("");
				txtDescripcionBarrio.setText("");
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
