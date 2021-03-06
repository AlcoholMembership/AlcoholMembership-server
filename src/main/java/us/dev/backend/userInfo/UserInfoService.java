package us.dev.backend.userInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserInfoService implements UserDetailsService {

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    /* UserInfo를 저장 할 때, 패스워드를 암호화하여 저장함. */
    public UserInfo saveUserInfo(UserInfo userInfo) {
        userInfo.setPassword(this.passwordEncoder.encode(userInfo.getPassword()));
        return this.userInfoRepository.save(userInfo);
    }

    @Override
    public UserDetails loadUserByUsername(String qrid) throws UsernameNotFoundException {
        /* input 해당하는 Qrid 찾는다. 없으면 orElseThrow로 Exception을 보기좋게 출력함 */
        UserInfo userInfo = userInfoRepository.findByQrid(qrid)
                .orElseThrow(()->new UsernameNotFoundException(qrid));

        /* UserInfo Object -> Spring Security가 이해할 수 있는 UserDatails로 TypeCast */
        /* Token을 발급하기 위해, 아래 값으로 DB 내 고유식별값이 있는지 확인함. */
        return new User(userInfo.getQrid(),userInfo.getPassword(),authorities(userInfo.getRoles()));
    }

    private Collection<? extends GrantedAuthority> authorities(Set<UserRole> roles) {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                .collect(Collectors.toSet());
    }
}
