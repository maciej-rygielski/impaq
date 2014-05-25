package com.impaq.pos.service;

import com.impaq.pos.model.Product;

public interface FailedProductScannedListener {

	void onProductScanFailure(Product product, String code);

}
