package us.dev.backend.common;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* 어디에 붙일 건지 */
@Target(ElementType.METHOD)
/* 어디까지 적용할 것인지 */
@Retention(RetentionPolicy.SOURCE)
public @interface TestDescription {

    //value라는 함수 인터페이스가 원래 있나봄.
    String value();
}
