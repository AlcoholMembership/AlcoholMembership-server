package us.dev.backend.configs;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.dev.backend.common.BaseControllerTest;
import us.dev.backend.common.TestDescription;


public class JasycriptConfigTest extends BaseControllerTest {

    @Autowired
    JasycriptInfo jasycriptInfo;


    @Test
    @TestDescription("암호화 테스트")
    public void jasycriptTest() {
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setAlgorithm("PBEWithMD5AndDES");
        pbeEnc.setPassword(jasycriptInfo.getDbUser());

        String url = jasycriptInfo.getDbPath();
        String username = jasycriptInfo.getDbUser();
        String password = jasycriptInfo.getDbPwd();

        System.out.println("기존  URL :: " + url + " | 변경 URL :: " + pbeEnc.encrypt(url));
        System.out.println("기존  username :: " + username + " | 변경 username :: " + pbeEnc.encrypt(username));
        System.out.println("기존  password :: " + password + " | 변경 password :: " + pbeEnc.encrypt(password));
        System.out.println();

    }

}