package UserService;


import UserService.entity.PaymentCard;
import UserService.entity.User;
import UserService.repository.PaymentCardRepository;
import UserService.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Date;
import java.util.Optional;


@SpringBootApplication
@Transactional
public class UserServiceApplication {


	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Autowired
	UserRepository userRepository;

	@Autowired
	PaymentCardRepository paymentCardRepository;

	@Bean
	public CommandLineRunner startup() {


		return args -> {

			System.out.println("Running command line runner");
			System.out.println("==================");
		/*	System.out.println(userRepository.findAll(UserSpecification.hasNameEquals("David").and(UserSpecification.hasSurnameEquals(null))));
			System.out.println("==========");
			System.out.println(paymentCardRepository.count(PaymentCardSpecification.hasOwnerNameEquals("David")));
			System.out.println("=============");
			System.out.println(paymentCardRepository.getAllPaymentCardsByUserId(1L));
			System.out.println("===========");
			PaymentCard p = paymentCardRepository.findById(1L).orElseThrow(()->new Exception("Card not found"));
			p.setActive(!p.isActive());
			System.out.println("Found");
			System.out.println(p);
			System.out.println(paymentCardRepository.saveAndFlush(p));*/
			Optional<User> u = userRepository.findById(1L);
			if(u.isPresent()){
				var user = u.get();
				PaymentCard pc1 = new PaymentCard(18L,user,1000100010001000L,"ASD ASD",new Date(1768468200000L),true,null,null);
				PaymentCard pc2 = new PaymentCard(19L,user,1000200010001000L,"ASD ASD",new Date(1768468200000L),true,null,null);
				PaymentCard pc3 = new PaymentCard(20L,user,1000300010001000L,"ASD ASD",new Date(1768468200000L),true,null,null);
				PaymentCard pc4 = new PaymentCard(21L,user,1000400010001000L,"ASD ASD",new Date(1768468200000L),true,null,null);
				user.addPaymentCard(pc1);
				user.addPaymentCard(pc2);
				user.addPaymentCard(pc3);
				user.addPaymentCard(pc4);

			}
			System.out.println(paymentCardRepository.findAllWithUsers());



		};

	}

}
