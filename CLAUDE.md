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
- TDD로 개발: 테스트 코드를 먼저 작성하고 프로덕션 코드를 작성. 프로덕션 코드를 먼저 구상한 뒤 그것을 통과시키기 위한 테스트를 작성하지 않도록 주의
- `settings.gradle.kts`의 include() 블록은 모듈별로 분리
- 클래스 어노테이션 순서: 메타/도구(`@Generated`) → Lombok 유틸(`@Slf4j`) → Lombok 구조(`@RequiredArgsConstructor`) → Spring 스테레오타입(`@Service`, `@RestController`)
- 다중 파라미터 줄바꿈: 120자 이내면 한 줄, 초과하면 파라미터마다 한 줄씩
- 시간 표현은 `Instant` 사용 (`LocalDateTime` 사용 금지)
- 코드 작성·수정 후 항상 확인: 어노테이션·메서드·필드 선언 순서, 관련 코드 간 순서 일관성, 줄바꿈의 자연스러움
- 코드 작성 완료 후 `./gradlew spotlessApply` 실행하여 import 정리 + 코드 포맷팅 적용

## Java 25 / Spring Boot 4.x 활용 지침

코드 작성 시 항상 최신 기능을 우선 사용할 것:

- **Record**: DTO, 값 객체는 class 대신 record 사용
- **Sealed interface**: 도메인 이벤트, 에러 코드 등 타입 제한이 필요한 곳에 사용
- **Pattern matching**: `instanceof` 체크 시 패턴 변수 사용, switch expression 활용
- **Flexible Constructor Bodies**: `super()` 호출 전에 유효성 검증·필드 초기화 가능. static 헬퍼 메서드 불필요
- **Stream Gatherers**: 배치 처리, 윈도우 연산 시 `Gatherers.windowFixed()`, `windowSliding()` 활용
- **Scoped Values**: Virtual thread 환경에서 `ThreadLocal` 대신 `ScopedValue` 사용
- **Virtual threads**: 이미 활성화됨 (`spring.threads.virtual.enabled=true`). 별도 스레드풀 생성 지양
- **@HttpExchange**: 외부 API 호출 시 선언적 HTTP 클라이언트 인터페이스 사용 (RestClient 기반)
- **JSpecify Null Safety**: `@NonNull`, `@Nullable`로 null 안전성 명시
- **API 버저닝**: `@GetMapping(version = "1.0")` — URL 하드코딩 없이 선언적 버저닝
- **Observability**: `spring-boot-starter-opentelemetry` + `@Observed`로 메트릭·트레이스 자동 수집
- **Auto-Configuration 모듈 분리**: starter POM 동일 사용, 내부적으로 필요한 auto-configuration만 로드

## Module Dependency Rules

- `applications/*` → `core/*`, `shared/*`, `infrastructure/*`
- `core/*` → `shared/common` (only)
- `infrastructure/*` → `core/*`, `shared/*`
- `shared/common` → 외부 의존 없음 (순수 Java + Lombok + JSpecify)

## API Spec

- OpenAPI 명세: `docs/api-docs.json`
- HTTP 테스트: `http/` 디렉토리 (IntelliJ HTTP Client)
