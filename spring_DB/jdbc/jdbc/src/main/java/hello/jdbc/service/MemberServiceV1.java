package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV1;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;

@RequiredArgsConstructor
public class MemberServiceV1 {
    private final MemberRepositoryV1 memberRepositoryV1;

    public void accountTransfer(String fromId,String toId,int money) throws SQLException {
        Member fromMember = memberRepositoryV1.findById(fromId);
        Member toMember = memberRepositoryV1.findById(toId);
        memberRepositoryV1.update(fromId,fromMember.getMoney()-2000);
        validation(toMember);
        memberRepositoryV1.update(toId,toMember.getMoney()+2000);

    }
    private void validation(Member member){
        if(member.getMemberId().equals("ex")){
            throw new IllegalStateException("이체 중 예외 발생");
        }
    }
}
