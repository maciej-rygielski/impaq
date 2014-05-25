package com.impaq.pos.io;

import com.impaq.pos.model.Product;

public interface ProductDao {
	public Product getProductByCode(String code);
}
