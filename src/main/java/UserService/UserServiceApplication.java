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

    @Autowired
    UserRepository userRepository;

    @Bean
    public CommandLineRunner startup() {


        return args -> {
            User user1 = new User(null, "AAA", "AAA", new Date(813792585000L), "john.doe@email.com", new ArrayList<>(), true, null, null);
            User user2 = new User(null, "BBB", "AAA", new Date(813792585000L), "A.AA@email.com", new ArrayList<>(), true, null, null);
            User user3 = new User(null, "CCC", "AAA", new Date(813792585000L), "B.BB@email.com", new ArrayList<>(), true, null, null);
            User user4 = new User(null, "DDD", "AAA", new Date(813792585000L), "C.CC@email.com", new ArrayList<>(), true, null, null);
            User user5 = new User(null, "EEE", "AAA", new Date(813792585000L), "D.DD@email.com", new ArrayList<>(), true, null, null);
            User user6 = new User(null, "FFF", "AAA", new Date(813792585000L), "E.EE@email.com", new ArrayList<>(), true, null, null);


            PaymentCard pc1 = new PaymentCard(null, null, "1000100010001000", "ASD ASD", new Date(1768468200000L), true, null, null);
            PaymentCard pc3 = new PaymentCard(null, null, "2000100010001000", "ASD ASD", new Date(1768468200000L), true, null, null);
            PaymentCard pc4 = new PaymentCard(null, null, "3000100010001000", "ASD ASD", new Date(1768468200000L), true, null, null);
            PaymentCard pc5 = new PaymentCard(null, null, "4000100010001000", "ASD ASD", new Date(1768468200000L), true, null, null);
            PaymentCard pc6 = new PaymentCard(null, null, "5000100010001000", "ASD ASD", new Date(1768468200000L), true, null, null);
            PaymentCard pc7 = new PaymentCard(null, null, "6000100010001000", "ASD ASD", new Date(1768468200000L), true, null, null);
            user1.addPaymentCard(pc1);
            user2.addPaymentCard(pc3);
            user3.addPaymentCard(pc4);
            user4.addPaymentCard(pc5);
            user1.addPaymentCard(pc6);
            user1.addPaymentCard(pc7);

            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);
            userRepository.save(user4);
            userRepository.save(user5);
            userRepository.save(user6);
        };
    }

}
