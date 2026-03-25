package jp.co.internous.framepj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.internous.framepj.model.domain.MstCategory;
import jp.co.internous.framepj.model.domain.MstProduct;
import jp.co.internous.framepj.model.form.SearchForm;
import jp.co.internous.framepj.model.mapper.MstCategoryMapper;
import jp.co.internous.framepj.model.mapper.MstProductMapper;
import jp.co.internous.framepj.model.session.LoginSession;

/**
 * 商品検索に関する処理を行うコントローラー
 * @author インターノウス
 *
 */
@Controller
@RequestMapping("/frameweb")
public class IndexController {

	@Autowired
	private MstProductMapper mstProductMapper;
	
	@Autowired
	private MstCategoryMapper mstCategoryMapper;
	
	@Autowired
    private LoginSession loginSession;

	/**
	 * トップページを初期表示する。
	 * @param m 画面表示用オブジェクト
	 * @return トップページ
	 */
	@RequestMapping("/")
	public String index(Model m) {
		
		if (!loginSession.isLoggedIn() && loginSession.getTmpUserId() == 0) {
            java.util.Random random = new java.util.Random();
            int tmpUserId = (random.nextInt(900000000) + 100000000) * -1;
            loginSession.setTmpUserId(tmpUserId);
        }
		
		m.addAttribute("loginSession", loginSession);
		
	    List<MstProduct> productList = mstProductMapper.find();
	    m.addAttribute("products", productList);
	    
	    List<MstCategory> categoryList = mstCategoryMapper.find();
	    m.addAttribute("categories", categoryList);

		return "index";
	}

	/**
	 * 検索処理を行う
	 * @param f 検索用フォーム
	 * @param m 画面表示用オブジェクト
	 * @return トップページ
	 */
	
	@RequestMapping("/searchItem")
	public String searchItem(SearchForm f, Model m) {
		
		m.addAttribute("loginSession", loginSession);

		String keywords = f.getKeywords();
		int category = f.getCategory();

		if (keywords == null) {
			keywords = "";
		}

		keywords = keywords.replace("　", " ");
		keywords = keywords.replaceAll(" +", " ");
		keywords = keywords.trim();

		String[] words = keywords.split(" ");

		List<MstProduct> productList;

		if (!keywords.isEmpty() && category != 0) {
		    productList = mstProductMapper.findByCategoryAndProductName(category, words);
		} else if (!keywords.isEmpty()) {
		    productList = mstProductMapper.findByProductName(words);
		} else if (category != 0) {
		    productList = mstProductMapper.findByCategoryId(category);
		} else {
		    productList = mstProductMapper.find();
		}
		
		m.addAttribute("keywords", keywords);
		m.addAttribute("selected", category); 

		m.addAttribute("products", productList);
		
		List<MstCategory> categoryList = mstCategoryMapper.find();
		m.addAttribute("categories", categoryList);
		
		m.addAttribute("category", category);

		return "index";
	}
}