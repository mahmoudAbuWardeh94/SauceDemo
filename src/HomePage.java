import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.github.bonigarcia.wdm.WebDriverManager;

public class HomePage {
	public WebDriver driver;
	public SoftAssert softassert = new SoftAssert();

	@BeforeTest()
	public void this_is_before_test() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.get("https://www.saucedemo.com/");
		driver.manage().window().maximize();

		driver.findElement(By.xpath("//*[@id=\"user-name\"]")).sendKeys("standard_user");
		driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys("secret_sauce");
		driver.findElement(By.xpath("//*[@id=\"login-button\"]")).click();

	}

	@Test(priority = 1, groups = "Single")
	public void add_item() {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		driver.findElement(By.xpath("//*[@id=\"add-to-cart-sauce-labs-backpack\"]")).click();
		String getTheNumber = driver.findElement(By.xpath("//*[@id=\"shopping_cart_container\"]/a/span")).getText();

		Integer numberOfitem = Integer.parseInt(getTheNumber);
		softassert.assertEquals(numberOfitem, 1);
		softassert.assertAll();
	}

	@Test(priority = 2)
	public void add_all_items() {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		List<WebElement> allItems = driver.findElements(By.className("btn"));
		int theItemsSize = allItems.size();
		for (int i = 0; i < theItemsSize; i++) {
			allItems.get(i).click();
		}

		softassert.assertEquals(theItemsSize, 6, "this is the number in icon that appear in red");

		driver.findElement(By.xpath("//*[@id=\"shopping_cart_container\"]/a")).click();

		List<WebElement> cartItem = driver.findElements(By.className("cart_item"));

		int cartItemSize = cartItem.size();
		softassert.assertEquals(cartItemSize, 6, "this is the number of the user added in cart ");

		softassert.assertAll();

	}

	@Test(priority = 2, groups = "Single")
	public void check_the_price() {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

		String getPriceHomePage = driver
				.findElement(By.xpath("//*[@id=\"inventory_container\"]/div/div[1]/div[2]/div[2]/div")).getText();
		String remove$ = getPriceHomePage.replace("$", "");
		Double singlePriceInHomePage = Double.parseDouble(remove$);
//		System.out.println(singlePrice);
//		--------------------------Single Price In Home Page---------
		driver.findElement(By.xpath("//*[@id=\"shopping_cart_container\"]/a")).click();
		String getPriceInCart = driver
				.findElement(By.xpath("//*[@id=\"cart_contents_container\"]/div/div[1]/div[3]/div[2]/div[2]/div"))
				.getText();
		String remove$InCart = getPriceInCart.replace("$", "");
		Double singlePriceInCart = Double.parseDouble(remove$InCart);
//		System.out.println(singlePriceInCart);
//		--------------------------Single Price In Cart Page---------

		softassert.assertEquals(singlePriceInHomePage, singlePriceInCart,
				"this assert between the price in cart and in home page");

		driver.findElement(By.xpath("//*[@id=\"checkout\"]")).click();

		// ----------------information Field------------------
		driver.findElement(By.xpath("//*[@id=\"first-name\"]")).sendKeys("mahmoud");
		driver.findElement(By.xpath("//*[@id=\"last-name\"]")).sendKeys("ahmad");
		driver.findElement(By.xpath("//*[@id=\"postal-code\"]")).sendKeys("12345");
		driver.findElement(By.xpath("//*[@id=\"continue\"]")).click();

		// ----------------information Field------------------

		// --------------Single Price in checkout page---------------

		String getPriceInSummary = driver
				.findElement(By.xpath("//*[@id=\"checkout_summary_container\"]/div/div[1]/div[3]/div[2]/div[2]/div"))
				.getText();
		String remove$InSummary = getPriceInSummary.replace("$", "");
		Double priceInSummaryPage = Double.parseDouble(remove$InSummary);
//		System.out.println("------>>>>   "+priceInSummaryPage);
		// --------------Single Price in checkout page---------------
		softassert.assertEquals(priceInSummaryPage, singlePriceInCart,
				"this assert between the price in cart and in checkout");

		// --------------item Price in checkout page---------------

		String getItemPrice = driver.findElement(By.xpath("//*[@id=\"checkout_summary_container\"]/div/div[2]/div[6]"))
				.getText();

//		System.out.println("--------->>"+ getItemPrice);
		String remove$FromItem = getItemPrice.replace("Item total: $", "");
//		System.out.println("--------->>"+ remove$FromItem);

		Double itemPrice = Double.parseDouble(remove$InSummary);
//		System.out.println("++++++++ " + itemPrice);
		// --------------item Price in checkout page---------------

		String getTax = driver.findElement(By.xpath("//*[@id=\"checkout_summary_container\"]/div/div[2]/div[7]"))
				.getText();
//		System.out.println("TTTTAAAAXXX " + getTax);

		String replaceTax = getTax.replace("Tax: $", "");

		Double tax = Double.parseDouble(replaceTax);

//		System.out.println("TTTTAAAAXXX " + tax);

		softassert.assertEquals(tax + itemPrice, 32.39, "this is the total price");

		driver.findElement(By.xpath("//*[@id=\"finish\"]")).click();
		String actualThankyou = driver.findElement(By.xpath("//*[@id=\"checkout_complete_container\"]/h2")).getText();
//		System.out.println(actualThankyou);
		String expectedMsg = "Thank you for your order!";

		softassert.assertEquals(actualThankyou, expectedMsg);

		driver.findElement(By.xpath("//*[@id=\"back-to-products\"]")).click();
		softassert.assertAll();

	}

}
