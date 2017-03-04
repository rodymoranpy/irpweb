/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.py.modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ACER-PC
 */
@Entity
@Table(name = "permiso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Permiso.findAll", query = "SELECT p FROM Permiso p"),
    @NamedQuery(name = "Permiso.findById", query = "SELECT p FROM Permiso p WHERE p.id = :id"),
    @NamedQuery(name = "Permiso.findByEstado", query = "SELECT p FROM Permiso p WHERE p.estado = :estado"),
    @NamedQuery(name = "Permiso.findByFecharegitro", query = "SELECT p FROM Permiso p WHERE p.fecharegitro = :fecharegitro"),
    @NamedQuery(name = "Permiso.findByFechaultimamodificacion", query = "SELECT p FROM Permiso p WHERE p.fechaultimamodificacion = :fechaultimamodificacion"),
    @NamedQuery(name = "Permiso.findByNombre", query = "SELECT p FROM Permiso p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Permiso.findByUsuarioultimamodificacion", query = "SELECT p FROM Permiso p WHERE p.usuarioultimamodificacion = :usuarioultimamodificacion")})
public class Permiso implements Serializable {
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
    @JoinColumn(name = "rol_id", referencedColumnName = "id")
    @ManyToOne
    private Rol rolId;

    public Permiso() {
    }

    public Permiso(Integer id) {
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

    public Rol getRolId() {
        return rolId;
    }

    public void setRolId(Rol rolId) {
        this.rolId = rolId;
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
        if (!(object instanceof Permiso)) {
            return false;
        }
        Permiso other = (Permiso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.py.modelo.Permiso[ id=" + id + " ]";
    }
    
}
