package com.data.desensitization.service.encryption;

import java.math.BigInteger;

public class PrivateKey {
	private final BigInteger n;
	private final BigInteger a;
	
	public PrivateKey(BigInteger n, BigInteger a) {
		this.n = n;
		this.a = a;
	}
	
	public BigInteger getN() {
		return n;
	}
	
	public BigInteger getA() {
		return a;
	}
}
