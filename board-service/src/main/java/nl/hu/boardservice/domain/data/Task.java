package nl.hu.boardservice.domain.data;

import jakarta.persistence.*;
import lombok.*;
import nl.hu.boardservice.domain.entity.BaseEntity;

import java.util.*;

@AllArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Builder
@Table(name = "task")
public class Task extends BaseEntity {
    private String title;
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @Singular
    @ToString.Exclude
    private List<Tag> tags = new ArrayList<>();


    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "user_id")
    @CollectionTable(name = "task_assignees", joinColumns = @JoinColumn(name = "task_id"))
    @Singular
    private Map<String, String> assignees = new HashMap<>();

    public Task() {
    }

    public boolean hasAssignees() {
        return !assignees.isEmpty();
    }

    public void addAssignee(String userId, String name) {
        this.assignees.put(userId, name);
    }

    public void removeAssignee(String userId) {
        this.assignees.remove(userId);
    }

}
