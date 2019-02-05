package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ec.com.jaapz.modelo.Pago;
import ec.com.jaapz.modelo.Planilla;
import ec.com.jaapz.modelo.PlanillaDAO;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class RecaudacionesDeudaPorClienteC {
	PlanillaDAO planillaDao = new PlanillaDAO();
	@FXML private TextField txtCliente;
	@FXML private TextField txtTotalAdeudado;
	@FXML private TableView<Planilla> tvDatos;
	@FXML private Button btnReprte;
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	List<Planilla> listadoPlanillas = new ArrayList<Planilla>();
	
	public void initialize() {
		llenarTablaPlanillas("");
	}
	
	public void busquedaClienteDeudor() {
		llenarTablaPlanillas(txtCliente.getText());
	}
	
	@SuppressWarnings("unchecked")
	void llenarTablaPlanillas(String patron) {
		try{
			tvDatos.getColumns().clear();
			List<Planilla> listaPlanillas;
			
			listaPlanillas = planillaDao.getListaPlanillaPendPago(patron);
			
			ObservableList<Planilla> datos = FXCollections.observableArrayList();
			datos.setAll(listaPlanillas);

			//llenar los datos en la tabla
			TableColumn<Planilla, String> idColum = new TableColumn<>("Id");
			idColum.setMinWidth(10);
			idColum.setPrefWidth(100);
			idColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Planilla,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Planilla, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdPlanilla()));
				}
			});

			TableColumn<Planilla, String> fechaColum = new TableColumn<>("Fecha");
			fechaColum.setMinWidth(10);
			fechaColum.setPrefWidth(100);
			fechaColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Planilla,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Planilla, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(formateador.format(param.getValue().getFecha())));
				}
			});

			TableColumn<Planilla, String> clienteColum = new TableColumn<>("Cliente");
			clienteColum.setMinWidth(10);
			clienteColum.setPrefWidth(250);
			clienteColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Planilla,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Planilla, String> param) {
					String cliente = "";
					cliente = param.getValue().getCuentaCliente().getCliente().getNombre() + " " + param.getValue().getCuentaCliente().getCliente().getApellido();
					return new SimpleObjectProperty<String>(cliente);
				}
			});
			
			TableColumn<Planilla, String> valorPagoColum = new TableColumn<>("Valor pendiente");
			valorPagoColum.setMinWidth(10);
			valorPagoColum.setPrefWidth(80);
			valorPagoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Planilla, String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Planilla, String> param) {
					double total = 0.0;
					for(Pago pa : param.getValue().getPagos()){
						total = total + pa.getValor();
					}
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getTotalPagar() - total));
				}
			});

			tvDatos.getColumns().addAll(idColum, fechaColum,clienteColum,valorPagoColum);
			tvDatos.setItems(datos);
			
			sumarDatos();
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	public void sumarDatos() {
		try {
			if (tvDatos.getItems().isEmpty()) {
				txtTotalAdeudado.setText("0.00");
			}else {
				double total = 0;
				for(int i=0; i<tvDatos.getItems().size(); i++) {
					Double valorTotal = new Double(tvDatos.getItems().get(i).getTotalPagar());
					total += valorTotal;
					double totalPagado = 0.0;
					for(Planilla pla : tvDatos.getItems()) {
						for(Pago pa : pla.getPagos()){
							totalPagado = totalPagado + pa.getValor();
						}
					}
					txtTotalAdeudado.setText(String.valueOf(Double.valueOf(total - totalPagado)));
				}
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void verReporte() {
		
	}
}