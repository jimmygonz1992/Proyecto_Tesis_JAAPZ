package ec.com.jaapz.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class FacturaDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public Integer getIdFactura(){
		Integer i;
		List<Factura> resultado = new ArrayList<Factura>();
		Query query = getEntityManager().createNamedQuery("Factura.buscarIDFact");
		resultado = (List<Factura>) query.getResultList();
		if(resultado.size() == 0)
			i = 0;
		else
			i = resultado.get(0).getIdFactura();
		return i;
	}
}