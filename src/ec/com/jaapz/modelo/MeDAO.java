package ec.com.jaapz.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class MeDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Me> getListaMeses(){
		List<Me> resultado = new ArrayList<Me>();
		Query query = getEntityManager().createNamedQuery("Me.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Me>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Me> getBuscarMes(Integer patron) {
		List<Me> resultado; 
		Query query = getEntityManager().createNamedQuery("Me.buscarMes");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Me>) query.getResultList();
		return resultado;
	}
}
