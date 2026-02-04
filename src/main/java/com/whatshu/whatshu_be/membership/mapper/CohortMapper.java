package com.whatshu.whatshu_be.membership.mapper;

import com.whatshu.whatshu_be.membership.entity.Cohort;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CohortMapper {

    @Select("SELECT * FROM cohorts")
    public List<Cohort> findAllCohorts();

    @Insert("INSERT INTO cohorts VALUES (#{cohortNo}, #{organizer}, #{startDate}, #{endDate})")
    public void insertCohort(Cohort cohort);
}
