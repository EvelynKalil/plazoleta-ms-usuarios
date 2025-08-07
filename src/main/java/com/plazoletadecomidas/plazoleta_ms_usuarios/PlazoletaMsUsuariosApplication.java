package com.plazoletadecomidas.plazoleta_ms_usuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class PlazoletaMsUsuariosApplication {

	public static void main(String[] args) {
		try {
			Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
			System.setProperty("spring.datasource.url", dotenv.get("DB_URL"));
			System.setProperty("spring.datasource.username", dotenv.get("DB_USERNAME"));
			System.setProperty("spring.datasource.password", dotenv.get("DB_PASSWORD"));

			SpringApplication.run(PlazoletaMsUsuariosApplication.class, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
