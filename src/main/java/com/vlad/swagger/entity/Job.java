package com.vlad.swagger.entity;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @Column(name = "date")
    private Date date;

    @ManyToOne(fetch = FetchType.EAGER)
    //@JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonIgnore
    private User user;

    @JsonProperty("date_employment")
    @Column(name = "date_employment")
    private Date dateEmployment;

    public void removeUser(){
        this.setUser(null);
        this.setDateEmployment(null);
    }

}
