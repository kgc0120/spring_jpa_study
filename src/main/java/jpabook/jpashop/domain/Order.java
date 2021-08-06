package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY) // x: one은 기본이 EAGER이기 때문에 무조건 LAZY로 바꿔줘야 한다. n + 1 쿼리가 발생된다.
    @JoinColumn(name = "member_id") // 연관관계 주인에 JoinColumn
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>(); // 필드에거 바로 초기화하는 게 좋다.

//
//    persist(orderItemA)
//    persist(orderItemB)
//    persist(orderItemC)
//    persist(order)

//    CascadeType.ALL 사용하면 아래와 같이 한번만 persist 호출하면 된다.
//    persist(order)

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id") // 1 : 1 관계에서는 access가 많은 곳에 FK를 적용하는 것을 추천
    private Delivery delivery;

    private LocalDateTime orderDate; // 주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus state; // 주문상채 [ORDER, CANCEL]

    //===연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }



}
