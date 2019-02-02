package ec.com.jaapz.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

public class FacturaDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public Integer getIdFactura(){
		Integer i;
		List<Factura> resultado = new ArrayList<Factura>();
		Query query = getEntityManager().createNamedQuery("Factura.buscarIDFact");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Factura>) query.getResultList();
		if(resultado.size() == 0)
			i = 0;
		else
			i = resultado.get(0).getIdFactura();
		return i;
	}
	
	@SuppressWarnings("unchecked")
	public List<Factura> getListaFacturasCuenta(Integer idCuenta){
		List<Factura> resultado = new ArrayList<Factura>();
		Query query = getEntityManager().createNamedQuery("Factura.buscarFactCuenta");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idCuenta",idCuenta);
		resultado = (List<Factura>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Factura> getListaFacturasRecaudadas(Date fechaInicio, Date fechaFin){
		List<Factura> resultado = new ArrayList<Factura>();
		Query query = getEntityManager().createNamedQuery("Factura.buscarFactRecaudadas");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("fechaInicio", fechaInicio);
		query.setParameter("fechaFin", fechaFin);
		resultado = (List<Factura>) query.getResultList();
		return resultado;
	}
}