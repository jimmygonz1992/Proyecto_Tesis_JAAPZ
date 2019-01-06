package ec.com.jaapz.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the medidor database table.
 * 
 */
@Entity
@Table(name="medidor")
@NamedQueries({
	@NamedQuery(name="Medidor.medidoresDisponibles", query="SELECT m FROM Medidor m where m.estado = 'A'"),
	@NamedQuery(name="Medidor.recuperaMedidor", query="SELECT m FROM Medidor m WHERE m.codigo = (:codigo) and m.estado = 'A'"),
	@NamedQuery(name="Medidor.validarCodigo", query="SELECT m FROM Medidor m "
			+ "WHERE m.codigo = (:codigo) and m.estado = 'A'")
})

public class Medidor implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_medidor")
	private Integer idMedidor;

	private String codigo;

	private String estado;

	private byte[] foto;

	private String marca;
	
	private double precio;

	private String modelo;
	
	private boolean usado;

	@Column(name="usuario_crea")
	private Integer usuarioCrea;

	//bi-directional many-to-one association to CuentaCliente
	@OneToMany(mappedBy="medidor", cascade = CascadeType.ALL)
	private List<CuentaCliente> cuentaClientes;

	//bi-directional many-to-one association to LiquidacionOrden
	@OneToMany(mappedBy="medidor", cascade = CascadeType.ALL)
	private List<LiquidacionOrden> liquidacionOrdens;

	//bi-directional many-to-one association to EstadoMedidor
	@ManyToOne
	@JoinColumn(name="id_estado")
	private EstadoMedidor estadoMedidor;

	public Medidor() {
	}

	public Integer getIdMedidor() {
		return this.idMedidor;
	}

	public void setIdMedidor(Integer idMedidor) {
		this.idMedidor = idMedidor;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public byte[] getFoto() {
		return this.foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	public String getMarca() {
		return this.marca;
	}
	
	public double getPrecio() {
		return this.precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getModelo() {
		return this.modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public Integer getUsuarioCrea() {
		return this.usuarioCrea;
	}

	public void setUsuarioCrea(Integer usuarioCrea) {
		this.usuarioCrea = usuarioCrea;
	}

	public List<CuentaCliente> getCuentaClientes() {
		return this.cuentaClientes;
	}

	public void setCuentaClientes(List<CuentaCliente> cuentaClientes) {
		this.cuentaClientes = cuentaClientes;
	}

	public CuentaCliente addCuentaCliente(CuentaCliente cuentaCliente) {
		getCuentaClientes().add(cuentaCliente);
		cuentaCliente.setMedidor(this);

		return cuentaCliente;
	}

	public CuentaCliente removeCuentaCliente(CuentaCliente cuentaCliente) {
		getCuentaClientes().remove(cuentaCliente);
		cuentaCliente.setMedidor(null);

		return cuentaCliente;
	}

	public List<LiquidacionOrden> getLiquidacionOrdens() {
		return this.liquidacionOrdens;
	}

	public void setLiquidacionOrdens(List<LiquidacionOrden> liquidacionOrdens) {
		this.liquidacionOrdens = liquidacionOrdens;
	}

	public LiquidacionOrden addLiquidacionOrden(LiquidacionOrden liquidacionOrden) {
		getLiquidacionOrdens().add(liquidacionOrden);
		liquidacionOrden.setMedidor(this);

		return liquidacionOrden;
	}

	public LiquidacionOrden removeLiquidacionOrden(LiquidacionOrden liquidacionOrden) {
		getLiquidacionOrdens().remove(liquidacionOrden);
		liquidacionOrden.setMedidor(null);

		return liquidacionOrden;
	}

	public EstadoMedidor getEstadoMedidor() {
		return this.estadoMedidor;
	}

	public void setEstadoMedidor(EstadoMedidor estadoMedidor) {
		this.estadoMedidor = estadoMedidor;
	}

	public boolean isUsado() {
		return usado;
	}

	public void setUsado(boolean usado) {
		this.usado = usado;
	}

}