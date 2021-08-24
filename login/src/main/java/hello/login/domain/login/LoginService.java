package hello.login.domain.login;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final MemberRepository memberRepository;

    public Member login(String loginId, String password){
        Optional<Member> findMemberOptional = memberRepository.findByLoginId(loginId);
        Member member = findMemberOptional.get();

//        if(member.getPassword().equals(password)){
//            return member;
//        }else{
//            return null;
//        }
        return findMemberOptional.filter(m-> m.getPassword().equals(password)).orElse(null);
        // 옵셔널 자체에 filter 나 stream 메서드 쌉가능
        // 패스워드와 같으면 해당 값을 쓰고 아니면 null을 반환해라 ! 라는 뜻.
    }
}
