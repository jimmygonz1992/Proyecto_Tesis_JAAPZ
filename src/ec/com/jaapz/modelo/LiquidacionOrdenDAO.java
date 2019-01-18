package ec.com.jaapz.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ec.com.jaapz.util.Context;

public class LiquidacionOrdenDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<LiquidacionOrden> getListaLiquidacionOrden(String patron){
		List<LiquidacionOrden> resultado = new ArrayList<LiquidacionOrden>();
		Query query = getEntityManager().createNamedQuery("LiquidacionOrden.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<LiquidacionOrden>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<LiquidacionOrden> getListaLiquidacionOrdenPerfil(String patron){
		List<LiquidacionOrden> resultado = new ArrayList<LiquidacionOrden>();
		Query query = getEntityManager().createNamedQuery("LiquidacionOrden.buscarLiquidacionOrdenPerfil");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		query.setParameter("idPerfilUsuario", Context.getInstance().getIdPerfil());
		resultado = (List<LiquidacionOrden>) query.getResultList();
		return resultado;
	}
	
	//para recuperar en instalaciones
	@SuppressWarnings("unchecked")
	public List<LiquidacionOrden> getListaLiquidacionOrdenInstalaciones(String patron){
		List<LiquidacionOrden> resultado = new ArrayList<LiquidacionOrden>();
		Query query = getEntityManager().createNamedQuery("LiquidacionOrden.findAllInstalaciones");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<LiquidacionOrden>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<LiquidacionOrden> getListaLiquidacionOrdenPerfilInstalaciones(String patron){
		List<LiquidacionOrden> resultado = new ArrayList<LiquidacionOrden>();
		Query query = getEntityManager().createNamedQuery("LiquidacionOrden.buscarLiquidacionOrdenPerfilInstalaciones");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		query.setParameter("idPerfilUsuario", Context.getInstance().getIdPerfil());
		resultado = (List<LiquidacionOrden>) query.getResultList();
		return resultado;
	}
	
	//para agregar mas materiales solo cambia en el estado = REALIZADO
	@SuppressWarnings("unchecked")
	public List<LiquidacionOrden> getListaLiqOrdenEmitida(String patron){
		List<LiquidacionOrden> resultado = new ArrayList<LiquidacionOrden>();
		Query query = getEntityManager().createNamedQuery("LiquidacionOrden.findAllEmitida");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<LiquidacionOrden>) query.getResultList();
		return resultado;
	}
	
	//para agregar mas materiales solo cambia en el estado = REALIZADO
	@SuppressWarnings("unchecked")
	public List<LiquidacionOrden> getListaLiqOrdenPerfilEmitida(String patron){
		List<LiquidacionOrden> resultado = new ArrayList<LiquidacionOrden>();
		Query query = getEntityManager().createNamedQuery("LiquidacionOrden.buscarLiqOrdenPerfilEmitida");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		query.setParameter("idPerfilUsuario", Context.getInstance().getIdPerfil());
		resultado = (List<LiquidacionOrden>) query.getResultList();
		return resultado;
	}
	
	
	//esta es provisional para ver si puedo editar una orden de liquidacion
	//bueno en realidad si funcionó xD
	//para recuperar factura
	@SuppressWarnings("unchecked")
	public List<LiquidacionOrden> getRecuperaLiquidacionEmitida(Integer idLiquidacion){
		List<LiquidacionOrden> resultado = new ArrayList<LiquidacionOrden>();
		Query query = getEntityManager().createNamedQuery("LiquidacionOrden.recuperaLiquidacionEmitida");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idLiquidacion", idLiquidacion);
		resultado = (List<LiquidacionOrden>) query.getResultList();
		return resultado;
	}
}