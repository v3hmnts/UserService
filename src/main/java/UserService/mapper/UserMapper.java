package UserService.mapper;

import UserService.DTO.UserDTO;
import UserService.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);
    User toEntity(UserDTO userDTO);
    UserDTO toUserDTO(User user);
    List<User> toEntityList(List<UserDTO> userDTOList);
    List<UserDTO> toDTOList(List<User> userList);
}
