package ec.com.jaapz.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class CategoriaDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Categoria> getListaCategorias(){
		List<Categoria> resultado = new ArrayList<Categoria>();
		Query query = getEntityManager().createNamedQuery("Categoria.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Categoria>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Categoria> getListaCategorias(String patron){
		List<Categoria> resultado = new ArrayList<Categoria>();
		Query query = getEntityManager().createNamedQuery("Categoria.findCategorias");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<Categoria>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Categoria> getListaCatNormal(){
		List<Categoria> resultado = new ArrayList<Categoria>();
		Query query = getEntityManager().createNamedQuery("Categoria.findCatNormal");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Categoria>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Categoria> getCategoria(String patron){
		List<Categoria> resultado = new ArrayList<Categoria>();
		Query query = getEntityManager().createNamedQuery("Categoria.buscarCategoria");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<Categoria>) query.getResultList();
		return resultado;
	}
	public Categoria getCategoriaNombre(String patron){
		Categoria resultado = new Categoria();
		Query query = getEntityManager().createNamedQuery("Categoria.buscarCategoriaNombre");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (Categoria) query.getSingleResult();
		return resultado;
	}
}
