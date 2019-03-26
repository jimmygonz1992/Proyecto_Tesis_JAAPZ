package ec.com.jaapz.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ec.com.jaapz.util.Context;

public class ReparacionDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public Integer getIdReparacion(){
		Integer i;
		List<Reparacion> resultado = new ArrayList<Reparacion>();
		Query query = getEntityManager().createNamedQuery("Reparacion.buscarIDRepar");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Reparacion>) query.getResultList();
		if(resultado.size() == 0)
			i = 0;
		else
			i = resultado.get(0).getIdReparacion();
		return i;
	}

	//para recuperar en reparaciones
	@SuppressWarnings("unchecked")
	public List<Reparacion> getListaReparacionesInsp(String patron){
		List<Reparacion> resultado = new ArrayList<Reparacion>();
		Query query = getEntityManager().createNamedQuery("Reparacion.findAllReparaciones");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<Reparacion>) query.getResultList();
		return resultado;
	}

	@SuppressWarnings("unchecked")
	public List<Reparacion> getListaReparacionesPerfilReparaciones(String patron){
		List<Reparacion> resultado = new ArrayList<Reparacion>();
		Query query = getEntityManager().createNamedQuery("Reparacion.buscarReparacionPerfilReparaciones");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		query.setParameter("idPerfilUsuario", Context.getInstance().getIdUsuario());
		resultado = (List<Reparacion>) query.getResultList();
		return resultado;
	}

	//para recuperar en bodega.. lo diferente es unicamente el estadoEntrega
	@SuppressWarnings("unchecked")
	public List<Reparacion> getListadoSalidaReparaciones(String patron){
		List<Reparacion> resultado = new ArrayList<Reparacion>();
		Query query = getEntityManager().createNamedQuery("Reparacion.findAllReparacionesListadoSalida");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<Reparacion>) query.getResultList();
		return resultado;
	}

	@SuppressWarnings("unchecked")
	public List<Reparacion> getListaSalidaReparacionesPerfil(String patron){
		List<Reparacion> resultado = new ArrayList<Reparacion>();
		Query query = getEntityManager().createNamedQuery("Reparacion.buscarReparacionListadoSalidaPerfilReparaciones");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		query.setParameter("idPerfilUsuario", Context.getInstance().getIdPerfil());
		resultado = (List<Reparacion>) query.getResultList();
		return resultado;
	}

	//esta forma la utilicé para agregar mas materiales en editar orden de liquidacion y funcionó jeje
	//asi q voy a ver si funciona
	@SuppressWarnings("unchecked")
	public List<Reparacion> getRecuperaReparacion(Integer idReparacion){
		List<Reparacion> resultado = new ArrayList<Reparacion>();
		Query query = getEntityManager().createNamedQuery("Reparacion.recuperaReparaciones");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idReparacion", idReparacion);
		resultado = (List<Reparacion>) query.getResultList();
		return resultado;
	}

	//para asignar los trabajos de reparacion
	@SuppressWarnings("unchecked")
	public List<Reparacion> getListaReparacionAsignada(Integer patron){
		List<Reparacion> resultado = new ArrayList<Reparacion>();
		Query query = getEntityManager().createNamedQuery("Reparacion.buscarReparacionAsignada");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idPerfilUsuario", patron );
		resultado = (List<Reparacion>) query.getResultList();
		return resultado;
	}

	//para asignacion de trabajos de reparacion
	@SuppressWarnings("unchecked")
	public List<Reparacion> getListaReparacionPendiente(String patron){
		List<Reparacion> resultado = new ArrayList<Reparacion>();
		Query query = getEntityManager().createNamedQuery("Reparacion.buscarListaReparacion");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		resultado = (List<Reparacion>) query.getResultList();
		return resultado;
	}

	//para asignacion de trabajos de reparacion
	//para asignaciones de trabajo
	@SuppressWarnings("unchecked")
	public List<Reparacion> getListaReparacionPerfilPendiente(String patron){
		List<Reparacion> resultado = new ArrayList<Reparacion>();
		Query query = getEntityManager().createNamedQuery("Reparacion.buscarListaReparacionPerfil");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", "%" + patron + "%");
		query.setParameter("idPerfilUsuario", Context.getInstance().getIdUsuario());
		resultado = (List<Reparacion>) query.getResultList();
		return resultado;
	}
	//para asignacion de trabajos de reparacion
	@SuppressWarnings("unchecked")
	public List<Reparacion> getListaReparacionSolicitud(Integer idSolicitud){
		List<Reparacion> resultado = new ArrayList<Reparacion>();
		Query query = getEntityManager().createNamedQuery("Reparacion.buscarReparacionSolicitud");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idSolicitud", idSolicitud);
		resultado = (List<Reparacion>) query.getResultList();
		return resultado;
	}
}