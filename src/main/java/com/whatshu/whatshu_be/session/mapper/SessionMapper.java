package com.whatshu.whatshu_be.session.mapper;

import com.whatshu.whatshu_be.session.entity.Session;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface SessionMapper {

    // 1. 세션 생성하기 (DB에 INSERT)
    // 💡 방금 만든 세션의 ID(자동증가값)를 객체에 다시 담아주도록 Options를 설정합니다.
    @Insert("INSERT INTO session (title, date, type, description, cohort_no) " +
            "VALUES (#{title}, #{date}, #{type}, #{description}, #{cohortNo})")
    @Options(useGeneratedKeys = true, keyProperty = "sessionId")
    void insertSession(Session session);

    // 2. 특정 세션 상세 조회하기 (DB에서 SELECT)
    @Select("SELECT * FROM session WHERE session_id = #{sessionId}")
    Session selectSessionById(Long sessionId);
}