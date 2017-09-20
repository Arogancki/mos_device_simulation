package mosmessages.defined;

public enum ObjAir {
	READY ("READY"),
	NOT_READY ("NOT READY");
	
	private final String text;
	private ObjAir(String _text){
		text = _text;
	}
	static public ObjAir getFromString(String str){
		try{
			return ObjAir.valueOf(str);
		}
		catch (IllegalArgumentException e){
			return null;
		}
	}
	public String toString(){
		return text;
	}
	
	
}
