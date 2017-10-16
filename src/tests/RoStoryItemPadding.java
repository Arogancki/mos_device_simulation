package tests;

import java.util.ArrayList;

import mossimulator.Item;
import mossimulator.RunningOrder;
import mossimulator.Story;

public class RoStoryItemPadding {
	public static void run(){
		for (int xxx=0; xxx<20; xxx++){
			Item item = new mossimulator.Item();
			item.setItemID("item"+xxx);
			item.setItemSlug("Slug Test "+xxx);
			item.setObjID("idek "+xxx);
			item.setMosID("mosIDEK test "+xxx);
		}
		
		for (int xxx=0; xxx<4; xxx++){
			Story story = new mossimulator.Story();
			story.setStoryID("story"+xxx);
			story.setStorySlug("Slug Test story"+xxx);
			for (int s=0; s<5; s++){
				Item i = mossimulator.Item.getItemObj("item"+((5*xxx)+s));
				assert !(i==null) : "item is null";
				story.addItem(i);
			}
		}
		
		RunningOrder ro = new RunningOrder();
		ro.setRoID("ro");
		ro.setRoSlug("Jestem slugiem");
		for (int xxx=0; xxx<4; xxx++){
			Story wloz = Story.getItemObj("story"+xxx);
			assert !(wloz==null) : "story is null";
			ro.AddStories(wloz);
		}
	}
	public static void ShowRo(){
		RunningOrder ro = RunningOrder.getRunningOrderObj("ro");
		System.out.println(ro.getRoID()+":");
		ArrayList<Story> sa = ro.getStoryArray();
		for (Story story : sa){
			System.out.println("\t"+story.getStoryID() + " (" + story.getStorySlug() + "):");
			ArrayList<Item> ia = story.getItemsArray();
			for (Item item : ia){
				System.out.println("\t\t"+item.getItemID()+"("+item.getItemSlug()+")");
			}
		}
	}
}
