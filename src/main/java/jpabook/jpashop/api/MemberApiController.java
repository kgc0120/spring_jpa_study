package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

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

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id")Long id,
            @RequestBody @Valid UpdateMemberRequest request) {

        // 커맨드랑 쿼리를 분리한다.
        // update 문에서 member 객체 자체를 반환할수도 있지만 그럴경우 커맨드랑 쿼리랑 같이 있게된다.
        // 하나의 데이터를 pk 기반으로 조회할 때는 전체적으로 성능에 영향을 주는게 미미하다.
        // 이럴경우 보다 나은 유지보수성을 위해서 커맨드랑 쿼리를 분리하는 것이 좋다.
        memberService.update(id, request.getName());
        Member member = memberService.findOne(id);
        return new UpdateMemberResponse(id, member.getName());
    }

    @GetMapping("/api/v2/members")
    public Result memberV2() {
        List<Member> members = memberService.findMembers();
        var collect = members.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());

        return new Result(collect.size() ,collect);

    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto{
        String name;
    }


    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    @Data
    static class UpdateMemberRequest {
        private Long id;
        private String name;
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
