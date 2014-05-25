package com.impaq.pos;

import org.easymock.EasyMock;
import org.junit.Test;

import com.impaq.pos.io.LCDDisplay;
import com.impaq.pos.io.Printer;
import com.impaq.pos.io.ProductDao;
import com.impaq.pos.model.Product;
import com.impaq.pos.service.LcdDisplayManager;
import com.impaq.pos.service.PosMessageUtil;

public class PosTest {
	private static final int SUCCESFUL_CALLS_COUNT = 3;
	private static final int UNSUCCESFUL_CALLS_COUNT = 3;

	@Test
	public void testSuccesfulProductCodeScans() {
		TestServicesFactory factory = prepareTestFactoryForSuccesfulCalls();
		POSController posController = new POSController(factory);
		for (int i = 0; i < SUCCESFUL_CALLS_COUNT; i++) {
			posController.onScannerRead(getExistingProductCode(i));
		}
		// doing it here gives us the Guarantee that printer has
		// not been called earlier
		EasyMock.replay(factory.getPrinter());
		posController.onScannerRead(POSController.TRANSACTION_END_STRING);
		EasyMock.verify(factory.getProductDao(), factory.getLcdDisplay(),
				factory.getPrinter());
	}

	@Test
	public void testUnSuccesfulProductCodeScans() {
		TestServicesFactory factory = prepareTestFactoryForFailedCalls();
		POSController posController = new POSController(factory);
		for (int i = 0; i < SUCCESFUL_CALLS_COUNT; i++) {
			posController.onScannerRead(getNotExistingProductCode(i));
		}
		posController.onScannerRead(LcdDisplayManager.INVALID_PRODUCT_CODE);
		EasyMock.verify(factory.getProductDao(), factory.getLcdDisplay());
	}

	private TestServicesFactory prepareTestFactoryForFailedCalls() {
		TestServicesFactory factory = new TestServicesFactory();
		factory.setProductDao(prepareProductDaoMockForFailedCalls());
		factory.setLcdDisplay(getLcdDisplayForFailedCall());
		return factory;
	}

	private LCDDisplay getLcdDisplayForFailedCall() {
		LCDDisplay display = EasyMock.createMock(LCDDisplay.class);
		for (int i = 0; i < UNSUCCESFUL_CALLS_COUNT; i++) {
			display.showMessage(LcdDisplayManager.PRODUCT_NOT_FOUND_MESSAGE);
		}
		display.showMessage(LcdDisplayManager.INVALID_BARCODE_MESSAGE);
		EasyMock.replay(display);
		return display;
	}

	private TestServicesFactory prepareTestFactoryForSuccesfulCalls() {
		TestServicesFactory factory = new TestServicesFactory();
		factory.setProductDao(prepareProductDaoMockForSuccesfulCalls());
		factory.setLcdDisplay(getLcdDisplayForSucccesfulCall());
		factory.setPrinter(getSuccesfulTransactionPrinter());
		return factory;
	}

	private Printer getSuccesfulTransactionPrinter() {
		Printer result = EasyMock.createMock(Printer.class);
		double sum = 0.0;
		for (int i = 0; i < SUCCESFUL_CALLS_COUNT; i++) {
			Product product = getSampleProduct(i);
			result.printLine(PosMessageUtil.getProductDispalyString(product));
			sum += product.getPrice();
		}
		result.printLine(PosMessageUtil.getTransactionSummaryString(sum));
		return result;
	}

	private LCDDisplay getLcdDisplayForSucccesfulCall() {
		LCDDisplay display = EasyMock.createMock(LCDDisplay.class);
		double sum = 0.0;
		for (int i = 0; i < SUCCESFUL_CALLS_COUNT; i++) {
			Product product = getSampleProduct(i);
			display.showMessage(PosMessageUtil.getProductDispalyString(product));
			sum += product.getPrice();
		}
		display.showMessage(PosMessageUtil.getTransactionSummaryString(sum));
		EasyMock.replay(display);
		return display;
	}

	private ProductDao prepareProductDaoMockForSuccesfulCalls() {
		ProductDao productDao = EasyMock.createMock(ProductDao.class);
		for (int i = 0; i < SUCCESFUL_CALLS_COUNT; i++) {
			EasyMock.expect(
					productDao.getProductByCode(EasyMock
							.eq(getExistingProductCode(i)))).andReturn(
					getSampleProduct(i));
		}
		EasyMock.replay(productDao);
		return productDao;
	}

	private ProductDao prepareProductDaoMockForFailedCalls() {
		ProductDao productDao = EasyMock.createMock(ProductDao.class);
		for (int i = 0; i < UNSUCCESFUL_CALLS_COUNT; i++) {
			EasyMock.expect(
					productDao.getProductByCode(EasyMock
							.eq(getNotExistingProductCode(i)))).andReturn(null);
		}
		EasyMock.replay(productDao);
		return productDao;
	}

	private Product getSampleProduct(int i) {
		Product result = new Product();
		result.setName(getTestName(i));
		result.setPrice(2.0 * i);
		return result;
	}

	private String getTestName(int i) {
		return "name_" + i;
	}

	private String getExistingProductCode(int i) {
		return "product_code" + i;
	}

	private String getNotExistingProductCode(int i) {
		return "not_existing" + i;
	}
}
