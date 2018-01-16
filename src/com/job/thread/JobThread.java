package com.job.thread;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.jeeplus.modules.sys.utils.UserUtils;

public class JobThread extends HttpServlet implements Runnable {
	@Override
	public void run() {
		try {
			
			boolean initflag = true;
			int mm = 0;
			
            while(true){
            	if(initflag){
            		mm = 120; //初始化休眠300秒，即2分钟
            		initflag = false;
            	}else{
            		mm = 20;
            	}
                
                Thread.sleep(mm*1000L);
                
                doTask(); //条件执行
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}

	public void init() throws ServletException {
//        System.out.println("------------执行定时检查单品团购是否成团 Thread init-------------");
//        JobThread t=new JobThread();
//        new Thread(t).start();
    }
	
	public void doTask(){
//		System.out.println("-------------执行定时检查单品团购是否成团 BEGIN------------------------");
//		UserUtils.executeTask();
//		System.out.println("-------------执行定时检查单品团购是否成团 END------------------------");
	}
	
}

