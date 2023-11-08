package com.dog.data.model.matching

object DispositionMap {
    val map = mapOf(
        "FRIENDLY" to "친화적",
        "PROTECTIVE" to "수호적",
        "ACTIVE" to "활동적",
        "CALM" to "조용한",
        "RESERVED" to "내성적",
        "ALERT" to "주의 깊은",
        "AGGRESSIVE" to "도발적",
        "TIMID" to "무서워하는",
        "DEPENDENT" to "종속적",
        "INDEPENDENT" to "독립적",
        "CURIOUS" to "호기심 많은",
        "DOMINANT" to "지배적",
        "SUBMISSIVE" to "순종적",
        "SKITTISH" to "놀라기 쉬운",
        "PLAYFUL" to "놀이성 좋은",
        "PERSISTENT" to "집요한",
        "AFFECTIONATE" to "애정 깊은",
        "TRAINABLE" to "훈련 받기 쉬운",
        "INQUISITIVE" to "창의적",
        "LAIDBACK" to "느긋한"
    )

    fun getDisposition(key: String): String? {
        return map[key]
    }
}