package com.magadiflo.cart.config;

import java.time.Duration;

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

@Configuration
public class AppConfig {

	// Para que el acceso a las instancias (Ejm. del microservice-products) sea
	// balanceado
	@LoadBalanced
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
		// El id, se aplicará a cualquier CircuitBreaker que tengamos en la aplicación.
		// Ahora mismo, nosotros tenemos en CartResource un CircuitBreaker al que le
		// llamamos "cards". En este caso, aquí se emitirá el identificador "cards"
		// que es este mismo id. En realidad se emitirán todos los identificadores 
		// de todos los CircuitBreaker de nuestro proyecto.
		// Ahora, si queremos personalizar uno en particular, podemos hacer un 
		// if(id.equals("cards")){...}, pero en este caso aplicaremos la configuración
		// a todos los CircuitBreakers
		
		// En el caso del CircuitBreakerConfig, lo pusimos en custom() para configurarlo, 
		// pero si queremos sus datos por defecto, usaremos el ofDefault(). Lo mismo ocurre
		// con el TimeLimiterConfig
		return factory -> factory.configureDefault(id -> { //factory, una expresión lambda, donde se configura por cada CircuitBreaker (en nuestro caso tenemos al que creamos como "cards")
			return new Resilience4JConfigBuilder(id)
					.circuitBreakerConfig(CircuitBreakerConfig.custom()
							.slidingWindowSize(10) // Tamaño de la ventana deslizante. Por defecto es 100 request, que será nuestro 100% de peticiones
							.failureRateThreshold(50) // Umbral de la taza de fallos. Por defecto es 50%. Si el 50% de peticiones falla, pasará a estado abierto (cortocircuito)
							.waitDurationInOpenState(Duration.ofSeconds(10L))// Tiempo de espera en estado abiero. Por defecto son 60 segundos
							.permittedNumberOfCallsInHalfOpenState(5) // Número de llamadas permitidas en estado semi abierto. Por defecto son 10 request
							.slowCallRateThreshold(50) // Porcentaje del umbral de llamadas lentas. Por defecto es 100%
							.slowCallDurationThreshold(Duration.ofSeconds(2L)) // Una llamada es lenta si la duración del request es mayor que lo definido en esta línea
							.build())
					.timeLimiterConfig(TimeLimiterConfig.custom()
							.timeoutDuration(Duration.ofSeconds(6l)) // Duración del timeOut permitido. Por defecto es 1 segundo. Si se pasa del valor definido se va a considerar como un error, es decir se considerará como parte de la taza de fallos
							.build()) 
					.build();
		});
	}

}
