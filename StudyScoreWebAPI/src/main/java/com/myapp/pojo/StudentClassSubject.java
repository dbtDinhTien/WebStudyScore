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
@Table(name = "student_class_subject")
@NamedQueries({
    @NamedQuery(name = "StudentClassSubject.findAll", query = "SELECT s FROM StudentClassSubject s"),
    @NamedQuery(name = "StudentClassSubject.findById", query = "SELECT s FROM StudentClassSubject s WHERE s.id = :id")})
public class StudentClassSubject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "studentClassSubjectId")
    private Set<Score> scoreSet;
    @JoinColumn(name = "class_subject_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ClassSubject classSubjectId;
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User studentId;

    public StudentClassSubject() {
    }

    public StudentClassSubject(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<Score> getScoreSet() {
        return scoreSet;
    }

    public void setScoreSet(Set<Score> scoreSet) {
        this.scoreSet = scoreSet;
    }

    public ClassSubject getClassSubjectId() {
        return classSubjectId;
    }

    public void setClassSubjectId(ClassSubject classSubjectId) {
        this.classSubjectId = classSubjectId;
    }

    public User getStudentId() {
        return studentId;
    }

    public void setStudentId(User studentId) {
        this.studentId = studentId;
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
        if (!(object instanceof StudentClassSubject)) {
            return false;
        }
        StudentClassSubject other = (StudentClassSubject) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.myapp.pojo.StudentClassSubject[ id=" + id + " ]";
    }
    
}
