package com.whatshu.whatshu_be.auth.mapper;

import com.whatshu.whatshu_be.auth.entity.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Optional;

@Mapper
//자바 mysql 연결
public interface AccountMapper {

    @Insert("INSERT INTO account(email,password,role) VALUES(#{email},#{password},#{role})")
    void insertAccount(Account account);

    //전체 회원 조회(필요할까봐 해놧습니다.)
    @Select("SELECT * FROM account ")
    List<Account> findAllAccount();

    @Select("SELECT * FROM account WHERE email = #{email}")
    Optional<Account> findByEmail(String email);
}
