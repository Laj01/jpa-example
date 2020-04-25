package person;

import com.github.javafaker.Faker;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.ZoneId;
import java.util.List;

public class Main {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-example");

    private static Person randomPerson()
    {
        Address address = Address.builder()
                .country(Faker.instance().address().country())
                .state(Faker.instance().address().state())
                .city(Faker.instance().address().city())
                .zip(Faker.instance().address().zipCode())
                .streetAddress(Faker.instance().address().streetAddress())
                .build();
        Person person = Person.builder()
                .address(address)
                .dob(Faker.instance().date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .email(Faker.instance().internet().emailAddress())
                .name(Faker.instance().name().name())
                .profession(Faker.instance().company().profession())
                .gender(Faker.instance().options().option(Person.Gender.values()))
                .build() ;
        return person;
    }

    private static List<Person> getAllPerson()
    {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Person p", Person.class).getResultList();
        } finally {
            em.close();
        }
    }

    public static void main(String[] args) {
        EntityManager em = emf.createEntityManager();
        try
        {
            em.getTransaction().begin();
            for(int i = 0; i < 1000; i++)
            {
                em.persist(randomPerson());
            }
            em.getTransaction().commit();
        }
        finally {
            em.close();
        }

        for(Person item : getAllPerson())
        {
            System.out.println(item);
        }

    }
}