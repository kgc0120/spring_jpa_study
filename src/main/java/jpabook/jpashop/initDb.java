package jpabook.jpashop;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class initDb {

    private final InitService initeService;

    @PostConstruct
    // Spring Been이 모두 올라오면 PostConstruct 호출된다.
    // dbInit1 구현체를 init() 메소드 안에 바로 넣을 수 있을 것 같은데?
    // 잘 안된다. 라이플 사이클이 있어서 transaction 같은 기능이 안먹힌다. 별도의 Benn으로 등록 필요
    public void init() {
        initeService.dbInit1();
        initeService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        private final EntityManager em;

        public void dbInit1() {
            Member member = createMember("userA", "서울", "1", "1111");
            em.persist(member);

            Book book1 = createBook("JPA1 BOOK", 10000, 100);
            em.persist(book1);

            Book book2 = createBook("JPA2 BOOK", 20000, 100);
            em.persist(book2);

            var orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            var orderItem2 = OrderItem.createOrderItem(book2, 20000, 1);

            Delivery delivery = createDelivery(member);
            var order = Order.creatOrder(member, delivery, orderItem1, orderItem2);

            em.persist(order);
        }

        public void dbInit2() {
            Member member = createMember("userB", "순천", "2", "2222");
            em.persist(member);

            Book book1 = createBook("SPRING1 BOOK", 30000, 200);
            em.persist(book1);

            Book book2 = createBook("SPRING2 BOOK", 40000, 300);
            em.persist(book2);

            var orderItem1 = OrderItem.createOrderItem(book1, 20000, 2);
            var orderItem2 = OrderItem.createOrderItem(book2, 40000, 5);

            Delivery delivery = createDelivery(member);
            var order = Order.creatOrder(member, delivery, orderItem1, orderItem2);

            em.persist(order);
        }

    }

    private static Delivery createDelivery(Member member) {
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        return delivery;
    }

    private static Book createBook(String name, int price, int stockQuantity) {
        Book book1 = new Book();
        book1.setName(name);
        book1.setPrice(price);
        book1.setStockQuantity(stockQuantity);
        return book1;
    }

    private static Member createMember(String name, String city, String street, String zipcode) {
        Member member = new Member();
        member.setName(name);
        member.setAddress(new Address(city, street, zipcode));
        return member;
    }

}
