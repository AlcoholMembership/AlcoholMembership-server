# AlcoholMembership-server
## Init
~~~
0. 기본 의존성
    - Spring Web, Spring HATEOAS, Spring JPA, H2, PostgreSQL, Lombok
1. pom.xml 복사 붙여넣기
2. Test DB 분리하기.
    - main/resource/application.properties
    - test/resource/application-test.properties
    - Project Structure/Module/application-test를 test scope에 추가.
~~~


# 시큐리티 매커니즘
1. 회원가입
    - Android에서 userinfo[post]로 유저생성 <br>
        1) qr_id
        2) 
2. if(생성 성공하면) 비밀번호를 설정하면서 인증을 진행. 
    - ClientId/ClientSecret, qr_id, password로 인증진행
3. 토큰 발급, Android에게 토큰 리턴
4. Android는 해당 access_token으로 인증하게됨.
    
     