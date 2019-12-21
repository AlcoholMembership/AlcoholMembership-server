package us.dev.backend.Stamp;


import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/*
* HETEOAS
* */
public class StampResource extends Resource<Stamp> {

    public StampResource(Stamp stamp, Link... links) {
        super(stamp, links);
        //add(new Link("http://localhost:8080/api/events/"+event.getId())); 같은 거임.
        add(linkTo(StampController.class).slash(stamp.getQrid()).withSelfRel());

    }


    /*
    @JsonUnwrapped
    private Event event;
    
    public EventResource(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }
     */
}
