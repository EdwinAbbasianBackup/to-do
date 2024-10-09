package nl.hu.boardservice.presentation.dto.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.validator.constraints.UUID;


@Setter
public class PatchUserDTO {
    @UUID
    public String userId;

    @JsonCreator
    public PatchUserDTO(@JsonProperty("userId") String userId) {
        this.userId = userId;
    }
}
