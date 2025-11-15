package es.pocketrainer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

// extendí SpringBootServletInitializer para que pueda hacer deploy como war en container, así como mantener el autoejecutable, va acomañado del metodo
// configure
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/**
	 * Si quisiera ejecutar un metodo al inicio de todo
	 */
//	@Bean
//	public CommandLineRunner initData(ProductoRepository productoRepository) {
//
//		return args -> {
//
//			// Rescatamos todos los productos
//			List<Producto> productos = productoRepository.findAll();
//			
//			Random r = new Random();
//
//			// Para cada uno de ellos
//			for (Producto p : productos) {
//				// Vamos a añadirle un número aleatorio de puntuaciones, entre 1 y 10
//				for (int i = 0; i < r.nextInt(10); i++)
//					// Lo valoramos con una puntuación aleatoria, entre 3 y 5.
//					p.addPuntuacion(new Puntuacion(3 + r.nextInt(2)));
//			}
//
//			// Actualizamos los productos, almacenando así su puntuación
//			productoRepository.saveAll(productos);
//
//		};
//
//	}

}
