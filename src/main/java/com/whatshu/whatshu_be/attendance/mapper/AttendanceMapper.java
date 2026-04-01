package com.whatshu.whatshu_be.attendance.mapper;

import org.apache.ibatis.annotations.*;

@Mapper
public interface AttendanceMapper {
    // 1. 현재 출석 상태 조회 (이미 출석했는지 확인용)
    @Select("SELECT status FROM attendances WHERE session_id = #{sessionId} AND member_id = #{memberId}")
    String getAttendanceStatus(@Param("sessionId") Long sessionId, @Param("memberId") Long memberId);

    // 2. 출석 상태 업데이트 (ABSENT -> PRESENT)
    @Update("UPDATE attendances SET status = 'PRESENT', comment = #{comment} WHERE session_id = #{sessionId} AND member_id = #{memberId} AND status = 'ABSENT'")
    void updateAttendanceStatus(@Param("sessionId") Long sessionId, @Param("memberId") Long memberId, @Param("comment") String comment);

    // 3. 게스트 중복 출석 검사 (해당 세션에 동일한 게스트 이름이 있는지 카운트)
    @Select("SELECT COUNT(*) FROM attendances WHERE session_id = #{sessionId} AND guest_name = #{guestName}")
    int countGuestAttendance(@Param("sessionId") Long sessionId, @Param("guestName") String guestName);

    // 4. 게스트 출석 데이터 추가 (member_id는 null로 들어가고, guest_name이 채워짐)
    @Insert("INSERT INTO attendances (session_id, guest_name, status, comment) VALUES (#{sessionId}, #{guestName}, 'PRESENT', #{comment})")
    void insertGuestAttendance(@Param("sessionId") Long sessionId, @Param("guestName") String guestName, @Param("comment") String comment);
}