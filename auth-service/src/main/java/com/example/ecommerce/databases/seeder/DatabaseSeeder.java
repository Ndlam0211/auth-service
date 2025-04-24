package com.example.ecommerce.databases.seeder;

import com.example.ecommerce.modules.user.entities.User;
import com.example.ecommerce.modules.user.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Component
public class DatabaseSeeder implements CommandLineRunner {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepo userRepo;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        if(isTableEmpty()){
            String passwordEncode = passwordEncoder.encode("password");

            // C1: Using Native SQL
//            entityManager.createNativeQuery("insert into users (user_catalogue_id, user_name, email, password, phone) values (?,?,?,?,?)")
//                    .setParameter(1, 1)
//                    .setParameter(2, "lamnd")
//                    .setParameter(3, "lam@gmail.com")
//                    .setParameter(4,passwordEncode)
//                    .setParameter(5, "0123456789")
//                    .executeUpdate();

            // C2: Using JPQL
//            User user = new User();
//            user.setUserCatalogueId(1L);
//            user.setUserName("lamnd");
//            user.setEmail("lam@gmail.com");
//            user.setPassword(passwordEncode);
//            user.setPhone("1234567890");
//
//            entityManager.persist(user);

            // C3: Using Spring Data JPA (Repository)
            User user = new User();
            user.setUserName("lamnd");
            user.setEmail("lam@gmail.com");
            user.setPassword(passwordEncode);
            user.setPhone("1234567890");

            userRepo.save(user);
        }

    }

    private boolean isTableEmpty(){
        Long count = (Long) entityManager.createQuery("select count(*) from User").getSingleResult();
        return count == 0;
    }
}
