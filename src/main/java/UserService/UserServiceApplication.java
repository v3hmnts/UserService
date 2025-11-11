package UserService;

import UserService.entity.PaymentCard;
import UserService.entity.User;
import UserService.mapper.UserMapper;
import UserService.repository.PaymentCardRepository;
import UserService.repository.UserRepository;
import UserService.service.IPaymentCardService;
import UserService.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.sql.Date;
import java.util.ArrayList;


@SpringBootApplication
@EnableJpaRepositories
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
