package com.vlad.swagger.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private Integer age;

    @ManyToMany (fetch = FetchType.EAGER)
    @JoinTable(name = "group_member",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "group_id"))
    @JsonIgnore
    private Set<Group> groups = new HashSet<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    //@JoinColumn(name = "user_id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonIgnore
    private Set<Job> jobs = new HashSet<>();

    public void addGroup(Group group){
        this.groups.add(group);
        group.getUsers().add(this);
    }

    public void removeGroup(Group group){
        this.groups.remove(group);
        group.getUsers().remove(this);
    }

    public void addJob(Job job) {
        this.jobs.add(job);
        job.setUser(this);
    }
    public void removeJob(Job job) {
        this.jobs.remove(job);
    }

}
