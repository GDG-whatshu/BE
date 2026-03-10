package com.whatshu.whatshu_be.membership.mapper;

import com.whatshu.whatshu_be.membership.dto.FinishedSessionsResponseDto;
import com.whatshu.whatshu_be.membership.entity.Cohort;
import com.whatshu.whatshu_be.membership.entity.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CohortMapper {

    @Select("SELECT * FROM cohorts")
    public List<Cohort> findAllCohorts();

    @Select("SELECT * FROM cohorts WHERE cohort_no = #{cohortNo}")
    public Optional<Cohort> findCohortByCohortNo(int cohortNo);

    @Insert("INSERT INTO cohorts VALUES (#{cohortNo}, #{organizer}, #{startDate}, #{endDate})")
    public void insertCohort(Cohort cohort);

    @Select("SELECT * FROM members WHERE cohort_no = #{cohortNo}")
    public List<Member> findMembersByCohortNo(int cohortNo);

    @Select("""
        SELECT s.session_id, s.type, s.title, s.date, SUM(CASE status WHEN 'PRESENT' THEN 1 ELSE 0 END) AS present_members
        FROM attendances AS a JOIN sessions AS s USING (session_id)
        WHERE s.cohort_no = #{cohortNo} AND a.member_id IS NOT NULL AND s.date < CURDATE()
        GROUP BY s.session_id
    """)
    public List<FinishedSessionsResponseDto.FinishedSession> findFinishedSessionsByCohortNo(int cohortNo);
}
