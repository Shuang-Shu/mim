package com.mdc.mim.common.utils;

import com.mdc.mim.common.exception.CycleBufferException;

/**
 * @author ShuangShu
 * @version 1.0
 * @description: 循环逻辑缓冲区，支持在[base, base + capacity)范围内存储数据，同时不断从base开始取出数据
 * @date 2023/12/6 20:22
 */
public class CycleBuffer<T> {
    private final int capacity;
    private final T[] buffer;

    private long base = 0L; // 指向队头
    private long baseIndex = 0L; // 指向基址的真实索引

    public CycleBuffer(int capacity) {
        this(capacity, 0);
    }

    @SuppressWarnings("unchecked")
    public CycleBuffer(int capacity, int initalBase) {
        this.capacity = capacity;
        this.base = initalBase;
        buffer = (T[]) new Object[capacity];
    }

    /**
     * @description: 设置指定索引处的元素
     * @param:
     * @return:
     * @author ShuangShu
     * @date: 2023/12/6 20:35
     */
    public void setAt(long index, T element) {
        if (index < base) {
            throw new CycleBufferException("index < base");
        } else if (index >= base + capacity) {
            throw new CycleBufferException("index >= base + capacity");
        }
        int logicIndex = (int) ((index - base) + baseIndex) % capacity;
        buffer[logicIndex] = element;
    }

    /**
     * @description: 获取下一个元素，返回null表示没有元素了
     * @param:
     * @return:
     * @author ShuangShu
     * @date: 2023/12/6 20:36
     */
    public T next() {
        int baseLogicIndex = (int) (baseIndex % capacity);
        if (buffer[baseLogicIndex] == null) {
            return null;
        } else {
            T element = buffer[baseLogicIndex];
            buffer[baseLogicIndex] = null;
            base++;
            baseIndex = (baseIndex + 1) % capacity;
            return element;
        }
    }

    public T peek() {
        int baseLogicIndex = (int) (baseIndex % capacity);
        return buffer[baseLogicIndex];
    }

    public long getBase() {
        return base;
    }
}
