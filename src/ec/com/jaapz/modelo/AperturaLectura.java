package ec.com.jaapz.modelo;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the apertura_lectura database table.
 * 
 */
@Entity
@Table(name="apertura_lectura")
@NamedQueries({
	@NamedQuery(name="AperturaLectura.findAll", query="SELECT a FROM AperturaLectura a where a.estado = 'A' order by a.idApertura"),
	@NamedQuery(name="AperturaLectura.buscarAnio", query="SELECT a FROM AperturaLectura a where a.estado = 'A' and a.anio.idAnio = :idAnio order by a.idApertura"),
	@NamedQuery(name="AperturaLectura.findAllRealizado", query="SELECT a FROM AperturaLectura a where a.estado = 'A' and a.estadoApertura = 'REALIZADO'"),
	@NamedQuery(name="AperturaLectura.buscarCiclo", query="SELECT a FROM AperturaLectura a where a.estado = 'A' and a.estadoApertura = 'EN PROCESO'"),
	@NamedQuery(name="AperturaLectura.buscarAperturaId", query="SELECT a FROM AperturaLectura a where a.estado = 'A' and a.idApertura = :idApertura"),
	@NamedQuery(name="AperturaLectura.buscarAperturaIdAnio", query="SELECT a FROM AperturaLectura a where a.estado = 'A' and a.anio.idAnio = :idAnio"),
	@NamedQuery(name="AperturaLectura.buscarAperturaIdAnioIdMes", query="SELECT a "
			+ "FROM AperturaLectura a where a.estado = 'A' and a.anio.idAnio = :idAnio and a.me.idMes = :idMes"),
	@NamedQuery(name="AperturaLectura.buscarAnioMes", query="SELECT a FROM AperturaLectura a where a.estado = 'A' and "
			+ "(lower(a.anio.descripcion) like lower(:patron) or lower(a.me.descripcion) like lower(:patron))")
})
public class AperturaLectura implements Serializable { //
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_apertura")
	private Integer idApertura;

	private String estado;

	@Column(name="estado_apertura")
	private String estadoApertura;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	private Time hora;
	
	@Column(name="cantidad_metros")
	private Integer cantidadMetros;

	@Column(name="costo_metros")
	private Double costoMetros;
	
	private String observacion;

	@Column(name="usuario_crea")
	private Integer usuarioCrea;

	//bi-directional many-to-one association to Anio
	@ManyToOne
	@JoinColumn(name="id_anio")
	private Anio anio;

	//bi-directional many-to-one association to Me
	@ManyToOne
	@JoinColumn(name="id_mes")
	private Me me;

	//bi-directional many-to-one association to Planilla
	@OneToMany(mappedBy="aperturaLectura", cascade = CascadeType.ALL)
	private List<Planilla> planillas;

	//bi-directional many-to-one association to ResponsableLectura
	@OneToMany(mappedBy="aperturaLectura", cascade = CascadeType.ALL)
	private List<ResponsableLectura> responsableLecturas;

	public AperturaLectura() {
	}

	public Integer getIdApertura() {
		return this.idApertura;
	}

	public void setIdApertura(Integer idApertura) {
		this.idApertura = idApertura;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getEstadoApertura() {
		return this.estadoApertura;
	}

	public void setEstadoApertura(String estadoApertura) {
		this.estadoApertura = estadoApertura;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Time getHora() {
		return this.hora;
	}

	public void setHora(Time hora) {
		this.hora = hora;
	}

	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Integer getUsuarioCrea() {
		return this.usuarioCrea;
	}

	public void setUsuarioCrea(Integer usuarioCrea) {
		this.usuarioCrea = usuarioCrea;
	}

	public Anio getAnio() {
		return this.anio;
	}

	public void setAnio(Anio anio) {
		this.anio = anio;
	}

	public Me getMe() {
		return this.me;
	}

	public void setMe(Me me) {
		this.me = me;
	}

	public List<Planilla> getPlanillas() {
		return this.planillas;
	}

	public void setPlanillas(List<Planilla> planillas) {
		this.planillas = planillas;
	}

	public Planilla addPlanilla(Planilla planilla) {
		getPlanillas().add(planilla);
		planilla.setAperturaLectura(this);

		return planilla;
	}

	public Planilla removePlanilla(Planilla planilla) {
		getPlanillas().remove(planilla);
		planilla.setAperturaLectura(null);

		return planilla;
	}

	public List<ResponsableLectura> getResponsableLecturas() {
		return this.responsableLecturas;
	}

	public void setResponsableLecturas(List<ResponsableLectura> responsableLecturas) {
		this.responsableLecturas = responsableLecturas;
	}

	public ResponsableLectura addResponsableLectura(ResponsableLectura responsableLectura) {
		getResponsableLecturas().add(responsableLectura);
		responsableLectura.setAperturaLectura(this);

		return responsableLectura;
	}

	public ResponsableLectura removeResponsableLectura(ResponsableLectura responsableLectura) {
		getResponsableLecturas().remove(responsableLectura);
		responsableLectura.setAperturaLectura(null);

		return responsableLectura;
	}

	public Integer getCantidadMetros() {
		return cantidadMetros;
	}

	public void setCantidadMetros(Integer cantidadMetros) {
		this.cantidadMetros = cantidadMetros;
	}

	public Double getCostoMetros() {
		return costoMetros;
	}

	public void setCostoMetros(Double costoMetros) {
		this.costoMetros = costoMetros;
	}



}