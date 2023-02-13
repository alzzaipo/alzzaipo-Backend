package csct3434.ipo.service;

import csct3434.ipo.web.domain.Member.Member;
import csct3434.ipo.web.domain.Member.MemberRepository;
import csct3434.ipo.web.domain.Portfolio.Portfolio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Member save(Member member) {
        return memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public Member findByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElse(null);

        if(member == null) {
            log.warn("member not found by email : " + email);
        } else {
            log.warn("member found by email : " + email);
        }

        return member;
    }

    public List<Portfolio> getPortfoliosByMember(Member member) {
        return member.getPortfolios();
    }
}
