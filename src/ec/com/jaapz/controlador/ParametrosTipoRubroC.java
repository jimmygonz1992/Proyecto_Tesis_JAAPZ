package ec.com.jaapz.controlador;

import java.util.List;
import java.util.Optional;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ParametrosTipoRubroC {
	private @FXML TableView<TipoRubro> tvDatos;
	private @FXML TextField txtCodigo;
	private @FXML TextField txtDescripcion;
	private @FXML CheckBox chkEstado;
	private @FXML Button btnAceptar;
	private @FXML Button btnNuevo;
	TipoRubroDAO tipoRubroDAO = new TipoRubroDAO();
	ControllerHelper helper = new ControllerHelper();
	
	public void initialize() {
		btnNuevo.setStyle("-fx-cursor: hand;");
		btnAceptar.setStyle("-fx-cursor: hand;");
		txtCodigo.setEditable(false);
		txtCodigo.setVisible(false);
		
		limpiar();
		llenarDatos();
		tvDatos.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				recuperarDatos(tvDatos.getSelectionModel().getSelectedItem());
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	void llenarDatos(){
		try{
			tvDatos.getColumns().clear();
			List<TipoRubro> listaTipoRubro;
			ObservableList<TipoRubro> datos = FXCollections.observableArrayList();
			listaTipoRubro = tipoRubroDAO.getListaTipoRubro();
			datos.setAll(listaTipoRubro);

			//llenar los datos en la tabla
			TableColumn<TipoRubro, String> idColum = new TableColumn<>("Ítem");
			idColum.setMinWidth(125);
			idColum.setCellValueFactory(new PropertyValueFactory<TipoRubro, String>("idTipo"));
			TableColumn<TipoRubro, String> descripcionColum = new TableColumn<>("Descripción");
			descripcionColum.setMinWidth(400);
			descripcionColum.setCellValueFactory(new PropertyValueFactory<TipoRubro, String>("descripcion"));
			TableColumn<TipoRubro, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(83);
			estadoColum.setCellValueFactory(new PropertyValueFactory<TipoRubro, String>("estado"));

			tvDatos.getColumns().addAll(idColum, descripcionColum,estadoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	public void recuperarDatos(TipoRubro datos){
		try{
			txtCodigo.setText(String.valueOf(datos.getIdTipo()));
			txtDescripcion.setText(String.valueOf(datos.getDescripcion()));
			if(datos.getEstado().equals("A"))
				chkEstado.setSelected(true);
			else
				chkEstado.setSelected(false);

		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	void limpiar(){
		txtCodigo.setText("0");
		txtDescripcion.setText("");
		chkEstado.setSelected(true);
	}
	
	public void grabar() {
		try {
			TipoRubro tipoRubro = new TipoRubro();
			if(chkEstado.isSelected() == true)
				tipoRubro.setEstado("A");
			else
				tipoRubro.setEstado("I");
				tipoRubro.setDescripcion(txtDescripcion.getText());

			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				tipoRubroDAO.getEntityManager().getTransaction().begin();
				if(txtCodigo.getText().equals("0")) {//inserta
					tipoRubro.setIdTipo(null);
					tipoRubroDAO.getEntityManager().persist(tipoRubro);
				}else {//modifica
					tipoRubro.setIdTipo(Integer.parseInt(txtCodigo.getText()));
					tipoRubroDAO.getEntityManager().merge(tipoRubro);
				}
				tipoRubroDAO.getEntityManager().getTransaction().commit();
				helper.mostrarAlertaInformacion("Datos Grabados", Context.getInstance().getStage());
				llenarDatos();
				limpiar();
			}
		}catch(Exception ex) {
			tipoRubroDAO.getEntityManager().getTransaction().rollback();
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
			System.out.println(ex.getMessage());
		}
	}
	
	public void nuevo() {
		limpiar();
	}
}