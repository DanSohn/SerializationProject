/*
This part of your system will create arbitrary objects under control of the user. Allow the user to create one
or more objects from a selection of objects using some sort of text-based menu system or GUI.
 */

package sender;

import java.util.Scanner;

public class ObjectCreator {

    private static void userMenu(){
        System.out.println("****************************************");
        System.out.println("******         User Menu          ******");
        System.out.println("****************************************");
        System.out.println("******      Select An Option      ******");
        System.out.println("1) Simple object with only primitives");
        System.out.println("2) Object with reference to another object");
        System.out.println("3) Object containing array of primitives");
        System.out.println("4) Object containing array of object references");
        System.out.println("5) Object using Java Collection's Stack to reference other objects");
        System.out.println("I choose:");
    }

    private static int userChoice(){
        userMenu();
        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();
        if(choice < 1 || choice > 5){
            System.out.println("Invalid choice. Please try again");
            choice = userChoice();
        }
        return choice;
    }
    public Object objectCreator() throws Exception{

        int choice = userChoice();
        System.out.println(choice);
        // create instance of ObjectHandler to reflectively set values for my object.
        ObjectHandler h = new ObjectHandler();
        Object obj = null;
        Object iniObj;
        if(choice == 1){
            obj = h.HandlerA();
        }else if(choice == 2){
            obj = h.HandlerB();
        }else if(choice == 3){
            obj = h.HandlerC();
        }else if(choice == 4){
            obj = h.HandlerD();
        }else if(choice == 5){
            obj = h.HandlerE();
        }
        return obj;

    }

}