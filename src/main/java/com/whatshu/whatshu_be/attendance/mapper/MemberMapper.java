package com.whatshu.whatshu_be.attendance.mapper;

import com.whatshu.whatshu_be.attendance.entity.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface MemberMapper {

        // 🚨 세션의 기수(예: 7)와 프론트에서 입력한 이름(예: "김민준")으로 멤버 찾기
        Optional<Member> findByCohortNoAndName(@Param("cohortNo") Integer cohortNo, @Param("name") String name);
}