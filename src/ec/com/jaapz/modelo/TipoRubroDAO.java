package ec.com.jaapz.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class TipoRubroDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<TipoRubro> getListaTipoRubro(){
		List<TipoRubro> resultado = new ArrayList<TipoRubro>();
		Query query = getEntityManager().createNamedQuery("TipoRubro.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<TipoRubro>) query.getResultList();
		return resultado;
	}
}