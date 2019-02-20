package ec.com.jaapz.util;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

import ec.com.jaapz.modelo.Factura;
import ec.com.jaapz.modelo.FacturaDAO;
import ec.com.jaapz.modelo.FacturaDetalle;
import ec.com.jaapz.modelo.Pago;
import ec.com.jaapz.modelo.Planilla;
import ec.com.jaapz.modelo.PlanillaDAO;
import ec.com.jaapz.modelo.PlanillaDetalle;

public class GenerarPlanillasPDF {
	PlanillaDAO planillaDAO = new PlanillaDAO();
	FacturaDAO facturaDAO = new FacturaDAO();
	
	public String crearEstadoCuenta(Planilla planilla) {
		try {
			String carpetaPDF = "";
			carpetaPDF = "C:\\PlanillasPDF";
			File folder = new File(carpetaPDF);
			if(!folder.exists())
				folder.mkdir();
			
			String bandera = "";
			//-------------------------------------------------------------------------------------------------
			Font fuenteTituloCuadro = new Font();
			fuenteTituloCuadro.setSize(10);
			fuenteTituloCuadro.setStyle(Font.BOLD);
	        
			Font fuenteContenido = new Font();
			fuenteContenido.setSize(9);
			fuenteContenido.setFamily("Calibri");
		
			//generar archivos pdf
			SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");
			String destino = carpetaPDF + "\\" + planilla.getCuentaCliente().getCliente().getCedula() + "-" + planilla.getCuentaCliente().getIdCuenta() + "-"
					+ "" + planilla.getAperturaLectura().getMe().getDescripcion() + "-" + format.format(planilla.getFecha()) + ".pdf";
			Document document = new Document(PageSize.LETTER , 36, 36, 36, 36);
			document.addTitle("ESTADO DE CUENTA");
			com.itextpdf.text.Image imagen = com.itextpdf.text.Image.getInstance("recursos/img/encabezado.png");
			PdfWriter.getInstance(document, new FileOutputStream(destino));
			document.open();
	        imagen.setAlignment(Element.ALIGN_CENTER);
	        imagen.scaleAbsoluteWidth(400f);
	        imagen.scaleAbsoluteHeight(50f);
	        document.add(imagen);
	        //separador de linea
	        LineSeparator objectName = new LineSeparator();    
	        document.add(objectName); 
	        
	        //el titulo del documento
	        Font fuenteTitulo = new Font();
	        fuenteTitulo.setSize(12);
	        fuenteTitulo.setStyle(Font.BOLD);
	        Paragraph titulo = new Paragraph("ESTADO DE CUENTA",fuenteTitulo);
	        titulo.setAlignment(Element.ALIGN_CENTER);
	        document.add(titulo);
	        
	        //primera parte que son los datos del cliente
	        document.add(new Paragraph(" "));
	        Font fuenteSubTitulo = new Font();
	        fuenteSubTitulo.setSize(11);
	        fuenteSubTitulo.setStyle(Font.BOLD | Font.UNDERLINE);
	        Paragraph subtituloDatos = new Paragraph("DATOS DEL CLIENTE",fuenteSubTitulo);
	        subtituloDatos.setAlignment(Element.ALIGN_LEFT);
	        document.add(subtituloDatos);
	        
	        //a continuacion son el contenido que son los datos del cliente
	        SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
	        Chunk tabCliente = new Chunk(new VerticalPositionMark(), 150f, false);
	        
	        Date fecha = new Date();
	        String numeroCuenta = "0000000000" + String.valueOf(planilla.getCuentaCliente().getIdCuenta());
	        Paragraph medidorCliente = new Paragraph();
	        medidorCliente.add("COD. MEDIDOR:");medidorCliente.add(tabCliente);
	        medidorCliente.add(planilla.getCuentaCliente().getMedidor().getCodigo() +  "\n");
	        Font fuenteCodigoMedidor = new Font();
	        fuenteCodigoMedidor.setSize(10);
	        fuenteCodigoMedidor.setStyle(Font.BOLD);
	        medidorCliente.setFont(fuenteCodigoMedidor);
	        document.add(medidorCliente);
	        
	        
	        Paragraph datosCliente = new Paragraph();
	        datosCliente.setFont(fuenteContenido);
	        
	        datosCliente.add("NO. DE CUENTA:");datosCliente.add(tabCliente);
	        datosCliente.add(numeroCuenta.substring(numeroCuenta.length() - 5, numeroCuenta.length()) + "\n");	        
	        datosCliente.add("APELLIDOS Y NOMBRES:");datosCliente.add(tabCliente);
	        datosCliente.add(planilla.getCuentaCliente().getCliente().getApellido() + " " + planilla.getCuentaCliente().getCliente().getNombre() +  "\n");
	        datosCliente.add("BARRIO:");datosCliente.add(tabCliente);
	        if(planilla.getCuentaCliente().getBarrio() != null)
	        	datosCliente.add(planilla.getCuentaCliente().getBarrio().getNombre() +  "\n");
	        else
	        	datosCliente.add("" +  "\n");
	        datosCliente.add("DIRECCIÓN:");datosCliente.add(tabCliente);
	        datosCliente.add(planilla.getCuentaCliente().getDireccion() +  "\n");
	        datosCliente.add("FECHA:");datosCliente.add(tabCliente);
	        datosCliente.add(formateador.format(fecha) +  "\n");
	        
	        document.add(datosCliente);
	        
	        //datos de la factura actual
	        document.add(new Paragraph(" "));
	        Paragraph subtituloFactura = new Paragraph("DATOS DE FACTURA",fuenteSubTitulo);
	        subtituloFactura.setAlignment(Element.ALIGN_LEFT);
	        document.add(subtituloFactura);
	        //aqui van los datos de la factura
	        Paragraph datosFactura = new Paragraph();
	        datosFactura.setFont(fuenteContenido);
	        datosFactura.add("LECTURA ACTUAL: " + planilla.getLecturaActual()); datosFactura.add(new Chunk(new VerticalPositionMark(), 200f, false));
	        datosFactura.add("LECTURA ANTERIOR: " + planilla.getLecturaAnterior()); datosFactura.add(new Chunk(new VerticalPositionMark(), 400f, false));
	        datosFactura.add("CONSUMO: " + planilla.getConsumo() + "\n");
	        datosFactura.add("MES FACTURADO: " + planilla.getAperturaLectura().getMe().getDescripcion()); datosFactura.add(new Chunk(new VerticalPositionMark(), 200f, false));
	        datosFactura.add("TOTAL MENSUAL: $" + planilla.getPlanillaDetalles().get(0).getSubtotal() + "\n");
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
	        datosFactura.add("DEUDA ANTERIOR: " + (deudaCero - planilla.getTotalPagar())); datosFactura.add(new Chunk(new VerticalPositionMark(), 200f, false));
	        double otros = 0;
	        for(int i = 1 ; i < planilla.getPlanillaDetalles().size() ; i ++)
	        	otros = otros + planilla.getPlanillaDetalles().get(i).getSubtotal();
	        datosFactura.add("OTROS: " + otros); datosFactura.add(new Chunk(new VerticalPositionMark(), 400f, false));
	        datosFactura.add("TOTAL PAGAR: " + (deudaAnterior)); //el deuda anterior es el total a pagar.. xq esta acumulando todas las deudas. incluso el actual
	        document.add(datosFactura);
	        
	        //seguiente es el cuadro con el detalle de la factura del mes
	        Paragraph subtituloFacturaDetalle = new Paragraph("DETALLES DEL MES",fuenteSubTitulo);
	        subtituloFacturaDetalle.setAlignment(Element.ALIGN_LEFT);
	        document.add(subtituloFacturaDetalle);
	        document.add(new Paragraph(" "));
	        //se grafica el cuadro
	        PdfPTable tablaDetalleMes = new PdfPTable(3);
	        tablaDetalleMes.setWidthPercentage(100);
	        float[] medidaCeldas = {20.00f,4.30f,4.30f};
	        tablaDetalleMes.setWidths(medidaCeldas);
	        PdfPCell celdaDescripcion = new PdfPCell(new Paragraph("DESCRIPCIÓN",fuenteTituloCuadro));
	        celdaDescripcion.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	        PdfPCell celdaCantidad = new PdfPCell(new Paragraph("CANTIDAD",fuenteTituloCuadro));
	        celdaCantidad.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	        PdfPCell celdaSubtotal = new PdfPCell(new Paragraph("SUBTOTAL",fuenteTituloCuadro));
	        celdaSubtotal.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	        tablaDetalleMes.addCell(celdaDescripcion);
	        tablaDetalleMes.addCell(celdaCantidad);
	        tablaDetalleMes.addCell(celdaSubtotal);
	        //falta hacer el ciclo para verificar los detalles de la factura
	        for(PlanillaDetalle pla : planilla.getPlanillaDetalles()) {
	        	if(pla.getEstado().equals(Constantes.ESTADO_ACTIVO)) {
	        		tablaDetalleMes.addCell(pla.getDescripcion());
	        		tablaDetalleMes.addCell(String.valueOf(pla.getCantidad()));
	    	        tablaDetalleMes.addCell(String.valueOf(pla.getSubtotal()));
	        	}
	        }
	        
	        document.add(tablaDetalleMes);
	        document.add(new Paragraph("\nCANCELAR ANTES DEL 15 DEL MES EN CURSO\n\n",fuenteContenido));
	        
	        //para los movimientos que se ha realizado
	        Paragraph subtituloMovimientos = new Paragraph("MOVIMIENTOS",fuenteSubTitulo);
	        subtituloMovimientos.setAlignment(Element.ALIGN_LEFT);
	        document.add(subtituloMovimientos);
	        document.add(new Paragraph(" "));
	        //tabla de movimientos
	        PdfPTable tablaMovimiento = new PdfPTable(3);
	        tablaMovimiento.setWidthPercentage(100);
	        float[] medidaCeldasMovimiento = {4.30f,20.00f,4.30f};
	        tablaMovimiento.setWidths(medidaCeldasMovimiento);
	        PdfPCell celdaFecha = new PdfPCell(new Paragraph("FECHA",fuenteTituloCuadro));
	        celdaFecha.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	        PdfPCell celdaDesc = new PdfPCell(new Paragraph("DESCRIPCIÓN",fuenteTituloCuadro));
	        celdaDesc.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	        PdfPCell celdaTotal = new PdfPCell(new Paragraph("TOTAL",fuenteTituloCuadro));
	        celdaTotal.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	        tablaMovimiento.addCell(celdaFecha);
	        tablaMovimiento.addCell(celdaDesc);
	        tablaMovimiento.addCell(celdaTotal);
	        String descripcion = "";
	        //aqui va el detalle
	        List<Factura> listaFactura = facturaDAO.getListaFacturasCuenta(planilla.getCuentaCliente().getIdCuenta());
	        for(Factura fac : listaFactura) {
	        	for(FacturaDetalle facDet : fac.getFacturaDetalles()) {
	        		PdfPCell fechaCelda = new PdfPCell(new Paragraph(formateador.format(fac.getFecha()),fuenteContenido));
	        		tablaMovimiento.addCell(fechaCelda);
	        		if(facDet.getPlanilla().getIdentInstalacion() != null)
	        			descripcion = "PAGO EFECTUADO POR INSTALACION DE NUEVO MEDIDOR MEDIANTE LA PLANILLA No. " + fac.getNumFactura();
	        		else
	        			descripcion = "PAGO EFECTUADO POR CONSUMO DEL MES DE " + facDet.getPlanilla().getAperturaLectura().getMe().getDescripcion().toUpperCase() + ""
	        					+ " MEDIANTE LA PLANILLA No. " + fac.getNumFactura();
	        		PdfPCell descripcionCelda = new PdfPCell(new Paragraph(descripcion,fuenteContenido));
	    	        tablaMovimiento.addCell(descripcionCelda);
	    	        PdfPCell totalCelda = new PdfPCell(new Paragraph(String.valueOf(facDet.getSubtotal()),fuenteContenido));
	    	        tablaMovimiento.addCell(totalCelda);
	        	}
	        }
	        document.add(tablaMovimiento);
	        document.close();
	        System.out.println("documento creado");
			//-------------------------------------------------------------------------------------------------
			bandera = destino;
			return bandera;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return "";
		}
	}
}
