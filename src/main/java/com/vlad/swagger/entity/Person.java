package com.vlad.swagger.entity;

import lombok.Data;
import javax.persistence.*;


@Data
@Entity
@Table(name = "auth_users")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String password;

}
