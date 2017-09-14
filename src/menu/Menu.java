package menu;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import mossimulator.Model;

public class Menu{
		private static Menu instance = null;
		public static void Start(){
			if(instance == null) {
				Menu[] options ={
						new SendMOSMessage(),
						new PrintMessages(),
						new Settings(),
						new Menu("Exit", null) {
							protected void Active(){ Model.Exit();}
							}
						};
				instance = 	new Menu("Mos Simulator - Main menu", options);
				instance.options = Arrays.copyOf(instance.options, instance.options.length-1);
		    }
			instance.OnClick();
		}
		private String name;
		private Menu[] options = null;
		Menu(String _name, Menu[] _options){
			name = _name;
			if (_options != null){
				options = Arrays.copyOf(_options, _options.length + 1);
				options[_options.length] = new Menu("Go to main menu", null);
			}
		}
		public void OnClick(){
			if (options != null){
				Choose();
			}
			else{
				Active();
			}
		}
		private String GetMenu(){
			String menu="";
			String separator="*";
			int maxLength = name.length(),
				length = 0,
				i = 1;
			for (Menu option : options){
				length = option.name.length()+2;
				maxLength = length > maxLength ? length : maxLength;
				menu += i++ + "." + option.name + "\n";
			}
			String bar = String.join("", Collections.nCopies(maxLength, separator));
			return bar + "\n" + name + "\n" + bar + "\n" + menu + bar;
		}
		private int Choose(){
			System.out.println(GetMenu());
			int choise=0;
			try {
				choise = (new Scanner(System.in)).nextInt();
			}
			catch (java.util.InputMismatchException e){
			}
			if (choise > options.length || choise < 1){
				System.out.println("Please choose wisely...");
				choise = Choose();
			}
			System.out.println();
			options[choise-1].OnClick();
			return choise;
		}
		//virtual
		protected void Active(){
			
		};
	}