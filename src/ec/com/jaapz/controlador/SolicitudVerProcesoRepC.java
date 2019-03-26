package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ec.com.jaapz.modelo.Cliente;
import ec.com.jaapz.modelo.Reparacion;
import ec.com.jaapz.modelo.ReparacionDAO;
import ec.com.jaapz.modelo.SolInspeccionInDAO;
import ec.com.jaapz.modelo.SolInspeccionRep;
import ec.com.jaapz.util.Constantes;
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

public class SolicitudVerProcesoRepC {
	@FXML TableView<Datos> tvDatosInstalacion;
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	
	@FXML Button btnBuscar;
	@FXML TextField txtNombres;
	@FXML TextField txtApellidos;
	@FXML TextField txtDireccion;
	@FXML TextField txtCedula;
	
	ControllerHelper helper = new ControllerHelper();
	SolInspeccionInDAO inspeccionDAO = new SolInspeccionInDAO();
	public void initialize() {
		limpiarDatos();
		btnBuscar.setStyle("-fx-cursor: hand;");
		List<Datos> datos = new ArrayList<Datos>();
		cargarDatosInstalacion(datos);
		
		ObservableList<Datos> datoIns = FXCollections.observableArrayList();
		List<Datos> mostrar = llenarDatosInstalacion();
		datoIns.setAll(mostrar);
		tvDatosInstalacion.setItems(datoIns);

	
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
	
	private List<Datos> llenarDatosInstalacion() {
		try {
			List<Datos> lista = new ArrayList<Datos>();
			Datos objeto;
			objeto = new Datos();
			objeto.setId(1);
			objeto.setDescripcion("Solicitud de reparación");
			lista.add(objeto);
			objeto = new Datos();
			objeto.setId(2);
			objeto.setDescripcion("Asignación de personal de reparación");
			lista.add(objeto);
			objeto = new Datos();
			objeto.setId(3);
			objeto.setDescripcion("Cierre de inspección de reparación");
			lista.add(objeto);
			objeto = new Datos();
			objeto.setId(4);
			objeto.setDescripcion("Orden de reparación");
			lista.add(objeto);
			objeto = new Datos();
			objeto.setId(5);
			objeto.setDescripcion("Asignación de personal de reparación");
			lista.add(objeto);
			objeto = new Datos();
			objeto.setId(6);
			objeto.setDescripcion("Salida de materiales para reparación");
			lista.add(objeto);
			objeto = new Datos();
			objeto.setId(7);
			objeto.setDescripcion("Cierre de reparación");
			lista.add(objeto);
			return lista;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}
	}

	public void buscarCliente(){
		try{
			helper.abrirPantallaModal("/solicitudes/SolicitudesNoAtendidasRep.fxml","Listado de solicitudes no atendidas", Context.getInstance().getStage());
			if (Context.getInstance().getReparacion() != null) {
				limpiarDatos();
				Cliente datoSeleccionado = Context.getInstance().getReparacion().getCuentaCliente().getCliente();
				txtNombres.setText(datoSeleccionado.getNombre());
				txtApellidos.setText(datoSeleccionado.getApellido());
				txtDireccion.setText(datoSeleccionado.getDireccion());
				txtCedula.setText(datoSeleccionado.getCedula());
				cargarResultadosInstalacion(Context.getInstance().getReparacion());
				Context.getInstance().setReparacion(null);
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}

	private void cargarResultadosInstalacion(SolInspeccionRep reparacion) {
		try {
			ReparacionDAO reparacionDAO = new ReparacionDAO();
			for(Datos dato : tvDatosInstalacion.getItems()) {
				if(dato.getId() == 7) {
					List<Reparacion> lista = reparacionDAO.getListaReparacionSolicitud(reparacion.getIdSolicitudRep());
					if(lista.size() > 0) {
						if(lista.get(0).getEstadoReparacion().equals(Constantes.EST_APERTURA_REALIZADO))
							dato.setAnalisis("SE HA REALIZADO LA REPARACIÓN");
						else
							dato.setAnalisis("AÚN NO PROCESADO");
					}
					else
						dato.setAnalisis("AÚN NO PROCESADO");
				}else if(dato.getId() == 6) {
					List<Reparacion> lista = reparacionDAO.getListaReparacionSolicitud(reparacion.getIdSolicitudRep());
					if(lista.size() > 0) {
						if(lista.get(0).getEstadoEntrega().equals(Constantes.EST_APERTURA_REALIZADO))
							dato.setAnalisis("SE HA REALIZADO LA SALIDA DE MATERIALES");
						else
							dato.setAnalisis("AÚN NO PROCESADO");
					}
					else
						dato.setAnalisis("AÚN NO PROCESADO");
				}else if(dato.getId() == 5) {
					List<Reparacion> lista = reparacionDAO.getListaReparacionSolicitud(reparacion.getIdSolicitudRep());
					if(lista.size() > 0) {
						if(lista.get(0).getUsuarioReparacion() != null)
							dato.setAnalisis("SE HA REALIZADO LA ASIGNACIÓN DEL USUARIO");
						else
							dato.setAnalisis("AÚN NO PROCESADO");
					}
					else
						dato.setAnalisis("AÚN NO PROCESADO");
				}else if(dato.getId() == 4) {
					List<Reparacion> lista = reparacionDAO.getListaReparacionSolicitud(reparacion.getIdSolicitudRep());
					if(lista.size() > 0)
						dato.setAnalisis("ORDEN DE REPARACIÓN GENERADA");
					else
						dato.setAnalisis("AÚN NO PROCESADO");
					
				}else if(dato.getId() == 3) {
					if(reparacion.getEstadoInspecRep().equals(Constantes.EST_INSPECCION_PENDIENTE))
						dato.setAnalisis("AÚN NO PROCESADO");
					else 
						dato.setAnalisis("REALIZADO");
				}else if(dato.getId() == 2) {
					if(reparacion.getIdUsuEncargado() != null)
						dato.setAnalisis("REALIZADO");
					else
						dato.setAnalisis("AÚN NO PROCESADO");
				}else if(dato.getId() == 1) {
					dato.setAnalisis("REALIZADO");
				}
			}
			tvDatosInstalacion.refresh();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void limpiarDatos() {
		tvDatosInstalacion.getItems().clear();
		ObservableList<Datos> dato = FXCollections.observableArrayList();
		List<Datos> mostrar = llenarDatosInstalacion();
		dato.setAll(mostrar);
		tvDatosInstalacion.setItems(dato);
		tvDatosInstalacion.refresh();
		
		txtApellidos.setText("");
		txtNombres.setText("");
		txtDireccion.setText("");	
		txtCedula.setText("");
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
