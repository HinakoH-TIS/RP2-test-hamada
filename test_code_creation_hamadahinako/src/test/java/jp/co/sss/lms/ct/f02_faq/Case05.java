package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


/**
 * 結合テスト よくある質問機能
 * ケース05
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース05 キーワード検索 正常系")
public class Case05 {
	
	private String filePath = "case05";

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		goTo("http://localhost:8080/lms/");
		
		assertEquals("ログイン | LMS", webDriver.getTitle());
		getEvidence(new Object() {}, filePath , "01_showLogin");
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		WebElement loginId = webDriver.findElement(By.id("loginId"));
		WebElement password = webDriver.findElement(By.id("password"));
		WebElement loginButton = webDriver.findElement(By.cssSelector("input[type='submit'][value='ログイン']"));
		
		loginId.clear();
		loginId.sendKeys("StudentAA01");
		password.clear();
		password.sendKeys("StudentAA01Pass");
		
		getEvidence(new Object() {}, filePath, "01_inputFilled");
		
		loginButton.click();
		
		//画面遷移が完了するまで待機
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.urlToBe("http://localhost:8080/lms/course/detail"));
		
		assertEquals("コース詳細 | LMS", webDriver.getTitle());
		getEvidence(new Object() {}, filePath, "02_courseDetail");
	}
	
	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {
		WebElement dropdownButton = webDriver.findElement(By.className("dropdown-toggle"));
		dropdownButton.click();
		
		getEvidence(new Object() {}, filePath, "01_showDropdown");
		
		WebElement dropdown = webDriver.findElement(By.className("dropdown-menu"));
		WebElement helpLink = dropdown.findElement(By.linkText("ヘルプ"));
		
		helpLink.click();
		
		//画面遷移が完了するまで待機
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.urlToBe("http://localhost:8080/lms/help"));
		
		assertEquals("ヘルプ | LMS", webDriver.getTitle());
		getEvidence(new Object() {}, filePath, "02_help");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {
		WebElement faqLink = webDriver.findElement(By.linkText("よくある質問"));
		faqLink.click();
		
		//ウィンドウハンドルを使い、別タブに移動（リンク先が別タブで開かれるため）
		String parentWindow = webDriver.getWindowHandle();
		Set<String> allWindows = webDriver.getWindowHandles();
        for (String window : allWindows) {
            if (!window.equals(parentWindow)) {
                webDriver.switchTo().window(window);
                break;
            }
        }
        
		assertEquals("よくある質問 | LMS", webDriver.getTitle());
		getEvidence(new Object() {}, filePath, "01_faq");
	}
	@Test
	@Order(5)
	@DisplayName("テスト05 キーワード検索で該当キーワードを含む検索結果だけ表示")
	void test05() {
		String keyWord = "助成金";
		
		WebElement keyWordInput = webDriver.findElement(By.id("form"));
		WebElement searchButton = webDriver.findElement(By.cssSelector("input[type='submit'][value='検索']"));
		keyWordInput.clear();
		keyWordInput.sendKeys(keyWord);
		
		getEvidence(new Object() {}, filePath, "01_keyWordFilled");
		
		searchButton.click();
		
		//検索結果をリストで取得し、キーワードを含んでいるか検証
		List<WebElement> resultList = webDriver.findElements(By.className("mb10"));
		for(WebElement result : resultList) {
			assertThat(result.getText(), is(containsString(keyWord)));
		}
		
		getEvidence(new Object() {}, filePath, "02_faqResult");
	}
	
	@Test
	@Order(6)
	@DisplayName("テスト06 「クリア」ボタン押下で入力したキーワードを消去")
	void test06() {
		WebElement keyWordInput = webDriver.findElement(By.id("form"));
		WebElement clearButton = webDriver.findElement(By.cssSelector("input[type='button'][value='クリア']"));
		clearButton.click();
		
		assertEquals("", keyWordInput.getText());
		getEvidence(new Object() {}, filePath, "01_keyWordCleared");
	}

}
