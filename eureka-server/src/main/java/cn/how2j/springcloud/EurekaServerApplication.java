package cn.how2j.springcloud;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import cn.hutool.core.util.NetUtil;

//这个类扮演注册中心的角色，用于注册各种微服务，以便于其他微服务找到和访问
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

	public static void main(String[] args) {
		//这个端口是默认的，不要修改，后面的子项目，都会访问这个端口
		int port = 8761;
		if (!NetUtil.isUsableLocalPort(port)){
			System.err.printf("端口%d被占用，无法启动%n", port);
			System.exit(1);
		}
		new SpringApplicationBuilder(EurekaServerApplication.class).properties("server.port=" + port).run(args);
	}
}
