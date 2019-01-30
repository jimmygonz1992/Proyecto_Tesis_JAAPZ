package ec.com.jaapz.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ec.com.jaapz.util.Context;

public class CuentaClienteDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<CuentaCliente> getListaCuentaClientes(String patron){
		List<CuentaCliente> resultado = new ArrayList<CuentaCliente>();
		Query query = getEntityManager().createNamedQuery("CuentaCliente.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<CuentaCliente>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<CuentaCliente> getListaCuentasActivas(){
		List<CuentaCliente> resultado = new ArrayList<CuentaCliente>();
		Query query = getEntityManager().createNamedQuery("CuentaCliente.bucarTodos");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<CuentaCliente>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<CuentaCliente> getListaCuentaClientePerfil(String patron){
		List<CuentaCliente> resultado = new ArrayList<CuentaCliente>();
		Query query = getEntityManager().createNamedQuery("CuentaCliente.buscarCuentaClientePerfil");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		query.setParameter("idPerfilUsuario", Context.getInstance().getIdPerfil());
		resultado = (List<CuentaCliente>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<CuentaCliente> getExisteCuenta(Integer cuenta){
		List<CuentaCliente> resultado = new ArrayList<CuentaCliente>();
		Query query = getEntityManager().createNamedQuery("CuentaCliente.existeCuenta");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("cuenta", cuenta);
		resultado = (List<CuentaCliente>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<CuentaCliente> getExisteCuentaMedidor(String medidor){
		List<CuentaCliente> resultado = new ArrayList<CuentaCliente>();
		Query query = getEntityManager().createNamedQuery("CuentaCliente.existeCuentaMedidor");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("medidor", medidor);
		resultado = (List<CuentaCliente>) query.getResultList();
		return resultado;
	}
}