import random
from faker import Faker

fake = Faker('ko_KR')

# 한국의 위도와 경도 범위
lat_range = (33, 43)
long_range = (124, 132)

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
        picture = '❤️ 💔 💌 💕 💞 💓 💗 💖 💘 💝 💟 💜 💛 💚 💙'
        password = fake.password(length=12)
        address = fake.address()
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
