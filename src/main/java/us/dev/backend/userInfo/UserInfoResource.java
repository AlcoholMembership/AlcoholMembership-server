package us.dev.backend.userInfo;


import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;


public class UserInfoResource extends Resource<UserInfo> {
    /*
        HATEOUS 부분.
        전체 Json 안에 보기 좋게 출력해줌
    */

    public UserInfoResource(UserInfo userInfo, Link... links) {
        super(userInfo, links);
        //add(new Link("http://localhost:8080/api/events/"+event.getId())); 같은 거임.
        add(linkTo(UserInfoController.class).withSelfRel());

    }

}
