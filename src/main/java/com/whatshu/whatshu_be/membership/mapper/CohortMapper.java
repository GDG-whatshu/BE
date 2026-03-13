package com.whatshu.whatshu_be.membership.mapper;

import com.whatshu.whatshu_be.membership.entity.Cohort;
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
}
