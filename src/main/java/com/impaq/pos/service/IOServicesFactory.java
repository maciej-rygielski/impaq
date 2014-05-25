package com.impaq.pos.service;

import com.impaq.pos.io.LCDDisplay;
import com.impaq.pos.io.Printer;
import com.impaq.pos.io.ProductDao;

public interface IOServicesFactory {
	public LCDDisplay getLcdDisplay() ;

	public Printer getPrinter();

	public ProductDao getProductDao();

}
