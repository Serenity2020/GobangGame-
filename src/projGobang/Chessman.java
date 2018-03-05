package projGobang;

public enum Chessman {
	BLACK("¡ñ"), WHITE("o");
	private String chessman;
	
	//Ë½ÓĞ¹¹ÔìÆ÷
	private Chessman(String chessman){
		this.chessman = chessman;
	}
	
	public String getChessman(){
		return this.chessman;
	}
}
