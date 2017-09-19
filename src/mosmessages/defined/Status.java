package mosmessages.defined;

public enum Status {
	ACK ("ACK"),
	OK ("OK"),
	UPDATED ("UPDATED"),
	MOVED ("MOVED"),
	BUSY  ("BUSY "),
	DELETED ("DELETED"),
	NCS_CTRL ("NCS CTRL"),
	MANUAL_CTRL ("MANUAL CTRL"),
	READY ("READY"),
	NOT_READY ("NOT READY"),
	PLAY ("PLAY"),
	STOP ("STOP"),
	NEW ("NEW");
	
	private final String status;
	private Status(String _status){
		status = _status;
	}
	public String toString(){
		return status;
	}
}
