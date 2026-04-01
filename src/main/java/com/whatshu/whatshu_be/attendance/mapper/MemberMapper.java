package com.whatshu.whatshu_be.attendance.mapper;

import com.whatshu.whatshu_be.attendance.entity.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MemberMapper {

    // 세션의 기수(예: 7)와 프론트에서 입력한 이름(예: "김민준")으로 멤버 찾기
    @Select("SELECT * FROM members WHERE cohort_no = #{cohortNo} AND name = #{name}")
    Optional<Member> findByCohortNoAndName(@Param("cohortNo") Integer cohortNo, @Param("name") String name);

    // MemberMapper.java에 추가
    @Select("SELECT * FROM members WHERE cohort_no = #{cohortNo}")
    List<Member> findMembersByCohortNo(Byte cohortNo);
}
