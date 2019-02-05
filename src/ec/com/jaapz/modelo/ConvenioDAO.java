package ec.com.jaapz.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class ConvenioDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Convenio> getConveniosAll(){
		List<Convenio> resultado = new ArrayList<Convenio>();
		Query query = getEntityManager().createNamedQuery("Convenio.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Convenio>) query.getResultList();
		return resultado;
	}
	
}
