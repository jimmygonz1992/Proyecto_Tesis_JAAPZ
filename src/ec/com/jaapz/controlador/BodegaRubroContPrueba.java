package ec.com.jaapz.controlador;

import java.util.ArrayList;
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
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class BodegaRubroContPrueba {
	@FXML private TextField txtCodigo;
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

	public void initialize(){
		limpiar();
		llenarDatos();
		
		tvDatos.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				recuperarDatos(tvDatos.getSelectionModel().getSelectedItem().getIdRubro());
			}
		});
	}
	
	public void recuperarDatos(int codigo){
		try{
			List<Rubro> listaRubro = new ArrayList<Rubro>();
			listaRubro = rubroDAO.getRubroN(codigo);
			for(int i = 0 ; i < listaRubro.size() ; i ++) {
				
				txtCodigo.setText(String.valueOf(listaRubro.get(i).getIdRubro()));
				cboTipoRubro.getSelectionModel().selectedItemProperty();
				//cboTipoRubro.getSelectionModel().select(datoSeleccionado.getSegPerfil());
				txtDescripcion.setText(listaRubro.get(i).getDescripcion());
				txtMarca.setText(listaRubro.get(i).getMarca());
				txtPrecio.setText(String.valueOf(listaRubro.get(i).getPrecio()));
				txtCantidad.setText(String.valueOf(listaRubro.get(i).getStock()));
				if(listaRubro.get(i).getEstado().equals("A"))
					chkEstado.setSelected(true);
				else
					chkEstado.setSelected(false);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	void limpiar() {
		txtCodigo.setText("0");
		txtCodigo.setEditable(false);
		txtDescripcion.setText("");
		txtMarca.setText("");
		txtPrecio.setText("");
		txtCantidad.setText("");
		chkEstado.setSelected(true);
		txtDescripcion.requestFocus();
	}
	
	public void nuevo(){
		limpiar();
	}
	
	@SuppressWarnings("unchecked")
	void llenarDatos(){
		try{
			tvDatos.getColumns().clear();
			RubroDAO rubroDAO = new RubroDAO();
			List<Rubro> listaRubros;
			listaRubros = rubroDAO.getListaRubros();
			ObservableList<Rubro> datos = FXCollections.observableArrayList();
			datos.setAll(listaRubros);

			//llenar los datos en la tabla
			TableColumn<Rubro, String> idColum = new TableColumn<>("Codigo");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(60);
			idColum.setCellValueFactory(new PropertyValueFactory<Rubro, String>("idRubro"));
			TableColumn<Rubro, String> nombresColum = new TableColumn<>("Material");
			nombresColum.setMinWidth(10);
			nombresColum.setPrefWidth(200);
			nombresColum.setCellValueFactory(new PropertyValueFactory<Rubro, String>("descripcion"));
			TableColumn<Rubro, String> marcaColum = new TableColumn<>("Marca");
			marcaColum.setMinWidth(10);
			marcaColum.setPrefWidth(100);
			marcaColum.setCellValueFactory(new PropertyValueFactory<Rubro, String>("marca"));
			TableColumn<Rubro, String> cantidadColum = new TableColumn<>("Cantidad");
			cantidadColum.setMinWidth(10);
			cantidadColum.setPrefWidth(90);
			cantidadColum.setCellValueFactory(new PropertyValueFactory<Rubro, String>("stock"));
			TableColumn<Rubro, Double> fechColum = new TableColumn<>("Precio");
			fechColum.setMinWidth(10);
			fechColum.setPrefWidth(90);
			fechColum.setCellValueFactory(new PropertyValueFactory<Rubro, Double>("precio"));
			TableColumn<Rubro, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(10);
			estadoColum.setPrefWidth(50);
			estadoColum.setCellValueFactory(new PropertyValueFactory<Rubro, String>("estado"));
			tvDatos.getColumns().addAll(idColum, nombresColum,marcaColum,cantidadColum,fechColum,estadoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex){
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
			listaRubro = rubroDAO.getValidarRubro(txtDescripcion.getText(), txtCodigo.getText());
			if(listaRubro.size() != 0)
				return true;
			else
				return false;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
	public void buscar() {
		
	}
	
	public void grabar(){
		/*	PrintReport pr = new PrintReport();
		pr.crearReporte("/recursos/informes/lista_materiales_disponibles.jasper", rubroDAO, null);
		pr.showReport("Listado");*/
		try {
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
}