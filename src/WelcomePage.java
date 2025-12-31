/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author weini
 */

import java.time.*;
import java.util.Scanner;

public class WelcomePage {
  
    public void showWelcome(String name){
        LocalTime time = LocalTime.now(ZoneId.of("GMT+8"));
        int hour = time.getHour();
        String greeting;
        
        if (hour<12){
            greeting = "Good Morning";
        } else if (hour<17){
            greeting = "Good Afternoon";
        } else{
            greeting = "Good Evening";
        }
        
        System.out.println();
        System.out.println(greeting + " " + name);
        System.out.println("Welcome to Smart Journal");
        System.out.println();
        System.out.println("1. Create, Edit & View Journal");
        System.out.println("2. View Weekly Mood Summary");
        System.out.println("Please enter your choice (1/2): ");
        
        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();
    }
}
