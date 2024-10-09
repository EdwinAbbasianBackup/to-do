package nl.hu.userservice.infrastructure.data;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity{
    private String username;
    private String password;


    protected User() { }

}
