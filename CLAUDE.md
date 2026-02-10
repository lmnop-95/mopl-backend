# MOPL Backend

콘텐츠 큐레이션 플랫폼 백엔드. DDD 모듈러 모놀리식.

## Architecture

```
applications/   — 실행 가능한 Spring Boot 앱 (api, websocket, sse, batch, scheduler, consumer)
core/           — 도메인 로직 (POJO 중심, 프레임워크 비의존)
shared/         — 공통 모듈 (exception, pagination, event, util)
infrastructure/ — 기술 구현 (persistence, redis, kafka, mail, storage, external-api)
```

- Core 모듈은 Infrastructure에 의존하지 않는다 (Port/Adapter 패턴)
- 모듈 간 통신은 도메인 이벤트로 처리
- User, Content만 soft delete. FK constraint 없음

## Tech Stack

- Java 25, Spring Boot 4.x, Gradle 9.x
- JPA, Redis, Kafka, PostgreSQL
- JWT 인증 (access token + refresh token cookie)
- CSRF: cookie `XSRF-TOKEN`, header `X-XSRF-TOKEN`

## Build & Test

```bash
./gradlew build                    # 전체 빌드 (format + style + bug analysis + test)
./gradlew test                     # 테스트만
./gradlew spotlessApply            # 포맷 자동 적용
./gradlew :applications:api:bootRun  # API 서버 실행
```

## Conventions

- buildSrc 컨벤션 플러그인 패턴 사용 (allprojects/subprojects 사용 금지)
- Lombok 표준 사용 (@Getter, @Builder, @RequiredArgsConstructor)
- 커서 기반 페이지네이션 (CursorRequest/CursorResponse)
- TDD로 개발
- `settings.gradle.kts`의 include() 블록은 모듈별로 분리

## Java 25 / Spring Boot 4.x 활용 지침

코드 작성 시 항상 최신 기능을 우선 사용할 것:

- **Record**: DTO, 값 객체는 class 대신 record 사용
- **Sealed interface**: 도메인 이벤트, 에러 코드 등 타입 제한이 필요한 곳에 사용
- **Pattern matching**: `instanceof` 체크 시 패턴 변수 사용, switch expression 활용
- **String templates**: 문자열 조합 시 STR 템플릿 사용 (preview 활성화 시)
- **RestClient**: 외부 API 호출 시 WebClient 대신 RestClient 사용
- **Virtual threads**: 이미 활성화됨 (`spring.threads.virtual.enabled=true`). 별도 스레드풀 생성 지양
- **Structured concurrency**: 병렬 I/O 작업 시 활용 (preview 활성화 시)

## Module Dependency Rules

- `applications/*` → `core/*`, `shared/*`, `infrastructure/*`
- `core/*` → `shared/common` (only)
- `infrastructure/*` → `core/*`, `shared/*`
- `shared/common` → 외부 의존 없음 (순수 Java + Lombok)

## API Spec

- OpenAPI 명세: `docs/api-docs.json`
- HTTP 테스트: `http/` 디렉토리 (IntelliJ HTTP Client)
