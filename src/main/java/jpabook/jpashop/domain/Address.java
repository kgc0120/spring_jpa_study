package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

// 값 타입은 변경 불가능하게 설계해야 한다.
// @Setter를 제거하고, 생성자에서 값을 모두 초기화해서 변경 불가능한 클래스를 만드는 것이 좋다.
@Embeddable //내장타입
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    // jpa 구현 라이브러리가 객체를 생성할 때 리플렉션, 프록시 기술을 사용해야 하므로 기본 생성자가 필요하다.
    // 기본 생성자는 public은 아무나 사용할 수 있어 protected 까지는 허용한다.
    protected Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
