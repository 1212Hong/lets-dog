# 스테이지 1: 빌드 스테이지
FROM amazoncorretto:11 as build

# 어플리케이션의 jar 파일을 가져옵니다. 이 ARG는 Docker 빌드 시에 전달됩니다.
ARG JAR_FILE=build/libs/fileuploadapp.jar

# jar 파일을 컨테이너 내부의 app.jar로 복사합니다.
COPY ${JAR_FILE} fileuploadapp.jar

# target/dependency 디렉토리를 생성하고 app.jar를 해당 디렉토리에 압축 해제합니다.
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf /app.jar)

# 스테이지 2: 실행 스테이지
FROM amazoncorretto:11

# /tmp 디렉토리를 볼륨으로 설정합니다.
VOLUME /tmp

# Build 스테이지에서 생성한 target/dependency 디렉토리를 DEPENDENCY 환경 변수로 설정합니다.
ARG DEPENDENCY=/target/dependency

# 필요한 파일들을 Build 스테이지에서 생성한 디렉토리로부터 복사합니다.
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# 어플리케이션을 실행합니다.
ENTRYPOINT ["java", "-cp", "app:app/lib/*", "com.dog.fileupload.DogApplication"]
