package cn.how2j.springcloud;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

import cn.hutool.core.util.NetUtil;

@SpringBootApplication
@EnableHystrixDashboard
public class ProductServiceHystrixDashboardApplication {

	public static void main(String[] args) {
		int port = 8020;
		if (!NetUtil.isUsableLocalPort(port)){
			System.err.printf("端口%d已被占用，无法启动", port);
			System.exit(1);
		}
		new SpringApplicationBuilder(ProductServiceHystrixDashboardApplication.class).properties("server.port=" + port).run(args);
	}
}
