package mosmessages.defined;

public enum RoElementStat {
	RO ("RO"),
	STORY ("STORY"),
	ITEM("ITEM");
	
	private final String text;
	private RoElementStat(String _text){
		text = _text;
	}
	static public RoElementStat getFromString(String str){
		try{
			return RoElementStat.valueOf(str.toUpperCase());
		}
		catch (IllegalArgumentException e){
			return null;
		}
	}
	public String toString(){
		return text;
	}
	
	
}
