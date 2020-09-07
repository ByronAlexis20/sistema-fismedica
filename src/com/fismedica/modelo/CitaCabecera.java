package com.fismedica.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Time;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="cita_cabecera")
@NamedQueries({
	@NamedQuery(name="CitaCabecera.buscarActivos", query="SELECT c FROM CitaCabecera c where c.estado = 'A' and c.estadoCita = 'PENDIENTE' and (lower(c.cliente.nombre) like lower(:patron) and lower(c.cliente.apellido) like lower(:patron))"),
	@NamedQuery(name="CitaCabecera.buscarPorFecha", query="SELECT c FROM CitaCabecera c where c.estado = 'A' and c.fechaCita = :fecha "
			+ " and c.medico.idMedico = :idMedico order by c.idCita desc"),
	@NamedQuery(name="CitaCabecera.buscarId", query="SELECT c FROM CitaCabecera c where c.estado = 'A' and c.idCita = :idCita"),
	@NamedQuery(name="CitaCabecera.buscarCitasPorNumero", query="SELECT c FROM CitaCabecera c where c.estado = 'A' and c.estadoNoCita = 1 and c.fechaCita = :fecha"
			+ " and c.medico.idMedico = :idMedico order by c.numeroCita desc"),
	@NamedQuery(name="CitaCabecera.buscarCitasPorNumeroHora", query="SELECT c FROM CitaCabecera c where c.estado = 'A' and c.estadoNoCita = 1 and c.fechaCita = :fecha"
			+ " and c.medico.idMedico = :idMedico and c.horaFin <> null order by c.numeroCita desc"),
	@NamedQuery(name="CitaCabecera.buscarPorFechaSeleccionado", query="SELECT c FROM CitaCabecera c where c.estado = 'A' and c.fechaCita = :fecha "
			+ " and c.estadoNoCita = 1 order by c.idCita"),
	@NamedQuery(name="CitaCabecera.buscarPorDia", query="SELECT c FROM CitaCabecera c where c.estado = 'A' and c.fechaCita = :fecha  "
			+ "and c.estadoNoCita = 1 and (c.estadoCita = 'PENDIENTE' or c.estadoCita = 'ATENDIDO') order by c.estadoCita"),
	@NamedQuery(name="CitaCabecera.buscarPorDiaMedico", query="SELECT c FROM CitaCabecera c where c.estado = 'A' and c.fechaCita = :fecha  and "
			+ "c.estadoNoCita = 1 and c.estadoCita = 'PENDIENTE' and c.medico.idMedico = :idMedico order by c.idCita"),
})
public class CitaCabecera implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_cita")
	private Integer idCita;

	private String estado;

	@Column(name="estado_cita")
	private String estadoCita;

	@Column(name="estado_no_cita")
	private Integer estadoNoCita;
	
	@Column(name="numero_cita")
	private Integer numeroCita;
	
	@Temporal(TemporalType.DATE)
	@Column(name="fecha_cita")
	private Date fechaCita;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_registro")
	private Date fechaRegistro;

	@Column(name="hora_fin")
	private Time horaFin;

	@Column(name="hora_inicio")
	private Time horaInicio;

	private String observaciones;

	//bi-directional many-to-one association to Cliente
	@ManyToOne
	@JoinColumn(name="id_cliente")
	private Cliente cliente;
	
	//bi-directional many-to-one association to Medico
	@ManyToOne
	@JoinColumn(name="id_medico")
	private Medico medico;

	//bi-directional many-to-one association to CitaDetalle
	@OneToMany(mappedBy="citaCabecera",cascade = CascadeType.ALL)
	private List<CitaDetalle> citaDetalles;

	public CitaCabecera() {
	}

	public Integer getIdCita() {
		return this.idCita;
	}

	public void setIdCita(Integer idCita) {
		this.idCita = idCita;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getEstadoCita() {
		return this.estadoCita;
	}

	public void setEstadoCita(String estadoCita) {
		this.estadoCita = estadoCita;
	}

	public Date getFechaCita() {
		return this.fechaCita;
	}

	public void setFechaCita(Date fechaCita) {
		this.fechaCita = fechaCita;
	}

	public Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public Time getHoraFin() {
		return this.horaFin;
	}

	public void setHoraFin(Time horaFin) {
		this.horaFin = horaFin;
	}

	public Time getHoraInicio() {
		return this.horaInicio;
	}

	public void setHoraInicio(Time horaInicio) {
		this.horaInicio = horaInicio;
	}

	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Medico getMedico() {
		return this.medico;
	}

	public void setMedico(Medico medico) {
		this.medico = medico;
	}

	public List<CitaDetalle> getCitaDetalles() {
		return this.citaDetalles;
	}

	public void setCitaDetalles(List<CitaDetalle> citaDetalles) {
		this.citaDetalles = citaDetalles;
	}

	public CitaDetalle addCitaDetalle(CitaDetalle citaDetalle) {
		getCitaDetalles().add(citaDetalle);
		citaDetalle.setCitaCabecera(this);

		return citaDetalle;
	}

	public CitaDetalle removeCitaDetalle(CitaDetalle citaDetalle) {
		getCitaDetalles().remove(citaDetalle);
		citaDetalle.setCitaCabecera(null);

		return citaDetalle;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Integer getNumeroCita() {
		return numeroCita;
	}

	public void setNumeroCita(Integer numeroCita) {
		this.numeroCita = numeroCita;
	}

	public Integer getEstadoNoCita() {
		return estadoNoCita;
	}

	public void setEstadoNoCita(Integer estadoNoCita) {
		this.estadoNoCita = estadoNoCita;
	}

}