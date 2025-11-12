package UserService.mapper;

import UserService.DTO.PageDTO;
import UserService.DTO.UserDTO;
import UserService.DTO.UserDTOWIthCards;
import UserService.entity.User;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PaymentCardMapper.class})
public interface UserMapper {

    User toEntity(UserDTO userDTO);

    UserDTO toUserDTO(User user);

    List<User> toEntityList(List<UserDTO> userDTOList);

    List<UserDTO> toDTOList(List<User> userList);

    UserDTOWIthCards toUserDTOWithCards(User user, @Context CycleAvoidingMappingContext ctx);

    User toEntityWithCards(UserDTOWIthCards userDTOWIthCards, @Context CycleAvoidingMappingContext ctx);

    List<User> toEntityListWithCards(List<UserDTOWIthCards> userDTOWIthCards, @Context CycleAvoidingMappingContext ctx);

    List<UserDTOWIthCards> toDTOList(List<User> userListWithCards, @Context CycleAvoidingMappingContext ctx);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateUserFromDTO(UserDTO dto, @MappingTarget User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateUserFromDTOWithCards(UserDTOWIthCards dto, @MappingTarget User user);

    UserDTO toDTOFromFullDTO(UserDTOWIthCards userDTOWIthCards);

    @Mapping(source = "number", target = "size")
    PageDTO<UserDTO> toUserDTOPage(Page<UserDTO> userDTOPage);

    @Mapping(source = "number", target = "size")
    PageDTO<UserDTOWIthCards> toUserDTOWithCardsPage(Page<UserDTOWIthCards> userDTOPage);


}
