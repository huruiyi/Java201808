package com.example.spring.tx.demoH;

public interface AccountService {

	/**
	 * @param from
	 * @param to
	 * @param money
	 */
	public void transfer(String from,String to,Double money);
}