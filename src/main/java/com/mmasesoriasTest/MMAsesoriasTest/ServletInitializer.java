package com.mmasesoriasTest.MMAsesoriasTest;

import com.mmasesoriasTest.MMAsesoriasTestApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MMAsesoriasTestApplication.class);
	}

}
