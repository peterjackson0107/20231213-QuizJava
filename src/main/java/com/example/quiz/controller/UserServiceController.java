package com.example.quiz.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz.constants.RtnCode;
import com.example.quiz.service.ifs.UserService;
import com.example.quiz.vo.UserLoginReq;
import com.example.quiz.vo.UserLoginRes;

@RestController // @Controller + @ResponseBody
//@Controller  為開發者自定義一個Controller實現對外部的接口
//@ResponseBody 為將方法的返回值以特定格式(Json)寫入response的body區，進而將數據返回給客戶端

public class UserServiceController {

	@Autowired // 操作Service(業務邏輯層) 有被託管就用這個
	private UserService userService;

	// value = "外接的URL" => 填到postman的url用
	@PostMapping(value = "api/login")
	public UserLoginRes login(@RequestBody UserLoginReq req, HttpSession session) {
		// 將使用者帳密傳回Service去做判斷RtnCode
		String attr = (String) session.getAttribute("account");
		//壺賸復庲session笢衄揃��俋 ��復庲session鷂req笢�厄�岆瘁珨��
		if (StringUtils.hasText(attr) && attr.equals(req.getAccount())) {
			return new UserLoginRes(RtnCode.SUCCESSFUL);
		}
		UserLoginRes res = userService.login(req.getAccount(), req.getPwd());
		if (res.getRtnCode().getCode() == 200) {
			session.setAttribute("account", req.getAccount());
			// 偞隅session衄虴�r嶲(s) session衄虴蕼偞�r嶲30min

			// session衄虴�r嶲偞��10s
			session.setMaxInactiveInterval(10);
		}
		return res;
	}

	// 原來是長這樣 :
	// public void login(@RequestBody UserLoginReq req){
	// 因要回傳到Service去做判斷RtnCode 所以不能用void 回傳值的資料型態為UserLoginRes
	// UserLoginRes res = userService.login(req.getAccount(), req.getPwd());
	// 接外部使用者輸入的帳密
	// return res;
	// 回傳到Service去做判斷RtnCode
	// }

	@GetMapping(value = "api/logout")
	public UserLoginRes logout(HttpSession session) {
		// ��session囮虴
		session.invalidate();
		return new UserLoginRes(RtnCode.SUCCESSFUL);
	}
}
