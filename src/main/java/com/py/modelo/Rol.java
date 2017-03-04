/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.py.modelo;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ACER-PC
 */
@Entity
@Table(name = "rol")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rol.findAll", query = "SELECT r FROM Rol r"),
    @NamedQuery(name = "Rol.findById", query = "SELECT r FROM Rol r WHERE r.id = :id"),
    @NamedQuery(name = "Rol.findByEstado", query = "SELECT r FROM Rol r WHERE r.estado = :estado"),
    @NamedQuery(name = "Rol.findByFecharegitro", query = "SELECT r FROM Rol r WHERE r.fecharegitro = :fecharegitro"),
    @NamedQuery(name = "Rol.findByFechaultimamodificacion", query = "SELECT r FROM Rol r WHERE r.fechaultimamodificacion = :fechaultimamodificacion"),
    @NamedQuery(name = "Rol.findByNombre", query = "SELECT r FROM Rol r WHERE r.nombre = :nombre"),
    @NamedQuery(name = "Rol.findByUsuarioultimamodificacion", query = "SELECT r FROM Rol r WHERE r.usuarioultimamodificacion = :usuarioultimamodificacion")})
public class Rol implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "estado")
    private String estado;
    @Column(name = "fecharegitro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecharegitro;
    @Column(name = "fechaultimamodificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaultimamodificacion;
    @Size(max = 255)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 255)
    @Column(name = "usuarioultimamodificacion")
    private String usuarioultimamodificacion;
    @JoinTable(name = "usuario_rol", joinColumns = {
        @JoinColumn(name = "roles_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "usuarios_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Usuario> usuarioCollection;
    @OneToMany(mappedBy = "rolId")
    private Collection<Permiso> permisoCollection;

    public Rol() {
    }

    public Rol(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFecharegitro() {
        return fecharegitro;
    }

    public void setFecharegitro(Date fecharegitro) {
        this.fecharegitro = fecharegitro;
    }

    public Date getFechaultimamodificacion() {
        return fechaultimamodificacion;
    }

    public void setFechaultimamodificacion(Date fechaultimamodificacion) {
        this.fechaultimamodificacion = fechaultimamodificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuarioultimamodificacion() {
        return usuarioultimamodificacion;
    }

    public void setUsuarioultimamodificacion(String usuarioultimamodificacion) {
        this.usuarioultimamodificacion = usuarioultimamodificacion;
    }

    @XmlTransient
    public Collection<Usuario> getUsuarioCollection() {
        return usuarioCollection;
    }

    public void setUsuarioCollection(Collection<Usuario> usuarioCollection) {
        this.usuarioCollection = usuarioCollection;
    }

    @XmlTransient
    public Collection<Permiso> getPermisoCollection() {
        return permisoCollection;
    }

    public void setPermisoCollection(Collection<Permiso> permisoCollection) {
        this.permisoCollection = permisoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rol)) {
            return false;
        }
        Rol other = (Rol) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.py.modelo.Rol[ id=" + id + " ]";
    }
    
}
