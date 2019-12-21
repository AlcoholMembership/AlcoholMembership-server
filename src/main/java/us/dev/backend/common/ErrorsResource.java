package us.dev.backend.common;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class ErrorsResource extends Resource<Errors> {
    public ErrorsResource(Errors content, Link...links) {
        super(content, links);
        //add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
        System.out.println("Test");
        System.out.println("test2");
    }
}
