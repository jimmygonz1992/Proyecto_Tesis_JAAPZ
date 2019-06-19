package ec.com.jaapz.modelo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the planilla_detalle database table.
 * 
 */
@Entity
@Table(name="planilla_detalle")
@NamedQuery(name="PlanillaDetalle.findAll", query="SELECT p FROM PlanillaDetalle p")
public class PlanillaDetalle implements Serializable, Comparable<PlanillaDetalle> {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_planilla_det")
	private Integer idPlanillaDet;

	private Integer cantidad;

	private String descripcion;

	private String estado;

	private double subtotal;

	@Column(name="usuario_crea")
	private Integer usuarioCrea;

	//bi-directional many-to-one association to ConvenioDetalle
	@ManyToOne
	@JoinColumn(name="id_convenio_det")
	private ConvenioDetalle convenioDetalle;
	
	
	@Column(name="identificador_operacion")
	private String identificadorOperacion;

	//bi-directional many-to-one association to Instalacion
	@ManyToOne
	@JoinColumn(name="id_instalacion")
	private Instalacion instalacion;

	//bi-directional many-to-one association to Planilla
	@ManyToOne
	@JoinColumn(name="id_planilla")
	private Planilla planilla;

	//bi-directional many-to-one association to Reparacion
	@ManyToOne
	@JoinColumn(name="id_reparacion")
	private Reparacion reparacion;

	//bi-directional many-to-one association to RegistroFoto
	@OneToMany(mappedBy="planillaDetalle", cascade = CascadeType.ALL)
	private List<RegistroFoto> registroFotos;

	public PlanillaDetalle() {
	}

	public Integer getIdPlanillaDet() {
		return this.idPlanillaDet;
	}

	public void setIdPlanillaDet(Integer idPlanillaDet) {
		this.idPlanillaDet = idPlanillaDet;
	}

	public Integer getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public double getSubtotal() {
		return this.subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public Integer getUsuarioCrea() {
		return this.usuarioCrea;
	}

	public void setUsuarioCrea(Integer usuarioCrea) {
		this.usuarioCrea = usuarioCrea;
	}

	public ConvenioDetalle getConvenioDetalle() {
		return this.convenioDetalle;
	}

	public void setConvenioDetalle(ConvenioDetalle convenioDetalle) {
		this.convenioDetalle = convenioDetalle;
	}

	public Instalacion getInstalacion() {
		return this.instalacion;
	}

	public void setInstalacion(Instalacion instalacion) {
		this.instalacion = instalacion;
	}

	public Planilla getPlanilla() {
		return this.planilla;
	}

	public void setPlanilla(Planilla planilla) {
		this.planilla = planilla;
	}

	public Reparacion getReparacion() {
		return this.reparacion;
	}

	public void setReparacion(Reparacion reparacion) {
		this.reparacion = reparacion;
	}

	public List<RegistroFoto> getRegistroFotos() {
		return this.registroFotos;
	}

	public void setRegistroFotos(List<RegistroFoto> registroFotos) {
		this.registroFotos = registroFotos;
	}

	public RegistroFoto addRegistroFoto(RegistroFoto registroFoto) {
		getRegistroFotos().add(registroFoto);
		registroFoto.setPlanillaDetalle(this);

		return registroFoto;
	}

	public RegistroFoto removeRegistroFoto(RegistroFoto registroFoto) {
		getRegistroFotos().remove(registroFoto);
		registroFoto.setPlanillaDetalle(null);

		return registroFoto;
	}

	
	
	public String getIdentificadorOperacion() {
		return identificadorOperacion;
	}

	public void setIdentificadorOperacion(String identificadorOperacion) {
		this.identificadorOperacion = identificadorOperacion;
	}

	@Override
	public int compareTo(PlanillaDetalle o) {
        if (this.idPlanillaDet < o.idPlanillaDet) {
            return -1;
        }
        if (this.idPlanillaDet  > o.idPlanillaDet) {
            return 1;
        }
        return 0;
    }

	@Override
	public String toString() {
		return "PlanillaDetalle [idPlanillaDet=" + idPlanillaDet + ", cantidad=" + cantidad + ", descripcion="
				+ descripcion + ", estado=" + estado + ", subtotal=" + subtotal + ", usuarioCrea=" + usuarioCrea
				+ ", convenioDetalle=" + convenioDetalle + ", identificadorOperacion=" + identificadorOperacion
				+ ", instalacion=" + instalacion + ", planilla=" + planilla + ", reparacion=" + reparacion
				+ ", registroFotos=" + registroFotos + "]";
	}
	
	
	
}