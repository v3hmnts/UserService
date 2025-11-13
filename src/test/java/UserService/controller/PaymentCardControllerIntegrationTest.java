package UserService.controller;

import UserService.DTO.PageDTO;
import UserService.DTO.PaymentCardDTO;
import UserService.DTO.UserDTOWIthCards;
import UserService.TestcontainersConfiguration;
import UserService.builders.UserBuilder;
import UserService.entity.User;
import UserService.mapper.PaymentCardMapper;
import UserService.mapper.UserMapper;
import UserService.repository.PaymentCardRepository;
import UserService.repository.UserRepository;
import UserService.service.IUserService;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Testcontainers
public class PaymentCardControllerIntegrationTest {

    @Autowired
    protected PostgreSQLContainer<?> postgreSQLContainer;
    @Autowired
    protected RedisContainer redisContainer;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PaymentCardMapper paymentCardMapper;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IUserService userService;
    @Autowired
    private PaymentCardRepository paymentCardRepository;

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
        paymentCardRepository.deleteAll();
    }

    @Test
    void getPaymentCardById_shouldRerunPaymentCard() {
        // Arrange
        User user = new UserBuilder()
                .name("TestName")
                .surname("TestSurname")
                .active(true)
                .email("testemail@test.ru")
                .birthDate(Date.valueOf("2000-1-1"))
                .build();
        User savedUser = userRepository.save(user);
        PaymentCardDTO paymentCardDTO = new PaymentCardDTO()
                .toBuilder()
                .number("1111111111111111")
                .expirationDate(Date.valueOf("2030-1-1"))
                .active(true)
                .build();
        UserDTOWIthCards userDTOWIthCards = userService.addPaymentCardToUser(savedUser.getId(), paymentCardDTO);
        Long cardIdForLookUp = userDTOWIthCards.getCards().get(0).getId();

        //Act
        ResponseEntity<PaymentCardDTO> response = restTemplate.getForEntity(
                "/api/v1/payment-cards/{cardId}",
                PaymentCardDTO.class,
                cardIdForLookUp.toString()
        );

        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getNumber()).isEqualTo(paymentCardDTO.getNumber());
        assertThat(response.getBody().getHolder()).isEqualTo(userDTOWIthCards.getName().toUpperCase() + userDTOWIthCards.getSurname().toUpperCase());
        assertThat(response.getBody().getExpirationDate().toLocalDate()).isEqualTo(paymentCardDTO.getExpirationDate().toLocalDate());
        assertThat(response.getBody().isActive()).isEqualTo(paymentCardDTO.isActive());

    }

    @Test
    void getAllPaymentCardsFilteredBy_shouldRerunFilteredPagePaymentCards() {
        // Arrange
        User user = new UserBuilder()
                .name("TestName")
                .surname("TestSurname")
                .active(true)
                .email("testemail@test.ru")
                .birthDate(Date.valueOf("2000-1-1"))
                .build();
        User savedUser = userRepository.save(user);
        PaymentCardDTO card1 = new PaymentCardDTO()
                .toBuilder()
                .number("1111111111111111")
                .expirationDate(Date.valueOf("2030-1-1"))
                .active(true)
                .build();
        PaymentCardDTO card2 = new PaymentCardDTO()
                .toBuilder()
                .number("2222222222222222")
                .expirationDate(Date.valueOf("2028-1-1"))
                .active(true)
                .build();
        userService.addPaymentCardToUser(savedUser.getId(), card1);
        UserDTOWIthCards userDTOWIthCards = userService.addPaymentCardToUser(savedUser.getId(), card2);
        String filterNameParam = user.getName();


        //Act
        ResponseEntity<PageDTO<PaymentCardDTO>> response = restTemplate.exchange(
                "/api/v1/payment-cards/filtered?name={}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageDTO<PaymentCardDTO>>() {
                },
                filterNameParam
        );

        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalElements()).isEqualTo(2);
        assertThat(response.getBody().getContent().get(0).getHolder()).isEqualTo(userDTOWIthCards.getName().toUpperCase() + userDTOWIthCards.getSurname().toUpperCase());
        assertThat(response.getBody().getContent().get(1).getHolder()).isEqualTo(userDTOWIthCards.getName().toUpperCase() + userDTOWIthCards.getSurname().toUpperCase());

    }

    @Test
    void updatePaymentCardById_shouldRerunPaymentCard() {
        // Arrange
        User user = new UserBuilder()
                .name("TestName")
                .surname("TestSurname")
                .active(true)
                .email("testemail@test.ru")
                .birthDate(Date.valueOf("2000-1-1"))
                .build();
        User savedUser = userRepository.save(user);
        PaymentCardDTO paymentCardDTO = new PaymentCardDTO()
                .toBuilder()
                .number("1111111111111111")
                .expirationDate(Date.valueOf("2030-1-1"))
                .active(true)
                .build();
        UserDTOWIthCards userDTOWIthCards = userService.addPaymentCardToUser(savedUser.getId(), paymentCardDTO);
        Long cardIdForLookUp = userDTOWIthCards.getCards().get(0).getId();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        PaymentCardDTO updateDTO = new PaymentCardDTO()
                .toBuilder()
                .number("2111111111111111")
                .expirationDate(Date.valueOf("2035-5-5"))
                .active(false)
                .build();
        HttpEntity<PaymentCardDTO> requestEntity = new HttpEntity<>(updateDTO, httpHeaders);

        //Act
        ResponseEntity<PaymentCardDTO> response = restTemplate.exchange(
                "/api/v1/payment-cards/{cardId}",
                HttpMethod.PUT,
                requestEntity,
                PaymentCardDTO.class,
                cardIdForLookUp.toString()
        );

        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getNumber()).isEqualTo(updateDTO.getNumber());
        assertThat(response.getBody().getHolder()).isEqualTo(user.getName().toUpperCase() + user.getSurname().toUpperCase());
        assertThat(response.getBody().getExpirationDate().toLocalDate()).isEqualTo(updateDTO.getExpirationDate().toLocalDate());
        assertThat(response.getBody().isActive()).isEqualTo(updateDTO.isActive());

    }

    @Test
    void deactivatePaymentCardById_shouldRerunPaymentCard() {
        // Arrange
        User user = new UserBuilder()
                .name("TestName")
                .surname("TestSurname")
                .active(true)
                .email("testemail@test.ru")
                .birthDate(Date.valueOf("2000-1-1"))
                .build();
        User savedUser = userRepository.save(user);
        PaymentCardDTO paymentCardDTO = new PaymentCardDTO()
                .toBuilder()
                .number("1111111111111111")
                .expirationDate(Date.valueOf("2030-1-1"))
                .active(true)
                .build();
        UserDTOWIthCards userDTOWIthCards = userService.addPaymentCardToUser(savedUser.getId(), paymentCardDTO);
        Long cardIdForLookUp = userDTOWIthCards.getCards().get(0).getId();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PaymentCardDTO> requestEntity = new HttpEntity<>(httpHeaders);

        //Act
        ResponseEntity<PaymentCardDTO> response = restTemplate.exchange(
                "/api/v1/payment-cards/{cardId}/deactivate",
                HttpMethod.PATCH,
                requestEntity,
                PaymentCardDTO.class,
                cardIdForLookUp.toString()
        );

        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().isActive()).isEqualTo(false);


    }

    @Test
    void activatePaymentCardById_shouldRerunPaymentCard() {
        // Arrange
        User user = new UserBuilder()
                .name("TestName")
                .surname("TestSurname")
                .active(true)
                .email("testemail@test.ru")
                .birthDate(Date.valueOf("2000-1-1"))
                .build();
        User savedUser = userRepository.save(user);
        PaymentCardDTO paymentCardDTO = new PaymentCardDTO()
                .toBuilder()
                .number("1111111111111111")
                .expirationDate(Date.valueOf("2030-1-1"))
                .active(false)
                .build();
        UserDTOWIthCards userDTOWIthCards = userService.addPaymentCardToUser(savedUser.getId(), paymentCardDTO);
        Long cardIdForLookUp = userDTOWIthCards.getCards().get(0).getId();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PaymentCardDTO> requestEntity = new HttpEntity<>(httpHeaders);

        //Act
        ResponseEntity<PaymentCardDTO> response = restTemplate.exchange(
                "/api/v1/payment-cards/{cardId}/activate",
                HttpMethod.PATCH,
                requestEntity,
                PaymentCardDTO.class,
                cardIdForLookUp
        );

        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().isActive()).isEqualTo(true);
    }
}
