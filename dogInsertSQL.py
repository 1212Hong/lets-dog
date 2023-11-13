import random
from faker import Faker
# Python 스크립트로 SQL INSERT 문 생성하기

# 가정: dog_about_me, dog_name, dog_picture는 각각 고유한 값을 가진다고 가정합니다.

fake = Faker()

dispositions = [
    "FRIENDLY", "PROTECTIVE", "ACTIVE", "CALM", "RESERVED",
    "ALERT", "AGGRESSIVE", "TIMID", "DEPENDENT", "INDEPENDENT",
    "CURIOUS", "DOMINANT", "SUBMISSIVE", "SKITTISH", "PLAYFUL",
    "PERSISTENT", "AFFECTIONATE", "TRAINABLE", "INQUISITIVE", "LAIDBACK"
]

# 더미 이미지와 강아지 이름 리스트 (예시)
images = [f'image{i}.jpg' for i in range(1, 101)]
names = [f'개이름{i}' for i in range(1, 101)]
descriptions = [f'자기소개 {i}' for i in range(1, 101)]
breeds = ['잡종', '골든 리트리버', '치와와', '퍼그']  # 임의의 품종 예시
sizes = ['SMALL', 'MEDIUM', 'LARGE']

import random

sql_inserts = []
for i in range(1, 20):
    about_me = fake.sentence(nb_words=6)
    birthdate = f'2023-01-01'
    breed = random.choice(breeds)
    disposition_list = '|'.join(random.sample(dispositions, k=4))
    name = fake.first_name_nonbinary()
    picture = random.choice(images)
    size = random.choice(sizes)
    user_id = random.randint(1, 11)
    sql_inserts.append(
        f"INSERT INTO dog (dog_about_me, dog_birthdate, dog_breed, dog_disposition_list, dog_name, dog_picture, dog_size, user_id) VALUES "
        f"('{about_me}', '{birthdate}', '{breed}', '{disposition_list}', '{name}', '{picture}', '{size}', {user_id});"
    )

# 결과 출력
for insert_statement in sql_inserts:
    print(insert_statement)
