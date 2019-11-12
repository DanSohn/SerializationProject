/*
This part of your system will create arbitrary objects under control of the user. Allow the user to create one
or more objects from a selection of objects using some sort of text-based menu system or GUI.
 */

package sender;

import java.util.Scanner;

public class ObjectCreator {

    private static int userMenu(){

        Scanner input = new Scanner(System.in);
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

        return input.nextInt();
    }
    public static void main(String[] args){

        int choice = userMenu();
        System.out.println(choice);
    }
}