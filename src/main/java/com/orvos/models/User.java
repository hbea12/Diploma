package com.orvos.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name="users")
@Data
@ToString(of = "email")
@EqualsAndHashCode(of = "email")
@Slf4j
public class User {


    @Id
    @Column(name="user_id", unique=true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String userName;

    @NotNull
    private String password;

    private String password2;
    private Boolean samePassword;


    private Boolean defaultPasswordChanged;

    @OneToOne
    @JoinColumn(name="orvos_id")
    private Orvos orvos;

    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public User(String userName, String password, Orvos orvos) {
        this.userName = userName;
        this.password = password;
        this.orvos = orvos;
        this.defaultPasswordChanged = false;
    }

    public User() {
    }
}

