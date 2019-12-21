package us.dev.backend;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;


@RestController
public class testController {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/test")
    public String greeting(@RequestParam(value = "name") String name) {
        System.out.println(name);
        String json ="{\"id\": 1, \"content\": \"1234\"}";

        Gson gson = new Gson();

        TestModel test = gson.fromJson(json, TestModel.class);
        System.out.println(test.getId());
        System.out.println(test.getContent());

        return "/test";
    }
}
