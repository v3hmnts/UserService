package UserService.builders;

import UserService.entity.PaymentCard;
import UserService.entity.User;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class UserBuilder {
    public Long id;
    private String name;
    private String surname;
    private java.sql.Date birthDate;
    private String email;
    private boolean active;
    private List<PaymentCard> cards = new ArrayList<>();

    public UserBuilder id(Long id){
        this.id=id;
        return this;    }

    public UserBuilder name(String name){
        this.name=name;
        return this;
    }
    public UserBuilder surname(String surname){
        this.surname=surname;
        return this;
    }
    public UserBuilder birthDate(Date birthDate){
        this.birthDate=birthDate;
        return this;
    }
    public UserBuilder email(String email){
        this.email=email;
        return this;
    }

    public UserBuilder active(Boolean active){
        this.active=active;
        return this;
    }

    public UserBuilder cards(List<PaymentCard> cards){
        this.cards=cards;
        return this;
    }

    public User build(){
        User user = new User(this.id,
                this.name,
                this.surname,
                this.birthDate,
                this.email,
                this.active);
        if(!cards.isEmpty()){
            cards.forEach(user::addPaymentCard);
        }
        return user;
    }

}
