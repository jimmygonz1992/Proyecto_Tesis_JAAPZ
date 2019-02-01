package ec.com.jaapz.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class TipoPagoDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<TipoPago> getListaTipoPago(){
		List<TipoPago> resultado = new ArrayList<TipoPago>();
		Query query = getEntityManager().createNamedQuery("TipoPago.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<TipoPago>) query.getResultList();
		return resultado;
	}
}
