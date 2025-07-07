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
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ADMIN
 */
@Entity
@Table(name = "user")
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id"),
    @NamedQuery(name = "User.findByFirstName", query = "SELECT u FROM User u WHERE u.firstName = :firstName"),
    @NamedQuery(name = "User.findByLastName", query = "SELECT u FROM User u WHERE u.lastName = :lastName"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
    @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password"),
    @NamedQuery(name = "User.findByRole", query = "SELECT u FROM User u WHERE u.role = :role"),
    @NamedQuery(name = "User.findByAvatarUrl", query = "SELECT u FROM User u WHERE u.avatarUrl = :avatarUrl"),
    @NamedQuery(name = "User.findByStudentCode", query = "SELECT u FROM User u WHERE u.studentCode = :studentCode"),
    @NamedQuery(name = "User.findByLecturerCode", query = "SELECT u FROM User u WHERE u.lecturerCode = :lecturerCode"),
    @NamedQuery(name = "User.findByActive", query = "SELECT u FROM User u WHERE u.active = :active")})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "first_name")
    private String firstName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "last_name")
    private String lastName;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "role")
    private String role;
    @Size(max = 500)
    @Column(name = "avatar_url")
    private String avatarUrl;
    @Size(max = 50)
    @Column(name = "student_code")
    private String studentCode;
    @Size(max = 50)
    @Column(name = "lecturer_code")
    private String lecturerCode;
    @Column(name = "active")
    private Boolean active;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "authorId")
    private Set<ForumPost> forumPostSet;
    @JsonIgnore
    @OneToMany(mappedBy = "lecturerId")
    private Set<Class> classSet;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "authorId")
    private Set<ForumComment> forumCommentSet;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "studentId")
    private Set<StudentClassSubject> studentClassSubjectSet;
    @JoinColumn(name = "class_id", referencedColumnName = "id")
    @ManyToOne
    private Class classId;
    @JsonIgnore
    @OneToMany(mappedBy = "lecturerId")
    private Set<ClassSubject> classSubjectSet;
    @Transient
    private MultipartFile file;
    
    public User() {
    }

    public User(Integer id) {
        this.id = id;
    }

    public User(Integer id, String firstName, String lastName, String email, String password, String role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getLecturerCode() {
        return lecturerCode;
    }

    public void setLecturerCode(String lecturerCode) {
        this.lecturerCode = lecturerCode;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<ForumPost> getForumPostSet() {
        return forumPostSet;
    }

    public void setForumPostSet(Set<ForumPost> forumPostSet) {
        this.forumPostSet = forumPostSet;
    }

    public Set<Class> getClassSet() {
        return classSet;
    }

    public void setClassSet(Set<Class> classSet) {
        this.classSet = classSet;
    }

    public Set<ForumComment> getForumCommentSet() {
        return forumCommentSet;
    }

    public void setForumCommentSet(Set<ForumComment> forumCommentSet) {
        this.forumCommentSet = forumCommentSet;
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

    public Set<ClassSubject> getClassSubjectSet() {
        return classSubjectSet;
    }

    public void setClassSubjectSet(Set<ClassSubject> classSubjectSet) {
        this.classSubjectSet = classSubjectSet;
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
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.myapp.pojo.User[ id=" + id + " ]";
    }

    /**
     * @return the file
     */
    public MultipartFile getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(MultipartFile file) {
        this.file = file;
    }
    
}
