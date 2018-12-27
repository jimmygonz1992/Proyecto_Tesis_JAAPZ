package ec.com.jaapz.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ec.com.jaapz.util.Constantes;

public class SegUsuarioDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<SegUsuario> getUsuarioPerfil(String usuario) {
		List<SegUsuario> resultado; 
		Query query = getEntityManager().createNamedQuery("SegUsuario.buscarUsuario");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("usuario", usuario);
		resultado = (List<SegUsuario>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<SegUsuario> getUsuario(String usuario,String clave) {
		List<SegUsuario> resultado; 
		Query query = getEntityManager().createNamedQuery("SegUsuario.buscarPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("usuario", usuario);
		query.setParameter("clave", clave);
		resultado = (List<SegUsuario>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<SegUsuario> getValidarUsuario(String usuario,int idUsuario) {
		List<SegUsuario> resultado; 
		Query query = getEntityManager().createNamedQuery("SegUsuario.validarUsuario");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("usuario", usuario);
		query.setParameter("idUsuario", idUsuario);
		resultado = (List<SegUsuario>) query.getResultList();
		return resultado;
	}
	
	//para recuperar usuario
	@SuppressWarnings("unchecked")
	public List<SegUsuario> getRecuperaUsuario(String cedula){
		List<SegUsuario> resultado = new ArrayList<SegUsuario>();
		Query query = getEntityManager().createNamedQuery("SegUsuario.recuperaUsuario");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("cedula", cedula);
		resultado = (List<SegUsuario>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<SegUsuario> getListaUsuarios(){
		List<SegUsuario> resultado = new ArrayList<SegUsuario>();
		Query query = getEntityManager().createNamedQuery("SegUsuario.findAll");
		resultado = (List<SegUsuario>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<SegUsuario> getListaUsuariosInspeccion(){
		List<SegUsuario> resultado = new ArrayList<SegUsuario>();
		Query query = getEntityManager().createNamedQuery("SegUsuario.buscarInspeccion");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idINS", Constantes.ID_USU_INSPECCION);
		resultado = (List<SegUsuario>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<SegUsuario> getListaUsuariosLectura(){
		List<SegUsuario> resultado = new ArrayList<SegUsuario>();
		Query query = getEntityManager().createNamedQuery("SegUsuario.buscarLectura");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idLEC", Constantes.ID_USU_LECTURA);
		resultado = (List<SegUsuario>) query.getResultList();
		return resultado;
	}
}
