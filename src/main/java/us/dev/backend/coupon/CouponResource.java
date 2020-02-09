package us.dev.backend.coupon;


import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/*
* HETEOAS
* */
public class CouponResource extends Resource<Coupon> {

    public CouponResource(Coupon coupon, Link... links) {
        super(coupon, links);
        //add(new Link("http://localhost:8080/api/events/"+event.getId())); 같은 거임.
        add(linkTo(CouponController.class).slash(coupon.getQrid()).withSelfRel());

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
