package com.dpslink.lbmxtransfer;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendDocController {
	
//	@RequestMapping("/sendDocument")
//	public void sendDocument(@RequestParam(value="path") String path) {
//		System.out.println("The path is " + path);
//	}
	
	@RequestMapping("/test")
	public void test() {
		System.out.println("Got this");
	}

}
