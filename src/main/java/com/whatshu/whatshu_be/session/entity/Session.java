package com.whatshu.whatshu_be.session.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Session {
    // 복잡한 @Id, @Column 등의 어노테이션이 모두 빠집니다.
    private Long sessionId;
    private String title;
    private LocalDateTime date;
    private String type;
    private String description;
    private Byte cohortNo;
}