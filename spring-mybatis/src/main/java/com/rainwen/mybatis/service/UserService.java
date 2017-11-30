package com.rainwen.mybatis.service;

import com.rainwen.mybatis.entity.User;
import com.rainwen.mybatis.entity.UserExample;
import com.rainwen.mybatis.mapper.UserMapper;
import com.rainwen.mybatis.page.PageRequest;
import com.rainwen.mybatis.page.Pageable;
import com.rainwen.mybatis.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by rain.wen on 2017/7/28.
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserMapper userMapper;


    public void save(User user) {
        userMapper.insertSelective(user);
    }

    /**
     * 更新手机号
     *
     * @param id
     * @param mobile
     * @return
     */
    public int updateMobileById(Integer id, String mobile) {
        return userMapper.updateMobileById(id, mobile);
    }

    /**
     * 分页查询
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true)
    public List<UserVO> findByPage(Pageable pageable) {
        return userMapper.findByPage(pageable);
    }


    /**
     * 手机模糊查询
     * @param mobile
     * @return
     */
    @Transactional(readOnly = true)
    public List<User> findByMobile(String mobile) {
        UserExample example = new UserExample();
        example.createCriteria().andMobileLike(mobile + "%");
        //查询188开头列表
        List<User> userList = userMapper.selectByExample(example);
        return userList;
    }

    /**
     * ID查询
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public User findById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }


    /**
     * 更新用户积分,测试多数据源
     */
    public int updateUserPoint() {
        PageRequest pageRequest = new PageRequest(0,10);
        List<UserVO> userList = findByPage(pageRequest);
        for(UserVO user : userList) {
            userMapper.updateUserPointById(user.getId(), 1);
        }



        return userList.size();
    }

    public int insertAndSelectKey(User user) {
        userMapper.insertAndSelectKey(user);
        return user.getId();
    }

}
