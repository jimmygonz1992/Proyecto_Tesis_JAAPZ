package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import ec.com.jaapz.modelo.EstadoMedidor;
import ec.com.jaapz.modelo.EstadoMedidorDAO;
import ec.com.jaapz.modelo.IngresoDetalle;
import ec.com.jaapz.modelo.Medidor;
import ec.com.jaapz.modelo.MedidorDAO;
import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

public class BodegaModificarMedidorC {
	@FXML private TextField txtNumFactura;
	@FXML private TextField txtRUC;
	@FXML private TableView<Medidor> tvDatos;
	@FXML private TextField txtFechaIngreso;
	@FXML private Button btnAgregar;
	@FXML private TextField txtProveedor;
	@FXML private Button btnQuitar;
	@FXML private Label lblMedidores;
	IngresoDetalle detalleSeleccionado;
	MedidorDAO medidorDAO = new MedidorDAO();
	EstadoMedidorDAO estadoMedidorDAO = new EstadoMedidorDAO();
	ControllerHelper helper = new ControllerHelper();
	public void initialize() {
		try {
			SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
			detalleSeleccionado = Context.getInstance().getDetalleMedidor();
			txtNumFactura.setText(detalleSeleccionado.getIngreso().getNumeroIngreso());
			txtRUC.setText(detalleSeleccionado.getIngreso().getProveedor().getRuc());
			txtFechaIngreso.setText(formateador.format(detalleSeleccionado.getIngreso().getFecha()));
			txtProveedor.setText(detalleSeleccionado.getIngreso().getProveedor().getNombres() + " " + detalleSeleccionado.getIngreso().getProveedor().getApellidos());
			tvDatos.setEditable(true);
			llenarDatosMedidores(detalleSeleccionado.getIngreso().getIdIngreso());
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	private void llenarDatosMedidores(Integer idFactura) {
		try {
			tvDatos.getItems().clear();
			tvDatos.getColumns().clear();
			List<Medidor> listaMedidor = medidorDAO.getRecuperarMedidorFactura(idFactura);
			lblMedidores.setText("Total de medidores: " + listaMedidor.size());
			ObservableList<Medidor> datosReq = FXCollections.observableArrayList();
			datosReq.setAll(listaMedidor);
		
			TableColumn<Medidor, String> numeroColum = new TableColumn<>("Número Med.");
			numeroColum.setMinWidth(10);
			numeroColum.setPrefWidth(150);
			numeroColum.setCellFactory(TextFieldTableCell.<Medidor>forTableColumn());
			numeroColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Medidor, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Medidor, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getCodigo());
				}
			});
			TableColumn<Medidor, String> marcaColum = new TableColumn<>("Marca");
			marcaColum.setMinWidth(10);
			marcaColum.setPrefWidth(150);
			marcaColum.setCellFactory(TextFieldTableCell.<Medidor>forTableColumn());
			marcaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Medidor, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Medidor, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getMarca());
				}
			});
			TableColumn<Medidor, String> modeloColum = new TableColumn<>("Modelo");
			modeloColum.setMinWidth(10);
			modeloColum.setPrefWidth(150);
			modeloColum.setCellFactory(TextFieldTableCell.<Medidor>forTableColumn());
			modeloColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Medidor, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Medidor, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getModelo());
				}
			});
			TableColumn<Medidor, String> precioColum = new TableColumn<>("Precio");
			precioColum.setMinWidth(10);
			precioColum.setPrefWidth(150);
			precioColum.setCellFactory(TextFieldTableCell.<Medidor>forTableColumn());
			precioColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Medidor, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Medidor, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getPrecio()));
				}
			});
			
			
			tvDatos.getColumns().addAll(numeroColum, marcaColum, modeloColum,precioColum);
			tvDatos.setItems(datosReq);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void quitar() {
		try {
			if(tvDatos.getSelectionModel().getSelectedItem() == null) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar registro a eliminar", Context.getInstance().getStage());
				return;
			}
			if(tvDatos.getSelectionModel().getSelectedItem().isUsado() == true) {
				helper.mostrarAlertaAdvertencia("No se puede quitar el registro porque el medidor esta siendo utilizado por un cliente", Context.getInstance().getStage());
				return;
			}
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Se eliminará el medidor registrado \nDesea continuar?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				Medidor medidorQuitar = tvDatos.getSelectionModel().getSelectedItem();
				medidorQuitar.setEstado(Constantes.ESTADO_INACTIVO);
				
				medidorDAO.getEntityManager().getTransaction().begin();
				medidorDAO.getEntityManager().persist(medidorQuitar);
				medidorDAO.getEntityManager().getTransaction().commit();
				llenarDatosMedidores(detalleSeleccionado.getIngreso().getIdIngreso());
				
				double total = 0;
				for(Medidor med : tvDatos.getItems())
					total = total + med.getPrecio();
				detalleSeleccionado.setCantidad(tvDatos.getItems().size());
				detalleSeleccionado.setTotal(total);
				
				medidorDAO.getEntityManager().getTransaction().begin();
				medidorDAO.getEntityManager().merge(detalleSeleccionado);
				medidorDAO.getEntityManager().getTransaction().commit();
				lblMedidores.setText("Total de medidores: " + tvDatos.getItems().size());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void agregar() {
		try {
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Se registrará un nuevo medidor \nDesea continuar?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				Medidor medidorAgregar = new Medidor();				
				EstadoMedidor estadoM = estadoMedidorDAO.getEstadoBueno();
				medidorAgregar.setIdFactura(null);
				medidorAgregar.setCodigo("");
				medidorAgregar.setEstado(Constantes.ESTADO_ACTIVO);
				medidorAgregar.setIdFactura(detalleSeleccionado.getIngreso().getIdIngreso());
				medidorAgregar.setEstadoMedidor(estadoM);
				medidorAgregar.setMarca("S/M");
				medidorAgregar.setModelo("S/M");
				medidorAgregar.setPrecio(detalleSeleccionado.getRubro().getPrecio());
				
				medidorDAO.getEntityManager().getTransaction().begin();
				medidorDAO.getEntityManager().persist(medidorAgregar);
				medidorDAO.getEntityManager().getTransaction().commit();
				tvDatos.getItems().add(medidorAgregar);
				tvDatos.refresh();
				
				double total = 0;
				for(Medidor med : tvDatos.getItems())
					total = total + med.getPrecio();
				detalleSeleccionado.setCantidad(tvDatos.getItems().size());
				detalleSeleccionado.setTotal(total);
				
				medidorDAO.getEntityManager().getTransaction().begin();
				medidorDAO.getEntityManager().merge(detalleSeleccionado);
				medidorDAO.getEntityManager().getTransaction().commit();
				lblMedidores.setText("Total de medidores: " + tvDatos.getItems().size());
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
