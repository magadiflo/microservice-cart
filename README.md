# Microservicio Carrito de Compra
Tutorial tomado del libro: "Microservicios, un enfoque integrado"

# Sobre Hystrix y Resilience4j
Para las versiones iguales o inferiores a 2.3.12.RELEASE de Spring Boot se usa la dependencia de Hystrix

```
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>
```
Pero para las versiones actuales y superiores a 2.3.12.RELEASE de Spring Boot se usa Resilience4j  

```
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-circuitbreaker-resilience4j</artifactId>
</dependency>
```
Para trabajar con Resilience4j, lo podemos hacer con **anotaciones** o de **forma programática**.