package io.demo.danggn.service;


import io.demo.danggn.entity.Member;
import io.demo.danggn.dto.MemberRequest;
import io.demo.danggn.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void createMember(final MemberRequest request) {
        final Member member = new Member(request.name());
        memberRepository.save(member);
    }
}
