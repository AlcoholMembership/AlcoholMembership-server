package com.example.demo.events;


import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/*
* HETEOAS
* */
public class EventResource extends Resource<Event> {

    public EventResource(Event event, Link... links) {
        super(event, links);
        //add(new Link("http://localhost:8080/api/events/"+event.getId())); 같은 거임.
        add(linkTo(EventController.class).slash(event.getId()).withSelfRel());

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
