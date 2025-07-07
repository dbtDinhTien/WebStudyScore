/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myapp.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ADMIN
 */
@Entity
@Table(name = "score")
@NamedQueries({
    @NamedQuery(name = "Score.findAll", query = "SELECT s FROM Score s"),
    @NamedQuery(name = "Score.findById", query = "SELECT s FROM Score s WHERE s.id = :id"),
    @NamedQuery(name = "Score.findByMidtermScore", query = "SELECT s FROM Score s WHERE s.midtermScore = :midtermScore"),
    @NamedQuery(name = "Score.findByFinalScore", query = "SELECT s FROM Score s WHERE s.finalScore = :finalScore"),
    @NamedQuery(name = "Score.findByExtraScore1", query = "SELECT s FROM Score s WHERE s.extraScore1 = :extraScore1"),
    @NamedQuery(name = "Score.findByExtraScore2", query = "SELECT s FROM Score s WHERE s.extraScore2 = :extraScore2"),
    @NamedQuery(name = "Score.findByExtraScore3", query = "SELECT s FROM Score s WHERE s.extraScore3 = :extraScore3"),
    @NamedQuery(name = "Score.findByLockStatus", query = "SELECT s FROM Score s WHERE s.lockStatus = :lockStatus"),
    @NamedQuery(name = "Score.findByUpdatedAt", query = "SELECT s FROM Score s WHERE s.updatedAt = :updatedAt")})
public class Score implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "midterm_score")
    private Float midtermScore;
    @Column(name = "final_score")
    private Float finalScore;
    @Column(name = "extra_score1")
    private Float extraScore1;
    @Column(name = "extra_score2")
    private Float extraScore2;
    @Column(name = "extra_score3")
    private Float extraScore3;
    @Size(max = 6)
    @Column(name = "lock_status")
    private String lockStatus;
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @JoinColumn(name = "student_class_subject_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private StudentClassSubject studentClassSubjectId;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
    public Score() {
    }

    public Score(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getMidtermScore() {
        return midtermScore;
    }

    public void setMidtermScore(Float midtermScore) {
        this.midtermScore = midtermScore;
    }

    public Float getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(Float finalScore) {
        this.finalScore = finalScore;
    }

    public Float getExtraScore1() {
        return extraScore1;
    }

    public void setExtraScore1(Float extraScore1) {
        this.extraScore1 = extraScore1;
    }

    public Float getExtraScore2() {
        return extraScore2;
    }

    public void setExtraScore2(Float extraScore2) {
        this.extraScore2 = extraScore2;
    }

    public Float getExtraScore3() {
        return extraScore3;
    }

    public void setExtraScore3(Float extraScore3) {
        this.extraScore3 = extraScore3;
    }

    public String getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(String lockStatus) {
        this.lockStatus = lockStatus;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public StudentClassSubject getStudentClassSubjectId() {
        return studentClassSubjectId;
    }

    public void setStudentClassSubjectId(StudentClassSubject studentClassSubjectId) {
        this.studentClassSubjectId = studentClassSubjectId;
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
        if (!(object instanceof Score)) {
            return false;
        }
        Score other = (Score) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.myapp.pojo.Score[ id=" + id + " ]";
    }
    
}
