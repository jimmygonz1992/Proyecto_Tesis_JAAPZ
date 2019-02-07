package ec.com.jaapz.util;

import java.sql.Time;
import java.util.Date;

import ec.com.jaapz.modelo.SegAuditoria;
import ec.com.jaapz.modelo.SegAuditoriaDAO;

public class Auditoria {
	SegAuditoriaDAO auditoriaDAO = new SegAuditoriaDAO();
	
	public void grabarAuditoria(String descripcion, String tabla, String accion, Integer idUsuario) {
		try {
			Date dateAc = new Date();
			Time time = new Time(dateAc.getTime());
			SegAuditoria auditoria = new SegAuditoria();
			auditoria.setAccion(accion);
			auditoria.setDescripcion(descripcion);
			auditoria.setEstado(Constantes.ESTADO_ACTIVO);
			auditoria.setFecha(dateAc);
			auditoria.setHora(time);
			auditoria.setIdAuditoria(null);
			auditoria.setTablaAfectada(tabla);
			auditoria.setUsuarioCrea(idUsuario);
			
			
			auditoriaDAO.getEntityManager().getTransaction().begin();
			auditoriaDAO.getEntityManager().persist(auditoria);
			auditoriaDAO.getEntityManager().getTransaction().commit();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
