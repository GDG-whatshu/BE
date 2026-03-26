package com.whatshu.whatshu_be.attendance.mapper;

import org.apache.ibatis.annotations.*;

@Mapper
public interface AttendanceMapper {
    // 1. 현재 출석 상태 조회 (이미 출석했는지 확인용)
    @Select("SELECT status FROM attendances WHERE session_id = #{sessionId} AND member_id = #{memberId}")
    String getAttendanceStatus(@Param("sessionId") Long sessionId, @Param("memberId") Long memberId);

    // 2. 출석 상태 업데이트 (ABSENT -> ATTEND)
    @Update("UPDATE attendances SET status = 'ATTEND', comment = #{comment} WHERE session_id = #{sessionId} AND member_id = #{memberId}")
    void updateAttendanceStatus(@Param("sessionId") Long sessionId, @Param("memberId") Long memberId, @Param("comment") String comment);
}