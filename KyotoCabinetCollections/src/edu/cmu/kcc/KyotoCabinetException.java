package edu.cmu.kcc;

import kyotocabinet.DB;

public class KyotoCabinetException extends RuntimeException {
	public KyotoCabinetException(String msg,DB db) {
		super(msg+": "+db.error());
	}
}
