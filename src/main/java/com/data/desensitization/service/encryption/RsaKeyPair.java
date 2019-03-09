package com.data.desensitization.service.encryption;

public class RsaKeyPair {
	private final PrivateKey privatekey;
	private final PublicKey publickey;
	
	public RsaKeyPair(PublicKey publickey, PrivateKey privatekey) {
		this.privatekey = privatekey;
		this.publickey = publickey;
	}
	
	public PrivateKey getPrivateKey() {
		return privatekey;
	}
	
	public PublicKey getPublicKey() {
		return publickey;
	}
}
