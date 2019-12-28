package us.dev.backend.UserInfo;


import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/*
* HETEOAS
* */
public class UserInfoResource extends Resource<UserInfo> {

    public UserInfoResource(UserInfo userInfo, Link... links) {
        super(userInfo, links);
        //add(new Link("http://localhost:8080/api/events/"+event.getId())); 같은 거임.
        add(linkTo(UserInfoController.class).slash(userInfo.getQrid()).withSelfRel());

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
