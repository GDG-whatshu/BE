package com.whatshu.whatshu_be.session.mapper;

import com.whatshu.whatshu_be.session.entity.Attendance;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

@Mapper
public interface AttendanceMapper {

    // 1. 단일 출석 데이터 생성하기
    @Insert("INSERT INTO attendance (session_id, account_id, status, attended_at, guest_name) " +
            "VALUES (#{sessionId}, #{accountId}, #{status}, #{attendedAt}, #{guestName})")
    void insertAttendance(Attendance attendance);

    // 2. 여러 명 한 번에 생성하기 (세션 생성 시 전체 멤버 깔아둘 때 사용)
    @Insert({
            "<script>",
            "INSERT INTO attendance (session_id, account_id, status) VALUES ",
            "<foreach collection='list' item='item' separator=','>",
            "(#{item.sessionId}, #{item.accountId}, #{item.status})",
            "</foreach>",
            "</script>"
    })
    void insertAttendanceList(List<Attendance> attendances);

    // 3. 특정 세션(sessionId)에 속한 출석 기록 모두 조회하기
    @Select("SELECT * FROM attendance WHERE session_id = #{sessionId}")
    List<Attendance> selectAttendancesBySessionId(Long sessionId);

    // 4. 수동 출석 처리를 위한 업데이트 기능
    @Update("UPDATE attendance SET status = #{status}, attended_at = #{attendedAt} WHERE attendance_id = #{attendanceId}")
    void updateAttendance(Attendance attendance);
}