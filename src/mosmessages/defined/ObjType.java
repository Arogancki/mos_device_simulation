package mosmessages.defined;

public enum ObjType {
	STILL ("STILL"),
	AUDIO ("AUDIO"),
	VIDEO ("VIDEO");
	
	private final String text;
	private ObjType(String _text){
		text = _text;
	}
	public String toString(){
		return text;
	}
	static public ObjType getFromString(String str){
		try {
			return ObjType.valueOf(str.toUpperCase());
		}
		catch (IllegalArgumentException e){
			return null;
		}
	}
}
