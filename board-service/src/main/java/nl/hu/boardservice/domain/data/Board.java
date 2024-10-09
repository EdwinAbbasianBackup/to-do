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
@Table(name = "board")
public class Board extends BaseEntity {
    private String name;
    private String owner;

    @Singular
    @ElementCollection
    private Set<String> assignees = new HashSet<>();

    @Singular
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @ToString.Exclude
    private Set<Column> columns = new HashSet<>();

    @Singular
    @OneToMany
    @ToString.Exclude
    private Set<Tag> tags = new HashSet<>();
    public Board() { }

    public void addColumn(Column column) {
        this.columns.add(column);
    }

    public void removeColumn(Column column) {
        this.columns.remove(column);
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
    }

    public void addAssignee(String userId) {
        this.assignees.add(userId);
    }



}
