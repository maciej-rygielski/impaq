package com.impaq.pos.service;

import com.impaq.pos.model.Product;

public interface SuccesfulProductScannedListener {

	public void onProductScan(Product scannedProduct);
}
