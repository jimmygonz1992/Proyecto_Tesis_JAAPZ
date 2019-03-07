package ec.com.jaapz.controlador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ec.com.jaapz.modelo.Anio;
import ec.com.jaapz.modelo.AnioDAO;
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

public class ReporteConsolidadoC {
	@FXML private Button btnImprimir;
	@FXML private ComboBox<Me> cboMes;
	@FXML private ComboBox<Anio> cboAnio;
	@FXML private Button btnVisualizar;
	AnioDAO anioDAO = new AnioDAO();
	MeDAO mesDAO = new MeDAO();
	ControllerHelper helper = new ControllerHelper();
	public void initialize() {
		btnImprimir.setStyle("-fx-cursor: hand;");
		cboMes.setStyle("-fx-cursor: hand;");
		cboAnio.setStyle("-fx-cursor: hand;");
		btnVisualizar.setStyle("-fx-cursor: hand;");
		cargarComboAnio();
		cargarComboMes();
		btnVisualizar.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event arg0) {
				visualizarReporte();
			}
		});
		btnImprimir.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event arg0) {
				imprimirReporte();
			}
		});
	}
	private void visualizarReporte() {
		try {
			if(cboAnio.getSelectionModel().getSelectedItem() == null) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar año", Context.getInstance().getStage());
				return;
			}
			if(cboMes.getSelectionModel().getSelectedItem() == null) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar mes", Context.getInstance().getStage());
				return;
			}
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("MES", cboMes.getSelectionModel().getSelectedItem().getDescripcion());
			param.put("ANIO", cboAnio.getSelectionModel().getSelectedItem().getDescripcion());
			param.put("ID_MES", cboMes.getSelectionModel().getSelectedItem().getIdMes());
			param.put("ID_ANIO", cboAnio.getSelectionModel().getSelectedItem().getIdAnio());
			PrintReport reporte = new PrintReport();
			reporte.crearReporte("/recursos/informes/consolidadoConsumo.jasper", anioDAO, param);
			reporte.showReport("Consolidado de consumo mensual");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void imprimirReporte() {
		try {
			if(cboAnio.getSelectionModel().getSelectedItem() == null) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar año", Context.getInstance().getStage());
				return;
			}
			if(cboMes.getSelectionModel().getSelectedItem() == null) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar mes", Context.getInstance().getStage());
				return;
			}
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
