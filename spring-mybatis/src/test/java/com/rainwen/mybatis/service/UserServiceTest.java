package com.rainwen.mybatis.service;

import com.rainwen.mybatis.Application;
import com.rainwen.mybatis.entity.User;
import com.rainwen.mybatis.entity.UserExample;
import com.rainwen.mybatis.mapper.UserMapper;
import com.rainwen.mybatis.page.PageRequest;
import com.rainwen.mybatis.page.Pageable;
import com.rainwen.mybatis.vo.UserVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Random;

/**
 * Created by rain.wen on 2017/7/28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class UserServiceTest {

    private final static Logger logger = LoggerFactory.getLogger(UserServiceTest.class);

    @Autowired
    private UserService userService;


    /**
     * 插入
     */
    @Test
    public void saveTest() {
        logger.debug("==> begin");
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setUsername("name" + random.nextLong());
            user.setPassword("password" + random.nextInt());
            user.setMobile("18888888888");
            user.setSex(random.nextInt(2));
            user.setEmail(random.nextInt(1000000000) + "@qq.com");
            userService.save(user);
        }
        logger.debug("==> end");
    }

    /**
     * 保存
     */
    @Test
    public void findByPageTest() {
        logger.debug("==> begin");
        PageRequest pageRequest = new PageRequest(0,10);
        List<UserVO> userList = userService.findByPage(pageRequest);
        Assert.isTrue(userList.size() > 0, "");
        logger.debug("==> end");
    }

    /**
     * 更新
     */
    @Test
    public void updateTest() {
        logger.debug("==> begin");
        Integer id = 1;
        String mobile = "18888888881";
        int result = userService.updateMobileById(id, mobile);
        Assert.isTrue(result > 0, "");
        logger.debug("==> end");
    }

    /**
     * 使用 example 方式查询测试
     */
    @Test
    public void findByExampleTest() {
        logger.debug("==> begin");
        String mobile = "188";
        //查询188开头列表
        List<User> userList = userService.findByMobile(mobile);
        Assert.isTrue(userList.size() > 0, "");
        logger.debug("==> end");
    }

    /**
     * ID 查询
     */
    @Test
    public void findByIdTest() {
        logger.debug("==> begin");
        Integer id = 1;
        User user = userService.findById(id);
        Assert.isTrue(user != null,"");
        logger.debug("==> end");
    }

    /**
     * 查出前10位并更新积分
     */
    @Test
    public void updateUserPointTest() {
        logger.debug("==> begin");
        int size = userService.updateUserPoint();
        Assert.isTrue(size > 0,"");
        logger.debug("==> end");
    }

    @Test
    public void insertAndSelectKey() {
        logger.debug("==> begin");
        Random random = new Random();
        User user = new User();
        user.setUsername("name" + random.nextLong());
        user.setPassword("password" + random.nextInt());
        user.setMobile("18888888888");
        user.setSex(random.nextInt(2));
        user.setEmail(random.nextInt(1000000000) + "@qq.com");
        int id = userService.insertAndSelectKey(user);
        logger.info("==> id {}", id);
        Assert.isTrue(id > 0,"");
        logger.debug("==> end");
    }

    /**
     * 从库轮询测试
     */
    @Test
    public void findByIdSlaveRoundRobinTest(){
        logger.debug("==> begin");
        for(int i = 1 ; i < 10 ; i++){
            Integer id = i;
            User user = userService.findById(id);
            Assert.isTrue(user != null,"");
        }
        logger.debug("==> end");
    }

}
