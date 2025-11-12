package UserService.controller;

import UserService.DTO.PageDTO;
import UserService.DTO.UserDTO;
import UserService.DTO.UserDTOWIthCards;
import UserService.TestcontainersConfiguration;
import UserService.builders.UserBuilder;
import UserService.entity.PaymentCard;
import UserService.entity.User;
import UserService.mapper.UserMapper;
import UserService.repository.PaymentCardRepository;
import UserService.repository.UserRepository;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Testcontainers
public class UserControllerIntegrationTest {

    @Autowired
    protected PostgreSQLContainer<?> postgreSQLContainer;
    @Autowired
    protected RedisContainer redisContainer;
    @Autowired
    private UserMapper mapper;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PaymentCardRepository paymentCardRepository;

    @BeforeAll
    static void beforeAll() {

    }

    @AfterAll
    static void afterAll() {

    }

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
        paymentCardRepository.deleteAll();
    }

    @Test
    void addUser_shouldCreateUser() {
        // Arrange
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);


        UserDTO userDTO = new UserDTO();
        userDTO.setName("TEST");
        userDTO.setSurname("TEST");
        userDTO.setEmail("test@terst.ru");
        userDTO.setBirthDate(Date.valueOf("1999-06-05"));
        userDTO.setActive(true);

        HttpEntity<UserDTO> requestEntity = new HttpEntity<>(userDTO, httpHeaders);

        //Act
        ResponseEntity<UserDTO> response = restTemplate.postForEntity(
                "/api/v1/users",
                requestEntity,
                UserDTO.class
        );

        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getEmail()).isEqualTo(userDTO.getEmail());
        assertThat(response.getBody().getName()).isEqualTo(userDTO.getName());
        assertThat(response.getBody().getSurname()).isEqualTo(userDTO.getSurname());
        assertThat(response.getBody().getBirthDate().toLocalDate()).isEqualTo(userDTO.getBirthDate().toLocalDate());

    }

    @Test
    void givenRedisContainerConfiguredWithDynamicProperties_whenCheckingRunningStatus_thenStatusIsRunning() {
        assertTrue(redisContainer.isRunning());
        System.out.println(redisContainer.getHost());
        System.out.println(redisContainer.getMappedPort(6379).toString());

    }

    @Test
    void getUserById_shouldReturnUserById() {
        // Arrange
        User user = new UserBuilder()
                .name("TEST2")
                .surname("TEST2")
                .email("test2@test2.ru")
                .birthDate(Date.valueOf("1999-06-05"))
                .active(true)
                .build();

        User savedUsed = userRepository.save(user);


        //Act
        ResponseEntity<UserDTO> response = restTemplate.getForEntity(
                "/api/v1/users/{id}",
                UserDTO.class,
                savedUsed.getId()
        );

        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(savedUsed.getId());
        assertThat(response.getBody().getEmail()).isEqualTo(savedUsed.getEmail());
        assertThat(response.getBody().getName()).isEqualTo(savedUsed.getName());
        assertThat(response.getBody().getSurname()).isEqualTo(savedUsed.getSurname());
        assertThat(response.getBody().getBirthDate().toLocalDate()).isEqualTo(savedUsed.getBirthDate().toLocalDate());

    }

    @Test
    void getUserWithCardsById_shouldReturnUserWithCardsById() {
        // Arrange
        User user = new UserBuilder()
                .name("TEST")
                .surname("TEST")
                .email("test@test.ru")
                .birthDate(Date.valueOf("1999-06-05"))
                .active(true)
                .build();
        User savedUsed = userRepository.save(user);
        PaymentCard paymentCard1 = new PaymentCard(null, null, "2222222222222222", "ASD ASD", Date.valueOf("2028-01-01"), true, null, null);
        PaymentCard paymentCard2 = new PaymentCard(null, null, "3222222222222222", "ASD ASD", Date.valueOf("2028-01-01"), true, null, null);
        savedUsed.addPaymentCard(paymentCard1);
        savedUsed.addPaymentCard(paymentCard2);
        userRepository.save(savedUsed);

        //Act
        ResponseEntity<UserDTOWIthCards> response = restTemplate.getForEntity(
                "/api/v1/users/{id}/with-cards",
                UserDTOWIthCards.class,
                savedUsed.getId()
        );

        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(savedUsed.getId());
        assertThat(response.getBody().getEmail()).isEqualTo(savedUsed.getEmail());
        assertThat(response.getBody().getName()).isEqualTo(savedUsed.getName());
        assertThat(response.getBody().getSurname()).isEqualTo(savedUsed.getSurname());
        assertThat(response.getBody().getBirthDate().toLocalDate()).isEqualTo(savedUsed.getBirthDate().toLocalDate());
        assertThat(response.getBody().getCards().size()).isEqualTo(2);


    }

    @Test
    void getAllUsers_shouldReturnAllUsers() {
        // Arrange
        User user = new UserBuilder()
                .name("TEST")
                .surname("TEST")
                .email("test@test.ru")
                .birthDate(Date.valueOf("1999-06-05"))
                .active(true)
                .build();
        User user2 = new UserBuilder()
                .name("USER2")
                .surname("USER2")
                .email("user2@user2.ru")
                .birthDate(Date.valueOf("1998-06-05"))
                .active(true)
                .build();
        User savedUsed = userRepository.save(user);
        User savedUser2 = userRepository.save(user2);

        //Act
        ResponseEntity<PageDTO<UserDTO>> response = restTemplate.exchange(
                "/api/v1/users",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageDTO<UserDTO>>() {
                }
        );

        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalElements()).isEqualTo(2);

    }

    @Test
    void getAllUsersFilteredBy_shouldReturnAllFilteredUsers() {
        // Arrange
        User user = new UserBuilder()
                .name("TEST")
                .surname("TEST")
                .email("test@test.ru")
                .birthDate(Date.valueOf("1999-06-05"))
                .active(true)
                .build();
        User user2 = new UserBuilder()
                .name("USER2")
                .surname("USER2")
                .email("user2@user2.ru")
                .birthDate(Date.valueOf("1998-06-05"))
                .active(true)
                .build();
        User savedUsed = userRepository.save(user);
        User savedUser2 = userRepository.save(user2);
        String filterNameParam = "TEST";

        //Act
        ResponseEntity<PageDTO<UserDTO>> response = restTemplate.exchange(
                "/api/v1/users/filtered?name={nameParam}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageDTO<UserDTO>>() {
                },
                filterNameParam
        );

        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalElements()).isEqualTo(1);
        assertThat(response.getBody().getContent().getFirst().getName()).isEqualTo(filterNameParam);

    }

    @Test
    void getAllUsersWithCardsFilteredBy_shouldReturnAllUsersWithCardsFilteredBy() {
        // Arrange
        User user = new UserBuilder()
                .name("AAAA")
                .surname("AAAA")
                .email("AAAA@test.ru")
                .birthDate(Date.valueOf("1999-06-05"))
                .active(true)
                .build();
        User savedUsed = userRepository.save(user);
        PaymentCard paymentCard1 = new PaymentCard(null, null, "2222222222222222", "ASD ASD", Date.valueOf("2028-01-01"), true, null, null);
        PaymentCard paymentCard2 = new PaymentCard(null, null, "3222222222222222", "ASD ASD", Date.valueOf("2028-01-01"), true, null, null);
        savedUsed.addPaymentCard(paymentCard1);
        savedUsed.addPaymentCard(paymentCard2);
        User savedUser1 = userRepository.save(savedUsed);

        User user2 = new UserBuilder()
                .name("BBBB")
                .surname("BBBB")
                .email("BBBB@test.ru")
                .birthDate(Date.valueOf("2005-09-09"))
                .active(true)
                .build();
        User savedUsed2 = userRepository.save(user2);
        PaymentCard user2paymentCard1 = new PaymentCard(null, null, "5555555555555555", "ASD ASD", Date.valueOf("2028-01-01"), true, null, null);
        PaymentCard user2paymentCard2 = new PaymentCard(null, null, "6666666666666666", "ASD ASD", Date.valueOf("2028-01-01"), true, null, null);
        PaymentCard user2paymentCard3 = new PaymentCard(null, null, "7777777777777777", "ASD ASD", Date.valueOf("2028-01-01"), true, null, null);
        savedUsed2.addPaymentCard(user2paymentCard1);
        savedUsed2.addPaymentCard(user2paymentCard2);
        savedUsed2.addPaymentCard(user2paymentCard3);
        User savedUser2 = userRepository.save(savedUsed2);
        String filterNameParam = "BBBB";

        //Act
        ResponseEntity<PageDTO<UserDTOWIthCards>> response = restTemplate.exchange(
                "/api/v1/users/filtered/with-cards?name={nameParam}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageDTO<UserDTOWIthCards>>() {
                },
                filterNameParam
        );

        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalElements()).isEqualTo(1);
        assertThat(response.getBody().getContent().getFirst().getName()).isEqualTo(filterNameParam);
        assertThat(response.getBody().getContent().getFirst().getCards().size()).isEqualTo(3);

    }

    @Test
    void updateUserById_shouldReturnUpdatedUser() {
        // Arrange
        User user = new UserBuilder()
                .name("TEST")
                .surname("TEST")
                .email("test@test.ru")
                .birthDate(Date.valueOf("1999-06-05"))
                .active(true)
                .build();
        User savedUsed = userRepository.save(user);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);


        UserDTO userDTOUpdate = new UserDTO();
        userDTOUpdate.setName("ChangedName");
        userDTOUpdate.setSurname("ChangedSurname");
        userDTOUpdate.setEmail("changedEmail@terst.ru");
        userDTOUpdate.setBirthDate(Date.valueOf("1990-01-01"));
        userDTOUpdate.setActive(false);

        HttpEntity<UserDTO> requestEntity = new HttpEntity<>(userDTOUpdate, httpHeaders);


        //Act
        ResponseEntity<UserDTO> response = restTemplate.exchange(
                "/api/v1/users/{id}",
                HttpMethod.PUT,
                requestEntity,
                UserDTO.class,
                savedUsed.getId()
        );

        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(savedUsed.getId());
        assertThat(response.getBody().getEmail()).isEqualTo(userDTOUpdate.getEmail());
        assertThat(response.getBody().getName()).isEqualTo(userDTOUpdate.getName());
        assertThat(response.getBody().getSurname()).isEqualTo(userDTOUpdate.getSurname());
        assertThat(response.getBody().getBirthDate().toLocalDate()).isEqualTo(userDTOUpdate.getBirthDate().toLocalDate());

    }

    @Test
    void deactivateUserById_shouldReturnUpdatedUser() {
        // Arrange
        User user = new UserBuilder()
                .name("TEST")
                .surname("TEST")
                .email("test@test.ru")
                .birthDate(Date.valueOf("1999-06-05"))
                .active(true)
                .build();
        User savedUsed = userRepository.save(user);


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserDTO> requestEntity = new HttpEntity<>(httpHeaders);


        //Act
        ResponseEntity<UserDTO> response = restTemplate.exchange(
                "/api/v1/users/{id}/deactivate",
                HttpMethod.PATCH,
                requestEntity,
                UserDTO.class,
                savedUsed.getId()
        );

        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(savedUsed.getId());
        assertThat(response.getBody().isActive()).isEqualTo(false);


    }

    @Test
    void activateUserById_shouldReturnUpdatedUser() {
        // Arrange
        User user = new UserBuilder()
                .name("TEST")
                .surname("TEST")
                .email("test@test.ru")
                .birthDate(Date.valueOf("1999-06-05"))
                .active(false)
                .build();
        User savedUsed = userRepository.save(user);


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserDTO> requestEntity = new HttpEntity<>(httpHeaders);


        //Act
        ResponseEntity<UserDTO> response = restTemplate.exchange(
                "/api/v1/users/{id}/activate",
                HttpMethod.PATCH,
                requestEntity,
                UserDTO.class,
                savedUsed.getId()
        );

        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(savedUsed.getId());
        assertThat(response.getBody().isActive()).isEqualTo(true);


    }


}
