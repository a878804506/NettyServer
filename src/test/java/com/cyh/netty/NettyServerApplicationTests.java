package com.cyh.netty;

import com.cyh.netty.config.nettyServerConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NettyServerApplicationTests {

	@Test
	public void test01() {
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(nettyServerConfig.class);
		String [] a = applicationContext.getBeanDefinitionNames();
		for (String b : a )
			System.out.println(b);
	}
}
