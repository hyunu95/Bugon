package com.example.bugonbe.member.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import com.example.bugonbe.auth.domain.RefreshToken;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "members")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nickname;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false, name = "profile_image_url")
	private String profileImageUrl;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "providerType", column = @Column(name = "provider_type", nullable = false)),
		@AttributeOverride(name = "providerId", column = @Column(name = "provider_id", nullable = false))
	})
	private AuthProvider authProvider;

	@Embedded
	private RefreshToken refreshToken;

	private LocalDateTime deletedAt;

	public boolean isDeleted() {
		return deletedAt != null;
	}

	public Member(String nickname, String email, String profileImageUrl, AuthProvider authProvider) {
		this.nickname = nickname;
		this.email = email;
		this.profileImageUrl = profileImageUrl;
		this.authProvider = authProvider;
	}

	public void updateRefreshToken(RefreshToken refreshToken) {
		this.refreshToken = refreshToken;
	}

	@Override
	public final boolean equals(Object o) {
		if (!(o instanceof Member member)) return false;
		return Objects.equals(id, member.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}
