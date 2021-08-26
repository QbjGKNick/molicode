package com.shareyi.molicode;

import com.shareyi.molicode.common.utils.FileIoUtil;
import com.shareyi.molicode.common.utils.MoliCodeStringUtils;
import org.junit.Test;

/**
 * 描述
 *
 * @author david
 * @date 2018/9/1
 */
public class MyTest {

    @Test
    public void test(){
        System.out.println(FileIoUtil.getRunPath());
    }

    @Test
    public void testPwd() {
        System.out.println(MoliCodeStringUtils.md5PasswordWithSalt("molicodepwd"));
    }
}
