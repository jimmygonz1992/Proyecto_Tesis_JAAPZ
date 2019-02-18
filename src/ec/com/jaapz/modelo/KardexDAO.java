package ec.com.jaapz.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class KardexDAO extends ClaseDAO{
	public Kardex getKardexByDocumento(String documento, Integer idRubro){
		Kardex resultado = new Kardex();
		Query query = getEntityManager().createNamedQuery("Kardex.findKardex");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("documento",documento);
		query.setParameter("idRubro",idRubro);
		resultado = (Kardex) query.getSingleResult();
		return resultado;
	}

	@SuppressWarnings("unchecked")
	public List<Kardex> getListaKardex(String patron){
		List<Kardex> resultado = new ArrayList<Kardex>();
		Query query = getEntityManager().createNamedQuery("Kardex.ListaKardex");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<Kardex>) query.getResultList();
		return resultado;
	}
}