package ec.com.jaapz.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ec.com.jaapz.util.Context;

public class SolInspeccionRepDAO extends ClaseDAO{
	
	@SuppressWarnings("unchecked")
	public List<SolInspeccionRep> getListaInspeccionPerfilPendiente(String patron){
		List<SolInspeccionRep> resultado = new ArrayList<SolInspeccionRep>();
		Query query = getEntityManager().createNamedQuery("SolInspeccionRep.buscarInspeccionPerfilPendiente");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		query.setParameter("idPerfilUsuario", Context.getInstance().getIdUsuario());
		resultado = (List<SolInspeccionRep>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<SolInspeccionRep> getListaInspeccionPendiente(String patron){
		List<SolInspeccionRep> resultado = new ArrayList<SolInspeccionRep>();
		Query query = getEntityManager().createNamedQuery("SolInspeccionRep.findAllPendiente");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<SolInspeccionRep>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<SolInspeccionRep> getListaInspeccionAsignada(Integer patron){
		List<SolInspeccionRep> resultado = new ArrayList<SolInspeccionRep>();
		Query query = getEntityManager().createNamedQuery("SolInspeccionRep.buscarInspeccionAsignada");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idPerfilUsuario", patron );
		resultado = (List<SolInspeccionRep>) query.getResultList();
		return resultado;
	}
}