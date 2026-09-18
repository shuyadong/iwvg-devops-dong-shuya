package es.upm.miw.devops.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
class UserEntity {

    @Id
    private String id;
    private String firstName;
    private String familyName;
    private String email;
    private String identity;
    private String address;
    private String city;
    private String province;
    private String postalCode;
    private boolean active;
    @Enumerated(EnumType.STRING)
    private Role role;

    protected UserEntity() {
    }

    UserEntity(String id, String firstName, String familyName, String email, String identity,
               String address, String city, String province, String postalCode,
               boolean active, Role role) {
        this.id = id;
        this.firstName = firstName;
        this.familyName = familyName;
        this.email = email;
        this.identity = identity;
        this.address = address;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.active = active;
        this.role = role;
    }

    static UserEntity from(User user) {
        return new UserEntity(user.id(), user.firstName(), user.familyName(), user.email(), user.identity(),
                user.address(), user.city(), user.province(), user.postalCode(), user.active(), user.role());
    }

    User toUser() {
        return new User(this.id, this.firstName, this.familyName, this.email, this.identity,
                this.address, this.city, this.province, this.postalCode, this.active, this.role);
    }

    void setActive(boolean active) {
        this.active = active;
    }
}
