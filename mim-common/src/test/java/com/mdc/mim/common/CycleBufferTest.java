package com.mdc.mim.common;

import com.mdc.mim.common.utils.CycleBuffer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: TODO
 * @date 2023/12/6 20:32
 */
public class CycleBufferTest {
    @Test
    public void testCycleBuffer() {
        var buffer = new CycleBuffer<Integer>(3);
        buffer.setAt(0, 1);
        buffer.setAt(1, 2);
        buffer.setAt(2, 3);
        for (int i = 0; i < 3; i++) {
            Assertions.assertEquals(i + 1, buffer.next());
        }
        Assertions.assertNull(buffer.next());
        // 继续插入
        buffer.setAt(3, 4);
        buffer.setAt(4, 5);
        buffer.setAt(5, 6);
        for (int i = 3; i < 6; i++) {
            Assertions.assertEquals(i + 1, buffer.next());
        }
        Assertions.assertNull(buffer.next());
    }
}
