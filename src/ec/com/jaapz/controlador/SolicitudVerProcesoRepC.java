package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ec.com.jaapz.modelo.Cliente;
import ec.com.jaapz.modelo.Instalacion;
import ec.com.jaapz.modelo.InstalacionDAO;
import ec.com.jaapz.modelo.LiquidacionOrden;
import ec.com.jaapz.modelo.LiquidacionOrdenDAO;
import ec.com.jaapz.modelo.Pago;
import ec.com.jaapz.modelo.Planilla;
import ec.com.jaapz.modelo.PlanillaDAO;
import ec.com.jaapz.modelo.SolInspeccionIn;
import ec.com.jaapz.modelo.SolInspeccionInDAO;
import ec.com.jaapz.util.Constantes;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class SolicitudVerProcesoRepC {
	@FXML TableView<Datos> tvDatosInstalacion;
	@FXML TableView<SolInspeccionIn> tvDatosSolicitud;
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
		List<SolInspeccionIn> datosIns = new ArrayList<SolInspeccionIn>();
		cargarDatosSolicitudes(datosIns); 
		
		ObservableList<Datos> datoIns = FXCollections.observableArrayList();
		List<Datos> mostrar = llenarDatosInstalacion();
		datoIns.setAll(mostrar);
		tvDatosInstalacion.setItems(datoIns);
		tvDatosSolicitud.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event arg0) {
				System.out.println(tvDatosSolicitud.getSelectionModel().getSelectedItem().getFechaIngreso());
				cargarResultadosInstalacion(tvDatosSolicitud.getSelectionModel().getSelectedItem().getCliente());
			}
		});
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
	private void cargarDatosSolicitudes(List<SolInspeccionIn> datosMostrar) {
		try {
			ObservableList<SolInspeccionIn> datos = FXCollections.observableArrayList();
			datos.setAll(datosMostrar);
			TableColumn<SolInspeccionIn, String> descripcionColum = new TableColumn<>("No. Solicitud");
			descripcionColum.setMinWidth(10);
			descripcionColum.setPrefWidth(100);
			descripcionColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionIn,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionIn, String> param) {
					return new SimpleObjectProperty<String>(String.valueOf(param.getValue().getIdSolInspeccion()));
				}
			});
			TableColumn<SolInspeccionIn, String> resultadoColum = new TableColumn<>("Fecha");
			resultadoColum.setMinWidth(10);
			resultadoColum.setPrefWidth(200);
			resultadoColum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<SolInspeccionIn,String>, ObservableValue<String>>() {
				@Override
				public ObservableValue<String> call(CellDataFeatures<SolInspeccionIn, String> param) {
					return new SimpleObjectProperty<String>(formateador.format(param.getValue().getFechaIngreso()));
				}
			});
			tvDatosSolicitud.getColumns().addAll(descripcionColum,resultadoColum);
			tvDatosSolicitud.setItems(datos);
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
			objeto.setDescripcion("Solicitud de nuevo medidor");
			lista.add(objeto);
			objeto = new Datos();
			objeto.setId(2);
			objeto.setDescripcion("Asignación de personal de inspección");
			lista.add(objeto);
			objeto = new Datos();
			objeto.setId(3);
			objeto.setDescripcion("Cierre de solicitud de inspección");
			lista.add(objeto);
			objeto = new Datos();
			objeto.setId(4);
			objeto.setDescripcion("Orden de Liquidación");
			lista.add(objeto);
			objeto = new Datos();
			objeto.setId(5);
			objeto.setDescripcion("Cancelar el 60% del valor de instalación");
			lista.add(objeto);
			objeto = new Datos();
			objeto.setId(6);
			objeto.setDescripcion("Asignación del personal de instalación");
			lista.add(objeto);
			objeto = new Datos();
			objeto.setId(7);
			objeto.setDescripcion("Orden de retiro de materiales de bodega");
			lista.add(objeto);
			objeto = new Datos();
			objeto.setId(8);
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
			helper.abrirPantallaModal("/clientes/ClientesListaClientes.fxml","Listado de Clientes", Context.getInstance().getStage());
			if (Context.getInstance().getCliente() != null) {
				limpiarDatos();
				Cliente datoSeleccionado = Context.getInstance().getCliente();
				txtNombres.setText(datoSeleccionado.getNombre());
				txtApellidos.setText(datoSeleccionado.getApellido());
				txtDireccion.setText(datoSeleccionado.getDireccion());
				txtCedula.setText(datoSeleccionado.getCedula());
				cargarSolicitudes(datoSeleccionado);
				Context.getInstance().setCliente(null);
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	private void cargarSolicitudes(Cliente cliente) {
		try{
			ObservableList<SolInspeccionIn> datos = FXCollections.observableArrayList();
			List<SolInspeccionIn> lista = inspeccionDAO.getInspeccionCliente(cliente.getIdCliente());
			datos.setAll(lista);
			tvDatosSolicitud.setItems(datos);
			tvDatosSolicitud.refresh();
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	private void cargarResultadosInstalacion(Cliente cliente) {
		try {
			LiquidacionOrdenDAO ordenDAO = new LiquidacionOrdenDAO();
			PlanillaDAO planillaDAO = new PlanillaDAO();
			InstalacionDAO instalacionDAO = new InstalacionDAO();
			for(Datos dato : tvDatosInstalacion.getItems()) {
				if(dato.getId() == 8) {
					List<Instalacion> lista = instalacionDAO.getBuscarPorSolicitud(tvDatosSolicitud.getSelectionModel().getSelectedItem().getIdSolInspeccion());
					if(lista.size() > 0) {
						if(lista.get(0).getEstadoInstalacion() != null) {
							if(lista.get(0).getEstadoInstalacion().equals(Constantes.EST_INSPECCION_PENDIENTE)) 
								dato.setAnalisis("AÚN NO PROCESADO");
							else
								dato.setAnalisis("REALIZADO");
						}else
							dato.setAnalisis("AÚN NO PROCESADO");
					}
					else
						dato.setAnalisis("AÚN NO PROCESADO");
				}else if(dato.getId() == 7) {
					List<Instalacion> lista = instalacionDAO.getBuscarPorSolicitud(tvDatosSolicitud.getSelectionModel().getSelectedItem().getIdSolInspeccion());
					if(lista.size() > 0) 
						dato.setAnalisis("SE HA REALIZADO EL RETIRO DE MATERIALES");
					else
						dato.setAnalisis("AÚN NO PROCESADO");
				}else if(dato.getId() == 6) {
					List<LiquidacionOrden> lista = ordenDAO.getBuscarPorSolicitud(tvDatosSolicitud.getSelectionModel().getSelectedItem().getIdSolInspeccion());
					if(lista.size() > 0) {
						if(lista.get(0).getUsuarioInstalacion() != null)
							dato.setAnalisis("SE HA REALIZADO LA ASIGNACIÓN");
						else
							dato.setAnalisis("AÚN NO PROCESADO");
					}
					else
						dato.setAnalisis("AÚN NO PROCESADO");
				}else if(dato.getId() == 5) {
					List<Planilla> listaPlanilla = planillaDAO.getPlanillaSolicitud(tvDatosSolicitud.getSelectionModel().getSelectedItem().getIdSolInspeccion());
					double porcentaje = 0.0;
					double valorPagado = 0.0;
					for(Planilla pl : listaPlanilla) {
						if(pl.getIdentInstalacion().equals(Constantes.IDENT_INSTALACION)) {
							porcentaje = pl.getTotalPagar() * 0.6;//60 % del total a pagar
							for(Pago pag : pl.getPagos()) {
								if(pag.getEstado().equals(Constantes.ESTADO_ACTIVO))
									valorPagado = valorPagado + pag.getValor();
							}
						}
						if(valorPagado >= porcentaje)
							dato.setAnalisis("SE HA REALIZADO EL PAGO");
						else
							dato.setAnalisis("AÚN NO PROCESADO");
						
					}
					if(listaPlanilla.size() == 0)
						dato.setAnalisis("AÚN NO PROCESADO");
				}else if(dato.getId() == 4) {
					List<LiquidacionOrden> lista = ordenDAO.getBuscarPorSolicitud(tvDatosSolicitud.getSelectionModel().getSelectedItem().getIdSolInspeccion());
					if(lista.size() > 0)
						dato.setAnalisis("ORDEN DE LIQUIDACIÓN GENERADA");
					else
						dato.setAnalisis("AÚN NO PROCESADO");
					
				}else if(dato.getId() == 3) {
					if(tvDatosSolicitud.getSelectionModel().getSelectedItem().getEstadoInspeccion().equals(Constantes.EST_INSPECCION_PENDIENTE))
						dato.setAnalisis("AÚN NO PROCESADO");
					else {
						String resultado = "REALIZADO";
						if(tvDatosSolicitud.getSelectionModel().getSelectedItem().getFactibilidad() != null) {
							if(tvDatosSolicitud.getSelectionModel().getSelectedItem().getFactibilidad().equals(Constantes.EST_FACTIBLE))
								resultado = resultado + " - FACTIBLE";
							else
								resultado = resultado + " - NO FACTIBLE";	
						}
						dato.setAnalisis(resultado);
					}
				}else if(dato.getId() == 2) {
					if(tvDatosSolicitud.getSelectionModel().getSelectedItem().getIdUsuEncargado() != null)
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
