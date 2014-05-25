package com.impaq.pos;

import java.util.ArrayList;
import java.util.List;

import com.impaq.pos.io.ScanListener;
import com.impaq.pos.model.Product;
import com.impaq.pos.service.CodeValidationUtil;
import com.impaq.pos.service.FailedProductScannedListener;
import com.impaq.pos.service.PosTransactionController;
import com.impaq.pos.service.LcdDisplayManager;
import com.impaq.pos.service.SuccesfulProductScannedListener;
import com.impaq.pos.service.IOServicesFactory;

public class POSController implements ScanListener {
	public static final String TRANSACTION_END_STRING = "exit";
	private List<SuccesfulProductScannedListener> succesfulScanListeners = new ArrayList<SuccesfulProductScannedListener>();
	private List<FailedProductScannedListener> failedScanListeners = new ArrayList<FailedProductScannedListener>();
	private IOServicesFactory factory;
	private LcdDisplayManager displayManager;
	private PosTransactionController transactionController;

	public POSController(IOServicesFactory factory) {
		super();
		this.factory = factory;
		bindListeners(factory);
	}

	public void onScannerRead(String code) {
		if (isFinishTransactionCode(code)) {
			finishCurrentTransaction();
		} else {
			serveCodeScan(code);
		}
	}

	private void serveCodeScan(String code) {
		Product product = null;
		if (CodeValidationUtil.isValidProductCode(code)) {
			product = factory.getProductDao().getProductByCode(code);
		}
		if (product != null) {
			for (SuccesfulProductScannedListener listener : succesfulScanListeners) {
				listener.onProductScan(product);
			}
		} else {
			for (FailedProductScannedListener listener : failedScanListeners) {
				listener.onProductScanFailure(product, code);
			}
		}
	}

	private void finishCurrentTransaction() {
		transactionController.finishTransaction();
	}

	private void bindListeners(IOServicesFactory factory) {
		displayManager = new LcdDisplayManager(factory.getLcdDisplay());
		registerSuccesfulScanListener(displayManager);
		registerFailedScanListener(displayManager);
		transactionController = new PosTransactionController(factory.getLcdDisplay(),factory.getPrinter());
		registerSuccesfulScanListener(transactionController);
	}

	private boolean isFinishTransactionCode(String code) {
		return TRANSACTION_END_STRING.equals(code);
	}

	private void registerSuccesfulScanListener(SuccesfulProductScannedListener listener) {
		succesfulScanListeners.add(listener);
	}
	private void registerFailedScanListener(FailedProductScannedListener listener) {
		failedScanListeners.add(listener);
	}

	public PosTransactionController getInvoiceController() {
		return transactionController;
	}

	public void setInvoiceController(PosTransactionController invoiceController) {
		this.transactionController = invoiceController;
	}
	
	
}
