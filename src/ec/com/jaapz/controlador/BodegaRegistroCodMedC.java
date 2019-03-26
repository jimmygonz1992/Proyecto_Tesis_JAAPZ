package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ec.com.jaapz.modelo.Ingreso;
import ec.com.jaapz.modelo.IngresoDAO;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

public class BodegaRegistroCodMedC {
	@FXML private TextField txtCantidadMedidores;
	@FXML private TextField txtNumFactura;
	@FXML private TextField txtRUC;
	@FXML private TableView<Medidor> tvDatos;
	@FXML private TextField txtFechaIngreso;
	@FXML private Button btnNuevo;
	@FXML private Button btnGrabar;
	@FXML private TextField txtProveedor;
	@FXML private Label lblMedidores;
	@FXML private TextField txtTotal;
	@FXML private Button btnBuscar;
	IngresoDAO ingresoDAO = new IngresoDAO();
	MedidorDAO medidorDAO = new MedidorDAO();
	ControllerHelper helper = new ControllerHelper();
	
	public void initialize() {
		try {
			bloquear();
			tvDatos.setEditable(true);
			btnBuscar.setStyle("-fx-cursor: hand;");
			btnGrabar.setStyle("-fx-cursor: hand;");
			btnNuevo.setStyle("-fx-cursor: hand;");
			
			txtNumFactura.setOnKeyPressed(new EventHandler<KeyEvent>(){
				@Override
				public void handle(KeyEvent ke){
					if (ke.getCode().equals(KeyCode.ENTER)){
						recuperarFactura(txtNumFactura.getText());
					}
				}
			});
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	void bloquear() {
		txtNumFactura.setEditable(false);
		txtRUC.setEditable(false);
		txtCantidadMedidores.setEditable(false);
		txtFechaIngreso.setEditable(false);
		txtProveedor.setEditable(false);
		txtTotal.setEditable(false);
	}
	
	private void recuperarFactura(String numIngreso) {
		try{		
			List<Ingreso> listaIngreso = new ArrayList<Ingreso>();
			listaIngreso = ingresoDAO.getRecuperaIngreso(numIngreso);
			for(int i = 0 ; i < listaIngreso.size() ; i ++) {
				SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
				txtNumFactura.setText(listaIngreso.get(0).getNumeroIngreso());
				txtFechaIngreso.setText(formateador.format(listaIngreso.get(0).getFecha()));
				txtRUC.setText(listaIngreso.get(i).getProveedor().getRuc());
				txtProveedor.setText(listaIngreso.get(i).getProveedor().getNombres() + " " + listaIngreso.get(i).getProveedor().getApellidos());
				txtTotal.setText(String.valueOf(listaIngreso.get(0).getTotal()));
				tvDatos.getItems().clear();
				tvDatos.getColumns().clear();
				boolean bandera = false;
				for(IngresoDetalle detalle : listaIngreso.get(0).getIngresoDetalles()) {
					if(detalle.getRubro().getIdRubro() == Constantes.ID_MEDIDOR) { //tiene medidores el ingreso
						txtCantidadMedidores.setText(String.valueOf(detalle.getCantidad()));
						llenarDatosMedidores(listaIngreso.get(0).getIdIngreso());
						bandera = true;
					}
				}
				if(bandera == false) {
					lblMedidores.setText("Total de medidores: 0");
					txtCantidadMedidores.setText("0");
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	private void llenarDatosMedidores(Integer idFactura) {
		try {
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
			numeroColum.setOnEditCommit(
				    new EventHandler<CellEditEvent<Medidor, String>>() {
				        @Override
				        public void handle(CellEditEvent<Medidor, String> t) {
				        	String valor;
				        	if(validarCodigo(t.getNewValue(),tvDatos.getSelectionModel().getSelectedItem().getIdMedidor()) == true) {//el codigo ya existe existe
				        		valor = t.getOldValue();
				        		helper.mostrarAlertaAdvertencia("Codigo ya existe entre los registros!!!!!", Context.getInstance().getStage());
				        	}else {//el codigo existe.. no debe permitir ingresar el mismo
				        		valor = t.getNewValue();
				        	}
				            ((Medidor) t.getTableView().getItems().get(
				                t.getTablePosition().getRow())
				                ).setCodigo(valor);
				            tvDatos.refresh();
				        }
				    }
				);
			
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
			marcaColum.setOnEditCommit(
				    new EventHandler<CellEditEvent<Medidor, String>>() {
				        @Override
				        public void handle(CellEditEvent<Medidor, String> t) {
				        	
				            ((Medidor) t.getTableView().getItems().get(
				                t.getTablePosition().getRow())
				                ).setMarca(t.getNewValue());
				            tvDatos.refresh();
				        }
				    }
				);
			
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
			modeloColum.setOnEditCommit(
				    new EventHandler<CellEditEvent<Medidor, String>>() {
				        @Override
				        public void handle(CellEditEvent<Medidor, String> t) {
				        	
				            ((Medidor) t.getTableView().getItems().get(
				                t.getTablePosition().getRow())
				                ).setModelo(t.getNewValue());
				            tvDatos.refresh();
				        }
				    }
				);
			
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
	private boolean validarCodigo(String codigoIngresado,Integer idMedidor) {//el metodo retorna verdadero cuando el codigo ya la tiene otro registro
		try {
			boolean bandera = false;
			List<Medidor> listaMedidor = medidorDAO.getValidarCodigoMedidor(codigoIngresado);
			if(listaMedidor.size() > 0) //codigo existe en uno de los registros
				bandera = true;
			//la siguiente validacion es en la tabla
			for(Medidor medidor : tvDatos.getItems()) {
				if(medidor.getIdMedidor() != idMedidor) {//hay q preguntar por todos los registros menos por el propio.. xq va a saltar que si existe
					if(codigoIngresado.equals(medidor.getCodigo()))
						bandera = true; //el codigo ya la tiene otro registro.. 
				}
			}
			return bandera;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	public void buscarLiqCuenta() {
		try {
			helper.abrirPantallaModal("/bodega/BodegaListaIngreso.fxml","Listado de Ingresos", Context.getInstance().getStage());
			if(Context.getInstance().getIngresoSeleccionado() != null) {
				recuperarFactura(Context.getInstance().getIngresoSeleccionado().getNumeroIngreso());
				Context.getInstance().setIngresoSeleccionado(null);
			}
			
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void grabar() {
		try {
			if(validarDatos() == false)
				return;
			Optional<ButtonType> result = helper.mostrarAlertaConfirmacion("Se actualizaran los registros de los medidores \nDesea continuar?",Context.getInstance().getStage());
			if(result.get() == ButtonType.OK){
				medidorDAO.getEntityManager().getTransaction().begin();
				for(Medidor medidor : tvDatos.getItems()) {
					medidorDAO.getEntityManager().merge(medidor);
				}
				medidorDAO.getEntityManager().getTransaction().commit();
				helper.mostrarAlertaInformacion("Datos grabados correctamente", Context.getInstance().getStage());
				limpiar();
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	boolean validarDatos() {
		try {
			if(txtNumFactura.getText().equals("")) {
				helper.mostrarAlertaAdvertencia("No existe Nº Factura asociada", Context.getInstance().getStage());
				txtNumFactura.requestFocus();
				return false;
			}
						
			if(tvDatos.getItems().isEmpty()) {
				helper.mostrarAlertaAdvertencia("No contiene rubros", Context.getInstance().getStage());
				tvDatos.requestFocus();
				return false;
			}
			return true;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
	void limpiar() {
		txtNumFactura.setText("");
		txtFechaIngreso.setText("");
		txtRUC.setText("");
		txtProveedor.setText("");
		txtCantidadMedidores.setText("");
		txtTotal.setText("");
		tvDatos.getItems().clear();
		tvDatos.getColumns().clear();
	}
}
