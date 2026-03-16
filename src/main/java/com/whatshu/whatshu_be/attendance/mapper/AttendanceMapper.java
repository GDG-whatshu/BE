package com.whatshu.whatshu_be.attendance.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.whatshu.whatshu_be.attendance.entity.Attendance;

@Mapper
public interface AttendanceMapper {

    // 중복 출석 확인 (존재하면 true 반환)
    boolean existsBySessionIdAndUserId(@Param("sessionId") Long sessionId, @Param("userId") Long userId);

    // 출석 데이터 DB에 넣기
    void insertAttendance(Attendance attendance);
}