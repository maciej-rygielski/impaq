package com.impaq.pos.service;

import static com.impaq.pos.service.PosMessageUtil.getProductDispalyString;

import com.impaq.pos.io.LCDDisplay;
import com.impaq.pos.model.Product;

public class LcdDisplayManager implements SuccesfulProductScannedListener,
		FailedProductScannedListener {
	public static final String PRODUCT_NOT_FOUND_MESSAGE = "Product not found";
	public static final String INVALID_BARCODE_MESSAGE = "Invalid barcode";
	public final static String INVALID_PRODUCT_CODE = "";
	private LCDDisplay lcdDisplay;

	public LcdDisplayManager(LCDDisplay lcdDisplay) {
		this.lcdDisplay = lcdDisplay;
	}

	public void onProductScan(Product product) {
		lcdDisplay.showMessage(getProductDispalyString(product));
	}

	public void onProductScanFailure(Product product, String code) {
		if (CodeValidationUtil.isValidProductCode(code)) {
			if (product == null) {
				lcdDisplay.showMessage(PRODUCT_NOT_FOUND_MESSAGE);
			}
		} else {
			lcdDisplay.showMessage(INVALID_BARCODE_MESSAGE);
		}

	}
}
