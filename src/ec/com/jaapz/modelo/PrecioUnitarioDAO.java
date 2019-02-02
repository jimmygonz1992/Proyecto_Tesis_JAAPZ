package ec.com.jaapz.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class PrecioUnitarioDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<PrecioUnitario> getListaPrecios(){
		List<PrecioUnitario> resultado = new ArrayList<PrecioUnitario>();
		Query query = getEntityManager().createNamedQuery("PrecioUnitario.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<PrecioUnitario>) query.getResultList();
		return resultado;
	}
}
