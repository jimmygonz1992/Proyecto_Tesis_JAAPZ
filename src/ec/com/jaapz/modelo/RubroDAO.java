package ec.com.jaapz.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class RubroDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<Rubro> getRubro(String descripcion) {
		List<Rubro> resultado; 
		Query query = getEntityManager().createNamedQuery("Rubro.buscarPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("descripcion", "%" + descripcion + "%");		
		resultado = (List<Rubro>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Rubro> getValidarRubro(String descripcion,int idMaterial) {
		List<Rubro> resultado; 
		Query query = getEntityManager().createNamedQuery("Rubro.validarMaterial");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("descripcion", descripcion);
		query.setParameter("idRubro", idMaterial);
		resultado = (List<Rubro>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Rubro> getListaRubros(){
		List<Rubro> resultado = new ArrayList<Rubro>();
		Query query = getEntityManager().createNamedQuery("Rubro.findAll");
		resultado = (List<Rubro>) query.getResultList();
		return resultado;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Rubro> getRubroN(int codigo){
		List<Rubro> resultado = new ArrayList<Rubro>();
		Query query = getEntityManager().createNamedQuery("Rubro.findPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idRubro", codigo);
		resultado = (List<Rubro>) query.getResultList();
		return resultado;
	}
}
