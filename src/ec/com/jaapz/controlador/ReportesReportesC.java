package ec.com.jaapz.controlador;

import ec.com.jaapz.util.ControllerHelper;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class ReportesReportesC {
	@FXML private AnchorPane apUsuariosDia;
	@FXML private AnchorPane apConsolidado;
	@FXML private AnchorPane apUsuariosOrden;
	@FXML private AnchorPane apHistorial;
	@FXML private AnchorPane apRecaudaciones;
	@FXML private AnchorPane apDeudas;
	@FXML private AnchorPane apNomina;
	@FXML private AnchorPane apConsumo;
	@FXML private AnchorPane apMateriales;
	@FXML private AnchorPane apDineroCaja;
	@FXML private AnchorPane apUsuariosCortados;	
	@FXML private AnchorPane apSolicitudesNoAtendidas;
	@FXML private AnchorPane apInconsistenciaMarcaciones;
	@FXML private AnchorPane apClientesConvenio;
	@FXML private AnchorPane apInstaRepa;
	
	ControllerHelper helper = new ControllerHelper();
	public void initialize() {
		try {
			helper.mostrarVentanaContenedor("/reportes/ReporteConsolidado.fxml", apConsolidado);
			helper.mostrarVentanaContenedor("/reportes/UsuariosAlDia.fxml", apUsuariosDia);
			helper.mostrarVentanaContenedor("/reportes/ReporteHistorialUsuario.fxml", apHistorial);
			helper.mostrarVentanaContenedor("/recaudaciones/RecaudacionesDeudaGeneral.fxml", apDeudas);
			helper.mostrarVentanaContenedor("/reportes/ReporteNominas.fxml", apNomina);
			helper.mostrarVentanaContenedor("/reportes/ReporteLecturas.fxml", apConsumo);
			helper.mostrarVentanaContenedor("/recaudaciones/RecaudacionesVerReporte.fxml", apRecaudaciones);
			helper.mostrarVentanaContenedor("/reportes/ReporteMateriales.fxml", apMateriales);
			helper.mostrarVentanaContenedor("/reportes/DineroCaja.fxml", apDineroCaja);
			helper.mostrarVentanaContenedor("/reportes/ReporteInconsistencia.fxml", apInconsistenciaMarcaciones);
			helper.mostrarVentanaContenedor("/reportes/ReporteSolicitudNoAtendida.fxml", apSolicitudesNoAtendidas);
			helper.mostrarVentanaContenedor("/reportes/ClientesConvenio.fxml", apClientesConvenio);
			helper.mostrarVentanaContenedor("/reportes/InstalaReparac.fxml", apInstaRepa);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
