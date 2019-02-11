package ec.com.jaapz.modelo;

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
}
