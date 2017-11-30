package com.rainwen.mybatis.mapper;

import com.rainwen.mybatis.entity.User;
import com.rainwen.mybatis.entity.UserExample;
import com.rainwen.mybatis.page.Pageable;
import com.rainwen.mybatis.vo.UserVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserMapper {
    int countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    @Select("select id,username,mobile,email from t_user")
    List<UserVO> findByPage(@Param("page")Pageable pageable);

    @Update("update t_user t set t.mobile=#{mobile} where t.id = #{id}")
    int updateMobileById(@Param("id") Integer id, @Param("mobile") String mobile);

    @Update("update t_user t set t.point=t.point + #{point} where t.id = #{id}")
    int updateUserPointById(@Param("id") Integer id, @Param("point") Integer point);

    @Insert("insert into t_user (username,password,email,mobile,sex) values (#{user.username},#{user.password},#{user.email},#{user.mobile},#{user.sex})")
    @SelectKey(statement="select last_insert_id()", keyProperty="user.id", before=false, resultType=int.class)
    int insertAndSelectKey(@Param("user")User user);
}