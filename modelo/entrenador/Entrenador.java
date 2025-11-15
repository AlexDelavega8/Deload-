package es.pocketrainer.modelo.entrenador;

import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import es.pocketrainer.modelo.cliente.Cliente;
import es.pocketrainer.modelo.rutina.Rutina;
import es.pocketrainer.modelo.usuario.Usuario;
import es.pocketrainer.modelo.valormaestro.ValorMaestro;
import es.pocketrainer.modelo.videoconferencia.Videoconferencia;

@Entity
public class Entrenador {

	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "estado_codigo", referencedColumnName = "codigo", nullable = false)
	private ValorMaestro estado;
	private String codigo;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private String email;
	private String telefono;
	private ZonedDateTime fechaRegistroInicial;

	@OneToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuario usuario;

	@OneToMany(mappedBy = "entrenador")
	private List<Cliente> clienteLista;

	@OneToMany(mappedBy = "entrenador")
	private List<Videoconferencia> videoConferenciaLista;

	@OneToMany(mappedBy = "cliente")
	private List<Rutina> rutinaLista;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ValorMaestro getEstado() {
		return estado;
	}

	public void setEstado(ValorMaestro estado) {
		this.estado = estado;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	@Transient
	public String getNombreCompleto() {
		return nombre + " " + apellido1 + (apellido2 != null ? " " + apellido2 : "");
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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

	public ZonedDateTime getFechaRegistroInicial() {
		return fechaRegistroInicial;
	}

	public void setFechaRegistroInicial(ZonedDateTime fechaRegistroInicial) {
		this.fechaRegistroInicial = fechaRegistroInicial;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Cliente> getClienteLista() {
		return clienteLista;
	}

	public void setClienteLista(List<Cliente> clienteLista) {
		this.clienteLista = clienteLista;
	}

	public List<Videoconferencia> getVideoConferenciaLista() {
		return videoConferenciaLista;
	}

	public void setVideoConferenciaLista(List<Videoconferencia> videoConferenciaLista) {
		this.videoConferenciaLista = videoConferenciaLista;
	}

	public List<Rutina> getRutinaLista() {
		return rutinaLista;
	}

	public void setRutinaLista(List<Rutina> rutinaLista) {
		this.rutinaLista = rutinaLista;
	}

}
