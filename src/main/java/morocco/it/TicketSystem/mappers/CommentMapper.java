package morocco.it.TicketSystem.mappers;

import morocco.it.TicketSystem.dto.CommentDto;
import morocco.it.TicketSystem.entities.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CommentMapper {

    @Named("mapCommentsToDto")
    default List<CommentDto> mapCommentsToDto(List<Comment> comments) {
        return comments == null ? Collections.emptyList()
                : comments.stream()
                .map(this::fromEntityToDtoComment)
                .collect(Collectors.toList());
    }

    @Mapping(source = "createdBy.id", target = "createdById")
    CommentDto fromEntityToDtoComment(Comment comment);

    @Mapping(source = "createdById", target = "createdBy", qualifiedByName = "mapUserIdToUser")
    Comment fromDtoCommentToComment(CommentDto commentDto);
}
