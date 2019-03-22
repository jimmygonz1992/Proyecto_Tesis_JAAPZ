package ec.com.jaapz.controlador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ec.com.jaapz.modelo.Anio;
import ec.com.jaapz.modelo.AnioDAO;
import ec.com.jaapz.modelo.AperturaLectura;
import ec.com.jaapz.modelo.AperturaLecturaDAO;
import ec.com.jaapz.modelo.Me;
import ec.com.jaapz.modelo.MeDAO;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import ec.com.jaapz.util.PrintReport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class ReporteLecturasC {
	@FXML private Button btnImprimir;
	@FXML private ComboBox<Me> cboMes;
	@FXML private ComboBox<Anio> cboAnio;
	@FXML private Button btnVisualizar;
	
	AnioDAO anioDAO = new AnioDAO();
	MeDAO mesDAO = new MeDAO();
	AperturaLecturaDAO aperturaDAO = new AperturaLecturaDAO();
	ControllerHelper helper = new ControllerHelper();
	public void initialize() {
		try {
			cargarComboAnio();
			cargarComboMes();
			btnVisualizar.setOnMouseClicked(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					visualizar();
				}
			});
			btnImprimir.setOnMouseClicked(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					
				}
			});
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void visualizar() {
		try {
			List<AperturaLectura> listaApertura = aperturaDAO.getAperturaAnioMes(cboAnio.getSelectionModel().getSelectedItem().getIdAnio(), cboMes.getSelectionModel().getSelectedItem().getIdMes());
			if(listaApertura.size() <= 0) {
				helper.mostrarAlertaAdvertencia("No se ha registrado apertura en la fecha seleccionada", Context.getInstance().getStage());
				return;
			}
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("ANIO", cboAnio.getSelectionModel().getSelectedItem().getDescripcion());
			param.put("MES", cboMes.getSelectionModel().getSelectedItem().getDescripcion());
			param.put("ID_ANIO",cboAnio.getSelectionModel().getSelectedItem().getIdAnio());
			param.put("ID_MES", cboMes.getSelectionModel().getSelectedItem().getIdMes());
			PrintReport reporte = new PrintReport();
			reporte.crearReporte("/recursos/informes/reporteLecturas.jasper", anioDAO, param);
			reporte.showReport("Nómina de clientes");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void cargarComboAnio() {
		try {
			ObservableList<Anio> listaAnios = FXCollections.observableArrayList();
			List<Anio> datAn = anioDAO.getListaAnios();
			listaAnios.setAll(datAn);
			cboAnio.setItems(listaAnios);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void cargarComboMes() {
		try {
			cboMes.getItems().clear();
			ObservableList<Me> listaMeses = FXCollections.observableArrayList();
			List<Me> datMes = mesDAO.getListaMeses();
			listaMeses.setAll(datMes);
			cboMes.setItems(listaMeses);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
