package com.whatshu.whatshu_be.global.common;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

/**
 * 프로젝트 전역에서 사용되는 공통 응답 본문
 *
 * {@snippet :
 * // 사용 예시
 * import org.springframework.http.ResponseEntity;
 * import org.springframework.web.bind.annotation.GetMapping;
 * import org.springframework.web.bind.annotation.RequestMapping;
 * import org.springframework.web.bind.annotation.RestController;
 *
 * @RestController
 * @RequestMapping("/")
 * public class ExampleController {
 *
 *     @GetMapping
 *     public ResponseEntity<CommonResponseBody<HelloWorldResponseDto>> helloWorld() {
 *          HelloWorldResponseDto data = HelloWorldResponseDto.of("Hello World!");
 *
 *          return ResponseEntity.ok(CommonResponseBody.success(data));
 *     }
 * }
 *}
 * @param <T> DTO 타입
 */
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class CommonResponseBody<T> {

    private final String status;
    private final T data;

    /**
     * 요청이 성공적으로 처리되었을 때의 응답 객체 생성
     * @param data 실제 DTO 객체
     * @return 응답 객체
     * @param <T> DTO 타입
     */
    public static <T> CommonResponseBody<T> success(T data) {
        return new CommonResponseBodyBuilder<T>()
                .status("success").data(data).build();
    }
}
