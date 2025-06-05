package com.example.bugonbe.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bugonbe.member.domain.Member;
import com.example.bugonbe.member.domain.ProviderType;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByAuthProviderProviderTypeAndAuthProviderProviderId(ProviderType providerType, String providerId);

	Optional<Member> findByEmail(String email);
}
