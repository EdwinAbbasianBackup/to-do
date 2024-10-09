package nl.hu.boardservice.domain.data;

import jakarta.persistence.*;
import lombok.*;
import nl.hu.boardservice.domain.entity.BaseEntity;

import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "columns")
public class Column extends BaseEntity {
    private String name;

    @Singular
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @ToString.Exclude
    private Set<Task> tasks = new HashSet<>();

    public Column() { }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }

}
