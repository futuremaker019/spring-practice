package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        // 빈 화면이지만 new MemberForm()을 보내서 Validation 같은 역할을 하게 만들어준다.
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    // @Valid 를 사용하면 MemberForm에 선언된 @NotEmpty를 확인해준다.
    // @NotEmpty 뿐만아니라 다양한 어노테이션을 관리한다. javax.validation 확인가능
    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {

        // BindingResult가 에러를 캐치해준다. BindingResult를 사용하지 않으면 whitelabel page로 이동하게 된다.
        // 에러가 있다면 해당 if문을 타고 들어간다.
        // thymeleaf-springboot 라이브러리로 인해 memebrs/createMemberForm으로 돌아간다.
        // 에러를 해당 폼에서 받아 에러메시지를 출력해준다. fields.hasErrors, th:errors="*{name}
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);

        return "redirect:/";
    }
}
