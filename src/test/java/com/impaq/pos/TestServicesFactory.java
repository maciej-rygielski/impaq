package com.impaq.pos;

import com.impaq.pos.io.LCDDisplay;
import com.impaq.pos.io.Printer;
import com.impaq.pos.io.ProductDao;
import com.impaq.pos.service.IOServicesFactory;

public class TestServicesFactory implements IOServicesFactory {
	private LCDDisplay lcdDisplay;
	private Printer printer;
	private ProductDao productDao;

	public LCDDisplay getLcdDisplay() {
		return lcdDisplay;
	}

	public void setLcdDisplay(LCDDisplay lcdDisplay) {
		this.lcdDisplay = lcdDisplay;
	}

	public Printer getPrinter() {
		return printer;
	}

	public void setPrinter(Printer printer) {
		this.printer = printer;
	}

	public ProductDao getProductDao() {
		return productDao;
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

}
