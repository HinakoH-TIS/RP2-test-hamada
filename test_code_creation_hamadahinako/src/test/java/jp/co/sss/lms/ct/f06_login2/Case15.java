package jp.co.sss.lms.ct.f06_login2;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;

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
 * 結合テスト ログイン機能②
 * ケース15
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース15 受講生 初回ログイン 利用規約に不同意")
public class Case15 {
	
	private String caseNo = "case15";

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
	@DisplayName("テスト02 DBに初期登録された未ログインの受講生ユーザーでログイン")
	void test02() {
		WebElement loginId = webDriver.findElement(By.id("loginId"));
		WebElement password = webDriver.findElement(By.id("password"));
		WebElement loginButton = webDriver.findElement(By.cssSelector("input[type='submit'][value='ログイン']"));
		
		loginId.clear();
		loginId.sendKeys("StudentAA02");
		password.clear();
		password.sendKeys("StudentAA02");
		
		getEvidence(new Object() {}, caseNo, "01_inputFilled");
		
		loginButton.click();
		
		pageLoadTimeout(5);
		
		assertEquals("セキュリティ規約 | LMS", webDriver.getTitle());
		getEvidence(new Object() {}, caseNo, "02_agreement");
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 「同意します」チェックボックスにチェックをせず「次へ」ボタンを押下")
	void test03() {
		WebElement nextButton = webDriver.findElement(By.className("btn-primary"));
		
		nextButton.click();
		
		WebElement errorMsg = webDriver.findElement(By.className("error"));
		
		assertThat(errorMsg.getText(), is(containsString("セキュリティ規約への同意は必須です。")));
		getEvidence(new Object() {}, caseNo, "01_agreementFail");
	}

}
