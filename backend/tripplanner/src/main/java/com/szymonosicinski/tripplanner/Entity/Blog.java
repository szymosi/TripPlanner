package com.szymonosicinski.tripplanner.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.szymonosicinski.tripplanner.Util.PostgreSQLEnumType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.security.PrivateKey;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
@Table(name="blog",schema="public")
public class Blog {


    public static enum Visibility{guests, users, participants};

    @Id
    @Column(name="id")
    @org.hibernate.annotations.Type(type = "pg-uuid")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @NotNull
    @Length(max=50)
    @Column(name="name")
    private String name;

    @NotNull
    @Length(max=5000)
    @Column(name="description")
    private String description;

    @NotNull
    @Column(name="visibility")
    @Enumerated(EnumType.STRING)
    @Type(type = "pgsql_enum")
    private Visibility visibility;

    @NotNull
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_trip", referencedColumnName = "id")
    @JsonIgnore
    private Trip trip;

    @OneToMany(mappedBy = "blog", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, targetEntity = BlogEntry.class)
    @JsonIgnore
    private Set<BlogEntry> blogEntries=new HashSet<>();
}
