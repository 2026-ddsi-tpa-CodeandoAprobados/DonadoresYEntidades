package ar.edu.utn.dds.k3003;

import ar.edu.utn.dds.k3003.service.Fachada;
import io.micrometer.core.instrument.MeterRegistry;
import org.hibernate.stat.HibernateMetrics;
import jakarta.persistence.*;
import org.hibernate.SessionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public Fachada fachada(MeterRegistry meterRegistry, EntityManager entityManager, EntityManagerFactory entityManagerFactory) {
    HibernateMetrics.monitor(
            meterRegistry,
            entityManagerFactory.unwrap(SessionFactory.class),
            "BaseDeDatosRender"
    );
    return new Fachada(entityManager);
  }
}