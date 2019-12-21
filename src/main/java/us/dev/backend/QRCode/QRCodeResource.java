package us.dev.backend.QRCode;


import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/*
* HETEOAS
* */
public class QRCodeResource extends Resource<QRCode> {

    public QRCodeResource(QRCode qrCode, Link... links) {
        super(qrCode, links);
        //add(new Link("http://localhost:8080/api/events/"+event.getId())); 같은 거임.
        add(linkTo(QRCodeController.class).slash(qrCode.getQrid()).withSelfRel());

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
