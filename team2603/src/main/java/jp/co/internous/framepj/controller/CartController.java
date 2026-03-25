package jp.co.internous.framepj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import jp.co.internous.framepj.model.domain.TblCart;
import jp.co.internous.framepj.model.domain.dto.CartDto;
import jp.co.internous.framepj.model.form.CartForm;
import jp.co.internous.framepj.model.mapper.TblCartMapper;
import jp.co.internous.framepj.model.session.LoginSession;


/**
 * カート情報に関する処理のコントローラー
 * @author インターノウス
 *
 */
@Controller
@RequestMapping("/frameweb/cart")
public class CartController {
	
	@Autowired
	private LoginSession loginSession;
	@Autowired
	private TblCartMapper tblCartMapper;

	private Gson gson = new Gson();
	

	/**
	 * カート画面を初期表示する。
	 * @param m 画面表示用オブジェクト
	 * @return カート画面
	 */
	@RequestMapping("/")
	public String index(Model m) {
		int userId = loginSession.getUserId();
		int tmpUserId = loginSession.getTmpUserId();
		
		int searchId;
		if(userId != 0) {
			searchId = userId;
		} else {
			searchId = tmpUserId;
		}
		
		List<CartDto> carts = null;
		if (searchId != 0) {
			carts = tblCartMapper.findByUserId(searchId);
		}
		
		m.addAttribute("carts", carts);
		m.addAttribute("loginSession", loginSession);
		
		return "cart";
	}

	/**
	 * カートに追加処理を行う
	 * @param f カート情報のForm
	 * @param m 画面表示用オブジェクト
	 * @return カート画面
	 */
	@RequestMapping("/add")
	public String addCart(CartForm f, Model m) {
		int userId = loginSession.getUserId();
		int tmpUserId = loginSession.getTmpUserId();
		
		int searchId;
		if(userId != 0) {
			searchId = userId;
		} else {
			searchId = tmpUserId;
		}
		
		if (searchId == 0) {
			return index(m);
		}
		
		int productId = f.getProductId();
		int count  = tblCartMapper.findCountByUserIdAndProductId(searchId, productId);
		
		TblCart cart = new TblCart();
		cart.setUserId(searchId);
		cart.setProductId(productId);
		cart.setProductCount(f.getProductCount());
		
		if(count > 0) {
			tblCartMapper.update(cart);
		} else {
			tblCartMapper.insert(cart);
		}
		
		return index(m);
	}

	/**
	 * カート情報を削除する
	 * @param checkedIdList 選択したカート情報のIDリスト
	 * @return true:削除成功、false:削除失敗
	 */
	@PostMapping("/delete")
	@ResponseBody
	public boolean deleteCart(@RequestBody String checkedIdList) {
		com.google.gson.JsonObject jsonObject = gson.fromJson(checkedIdList, com.google.gson.JsonObject.class);
		
		java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<List<Integer>>(){}.getType();
		List<Integer> checkedIds = gson.fromJson(jsonObject.get("checkedIdList"), type);
		
		int result = tblCartMapper.deleteById(checkedIds);
		
		return result > 0;
	}
}