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
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<SegUsuario>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<SegUsuario> getListaUsuariosInspeccion(){
		List<SegUsuario> resultado;
		List<SegUsuario> usuarioInspeccion = new ArrayList<SegUsuario>();
		Query query = getEntityManager().createNamedQuery("SegUsuario.buscarTodosUsuarios");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<SegUsuario>) query.getResultList();
		for(SegUsuario usuario : resultado) {
			for(SegUsuarioPerfil perfil : usuario.getSegUsuarioPerfils()) {
				if(perfil.getSegPerfil().getIdPerfil() == Constantes.ID_USU_INSPECCION && perfil.getEstado().equals(Constantes.ESTADO_ACTIVO))
					usuarioInspeccion.add(usuario);
			}
		}
		return usuarioInspeccion;
	}
	
	//para asigancion de trabajos de reparacion
	@SuppressWarnings("unchecked")
	public List<SegUsuario> getListaUsuariosReparacion(){
		List<SegUsuario> resultado;
		List<SegUsuario> usuarioInspeccion = new ArrayList<SegUsuario>();
		Query query = getEntityManager().createNamedQuery("SegUsuario.buscarTodosUsuarios");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<SegUsuario>) query.getResultList();
		for(SegUsuario usuario : resultado) {
			for(SegUsuarioPerfil perfil : usuario.getSegUsuarioPerfils()) {
				if(perfil.getSegPerfil().getIdPerfil() == Constantes.ID_USU_REPARACIONES && perfil.getEstado().equals(Constantes.ESTADO_ACTIVO))
					usuarioInspeccion.add(usuario);
			}
		}
		return usuarioInspeccion;
	}
	
	@SuppressWarnings("unchecked")
	public List<SegUsuario> getListaUsuariosLectura(){
		List<SegUsuario> resultado;
		List<SegUsuario> usuarioInspeccion = new ArrayList<SegUsuario>();
		Query query = getEntityManager().createNamedQuery("SegUsuario.buscarTodosUsuarios");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<SegUsuario>) query.getResultList();
		for(SegUsuario usuario : resultado) {
			for(SegUsuarioPerfil perfil : usuario.getSegUsuarioPerfils()) {
				if(perfil.getSegPerfil().getIdPerfil() == Constantes.ID_USU_LECTURA && perfil.getEstado().equals(Constantes.ESTADO_ACTIVO))
					usuarioInspeccion.add(usuario);
			}
		}
		return usuarioInspeccion;
	}
	
	//para cambio de contrasenia
	@SuppressWarnings("unchecked")
	public List<SegUsuario> getRecuperaUsuario(String clave, String usuario){
		List<SegUsuario> resultado = new ArrayList<SegUsuario>();
		Query query = getEntityManager().createNamedQuery("SegUsuario.recuperaUsuarioValidaContrasenia");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("clave", clave);
		query.setParameter("usuario", usuario);
		resultado = (List<SegUsuario>) query.getResultList();
		return resultado;
	}
	
	//para asigancion de instalaciones
	@SuppressWarnings("unchecked")
	public List<SegUsuario> getListaUsuariosInstalacion(){
		List<SegUsuario> resultado;
		List<SegUsuario> usuarioInspeccion = new ArrayList<SegUsuario>();
		Query query = getEntityManager().createNamedQuery("SegUsuario.buscarTodosUsuarios");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<SegUsuario>) query.getResultList();
		for(SegUsuario usuario : resultado) {
			for(SegUsuarioPerfil perfil : usuario.getSegUsuarioPerfils()) {
				if(perfil.getSegPerfil().getIdPerfil() == Constantes.ID_USU_INSTALACIONES && perfil.getEstado().equals(Constantes.ESTADO_ACTIVO))
					usuarioInspeccion.add(usuario);
			}
		}
		return usuarioInspeccion;
	}
}