package com.impaq.pos.service;

import java.util.ArrayList;
import java.util.List;

import com.impaq.pos.io.LCDDisplay;
import com.impaq.pos.io.Printer;
import com.impaq.pos.model.Product;

public class PosTransactionController implements
		SuccesfulProductScannedListener {
	private List<Product> currentProducts = new ArrayList<Product>();
	private LCDDisplay lcdDisplay;
	private Printer printer;

	public PosTransactionController(LCDDisplay lcdDisplay, Printer printer) {
		super();
		this.lcdDisplay = lcdDisplay;
		this.printer = printer;
	}

	public void onProductScan(Product product) {
		currentProducts.add(product);
	}

	public void finishTransaction() {
		double sum = 0.0;
		for (Product product : currentProducts) {
			printer.printLine(PosMessageUtil.getProductDispalyString(product));
			sum += product.getPrice();
		}
		String totalStr = PosMessageUtil.getTransactionSummaryString(sum);
		printer.printLine(totalStr);
		lcdDisplay.showMessage(totalStr);

	}
}
