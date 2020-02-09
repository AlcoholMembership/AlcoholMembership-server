package us.dev.backend.userInfo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, String> {

    Optional<UserInfo> findByQrid(String qrid);

}
