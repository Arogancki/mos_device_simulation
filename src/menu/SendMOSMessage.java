package menu;

public class SendMOSMessage extends Menu{
	SendMOSMessage() {
		super("Send MOS message", createSubMenu());
	}
	private static Menu[] createSubMenu(){
		Menu[] result = {new Profile0(), new Profile1()};
		return result;
	}
	private static class Profile0 extends Menu{
		Profile0() {
			super("Profile 0 – Basic Communication", createSubMenu());
		}
		private static Menu[] createSubMenu(){
			Menu[] result = {new Heartbeat(), new ReqMachInfo(), new ListMachInfo()};
			return result;
		}
		private static class Heartbeat extends Menu{
			Heartbeat() {
				super("heartbeat", null);
			}
			protected void Active(){
				new mosmessages.profile0.Heartbeat().Send();
			}
		}
		private static class ReqMachInfo extends Menu{
			ReqMachInfo() {
				super("reqMachInfo", null);
			}
			protected void Active(){
				new mosmessages.profile0.ReqMachInfo().Send();
			}
		}
		private static class ListMachInfo extends Menu{
			ListMachInfo() {
				super("listMachInfo", null);
			}
			protected void Active(){
				new mosmessages.profile0.ListMachInfo().Send();
			}
		}
	}
	private static class Profile1 extends Menu{
		Profile1() {
			super("Profile 1 – Basic Object Workflow", createSubMenu());
		}
		private static Menu[] createSubMenu(){
			Menu[] result = {new MOSACK()};
			return result;
		}
		private static class MOSACK extends Menu{
			MOSACK() {
				super("mosAck", null);
			}
			protected void Active(){
				new mosmessages.profile1.MOSACK().Send();
			}
		}
	}
}