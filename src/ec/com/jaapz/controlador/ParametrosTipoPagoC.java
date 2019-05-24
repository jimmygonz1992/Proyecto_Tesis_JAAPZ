package ec.com.jaapz.controlador;

import java.util.List;
import java.util.Optional;

import ec.com.jaapz.modelo.TipoPago;
import ec.com.jaapz.modelo.TipoPagoDAO;
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

public class ParametrosTipoPagoC {
	private @FXML TableView<TipoPago> tvDatos;
	private @FXML TextField txtCodigo;
	private @FXML TextField txtDescripcion;
	private @FXML CheckBox chkEstado;
	private @FXML Button btnAceptar;
	private @FXML Button btnNuevo;
	TipoPagoDAO tipoPagoDAO = new TipoPagoDAO();
	ControllerHelper helper = new ControllerHelper();

	public void initialize() {
		btnAceptar.setStyle("-fx-cursor: hand;");
		btnNuevo.setStyle("-fx-cursor: hand;");
		txtCodigo.setEditable(false);
		txtCodigo.setVisible(false);

		limpiar();
		llenarDatos();
		//solo letras mayusculas
		txtDescripcion.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				String cadena = txtDescripcion.getText().toUpperCase();
				txtDescripcion.setText(cadena);
			}
		});
		
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
			List<TipoPago> listaTipoPago;
			ObservableList<TipoPago> datos = FXCollections.observableArrayList();
			listaTipoPago = tipoPagoDAO.getListaTipoPago();
			datos.setAll(listaTipoPago);

			//llenar los datos en la tabla
			TableColumn<TipoPago, String> idColum = new TableColumn<>("Ítem");
			idColum.setMinWidth(125);
			idColum.setCellValueFactory(new PropertyValueFactory<TipoPago, String>("idTipoPago"));
			TableColumn<TipoPago, String> descripcionColum = new TableColumn<>("Descripción");
			descripcionColum.setMinWidth(400);
			descripcionColum.setCellValueFactory(new PropertyValueFactory<TipoPago, String>("descrpcion"));
			TableColumn<TipoPago, String> estadoColum = new TableColumn<>("Estado");
			estadoColum.setMinWidth(85);
			estadoColum.setCellValueFactory(new PropertyValueFactory<TipoPago, String>("estado"));

			tvDatos.getColumns().addAll(idColum, descripcionColum,estadoColum);
			tvDatos.setItems(datos);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}

	public void recuperarDatos(TipoPago datos){
		try{
			txtCodigo.setText(String.valueOf(datos.getIdTipoPago()));
			txtDescripcion.setText(String.valueOf(datos.getDescrpcion()));
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
			TipoPago tipoPago = new TipoPago();
			if(chkEstado.isSelected() == true)
				tipoPago.setEstado("A");
			else
				tipoPago.setEstado("I");
			tipoPago.setDescrpcion(txtDescripcion.getText());

			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Desea Grabar los Datos?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				tipoPagoDAO.getEntityManager().getTransaction().begin();
				if(txtCodigo.getText().equals("0")) {//inserta
					tipoPago.setIdTipoPago(null);
					tipoPagoDAO.getEntityManager().persist(tipoPago);
				}else {//modifica
					tipoPago.setIdTipoPago(Integer.parseInt(txtCodigo.getText()));
					tipoPagoDAO.getEntityManager().merge(tipoPago);
				}
				tipoPagoDAO.getEntityManager().getTransaction().commit();
				helper.mostrarAlertaInformacion("Datos Grabados", Context.getInstance().getStage());
				llenarDatos();
				limpiar();
			}
		}catch(Exception ex) {
			tipoPagoDAO.getEntityManager().getTransaction().rollback();
			helper.mostrarAlertaError("Error al grabar", Context.getInstance().getStage());
			System.out.println(ex.getMessage());
		}
	}

	public void nuevo() {
		limpiar();
	}
}