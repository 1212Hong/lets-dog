package com.ssafy.dog.domain.dog.model;

public enum DogDisposition {
	FRIENDLY("친화적", "다른 강아지나 사람들에게 친화적으로 접근하는 성격"),
	PROTECTIVE("수호적", "주인이나 자신의 영역을 지키려는 성격"),
	ACTIVE("활동적", "많은 활동량과 놀이를 요구하는 성격"),
	CALM("조용한", "평온하고 안정된 성격"),
	RESERVED("내성적", "생소한 환경이나 사람들에게 조심스러운 성격"),
	ALERT("주의 깊은", "주변 환경에 민감하고 경계하는 성격"),
	AGGRESSIVE("도발적", "갑작스런 반응이나 공격성을 보이는 성격"),
	TIMID("무서워하는", "쉽게 놀라고 주변에 조심스러운 성격"),
	DEPENDENT("종속적", "주인이나 알려진 환경에 강하게 의존하는 성격"),
	INDEPENDENT("독립적", "혼자서도 잘 지내는, 주인에게 크게 의존하지 않는 성격"),
	CURIOUS("호기심 많은", "탐구하는 성격, 새로운 것에 관심을 많이 가짐"),
	DOMINANT("지배적", "리더십을 지니고 다른 강아지나 사람을 지배하려는 성격"),
	SUBMISSIVE("순종적", "유순하고 복종하는 성격"),
	SKITTISH("놀라기 쉬운", "쉽게 놀라는 성격"),
	PLAYFUL("놀이성 좋은", "놀이를 좋아하고 주변 환경과 쉽게 적응하는 성격"),
	PERSISTENT("집요한", "어떤 것에 목표를 정하면 끝까지 추구하는 성격"),
	AFFECTIONATE("애정 깊은", "주인이나 사람들에게 애정을 많이 보이는 성격"),
	TRAINABLE("훈련 받기 쉬운", "교육이나 훈련을 잘 받아들이는 성격"),
	INQUISITIVE("창의적", "새로운 물건이나 환경에 호기심을 가지고 접근하는 성격"),
	LAIDBACK("느긋한", "천천히, 느긋하게 행동하는 성격");

	private final String key;
	private final String description;

	DogDisposition(String key, String description) {
		this.key = key;
		this.description = description;
	}

	public String getKey() {
		return key;
	}

	public String getDescription() {
		return description;
	}
}
