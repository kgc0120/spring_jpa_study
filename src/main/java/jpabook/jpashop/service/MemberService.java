package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // jpa가 조회 같은 경우는 readOnly true로 하면 조금 더 최적화 한다.
// class에 @Transactional 애노테이션을 주면 public 메소드에는 기본적으로 다 적용된다.
// 데이터 수정같은 경우는 따로 @Transactional를 붙여준다. readOnly true가 되면 데이터 수정 안됨.
@RequiredArgsConstructor // lombok을 사용해서 MemberService final로 선언된 필드에 대해서 생성자 만들어준다. public MemberService(MemberRepository memberRepository) 생략가능
public class MemberService {


    private final MemberRepository memberRepository; // @Autowired field injection, 권장하지 않음
    // final로 해서 컴파일 시점에 체크와 수정되지 않도록 하는것을 권장

//    @Autowired // 생략가능 spring에 생성자가 하나일 경우 알아서 injection 해준다.
//    public MemberService(MemberRepository memberRepository) { // 생정자 injection, 권장장 중간에 set해서 바꿀수 없다.
//        this.memberRepository = memberRepository;
//    }

//    public static void main(String[] args) {
//        MemberService memberService = new MemberService(); // 테스트 케이스 작성할 때 명확하게 무엇을 주입하는지 알 수 있다.
//    }

    /**
     * 회원 가입
     */
    @Transactional
    public Long join(Member member) {

        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName()); // 멀티쓰레드나 여러 was에 동시적으로 insert 하는 경우도 발생할 수 있다.
        // 실무에서는 name 칼럼에 db에 유니크 제약조건을 걸어서 한 번 더 확인을 한다.
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name){
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }

}
