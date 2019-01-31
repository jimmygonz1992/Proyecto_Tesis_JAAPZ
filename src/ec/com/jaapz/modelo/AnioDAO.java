package ec.com.jaapz.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class AnioDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Anio> getListaAnios(){
		List<Anio> resultado = new ArrayList<Anio>();
		Query query = getEntityManager().createNamedQuery("Anio.findAll");
		resultado = (List<Anio>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Anio> getBuscarAnio(Integer patron) {
		List<Anio> resultado; 
		Query query = getEntityManager().createNamedQuery("Anio.bucarAnio");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Anio>) query.getResultList();
		return resultado;
	}
	
}
