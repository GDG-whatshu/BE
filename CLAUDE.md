# CLAUDE.md

이 마크다운 파일은 클로드 코드 등의 코딩 에이전트를 위해 전반적인 프로젝트의 구조 및 코딩 컨벤션을 제공합니다.

개별 사용자가 생성한 가이드라인은 `CLAUDE.local.md` 문서를 참고하십시오.

## 1. 프로젝트 소개 및 의존성 목록
### 프로젝트 소개
- 세션(Session)의 회원(Member)별 출석 현황을 관리하는 서비스의 백엔드 프로젝트입니다.
- 스프링 부트 4, 스프링 프레임워크 7을 사용합니다.
- JWT 방식의 인증을 사용합니다.
- API 스웨거 문서: `/swagger-ui.html`

### 의존성 목록
- Spring Web
- Spring Security
- Spring Boot Actuator
- Spring Boot DevTools
- H2 Database (로컬 테스트용)
- MyBatis
- Hibernate Validation
- Junit
- Lombok
- Springdoc-openapi

## 2. 브랜치 규칙 및 커밋 규칙
- 운영용 브랜치 `main`과 개발용 브랜치 `dev`로 나누어 운영합니다.
  - `main`과 `dev`에는 직접 푸쉬하지 않습니다.
- 항상 `dev`에서 브랜치를 분기하거나 pull하여 작업하며, PR의 병합 대상을 `dev`로 설정합니다.
- 브랜치명은 `feature/auth`와 같이 `작업 종류/핵심 키워드`로 짓습니다.
- 가능한 경우, `dev`에서 로컬 브랜치로 커밋을 가져올 때 `git pull --rebase`를 사용하여 커밋 히스토리를 일직선으로 정리합니다.
- 커밋 메시지는 `타입: 요약`으로 작성합니다.
  - 예1: `feat: 회원가입`
  - 예2: `fix: 관리자로 로그인하지 않고 기수를 생성할 수 있는 문제 수정`

## 3. 프로젝트 폴더 구조
```text
com.whatshu.whatshu_be
├── global             <-- [프로젝트 공통 영역]
|   ├── config         (전역에서 사용되는 설정. 예: Security, WebMvc, Swagger 등)
|   ├── common         (공통 객체. 예: BaseEntity, ApiResponse)
|   ├── util           (유틸리티. 예: DateUtil, StringUtil)
|   ├── filter         (전역에서 사용되는 필터. 예: JwtAuthenticationFilter)
|   └── error          (에러 핸들링. 예: GlobalExceptionHandler, ErrorCode)
|
├── domain1             <-- [도메인1]
|   ├── controller
|   ├── service
|   ├── mapper
|   ├── entity         
|   └── dto            (Data Transfer Object. 예: RegisterRequestDto)
|
├── domain2            <-- [도메인2]
|   ├── controller
|   ├── service
|   ├── mapper
|   ├── entity
|   └── dto
|
└── WhatShuBeApplication.java
```
- 도메인형 구조입니다.
- 필요한 경우, 패키지를 추가해도 좋습니다.

## 4. 코드 규칙
### 0) 공통 사항
- Lombok 어노테이션의 남용을 자제합니다.
  - 특히, `@Data` 어노테이션은 아예 사용하지 않습니다.
- `@Setter`를 사용하지 않고, 필드 값을 변경하는 비즈니스 로직 메소드를 추가합니다. 
- 줄바꿈, 들여쓰기를 사용하여 코드가 가로로 너무 길어지지 않게 합니다.
- 이름 규칙
  - 클래스
    - PascalCase를 사용합니다.
    - 해당 클래스의 역할로 끝냅니다. (예: `AuthController`, `AuthService`, `MemberMapper`)
      - 예외: `entity` 패키지에 소속된 클래스는 해당 규칙을 적용받지 않습니다. (예: `Member`, `Cohort`)
  - 패키지
    - 모든 문자를 소문자로 짓습니다.
    - 단수형을 사용하며, 여러 개의 단어가 결합되더라도 언더바(`_`)를 사용하지 않고 이어 붙입니다.
  - 변수, 메소드
    - camelCase를 사용합니다.
- 클래스의 body는 한 줄 공백을 두고 시작합니다.
- 의존성 주입 시 생성자 주입을 사용하며, `private final` 필드로 선언합니다.
  - 판단에 따라 적절한 경우 `@RequiredArgsConstructor`를 사용해도 좋습니다.
- 본 파트 및 아래에 없는 항목에 대해서는 사용자에게 물어보십시오.

### 1) 엔티티
- 이 프로젝트에서 엔티티는 MyBatis에 의하여 쿼리 결과가 매핑되는 POJO로 정의합니다.
- `@Getter`, `@NoArgsConstructor` 어노테이션을 붙입니다.

### 2) DTO
- 클래스 이름을 지을 때, 요청과 응답을 구분합니다.
  - 요청의 경우 `Request`, 응답의 경우 `Response`를 이름에 포함합니다.
    - 예: `SignupRequestDto`, `CohortRequestDto`
- 요청 DTO의 경우 `@Getter`, `@NoArgsConstructor` 어노테이션을 붙입니다.

### 3) 컨트롤러
- 컨트롤러는 항상 `ResponseEntity<ApiResponse>`를 반환합니다.
  - `ApiResponse`는 이 프로젝트에서 사용되는 표준 응답 객체입니다.
- 예외를 직접 컨트롤러에서 처리하지 않습니다.

### 4) 서비스
- DB 쓰기 작업이 있는 메소드에는 `@Transactional` 어노테이션을 붙입니다.