package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;

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

/**
 * 結合テスト よくある質問機能
 * ケース06
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース06 カテゴリ検索 正常系")
public class Case06 {

	private String caseNo = "case06";

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
		getEvidence(new Object() {}, caseNo, "01_showLogin");
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

		getEvidence(new Object() {}, caseNo, "01_inputFilled");

		loginButton.click();
		
		waitForUrlToBe("http://localhost:8080/lms/course/detail");

		assertEquals("コース詳細 | LMS", webDriver.getTitle());
		getEvidence(new Object() {}, caseNo, "02_courseDetail");
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {
		WebElement dropdownButton = webDriver.findElement(By.className("dropdown-toggle"));
		dropdownButton.click();

		getEvidence(new Object() {}, caseNo, "01_showDropdown");

		WebElement dropdown = webDriver.findElement(By.className("dropdown-menu"));
		WebElement helpLink = dropdown.findElement(By.linkText("ヘルプ"));

		helpLink.click();
		
		waitForUrlToBe("http://localhost:8080/lms/help");

		assertEquals("ヘルプ | LMS", webDriver.getTitle());
		getEvidence(new Object() {}, caseNo, "02_help");
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
		getEvidence(new Object() {}, caseNo, "01_faq");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 カテゴリ検索で該当カテゴリの検索結果だけ表示")
	void test05() {
		WebElement trainingCategory = webDriver.findElement(By.linkText("【研修関係】"));
		trainingCategory.click();
		
		String currentURL = webDriver.getCurrentUrl();
		assertEquals("http://localhost:8080/lms/faq?frequentlyAskedQuestionCategoryId=1", currentURL);
		
		List<WebElement> resultList = webDriver.findElements(By.className("mb10"));
		assertThat(resultList.get(0).getText(), is(containsString("キャンセル料・途中退校について")));
		assertThat(resultList.get(1).getText(), is(containsString("研修の申し込みはどのようにすれば良いですか？")));
		
		getEvidence(new Object() {}, caseNo, "01_faqFilteredByCategory");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 検索結果の質問をクリックしその回答を表示")
	void test06() {
		
		WebElement question = webDriver.findElement(By.id("question-h0"));
		question.click();
		
		WebElement answer = webDriver.findElement(By.id("answer-h0"));
		
		String answerString = "受講者の退職や解雇等、やむを得ない事情による途中終了に関してなど、事情をお伺いした上"
				+ "で、協議という形を取らせて頂きます。 弊社営業担当までご相談下さい。";
		
		assertThat(answer.getText(), is(containsString(answerString)));
		getEvidence(new Object() {}, caseNo, "01_faqFilteredByCategoryAnswer");
	}

}
