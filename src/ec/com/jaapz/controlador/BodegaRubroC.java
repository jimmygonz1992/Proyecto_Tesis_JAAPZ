package ec.com.jaapz.controlador;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import ec.com.jaapz.modelo.Rubro;
import ec.com.jaapz.modelo.RubroDAO;
import ec.com.jaapz.modelo.TipoRubro;
import ec.com.jaapz.modelo.TipoRubroDAO;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class BodegaRubroC {
	@FXML private TextField txtCodigo;
	@FXML private TextField txtCodigoProducto;
	@FXML private TextField txtDescripcion;
	@FXML private TextField txtMarca;
	@FXML private TextField txtPrecio;
	@FXML private TextField txtCantidad;
	@FXML private CheckBox chkEstado;
	@FXML private ComboBox<TipoRubro> cboTipoRubro;
	@FXML private Button btnGrabar;
	@FXML private Button btnNuevo;
	@FXML private Button btnBuscar;
	ControllerHelper helper = new ControllerHelper();
	TipoRubroDAO tipoRubroDAO = new TipoRubroDAO();
	RubroDAO rubroDAO = new RubroDAO();
	private @FXML TableView<Rubro> tvDatos;
	
	@SuppressWarnings("unused")
	public void initialize(){
		btnBuscar.setStyle("-fx-cursor: hand;");
		btnGrabar.setStyle("-fx-cursor: hand;");
		btnNuevo.setStyle("-fx-cursor: hand;");
		cboTipoRubro.setStyle("-fx-cursor: hand;");
		
		int maxLength = 13;
		limpiar();
		llenarComboPerfil();
		chkEstado.setSelected(true);
		txtCodigo.setVisible(false);
	}
	
	void limpiar() {
		txtCodigo.setText("0");
		txtCodigoProducto.setText("");
		txtCodigo.setEditable(false);
		txtDescripcion.setText("");
		txtMarca.setText("");
		txtPrecio.setText("");
		txtCantidad.setText("");
		chkEstado.setSelected(true);
		txtDescripcion.requestFocus();
	}
	
	private void llenarComboPerfil(){
		try{
			cboTipoRubro.setPromptText("Seleccione Tipo de Rubro");
			List<TipoRubro> listaTipoRubro;
			listaTipoRubro = tipoRubroDAO.getListaTipoRubro();
			ObservableList<TipoRubro> datos = FXCollections.observableArrayList();
		
			datos.addAll(listaTipoRubro);
			cboTipoRubro.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	public void grabar(){
	/*	PrintReport pr = new PrintReport();
		pr.crearReporte("/recursos/informes/lista_materiales_disponibles.jasper", rubroDAO, null);
		pr.showReport("Listado");*/
		try {
			java.util.Date utilDate = new java.util.Date(); 
			long lnMilisegundos = utilDate.getTime();
			java.sql.Time sqlTime = new java.sql.Time(lnMilisegundos);
			String estado;
			if(validarDatos() == false){
				return;
			}
			if(chkEstado.isSelected() == true)
				estado = "A";
			else
				estado = "I";
			Rubro rubro = new Rubro();
			rubro.setEstado(estado);
			rubro.setTipoRubro(cboTipoRubro.getSelectionModel().getSelectedItem());
			rubro.setDescripcion(txtDescripcion.getText());
			rubro.setMarca(txtMarca.getText());
			rubro.setCodigo(txtCodigoProducto.getText());
			rubro.setUsuarioCrea(Context.getInstance().getUsuariosC().getIdUsuario());
			rubro.setHoraCrea(sqlTime);
			rubro.setFechaCrea(new Date());
			//falta la hora
			rubro.setPrecio(Double.parseDouble(txtPrecio.getText()));
			rubro.setStock(Integer.parseInt(txtCantidad.getText()));
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				rubro.setEstado(estado);
				rubroDAO.getEntityManager().getTransaction().begin();
				if(txtCodigo.getText().equals("0")) {//inserta
					rubro.setIdRubro(null);
					rubroDAO.getEntityManager().persist(rubro);
				}else {//modifica
					rubro.setIdRubro(Integer.parseInt(txtCodigo.getText()));
					rubroDAO.getEntityManager().merge(rubro);
				}
				rubroDAO.getEntityManager().getTransaction().commit();
				helper.mostrarAlertaInformacion("Datos Grabados Correctamente", Context.getInstance().getStage());
				limpiar();
			}
		}catch(Exception ex) {
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
			rubroDAO.getEntityManager().getTransaction().rollback();
			System.out.println(ex.getMessage());
		}
	}
	
	boolean validarDatos() {
		try {
			if(cboTipoRubro.getValue() == null) {
				helper.mostrarAlertaAdvertencia("Debe Seleccionar un Tipo de Rubro", Context.getInstance().getStage());
				cboTipoRubro.requestFocus();
				return false;	
			}
			
			if(txtDescripcion.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe ingresar Rubro", Context.getInstance().getStage());
				txtDescripcion.requestFocus();
				return false;
			}
			
			if(txtCodigoProducto.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe ingresar Código de Rubro", Context.getInstance().getStage());
				txtCodigoProducto.requestFocus();
				return false;
			}
		/*	if(txtMarca.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe ingresar Marca", Context.getInstance().getStage());
				txtMarca.requestFocus();
				return false;
			}*/
			if(txtPrecio.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe ingresar Precio", Context.getInstance().getStage());
				txtPrecio.requestFocus();
				return false;
			}
			if(txtCantidad.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("Debe ingresar Cantidad", Context.getInstance().getStage());
				txtCantidad.requestFocus();
				return false;	
			}
			
			if(validarRubro() == true) {
				helper.mostrarAlertaAdvertencia("El elemento ya existe!!", Context.getInstance().getStage());
				txtDescripcion.requestFocus();
				return false;	
			}
			return true;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
	boolean validarRubro() {
		try {
			List<Rubro> listaRubro;
			listaRubro = rubroDAO.getValidarRubro(txtDescripcion.getText(), txtCodigoProducto.getText());
			if(listaRubro.size() != 0)
				return true;
			else
				return false;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
	public void nuevo() throws IOException{
		limpiar();
	}
	
	public void buscar() {
		try{
			helper.abrirPantallaModal("/bodega/ListadoRubros.fxml","Listado de Rubros", Context.getInstance().getStage());
			if (Context.getInstance().getRubros() != null) {
				Rubro datoSeleccionado = Context.getInstance().getRubros();
				llenarDatos(datoSeleccionado);
				Context.getInstance().setRubros(null);
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	void llenarDatos(Rubro datoSeleccionado){
		try {
			txtCodigo.setText(String.valueOf(datoSeleccionado.getIdRubro()));
			cboTipoRubro.getSelectionModel().select(datoSeleccionado.getTipoRubro());
			
			if(datoSeleccionado.getCodigo() == null)
				txtCodigoProducto.setText("");
			else
				txtCodigoProducto.setText(datoSeleccionado.getCodigo());
			
			if(datoSeleccionado.getDescripcion() == null)
				txtDescripcion.setText("");
			else
				txtDescripcion.setText(datoSeleccionado.getDescripcion());

			if(datoSeleccionado.getMarca() == null)
				txtMarca.setText("");
			else
				txtMarca.setText(datoSeleccionado.getMarca());
			
			txtPrecio.setText(String.valueOf(datoSeleccionado.getPrecio()));

			if(datoSeleccionado.getStock() == null)
				txtCantidad.setText("");
			else
				txtCantidad.setText(Integer.toString(datoSeleccionado.getStock()));

			if(datoSeleccionado.getEstado().equals("A")) 
				chkEstado.setSelected(true);
			else
				chkEstado.setSelected(false);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}