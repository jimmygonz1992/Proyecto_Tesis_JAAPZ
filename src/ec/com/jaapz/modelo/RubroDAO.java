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
	public List<Rubro> getValidarRubro(String descripcion, String codigo) {
		List<Rubro> resultado; 
		Query query = getEntityManager().createNamedQuery("Rubro.validarMaterial");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("descripcion", descripcion);
		query.setParameter("codigo", codigo);
		resultado = (List<Rubro>) query.getResultList();
		return resultado;
	}

	@SuppressWarnings("unchecked")
	public List<Rubro> getListaRubros(String patron){
		List<Rubro> resultado = new ArrayList<Rubro>();
		Query query = getEntityManager().createNamedQuery("Rubro.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<Rubro>) query.getResultList();
		return resultado;
	}

	@SuppressWarnings("unchecked")
	public List<Rubro> getListaRubrosKardex(){
		List<Rubro> resultado = new ArrayList<Rubro>();
		Query query = getEntityManager().createNamedQuery("Rubro.findAllKardex");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
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

	//para validar rubro existente
	@SuppressWarnings("unchecked")
	public List<Rubro> getRecuperaRubro(String codigo){
		List<Rubro> resultado = new ArrayList<Rubro>();
		Query query = getEntityManager().createNamedQuery("Rubro.recuperaRubro");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("codigo", codigo);
		resultado = (List<Rubro>) query.getResultList();
		return resultado;
	}
	
	public Rubro getRubroById(Integer idRubro) {
		Rubro rubro = new Rubro();
		Query query = getEntityManager().createNamedQuery("Rubro.rubroById");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idRubro", idRubro);
		rubro = (Rubro) query.getSingleResult();
		return rubro;
	}
}