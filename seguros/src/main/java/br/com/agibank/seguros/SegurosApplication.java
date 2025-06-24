package br.com.agibank.seguros;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableRetry
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class SegurosApplication {

	public static void main(String[] args) {
		SpringApplication.run(SegurosApplication.class, args);
	}

}
