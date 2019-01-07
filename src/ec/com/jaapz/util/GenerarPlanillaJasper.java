package ec.com.jaapz.util;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ec.com.jaapz.modelo.Pago;
import ec.com.jaapz.modelo.Planilla;
import ec.com.jaapz.modelo.PlanillaDAO;

public class GenerarPlanillaJasper {
	PlanillaDAO planillaDAO = new PlanillaDAO();
	public boolean crearPlanillaCliente(Planilla planilla) {
		try {
			boolean bandera = true;
			SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
			
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("ID_CUENTA", planilla.getCuentaCliente().getIdCuenta());
			param.put("BARRIO", planilla.getCuentaCliente().getBarrio().getNombre());
			param.put("FECHA", formateador.format(planilla.getFecha()));
			param.put("LECTURA_ACTUAL", String.valueOf(planilla.getLecturaActual()));
			param.put("LECTURA_ANTERIOR", String.valueOf(planilla.getLecturaAnterior()));
			param.put("CONSUMO_MENSUAL", String.valueOf(planilla.getConsumo()));
			param.put("SUBTOTAL_MENSUAL", String.valueOf("$ " + (planilla.getConsumo() * planilla.getCuentaCliente().getCategoria().getValorM3())));
			//calcular la deuda anterior
	        double deudaAnterior = 0,deudaCero = 0;
	        List<Planilla> planillasGeneradas = planillaDAO.getPlanillaCuenta(planilla.getCuentaCliente().getIdCuenta());
	        for(Planilla pla : planillasGeneradas) {
	        	if(pla.getCancelado().equals(Constantes.EST_FAC_PENDIENTE)) {// no pregunto por los activos xq en la consulta ya esta preguntando por los activos
	        		if(pla.getPagos().size() == 0)
	        			deudaAnterior = deudaAnterior + pla.getTotalPagar();
	        		else {
	        			double pagos = 0;
	        			for(Pago pa : pla.getPagos()) {
	        				if(pa.getEstado().equals(Constantes.ESTADO_ACTIVO))
	        					pagos = pagos + pa.getValor();
	        			}
	        			deudaAnterior = deudaAnterior + (pla.getTotalPagar() - pagos);
	        		}
	        	}
	        }
	        if(deudaAnterior == 0)
	        	deudaCero = planilla.getTotalPagar();
	        else
	        	deudaCero = deudaAnterior;
	        
			param.put("DEUDA_ANTERIOR", String.valueOf("$ " + (deudaCero - planilla.getTotalPagar())));
			
			double otros = 0;
	        for(int i = 1 ; i < planilla.getPlanillaDetalles().size() ; i ++)
	        	otros = otros + planilla.getPlanillaDetalles().get(i).getSubtotal();
			param.put("OTROS", String.valueOf("$ " + otros));
			param.put("TOTAL_PAGAR", String.valueOf("$ " + deudaAnterior));
			param.put("CANCELAR_ANTES", "CANCELAR ANTES DEL 15 DEL MES EN CURSO");
			param.put("TOTAL_LETRAS", planilla.getTotalLetras());
			String detalleOtros = "";
			if(planilla.getPlanillaDetalles().size() > 1)
				detalleOtros = planilla.getPlanillaDetalles().get(1).getDescripcion();
			param.put("DETALLE_OTROS", detalleOtros);
			String numeroCuenta = "0000000000" + String.valueOf(planilla.getCuentaCliente().getIdCuenta());
			param.put("NO_FACTURA", numeroCuenta.substring(numeroCuenta.length() - 5, numeroCuenta.length()));
			List<Planilla> listaPlanillas = planillaDAO.getPlanillaAtrasada(planilla.getCuentaCliente().getIdCuenta(),planilla.getIdPlanilla());
			int atrasadas = 0;
			for(Planilla pl : listaPlanillas) {
				if(pl.getCancelado().equals(Constantes.EST_FAC_PENDIENTE))
					atrasadas = atrasadas + 1;
			}
			param.put("ATRASO", String.valueOf(atrasadas));
			PrintReport printReport = new PrintReport();
			printReport.crearReporte("/recursos/informes/planilla_consumo.jasper", planillaDAO, param);
			printReport.imprimirReporte();
			return bandera;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
}
