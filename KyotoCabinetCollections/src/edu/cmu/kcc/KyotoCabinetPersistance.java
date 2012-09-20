package edu.cmu.kcc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import kyotocabinet.DB;

import org.apache.log4j.Logger;


public class KyotoCabinetPersistance {
	public static final HashMap<Character,Integer> MODES = new HashMap<Character,Integer>();
	public static final char
		MODE_READ = 'r',
		MODE_WRITE= 'w',
		MODE_APPEND='a';
	static {
		MODES.put(MODE_READ,   DB.OREADER);
		MODES.put(MODE_WRITE,  DB.OWRITER | DB.OTRUNCATE | DB.OCREATE);
		MODES.put(MODE_APPEND, DB.OWRITER | DB.OCREATE);
	}
	public char mode;
	private Logger logger;
	
	public KyotoCabinetPersistance() {
		this.logger = Logger.getLogger(KyotoCabinetPersistance.class);
	}
	public KyotoCabinetPersistance(Logger l) {
		this.logger = l;
	}

	public DB initDB(String fqpath, char mode) throws IOException {
		int imode = MODES.get(mode);
		return initDB(fqpath,imode);
	}
	public DB initDB(String fqpath, int mode) throws IOException {
		DB db;
		try {
			db = new DB(DB.GEXCEPTIONAL);
			logger.debug("Opening Kyoto Cabinet at "+fqpath+" in mode "+mode);
			if(db.open(fqpath,mode)) return db;
			kyotocabinet.Error code = db.error();
			switch(code.code()) {
			case kyotocabinet.Error.BROKEN:
				throw new FileNotFoundException("Couldn't open database at "+fqpath+": "+code);
			case kyotocabinet.Error.NOPERM:
				throw new IOException("Permission denied on "+fqpath);
			}
			throw new IllegalStateException("Couldn't open database at "+fqpath+": "+code);
		} catch (UnsatisfiedLinkError e) {
			throw new IllegalStateException("Kyoto Cabinet requires \"-Djava.library.path=/usr/local/lib\""
					+"(or correct path to libjtokyocabinet.so) on command line or in VM arguments.",e);			
		}
	}
	public void freeze(List<DB> dbs) {
		if(mode != MODE_READ) {
			for (DB db: dbs) {
				if (!db.synchronize(false, null))
					throw new IllegalStateException("Problem syncing Kyoto Cabinet: "+db.error());
			}
		}
	}
	public void close(List<DB> dbs) {
		for (DB db: dbs) {
			close(db);
		}
		logger.info("All "+dbs.size()+" databases closed.");
	}
	public void close(DB db) {
		if (!db.close())
			throw new IllegalStateException("Problem closing database at "+db.path()+": "+db.error());
	}
}
