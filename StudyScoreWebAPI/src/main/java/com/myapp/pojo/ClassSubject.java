/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author ADMIN
 */
@Entity
@Table(name = "class_subject")
@NamedQueries({
    @NamedQuery(name = "ClassSubject.findAll", query = "SELECT c FROM ClassSubject c"),
    @NamedQuery(name = "ClassSubject.findById", query = "SELECT c FROM ClassSubject c WHERE c.id = :id")})
public class ClassSubject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "classSubjectId")
    private Set<StudentClassSubject> studentClassSubjectSet;
    @JoinColumn(name = "class_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Class classId;
    @JoinColumn(name = "subject_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Subject subjectId;
    @JoinColumn(name = "lecturer_id", referencedColumnName = "id")
    @ManyToOne
    private User lecturerId;

    public ClassSubject() {
    }

    public ClassSubject(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<StudentClassSubject> getStudentClassSubjectSet() {
        return studentClassSubjectSet;
    }

    public void setStudentClassSubjectSet(Set<StudentClassSubject> studentClassSubjectSet) {
        this.studentClassSubjectSet = studentClassSubjectSet;
    }

    public Class getClassId() {
        return classId;
    }

    public void setClassId(Class classId) {
        this.classId = classId;
    }

    public Subject getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Subject subjectId) {
        this.subjectId = subjectId;
    }

    public User getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(User lecturerId) {
        this.lecturerId = lecturerId;
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
        if (!(object instanceof ClassSubject)) {
            return false;
        }
        ClassSubject other = (ClassSubject) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.myapp.pojo.ClassSubject[ id=" + id + " ]";
    }
    
}
