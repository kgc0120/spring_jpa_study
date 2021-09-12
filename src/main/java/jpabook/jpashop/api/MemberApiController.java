package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
@AllArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/v1/members")
    public CreatMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreatMemberResponse(id);
    }

    @PostMapping("/api/v2/members")
    // Entity를 노출하면 사이드 임팩트 발생할 확율이 높다.
    // Entity 스팩이 바뀌면 api에도 큰 영향이 간다.
    // class 만들어서 client 요청을 받으면 Entity 스팩이 바뀔 때 compile 에러가 발생하고 어떤 값이 들어오는지 확인이 유리하다.
    public CreatMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {

        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreatMemberResponse(id);
    }

    @Data
    static class CreateMemberRequest {
        @NotEmpty
        private String name;
    }

    @Data
    static class CreatMemberResponse {
        private  Long id;

        public CreatMemberResponse(Long id) {
            this.id = id;
        }
    }

}
