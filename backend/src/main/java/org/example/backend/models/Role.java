package org.example.backend.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.spi.ToolProvider;

@Entity
@NoArgsConstructor
@Data
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "role")
    @ToString.Exclude
    @Enumerated(EnumType.STRING)
    private AppRoles roleName;


    public Role(AppRoles role) {
        this.roleName = role;
    }


}
