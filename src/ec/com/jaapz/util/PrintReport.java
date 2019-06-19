package ec.com.jaapz.util;

import java.sql.Connection;
import java.util.Map;

import ec.com.jaapz.modelo.ClaseDAO;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class PrintReport {
	public static final String FORMATO_PDF = "PDF";
	public static final String FORMATO_XLS = "XLS";
	
	private JasperReport report;
	private JasperPrint reportFilled;
	private JasperViewer viewer;
	//ControllerHelper helper = new ControllerHelper();
	
	public void crearReporte(String path, ClaseDAO claseDAO,Map<String, Object> param) {
		try {
			String applicationPath = System.getProperty("user.dir");
			applicationPath = applicationPath + path;
			//helper.mostrarAlertaAdvertencia(applicationPath, Context.getInstance().getStage());
			Connection cn = claseDAO.abreConexion();
			report = (JasperReport) JRLoader.loadObjectFromFile(applicationPath);
			reportFilled = JasperFillManager.fillReport(report, param, cn);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	public void showReport(String titulo) {
		try {
			viewer = new JasperViewer(reportFilled,false);
			viewer.setTitle(titulo);
			viewer.setVisible(true);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	public void exportToPDF(String destino) {
		try {
			//JasperExportManager.exportReportToPdfFile(destino);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	public void imprimirReporte() {
		try {
			JasperPrintManager.printReport(reportFilled, false);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
