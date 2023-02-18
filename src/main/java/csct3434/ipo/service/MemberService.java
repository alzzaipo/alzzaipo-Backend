package csct3434.ipo.service;

import csct3434.ipo.web.domain.Member.Member;
import csct3434.ipo.web.domain.Member.MemberRepository;
import csct3434.ipo.web.domain.Portfolio.Portfolio;
import csct3434.ipo.web.dto.PortfolioListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Member save(Member member) {
        return memberRepository.save(member);
    }

    public Member findMemberById(Long memberId) { return memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("해당 Member를 찾을 수 없습니다. id=" + memberId)); }

    @Transactional(readOnly = true)
    public List<Portfolio> getPortfolioListByMember(Member member) {
        return member.getPortfolios();
    }

    @Transactional(readOnly = true)
    public List<PortfolioListDto> getPortfolioListDtosByMemberId(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 Member를 찾을 수 없습니다. id=" + memberId));

        return member.getPortfolios().stream()
                .map(Portfolio::toDto)
                .collect(Collectors.toList());
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 Member를 찾을 수 없습니다. email=" + email));
    }
}
