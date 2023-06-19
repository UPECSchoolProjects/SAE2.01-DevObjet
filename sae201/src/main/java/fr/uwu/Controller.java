package fr.uwu;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@RestController
public class Controller {

    public static void main(String[] args) {
        SpringApplication.run(Controller.class, args);
    }

    @RequestMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    @RequestMapping("/hello/{name}")
    public String helloName(@PathVariable String name) {
        return "Hello " + name;
    }

}
