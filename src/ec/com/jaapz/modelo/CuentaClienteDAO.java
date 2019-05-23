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
	
	//para corte de agua
	@SuppressWarnings("unchecked")
	public List<CuentaCliente> getListaCuentasAsignadas(Integer patron){
		List<CuentaCliente> resultado = new ArrayList<CuentaCliente>();
		Query query = getEntityManager().createNamedQuery("CuentaCliente.buscarCuentaAsignada");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idPerfilUsuario", patron );
		resultado = (List<CuentaCliente>) query.getResultList();
		return resultado;
	}
	
	//para corte agua
	@SuppressWarnings("unchecked")
	public List<CuentaCliente> getListaCuentaCortes(String patron){
		List<CuentaCliente> resultado = new ArrayList<CuentaCliente>();
		Query query = getEntityManager().createNamedQuery("CuentaCliente.buscaCuentasParaCorte");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<CuentaCliente>) query.getResultList();
		return resultado;
	}
	//para corte
	@SuppressWarnings("unchecked")
	public List<CuentaCliente> getListaCuentaCortesPerfil(String patron){
		List<CuentaCliente> resultado = new ArrayList<CuentaCliente>();
		Query query = getEntityManager().createNamedQuery("CuentaCliente.buscarCuentasCortePerfil");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		query.setParameter("idPerfilUsuario", Context.getInstance().getIdUsuario());
		resultado = (List<CuentaCliente>) query.getResultList();
		return resultado;
	}
	
	//para corte ya asignado listado principal
	@SuppressWarnings("unchecked")
	public List<CuentaCliente> getListaCuentasCortes(String patron){
		List<CuentaCliente> resultado = new ArrayList<CuentaCliente>();
		Query query = getEntityManager().createNamedQuery("CuentaCliente.CuentasPendientesAdm");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<CuentaCliente>) query.getResultList();
		return resultado;
	}
	
	//para corte ya asignado
	@SuppressWarnings("unchecked")
	public List<CuentaCliente> getListaCuentasPendPefil(String patron){
		List<CuentaCliente> resultado = new ArrayList<CuentaCliente>();
		Query query = getEntityManager().createNamedQuery("CuentaCliente.cuentasCortePendientes");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		query.setParameter("idPerfilUsuario", Context.getInstance().getIdUsuario());
		resultado = (List<CuentaCliente>) query.getResultList();
		return resultado;
	}
}