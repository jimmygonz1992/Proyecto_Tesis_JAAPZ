package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ec.com.jaapz.modelo.CuentaCliente;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

public class SolicitudVerProcesoC {
	@FXML TableView<Datos> tvDatosInstalacion;
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	
	@FXML TableView<Datos> tvDatosReparacion;
	@FXML Button btnBuscar;
	@FXML TextField txtCliente;
	@FXML TextField txtDireccion;
	@FXML TextField txtCedula;
	@FXML TextField txtIdCuenta;
	@FXML TextField txtCategoria;
	
	private CuentaCliente cuentaSeleccionada;
	ControllerHelper helper = new ControllerHelper();
	
	public void initialize() {
		limpiarDatos();
		btnBuscar.setStyle("-fx-cursor: hand;");
		List<Datos> datos = new ArrayList<Datos>();
		cargarDatosInstalacion(datos);
		cargarDatosReparacion(datos);
		ObservableList<Datos> datoIns = FXCollections.observableArrayList();
		List<Datos> mostrar = llenarDatosInstalacion();
		datoIns.setAll(mostrar);
		tvDatosInstalacion.setItems(datoIns);
		
		ObservableList<Datos> datoRep = FXCollections.observableArrayList();
		List<Datos> mostrarRep = llenarDatosReparacion();
		datoRep.setAll(mostrarRep);
		tvDatosReparacion.setItems(datoRep);
		tvDatosReparacion.refresh();
	}
	
	@SuppressWarnings("unchecked")
	private void cargarDatosInstalacion(List<Datos> datosMostrar) {
		try {
			ObservableList<Datos> datos = FXCollections.observableArrayList();
			datos.setAll(datosMostrar);
			TableColumn<Datos, String> descripcionColum = new TableColumn<>("Descripción");
			descripcionColum.setMinWidth(10);
			descripcionColum.setPrefWidth(500);
			descripcionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Datos,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Datos, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getDescripcion());
				}
			});
			TableColumn<Datos, String> resultadoColum = new TableColumn<>("Resultado");
			resultadoColum.setMinWidth(10);
			resultadoColum.setPrefWidth(200);
			resultadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Datos,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Datos, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getAnalisis());
				}
			});
			tvDatosInstalacion.getColumns().addAll(descripcionColum,resultadoColum);
			tvDatosInstalacion.setItems(datos);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	private void cargarDatosReparacion(List<Datos> datosMostrar) {
		try {
			ObservableList<Datos> datos = FXCollections.observableArrayList();
			datos.setAll(datosMostrar);
			TableColumn<Datos, String> descripcionColum = new TableColumn<>("Descripción");
			descripcionColum.setMinWidth(10);
			descripcionColum.setPrefWidth(500);
			descripcionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Datos,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Datos, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getDescripcion());
				}
			});
			TableColumn<Datos, String> resultadoColum = new TableColumn<>("Resultado");
			resultadoColum.setMinWidth(10);
			resultadoColum.setPrefWidth(200);
			resultadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Datos,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<Datos, String> param) {
					return new SimpleObjectProperty<String>(param.getValue().getAnalisis());
				}
			});
			tvDatosReparacion.getColumns().addAll(descripcionColum,resultadoColum);
			tvDatosReparacion.setItems(datos);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private List<Datos> llenarDatosInstalacion() {
		try {
			List<Datos> lista = new ArrayList<Datos>();
			Datos objeto;
			objeto = new Datos();
			objeto.setDescripcion("Solicitud de nuevo medidor");
			lista.add(objeto);
			objeto = new Datos();
			objeto.setDescripcion("Asignación de personal de inspección");
			lista.add(objeto);
			objeto = new Datos();
			objeto.setDescripcion("Cierre de solicitud de inspección");
			lista.add(objeto);
			objeto = new Datos();
			objeto.setDescripcion("Cancelar el 60% del valor de instalación");
			lista.add(objeto);
			objeto = new Datos();
			objeto.setDescripcion("Asignación del personal de instalación");
			lista.add(objeto);
			objeto = new Datos();
			objeto.setDescripcion("Retiro de materiales de bodega para instalación");
			lista.add(objeto);
			objeto = new Datos();
			objeto.setDescripcion("Cierre de instalación");
			lista.add(objeto);
			return lista;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}
	private List<Datos> llenarDatosReparacion() {
		try {
			List<Datos> lista = new ArrayList<Datos>();
			Datos objeto;
			objeto = new Datos();
			objeto.setDescripcion("Solicitud de Reparación");
			lista.add(objeto);
			objeto = new Datos();
			objeto.setDescripcion("Asignación de personal de inspección");
			lista.add(objeto);
			objeto = new Datos();
			objeto.setDescripcion("Cierre de solicitud de inspección");
			lista.add(objeto);
			objeto = new Datos();
			objeto.setDescripcion("Cancelar el 60% del valor de instalación");
			lista.add(objeto);
			objeto = new Datos();
			objeto.setDescripcion("Asignación del personal de instalación");
			lista.add(objeto);
			objeto = new Datos();
			objeto.setDescripcion("Retiro de materiales de bodega para instalación");
			lista.add(objeto);
			objeto = new Datos();
			objeto.setDescripcion("Cierre de instalación");
			lista.add(objeto);
			return lista;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}

	public void buscarCliente(){
		try{
			helper.abrirPantallaModal("/reparaciones/ReparacionesListadoCuentas.fxml","Listado de Clientes", Context.getInstance().getStage());
			if (Context.getInstance().getCuentaCliente() != null) {
				limpiarDatos();
				CuentaCliente datoSeleccionado = Context.getInstance().getCuentaCliente();
				cuentaSeleccionada = datoSeleccionado;
				txtCliente.setText(cuentaSeleccionada.getCliente().getNombre() + " " + cuentaSeleccionada.getCliente().getApellido());
				txtDireccion.setText(cuentaSeleccionada.getDireccion());
				txtCedula.setText(cuentaSeleccionada.getCliente().getCedula());
				txtIdCuenta.setText(String.valueOf(cuentaSeleccionada.getIdCuenta()));
				txtCategoria.setText(cuentaSeleccionada.getCategoria().getDescripcion());
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	private void limpiarDatos() {
		tvDatosInstalacion.getItems().clear();
		tvDatosReparacion.getItems().clear();
		ObservableList<Datos> dato = FXCollections.observableArrayList();
		List<Datos> mostrar = llenarDatosInstalacion();
		dato.setAll(mostrar);
		tvDatosInstalacion.setItems(dato);
		tvDatosInstalacion.refresh();
		txtCliente.setText("");
		txtDireccion.setText("");
		txtCedula.setText("");
		txtIdCuenta.setText("");
		txtCategoria.setText("");
	}
	public class Datos {
		private Integer id;
		private String descripcion;
		private String analisis;
		public String getDescripcion() {
			return descripcion;
		}
		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}
		public String getAnalisis() {
			return analisis;
		}
		public void setAnalisis(String analisis) {
			this.analisis = analisis;
		}
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		
	}
}