package projGobang;

public enum Chessman {
	BLACK("��"), WHITE("o");
	private String chessman;
	
	//˽�й�����
	private Chessman(String chessman){
		this.chessman = chessman;
	}
	
	public String getChessman(){
		return this.chessman;
	}
}
