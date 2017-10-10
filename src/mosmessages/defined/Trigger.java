package mosmessages.defined;

public enum Trigger {
	MANUAL ("MANUAL"),
	TIMED ("TIMED"),
	CHAINED("CHAINED");
	
	private final String text;
	private Trigger(String _text){
		text = _text;
	}
	static public Trigger getFromString(String str){
		try{
			return Trigger.valueOf(str.toUpperCase());
		}
		catch (IllegalArgumentException e){
			return null;
		}
	}
	public String toString(){
		return text;
	}
	
	
}
