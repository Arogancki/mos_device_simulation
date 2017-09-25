package mossimulator;

public class MosSimulator{
	public static void main(String[] args) {
		while (Model.getPowerSwitch()){
			menu.Menu.Start();
		}
		System.out.print("Closed");
	}
}