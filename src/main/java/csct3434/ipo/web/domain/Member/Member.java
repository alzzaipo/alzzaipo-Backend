package csct3434.ipo.web.domain.Member;

import csct3434.ipo.web.domain.BaseTimeEntity;
import csct3434.ipo.web.domain.Portfolio.Portfolio;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.engine.internal.CascadePoint;
import org.springframework.transaction.annotation.Transactional;

import javax.sound.sampled.Port;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String nickname;

    private String email;

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Portfolio> portfolios = new ArrayList<>();

    @Builder
    public Member(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }

    public void addPortfolio(Portfolio portfolio) {
        this.portfolios.add(portfolio);
        portfolio.setMember(this);
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }
}
