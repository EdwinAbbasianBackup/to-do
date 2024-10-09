package nl.hu.boardservice.domain.data;

import jakarta.persistence.*;
import lombok.*;
import nl.hu.boardservice.domain.entity.BaseEntity;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tag")
@Entity
public class Tag extends BaseEntity {
    private String name;

    @ManyToMany(mappedBy = "tags")
    @ToString.Exclude
    private Set<Task> tasks = new HashSet<>();

}
