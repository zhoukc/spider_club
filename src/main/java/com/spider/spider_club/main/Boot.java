package com.spider.spider_club.main;

import cn.wanghaomiao.seimi.core.Seimi;


public class Boot {


    public static void main(String[] args) {
//        SeimiConfig config = new SeimiConfig();
//        config.setSeimiAgentHost("127.0.0.1");
        Seimi s = new Seimi();
        s.goRun("basic");

    }
}
