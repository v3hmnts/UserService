package UserService.mapper;

import UserService.DTO.PaymentCardDTO;
import UserService.DTO.PaymentCardDTOWithUser;
import UserService.entity.PaymentCard;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentCardMapper {

    @Mapping(source = "user.id", target = "userId")
    PaymentCardDTO toPaymentCardDTO(PaymentCard paymentCard);

    @InheritInverseConfiguration
    PaymentCard toEntity(PaymentCardDTO paymentCardDTO);

    PaymentCardDTOWithUser toPaymentCardDTOWithUser(PaymentCard paymentCard, @Context CycleAvoidingMappingContext ctx);

    PaymentCard toEntity(PaymentCardDTOWithUser paymentCardDTOWithUser, @Context CycleAvoidingMappingContext ctx);

    PaymentCardDTO toPaymentCardDTOWithUser(PaymentCardDTOWithUser paymentCardDTOWithUser);

    List<PaymentCard> toEntityList(List<PaymentCardDTO> paymentCardDTOS);

    List<PaymentCard> toEntityListWithUsers(List<PaymentCardDTOWithUser> paymentCardDTOWithUsers, @Context CycleAvoidingMappingContext ctx);

    List<PaymentCardDTO> toPaymentCardDTOList(List<PaymentCard> paymentCards);

    List<PaymentCardDTOWithUser> toPaymentCardDTOWithUsersList(List<PaymentCard> paymentCards);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateFromDTO(PaymentCardDTO dto, @MappingTarget PaymentCard paymentCard);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateFromDTOWithCards(PaymentCardDTOWithUser dto, @MappingTarget PaymentCard paymentCard);

}
