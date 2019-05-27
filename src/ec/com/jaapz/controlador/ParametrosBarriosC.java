package ec.com.jaapz.controlador;

import java.util.List;
import java.util.Optional;

import ec.com.jaapz.modelo.Barrio;
import ec.com.jaapz.modelo.BarrioDAO;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

public class ParametrosBarriosC {
	private @FXML TableView<Barrio> tvDatos;
	private @FXML TextField txtCodigo;
	private @FXML TextField txtNombre;
	private @FXML TextField txtDescripcion;
	private @FXML CheckBox chkEstado;
	private @FXML Button btnAceptar;
	private @FXML Button btnNuevo;

	BarrioDAO junBarrioDAO = new BarrioDAO();
	ControllerHelper helper = new ControllerHelper();
	public void initialize() {
		btnAceptar.setStyle("-fx-cursor: hand;");
		btnNuevo.setStyle("-fx-cursor: hand;");
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

		//solo letras mayusculas
		txtDescripcion.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				String cadena = txtDescripcion.getText().toUpperCase();
				txtDescripcion.setText(cadena);
			}
		});

		//solo letras mayusculas
		txtNombre.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				String cadena = txtNombre.getText().toUpperCase();
				txtNombre.setText(cadena);
			}
		});
	}
	public void recuperarDatos(Barrio datos){
		try{
			txtCodigo.setText(String.valueOf(datos.getIdBarrio()));
			txtNombre.setText(datos.getNombre());
			txtDescripcion.setText(datos.getDescripcion());
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
		txtNombre.setText("");
		txtDescripcion.setText("");
		chkEstado.setSelected(true);
	}
	@SuppressWarnings("unchecked")
	void llenarDatos(){
		try{
			tvDatos.getColumns().clear();
			List<Barrio> listaBarrios;
			ObservableList<Barrio> datos = FXCollections.observableArrayList();
			listaBarrios = junBarrioDAO.getListaBarrios();
			datos.setAll(listaBarrios);

			//llenar los datos en la tabla
			TableColumn<Barrio, String> idColum = new TableColumn<>("Ítem");
			idColum.setMinWidth(100);
			idColum.setCellValueFactory(new PropertyValueFactory<Barrio, String>("idBarrio"));
			TableColumn<Barrio, String> descripcionColum = new TableColumn<>("Nombre");
			descripcionColum.setMinWidth(400);
			descripcionColum.setCellValueFactory(new PropertyValueFactory<Barrio, String>("nombre"));
			TableColumn<Barrio, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(90);
			estadoColum.setCellValueFactory(new PropertyValueFactory<Barrio, String>("estado"));

			tvDatos.getColumns().addAll(idColum, descripcionColum,estadoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	public void grabar() {
		try {
			Barrio barrio = new Barrio();
			if(chkEstado.isSelected() == true)
				barrio.setEstado("A");
			else
				barrio.setEstado("I");
			barrio.setDescripcion(txtDescripcion.getText());
			barrio.setNombre(txtNombre.getText());

			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				junBarrioDAO.getEntityManager().getTransaction().begin();
				if(txtCodigo.getText().equals("0")) {//inserta
					barrio.setIdBarrio(null);
					junBarrioDAO.getEntityManager().persist(barrio);
				}else {//modifica
					barrio.setIdBarrio(Integer.parseInt(txtCodigo.getText()));
					junBarrioDAO.getEntityManager().merge(barrio);
				}
				junBarrioDAO.getEntityManager().getTransaction().commit();
				helper.mostrarAlertaInformacion("Datos Grabados", Context.getInstance().getStage());
				llenarDatos();
				limpiar();
			}
		}catch(Exception ex) {
			junBarrioDAO.getEntityManager().getTransaction().rollback();
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
			System.out.println(ex.getMessage());
		}
	}
	public void nuevo() {
		limpiar();
	}
}
