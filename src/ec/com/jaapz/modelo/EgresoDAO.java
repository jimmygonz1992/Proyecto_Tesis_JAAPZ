package ec.com.jaapz.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

public class EgresoDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Egreso> getListaEgresos(Date fechaInicio, Date fechaFin){
		List<Egreso> resultado = new ArrayList<Egreso>();
		Query query = getEntityManager().createNamedQuery("Egreso.buscarEgresos");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("fechaInicio", fechaInicio);
		query.setParameter("fechaFin", fechaFin);
		resultado = (List<Egreso>) query.getResultList();
		return resultado;
	}
}