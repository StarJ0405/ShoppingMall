package com.team.shopping;

import com.team.shopping.Enums.OsType;
import com.team.shopping.Exceptions.DataNotFoundException;
import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShoppingApplication {
	@Getter
	private static OsType osType;

	public static void main(String[] args) {
		osType = OsType.getOsType();
		if (osType != null)
			SpringApplication.run(ShoppingApplication.class, args);
		else
			throw new DataNotFoundException("없는 OS 입니다.");
	}
}
