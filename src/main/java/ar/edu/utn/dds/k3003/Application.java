package ar.edu.utn.dds.k3003;

import ar.edu.utn.dds.k3003.clients.DonacionesClient;
import ar.edu.utn.dds.k3003.clients.IncentivosClient;
import ar.edu.utn.dds.k3003.clients.LogisticaClient;
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
  public Fachada fachada(MeterRegistry meterRegistry,
                         EntityManager entityManager,
                         EntityManagerFactory entityManagerFactory,
                         DonacionesClient donacionesClient,
                         IncentivosClient incentivosClient,
                         LogisticaClient logisticaClient) {

    HibernateMetrics.monitor(
            meterRegistry,
            entityManagerFactory.unwrap(SessionFactory.class),
            "BaseDeDatosRender"
    );

    Fachada fachada = new Fachada(entityManager);
    fachada.setDonacionesClient(donacionesClient);
    fachada.setIncentivosClient(incentivosClient);
    fachada.setLogisticaClient(logisticaClient);

    return fachada;
  }
}