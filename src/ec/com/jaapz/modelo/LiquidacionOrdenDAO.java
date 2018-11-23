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
}
