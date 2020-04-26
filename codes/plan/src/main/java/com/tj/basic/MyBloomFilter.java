package com.tj.basic;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

public class MyBloomFilter {
    public static void main(String[] args) {
        //api调用
        BloomFilter<CharSequence> bf = BloomFilter.create(
                Funnels.stringFunnel(Charsets.UTF_8), 100000, 0.0002
        );
    }
}
