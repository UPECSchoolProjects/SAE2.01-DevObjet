import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.Requestcontroller;

@RestController
public class Controller {

    @RequestMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    @RequestMapping("/hello/{name}")
    public String helloName(@PathVariable String name) {
        return "Hello " + name;
    }

}
