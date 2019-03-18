package ec.com.jaapz.controlador;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ec.com.jaapz.modelo.AperturaLectura;
import ec.com.jaapz.modelo.Empresa;
import ec.com.jaapz.modelo.EmpresaDAO;
import ec.com.jaapz.util.Context;
import ec.com.jaapz.util.ControllerHelper;
import ec.com.jaapz.util.PrintReport;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ReporteInconsistenciaC {
	@FXML private TextField txtMesAperRes;
	@FXML private Button btnBuscarApertura;
	@FXML private Button btnVisualizar;
	@FXML private Button btnImprimir;
	
	@FXML private TextField txtAnioAperRes;
	@FXML private TextField txtFechAperRes;
	@FXML private TextField txtEstadoApeAsi;
	AperturaLectura aperturaSeleccionada = new AperturaLectura();
	EmpresaDAO empresaDAO = new EmpresaDAO();
	
	ControllerHelper helper = new ControllerHelper();
	public void initialize() {
		btnBuscarApertura.setStyle("-fx-cursor: hand;");
		btnVisualizar.setStyle("-fx-cursor: hand;");
		btnImprimir.setStyle("-fx-cursor: hand;");
		
	}
	
	public void buscarApertura() {
		try {
			helper.abrirPantallaModal("/lecturas/LecturasListaApertura.fxml","Aperturas Realizadas", Context.getInstance().getStage());
			if(Context.getInstance().getApertura() != null) {
				aperturaSeleccionada = Context.getInstance().getApertura();
				Context.getInstance().setApertura(null);
				recuperarAperturaSeleccionada(aperturaSeleccionada);//recupera los datos de la asignacion seleccionada
			}else {
				txtAnioAperRes.setText("");
				txtMesAperRes.setText("");
				txtFechAperRes.setText("");
				txtEstadoApeAsi.setText("");
				Context.getInstance().setApertura(null);
				aperturaSeleccionada = null;
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void recuperarAperturaSeleccionada(AperturaLectura aperturaSeleccionada) {
		try {
			SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yy");
			txtAnioAperRes.setText(String.valueOf(aperturaSeleccionada.getAnio().getDescripcion()));
			txtMesAperRes.setText(String.valueOf(aperturaSeleccionada.getMe().getDescripcion()));
			txtFechAperRes.setText(formateador.format(aperturaSeleccionada.getFecha()));
			txtEstadoApeAsi.setText(aperturaSeleccionada.getEstadoApertura());
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void imprimirReporte() {
		try {
			
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public void visualizarReporte() {
		try {
			if(aperturaSeleccionada == null) {
				helper.mostrarAlertaAdvertencia("Debe seleccionar una apertura", Context.getInstance().getStage());
				return;
			}
			Integer cantidad = 0;
			List<Empresa> listaEmpresa = empresaDAO.getEmpresa();
			if(listaEmpresa.size() > 0) {
				if(listaEmpresa.get(0).getInconsistencia() != null)
					cantidad = listaEmpresa.get(0).getInconsistencia();
			}
			SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", new Locale("MX"));
			Date fechaDate = new Date();
			String fechaSistema = formateador.format(fechaDate);
			String fecha = dateFormatter("yyyy-MM-dd hh:mm:ss","d 'de' MMMM 'del' yyyy", fechaSistema);
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("FECHA", fecha);
			param.put("CANTIDAD", cantidad);
			param.put("ID_APERTURA", aperturaSeleccionada.getIdApertura());
			PrintReport reporte = new PrintReport();
			reporte.crearReporte("/recursos/informes/inconsistenciaMarcaciones.jasper", empresaDAO, param);
			reporte.showReport("Inconsistencia de marcaciones");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public static final Locale LOCALE_MX = new Locale("es", "MX");
	public static String dateFormatter(String inputFormat, String outputFormat, String inputDate){
	      //Define formato default de entrada.   
	      String input = inputFormat.isEmpty()? "yyyy-MM-dd hh:mm:ss" : inputFormat; 
	      //Define formato default de salida.
	      String output = outputFormat.isEmpty()? "d 'de' MMMM 'del' yyyy" : outputFormat;
	    String outputDate = inputDate;
	    try {
	        outputDate = new SimpleDateFormat(output, LOCALE_MX).format(new SimpleDateFormat(input, LOCALE_MX).parse(inputDate));
	    } catch (Exception e) {
	        System.out.println("dateFormatter(): " + e.getMessage());           
	    }
	    return outputDate;
	}
}
