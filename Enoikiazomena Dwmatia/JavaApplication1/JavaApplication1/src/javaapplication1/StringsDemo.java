/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javaapplication1;

/**
 *
 * @author user
 */
public class StringsDemo {

    public static void main(String[] args) {

    	byte[] bytes = {0, 1, 1, 0};

    	char[] characters = {'a', 'b', 'C', 'D'};

    	StringBuffer strBuffer = new StringBuffer("abcde");

//    	Examples of Creation of Strings

    	String byteStr = new String(bytes);      

    	String charStr = new String(characters); 

    	String buffStr = new String(strBuffer);

    	System.out.println("byteStr : "+byteStr);

    	System.out.println("charStr : "+charStr);

    	System.out.println("buffStr : "+buffStr);

    }

}