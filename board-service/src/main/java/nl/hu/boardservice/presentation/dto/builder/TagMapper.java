package nl.hu.boardservice.presentation.dto.builder;

import nl.hu.boardservice.domain.data.Tag;
import nl.hu.boardservice.presentation.dto.tag.TagDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagDTO toDTO(Tag tag);
    Tag toEntity(TagDTO tagDTO);

    List<Tag> toEntity(List<TagDTO> tagDTOs);

    List<TagDTO> toDTOList(List<Tag> tagList);
}