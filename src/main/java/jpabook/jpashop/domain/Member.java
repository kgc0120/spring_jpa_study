package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @NotEmpty
    private String name;

    @Embedded // 내장타입
    private Address address;

    @OneToMany(mappedBy = "member") // Order Table에 있는 member 필드에 대해서 매핑이 된거다.
    // 연관관계 매핑의 주인은 FK를 가지고 있는쪽이 된다.
    // mappedBy하면 select만 가능
    @JsonIgnore
    private List<Order> orders = new ArrayList<>();



}
