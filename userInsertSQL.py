import random
from faker import Faker

fake = Faker('ko_KR')

# 한국의 위도와 경도 범위
lat_range = (35.126033, 35.227987)
long_range = (126.713671, 126.912103)

# SQL INSERT 문을 저장할 리스트
sql_inserts = []

# 이미 생성된 login_id와 nickname을 추적하기 위한 세트
used_login_ids = set()
used_nicknames = set()

# 랜덤한 자기소개 문구 생성을 위한 예시 문구들
about_me_examples = [
    "안녕하세요, 새로운 친구를 만나길 좋아합니다.",
    "여행을 좋아하고 다양한 음식을 시도해보는 것을 즐깁니다.",
    "자연에서 시간을 보내는 것을 좋아합니다.",
    "음악, 영화, 그리고 책을 좋아합니다.",
    # 여기에 더 많은 문구를 추가할 수 있습니다.
]

image_urls = [
    "https://img.freepik.com/premium-vector/cute-dog-cartoon-illustration_569774-126.jpg?w=740",
    "https://upload.wikimedia.org/wikipedia/commons/a/af/Cara_de_quem_caiu_do_caminh%C3%A3o..._%28cropped%29.jpg",
    "https://upload.wikimedia.org/wikipedia/commons/6/6e/Golde33443.jpg",
    "https://dimg.donga.com/wps/NEWS/IMAGE/2022/01/28/111500268.2.jpg",
    "https://mimg.segye.com/content/image/2022/05/23/20220523519355.jpg",
    "https://upload.wikimedia.org/wikipedia/commons/a/af/Cara_de_quem_caiu_do_caminh%C3%A3o..._%28cropped%29.jpg"
]

dong_info = {
    "동구": ["충장동", "동명동", "계림1동", "계림2동", "산수1동", "산수2동", "지산1동", "지산2동", "서남동", "학동", "학운동", "지원1동", "지원2동"],
    "서구": ["양동", "양3동", "농성1동", "농성2동", "광천동", "유덕동", "치평동", "상무1동", "상무2동", "화정1동", "화정2동", "화정3동", "화정4동", "서창동", "금호1동", "금호2동", "풍암동", "동천동"],
    "남구": ["양림동", "방림1동", "방림2동", "봉선1동", "봉선2동", "사직동", "월산동", "월산4동", "월산5동", "백운1동", "백운2동", "주월1동", "주월2동", "효덕동", "진월동", "송암동", "대촌동"],
    "북구": ["중흥1동", "중흥2동", "중흥3동", "중앙동", "임동", "신안동", "용봉동", "운암1동", "운암2동", "운암3동", "동림동", "우산동", "풍향동", "문화동", "문흥1동", "문흥2동", "두암1동", "두암2동", "두암3동", "삼각동", "일곡동"],
    "광산구": ["동림동", "산수동", "양동", "용봉동", "우산동"]
}


# 랜덤한 데이터를 생성하고 SQL INSERT 문을 만드는 함수
def generate_insert_statements(num_statements):
    for _ in range(num_statements):
        about_me = random.choice(about_me_examples)
        gender = random.choice(['FEMALE', 'MALE'])
        # 중복되지 않는 login_id 생성
        login_id = fake.unique.email()
        # 중복되지 않는 nickname 생성
        nickname = fake.unique.first_name()
        phone = '010' + fake.numerify(text='########')
        picture = random.choice(image_urls)
        password = fake.password(length=12)
        district, dongs = random.choice(list(dong_info.items()))
        dong = random.choice(dongs)
        address = f"광주광역시 {district} {dong}"
        latitude = random.uniform(*lat_range)
        longitude = random.uniform(*long_range)

        # SQL INSERT 문 생성
        insert_statement = f"""INSERT INTO user (user_about_me, user_gender, user_login_id, user_nickname, user_phone, user_picture, user_pw, user_address, user_latitude, user_longitude) VALUES ("{about_me}", '{gender}', '{login_id}', '{nickname}', '{phone}', '{picture}', '{password}', "{address}", {latitude}, {longitude});"""
        sql_inserts.append(insert_statement)


# SQL 문 생성
generate_insert_statements(100)

# 생성된 SQL 문을 출력
for statement in sql_inserts:
    print(statement)
