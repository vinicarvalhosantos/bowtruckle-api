package br.com.santos.vinicius.bowtruckleapi;

import br.com.santos.vinicius.bowtruckleapi.singleton.StreamSingleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class BowtruckleApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BowtruckleApiApplication.class, args);
        StreamSingleton stream = StreamSingleton.getInstance();

        stream.startMainStreamEvents();
    }

}
