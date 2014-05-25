package com.impaq.pos.service;

import com.impaq.pos.model.Product;

public class PosMessageUtil {

	public static String getProductDispalyString(Product product) {
		StringBuffer sb = new StringBuffer();
		sb.append(product.getName());
		sb.append(" - ");
		sb.append(product.getPrice());
		return sb.toString();
	}

	public static String getTransactionSummaryString(double sum) {
		StringBuffer sb = new StringBuffer();
		sb.append("Total: ");
		sb.append(sum);// TODO dodac formatowanie liczby
		return sb.toString();
	}

}
