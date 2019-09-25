package cn.how2j.springcloud;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import brave.sampler.Sampler;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.NumberUtil;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
public class ProductViewServiceFeignApplication {
	
	@Bean
	public Sampler defaultSampler(){
		return Sampler.ALWAYS_SAMPLE;
	}

	public static void main(String[] args){
		//判断rabbitMQ是否已启动
		int rabbitMQPort = 5672;
		if (NetUtil.isUsableLocalPort(rabbitMQPort)){
			System.err.printf("未在端口%d发现RabbitMQ服务，请检查RabbitMQ是否已启动", rabbitMQPort);
			System.exit(1);
		}
		
		int port = 0;
		int defaultPort = 8012;
		Future<Integer> future = ThreadUtil.execAsync(() ->{
			int p = 0;
			System.out.println("请于5秒钟内输入端口号，推荐8012，8013或8014，超过5秒则默认使用" + defaultPort);
			Scanner s = new Scanner(System.in);
			while(true){
				String strPort = s.nextLine();
				if (!NumberUtil.isInteger(strPort)){
					System.err.println("只能输入数字");
					continue;
				}
				else{
					p = Convert.toInt(strPort);
					s.close();
					break;
				}
			}
			return p;
		});
		try{
			port = future.get(5, TimeUnit.SECONDS);
		}catch(InterruptedException | ExecutionException | TimeoutException e){
			port = defaultPort;
		}
		if (!NetUtil.isUsableLocalPort(port)){
			System.err.printf("端口%d已被占用，无法启动%n", port);
			System.exit(1);
		}
		new SpringApplicationBuilder(ProductViewServiceFeignApplication.class).properties("server.port=" + port).run(args);
	}
}
