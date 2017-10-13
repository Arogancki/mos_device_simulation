package mosmessages.defined;

public enum RoElementAction {
	INSERT ("INSERT"),
	REPLACE ("REPLACE"),
	DELETE ("DELETE"),
	SWAP ("SWAP"),
	MOVE("MOVE");
	
	private final String text;
	private RoElementAction(String _text){
		text = _text;
	}
	static public RoElementAction getFromString(String str){
		try{
			return RoElementAction.valueOf(str.toUpperCase());
		}
		catch (IllegalArgumentException e){
			return null;
		}
	}
	public String toString(){
		return text;
	}
	
	
}
