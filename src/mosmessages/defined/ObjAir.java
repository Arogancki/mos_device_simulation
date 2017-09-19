package mosmessages.defined;

public enum ObjAir {
	READY ("READY"),
	NOT_READY ("NOT READY");
	
	private final String text;
	private ObjAir(String _text){
		text = _text;
	}
	public String toString(){
		return text;
	}
}
