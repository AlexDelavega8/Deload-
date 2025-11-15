package es.pocketrainer.modelo.cliente;

import java.time.Period;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import es.pocketrainer.modelo.ejercicio.Material;
import es.pocketrainer.modelo.entrenador.Entrenador;
import es.pocketrainer.modelo.rutina.Rutina;
import es.pocketrainer.modelo.usuario.Usuario;
import es.pocketrainer.modelo.valormaestro.ValorMaestro;
import es.pocketrainer.modelo.videoconferencia.Videoconferencia;

@Entity
public class Cliente {

	@Id
	@GeneratedValue
	private Long id;
	private String codigo;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "estado_codigo", referencedColumnName = "codigo", nullable = false)
	private ValorMaestro estado;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private String nif;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "sexo_codigo", referencedColumnName = "codigo")
	private ValorMaestro sexo;
	private ZonedDateTime fechaNacimiento;
	private String email;
	private String telefono;
	private String disponibilidadTelefonica;

	private String domicilio;
	private String codigoPostal;
	private String localidad;
	private String pais;
	private String objetivos;
	private Boolean entrenaActualmente;
	private String actividadFisicaActual;
	private String clavesEntrenamiento;
	private String lesiones;
	private Integer altura;
	private Double peso;

	private Boolean aceptaPoliticaPrivacidad;
	private ZonedDateTime fechaAceptaPoliticaPrivacidad;
	private Boolean aceptaTerminosCondiciones;
	private ZonedDateTime fechaAceptaTerminosCondiciones;

	@OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
	private ClienteConfiguracion clienteConfiguracion;

	@ManyToMany
	@JoinTable(name = "cliente_dia_entrenamiento", joinColumns = {
			@JoinColumn(name = "cliente_id", referencedColumnName = "id", nullable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "dia_entrenamiento_codigo", referencedColumnName = "codigo", nullable = false) })
	private Set<ValorMaestro> diaEntrenamientoLista;

	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
	private List<ClienteParq> clienteParqLista;

	@ManyToMany
	@JoinTable(name = "cliente_lugar_entrenamiento", joinColumns = {
			@JoinColumn(name = "cliente_id", referencedColumnName = "id", nullable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "lugar_entrenamiento_codigo", referencedColumnName = "codigo", nullable = false) })
	private Set<ValorMaestro> lugarEntrenamientoLista;

	@ManyToMany
	@JoinTable(name = "cliente_medio_conocimiento", joinColumns = {
			@JoinColumn(name = "cliente_id", referencedColumnName = "id", nullable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "medio_conocimiento_codigo", referencedColumnName = "codigo", nullable = false) })
	private Set<ValorMaestro> medioConocimientoLista;

	@ManyToMany
	@JoinTable(name = "cliente_perfil", joinColumns = { @JoinColumn(name = "cliente_id", referencedColumnName = "id", nullable = false) }, inverseJoinColumns = {
			@JoinColumn(name = "perfil_codigo", referencedColumnName = "codigo", nullable = false) })
	private Set<ValorMaestro> perfilLista;

	@ManyToMany
	@JoinTable(name = "cliente_material", joinColumns = { @JoinColumn(name = "cliente_id", referencedColumnName = "id", nullable = false) }, inverseJoinColumns = {
			@JoinColumn(name = "material_id", referencedColumnName = "id", nullable = false) })
	private Set<Material> materialLista;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuario usuario;
	private ZonedDateTime fechaRegistroInicial;

	@ManyToOne
	@JoinColumn(name = "entrenador_id")
	private Entrenador entrenador;

	@OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
	private Suscripcion suscripcion;

	@OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
	private ClienteDatosCobro clienteDatosCobro;

	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Videoconferencia> videoConferenciaLista;

	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("fecha_creacion desc, id asc")
	private List<Rutina> rutinaLista;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public ValorMaestro getEstado() {
		return estado;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Transient
	public String getNombreCompleto() {
		return nombre + " " + apellido1 + (apellido2 != null ? " " + apellido2 : "");
	}

	public String getApellido1() {
		return apellido1;
	}

	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}

	public String getApellido2() {
		return apellido2;
	}

	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}

	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public ValorMaestro getSexo() {
		return sexo;
	}

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public ZonedDateTime getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(ZonedDateTime fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDisponibilidadTelefonica() {
		return disponibilidadTelefonica;
	}

	public void setDisponibilidadTelefonica(String disponibilidadTelefonica) {
		this.disponibilidadTelefonica = disponibilidadTelefonica;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getObjetivos() {
		return objetivos;
	}

	public void setObjetivos(String objetivos) {
		this.objetivos = objetivos;
	}

	public Boolean getEntrenaActualmente() {
		return entrenaActualmente;
	}

	public void setEntrenaActualmente(Boolean entrenaActualmente) {
		this.entrenaActualmente = entrenaActualmente;
	}

	public String getActividadFisicaActual() {
		return actividadFisicaActual;
	}

	public void setActividadFisicaActual(String actividadFisicaActual) {
		this.actividadFisicaActual = actividadFisicaActual;
	}

	public String getClavesEntrenamiento() {
		return clavesEntrenamiento;
	}

	public void setClavesEntrenamiento(String clavesEntrenamiento) {
		this.clavesEntrenamiento = clavesEntrenamiento;
	}

	public String getLesiones() {
		return lesiones;
	}

	public void setLesiones(String lesiones) {
		this.lesiones = lesiones;
	}

	public Integer getAltura() {
		return altura;
	}

	public void setAltura(Integer altura) {
		this.altura = altura;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public ZonedDateTime getFechaRegistroInicial() {
		return fechaRegistroInicial;
	}

	public void setFechaRegistroInicial(ZonedDateTime fechaRegistroInicial) {
		this.fechaRegistroInicial = fechaRegistroInicial;
	}

	public Entrenador getEntrenador() {
		return entrenador;
	}

	public void setEntrenador(Entrenador entrenador) {
		this.entrenador = entrenador;
	}

	public Suscripcion getSuscripcion() {
		return suscripcion;
	}

	public void setSuscripcion(Suscripcion suscripcion) {
		this.suscripcion = suscripcion;
	}

	public ClienteDatosCobro getClienteDatosCobro() {
		return clienteDatosCobro;
	}

	public void setClienteDatosCobro(ClienteDatosCobro clienteDatosCobro) {
		this.clienteDatosCobro = clienteDatosCobro;
	}

	public void setEstado(ValorMaestro estado) {
		this.estado = estado;
	}

	public void setSexo(ValorMaestro sexo) {
		this.sexo = sexo;
	}

	public List<Videoconferencia> getVideoConferenciaLista() {
		return videoConferenciaLista;
	}

	public void setVideoConferenciaLista(List<Videoconferencia> videoConferenciaLista) {
		this.videoConferenciaLista = videoConferenciaLista;
	}

	public void addVideoconferencia(Videoconferencia cideoconferencia) {
		if (videoConferenciaLista == null) {
			videoConferenciaLista = new ArrayList<>();
		}
		cideoconferencia.setCliente(this);
		videoConferenciaLista.add(cideoconferencia);
	}

	public List<Rutina> getRutinaLista() {
		return rutinaLista;
	}

	public void setRutinaLista(List<Rutina> rutinaLista) {
		this.rutinaLista = rutinaLista;
	}

	public void addRutina(Rutina rutina) {
		if (rutinaLista == null) {
			rutinaLista = new ArrayList<>();
		}
		rutina.setCliente(this);
		rutinaLista.add(rutina);
	}

	public Set<ValorMaestro> getDiaEntrenamientoLista() {
		return diaEntrenamientoLista;
	}

	public void setDiaEntrenamientoLista(Set<ValorMaestro> diaEntrenamientoLista) {
		this.diaEntrenamientoLista = diaEntrenamientoLista;
	}

	public List<ClienteParq> getClienteParqLista() {
		return clienteParqLista;
	}

	public void setClienteParqLista(List<ClienteParq> clienteParqLista) {
		this.clienteParqLista = clienteParqLista;
	}

	public void addClienteParq(ClienteParq clienteParq) {
		if (clienteParqLista == null) {
			clienteParqLista = new ArrayList<>();
		}
		clienteParq.setCliente(this);
		clienteParqLista.add(clienteParq);
	}

	public Set<ValorMaestro> getLugarEntrenamientoLista() {
		return lugarEntrenamientoLista;
	}

	public void setLugarEntrenamientoLista(Set<ValorMaestro> lugarEntrenamientoLista) {
		this.lugarEntrenamientoLista = lugarEntrenamientoLista;
	}

	public Set<ValorMaestro> getMedioConocimientoLista() {
		return medioConocimientoLista;
	}

	public void setMedioConocimientoLista(Set<ValorMaestro> medioConocimientoLista) {
		this.medioConocimientoLista = medioConocimientoLista;
	}

	public Set<ValorMaestro> getPerfilLista() {
		return perfilLista;
	}

	public void setPerfilLista(Set<ValorMaestro> perfilLista) {
		this.perfilLista = perfilLista;
	}

	public Set<Material> getMaterialLista() {
		return materialLista;
	}

	public void setMaterialLista(Set<Material> materialLista) {
		this.materialLista = materialLista;
	}

	public Boolean getAceptaPoliticaPrivacidad() {
		return aceptaPoliticaPrivacidad;
	}

	public void setAceptaPoliticaPrivacidad(Boolean aceptaPoliticaPrivacidad) {
		this.aceptaPoliticaPrivacidad = aceptaPoliticaPrivacidad;
	}

	public ZonedDateTime getFechaAceptaPoliticaPrivacidad() {
		return fechaAceptaPoliticaPrivacidad;
	}

	public void setFechaAceptaPoliticaPrivacidad(ZonedDateTime fechaAceptaPoliticaPrivacidad) {
		this.fechaAceptaPoliticaPrivacidad = fechaAceptaPoliticaPrivacidad;
	}

	public Boolean getAceptaTerminosCondiciones() {
		return aceptaTerminosCondiciones;
	}

	public void setAceptaTerminosCondiciones(Boolean aceptaTerminosCondiciones) {
		this.aceptaTerminosCondiciones = aceptaTerminosCondiciones;
	}

	public ZonedDateTime getFechaAceptaTerminosCondiciones() {
		return fechaAceptaTerminosCondiciones;
	}

	public void setFechaAceptaTerminosCondiciones(ZonedDateTime fechaAceptaTerminosCondiciones) {
		this.fechaAceptaTerminosCondiciones = fechaAceptaTerminosCondiciones;
	}

	public int getEdad() {
		return fechaNacimiento != null ? Period.between(fechaNacimiento.toLocalDate(), ZonedDateTime.now().toLocalDate()).getYears() : 0;
	}

	public ClienteConfiguracion getClienteConfiguracion() {
		return clienteConfiguracion;
	}

	public void setClienteConfiguracion(ClienteConfiguracion clienteConfiguracion) {
		this.clienteConfiguracion = clienteConfiguracion;
	}

}
