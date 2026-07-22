package ar.edu.utn.dds.k3003;

import ar.edu.utn.dds.k3003.service.Fachada;
import ar.edu.utn.dds.k3003.tools.DonadoresYEntidadesTools;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.persistence.*;
import org.hibernate.SessionFactory;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallbackProvider;
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
    org.hibernate.orm.micrometer.HibernateMetrics.monitor(
            meterRegistry,
            entityManagerFactory.unwrap(SessionFactory.class),
            "BaseDeDatosRender"
    );
    return new Fachada(entityManager);
  }

  @Bean
  public ToolCallbackProvider registrarToolsMcp(DonadoresYEntidadesTools tools) {
    return () -> ToolCallbacks.from(tools);
  }
}