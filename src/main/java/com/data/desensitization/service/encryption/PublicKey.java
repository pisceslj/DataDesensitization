package com.data.desensitization.service.encryption;

import java.math.BigInteger;

public class PublicKey {
	private final BigInteger n;
	private final BigInteger b;
	
	public PublicKey(BigInteger n, BigInteger b) {
		this.n = n;
		this.b = b;
	}
	
	public BigInteger getN() {
		return n;
	}
	
	public BigInteger getB() {
		return b;
	}
}
