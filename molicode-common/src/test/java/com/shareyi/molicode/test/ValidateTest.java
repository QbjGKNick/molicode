package com.shareyi.molicode.test;

import com.shareyi.molicode.common.valid.Validate;
import org.junit.Test;

/**
 * 验证器测试
 *
 * @author david
 * @date 2020-10-31
 */
public class ValidateTest {

    @Test
    public void testLetterValidate(){
        Validate.checkChineseAndLetterNum("gitee1", "nickName不能包含非法字符");

    }
}
