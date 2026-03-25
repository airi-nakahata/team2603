package jp.co.internous.framepj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import jp.co.internous.framepj.model.domain.MstUser;
import jp.co.internous.framepj.model.form.UserForm;
import jp.co.internous.framepj.model.mapper.MstUserMapper;
import jp.co.internous.framepj.model.mapper.TblCartMapper;
import jp.co.internous.framepj.model.session.LoginSession;


/**
 * 認証に関する処理を行うコントローラー
 * @author インターノウス
 *
 */
@RestController
@RequestMapping("/frameweb/auth")
public class AuthController {
	
	@Autowired
	private MstUserMapper mstUserMapper;
	@Autowired
	private LoginSession loginSession; 
	@Autowired
	private TblCartMapper tblCartMapper;
	
	private Gson gson = new Gson();
	
		
	/**
	 * ログイン処理をおこなう
	 * @param f ユーザーフォーム
	 * @return ログインしたユーザー情報(JSON形式)
	 */
	@PostMapping("/login")
	public String login(@RequestBody UserForm f) {
		MstUser user = mstUserMapper.findByUserNameAndPassword(f.getUserName(),f.getPassword());
		
		if(user != null) {
			int tmpUserId = loginSession.getTmpUserId();
			loginSession.setUserId(user.getId());
			loginSession.setTmpUserId(0);
			loginSession.setUserName(user.getUserName());
			loginSession.setPassword(user.getPassword());
			loginSession.setLoggedIn(true);
			tblCartMapper.updateUserId(user.getId(), tmpUserId);
		} else {
			loginSession.setLoggedIn(false);
		}
		
		return gson.toJson(user);
	}
	
	/**
	 * ログアウト処理をおこなう
	 * @return 空文字
	 */
	@PostMapping("/logout")
	public String logout() {
		int userId = loginSession.getUserId();
		int tmpUserId = loginSession.getTmpUserId();
		
		if (userId != 0) {
			tblCartMapper.deleteByUserId(userId);
		} else if (tmpUserId != 0) {
			tblCartMapper.deleteByUserId(tmpUserId);
		}
		
		loginSession.setUserId(0);
		loginSession.setTmpUserId(0);
		loginSession.setUserName(null);
		loginSession.setPassword(null);
		loginSession.setLoggedIn(false);
		
		return "";
	}

	/**
	 * パスワード再設定をおこなう
	 * @param f ユーザーフォーム
	 * @return 処理後のメッセージ
	 */
	@PostMapping("/resetPassword")
	public String resetPassword(@RequestBody UserForm f) {
		mstUserMapper.updatePassword(f.getUserName(), f.getNewPassword());
		loginSession.setPassword(f.getNewPassword());
		return "パスワードが再設定されました。";
	}
}
