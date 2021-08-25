package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.argumentresolver.Login;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {


    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

//    @GetMapping("/")
    public String home() {
        return "home";
    }

//    @GetMapping("/")
    public String homeLogin(@CookieValue(name="memberId",required = false) Long memberId, Model model) {

        if(memberId == null){
            return "home";
        }

        //로그인
        Member loginMember = memberRepository.findById(memberId);
        if(loginMember == null){
            return "home";
        }

        model.addAttribute("member",loginMember);
        return "loginHome";
    }



//    @GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Long memberId, Model model) {


        //세션 관리자에에 저장된 회원 정보 조회
        Object member = (Member) sessionManager.getSession(request);

        if(member == null){
            return "home";
        }

        model.addAttribute("member",member);
        return "loginHome";
    }





//    @GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Long memberId, Model model) {

        HttpSession session = request.getSession(false);
        if(session == null){
            return "home";
        }

        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        //세션 관리자에에 저장된 회원 정보 조회
//        Object member = (Member) sessionManager.getSession(request);

        if(member == null){
            return "home";
        }

        model.addAttribute("member",member);
        return "loginHome";
    }

//    @GetMapping("/")
    public String homeLoginV3Spring(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member member, Model model) {

//        HttpSession session = request.getSession(false);
//        if(session == null){
//            return "home";
//        }

//        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        //세션 관리자에에 저장된 회원 정보 조회
//        Object member = (Member) sessionManager.getSession(request);

        if(member == null){
            return "home";
        }

        model.addAttribute("member",member);
        return "loginHome";
    }

    @GetMapping("/")
    public String homeLoginV3SpringArgumentResolver(@Login Member member, Model model) {
        /*
         * 이번엔 아규먼트리졸버를 통해서 주입받는 방법
         */

        if(member == null){
            return "home";
        }

        model.addAttribute("member",member);
        return "loginHome";
    }
}