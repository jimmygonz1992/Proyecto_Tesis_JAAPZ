package ec.com.jaapz.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class MaterialAdicionalDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<MaterialAdicional> getListaAdicionales(String patron){
		List<MaterialAdicional> resultado = new ArrayList<MaterialAdicional>();
		Query query = getEntityManager().createNamedQuery("MaterialAdicional.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		
		resultado = (List<MaterialAdicional>) query.getResultList();
		return resultado;
	}
}
