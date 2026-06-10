package ar.edu.utn.dds.k3003;

import ar.edu.utn.dds.k3003.repositories.*;
import jakarta.persistence.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public Fachada fachada() {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("db");
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    return new Fachada(entityManager,transaction);
  }
}
