package com.example.springakka;
public aspect HelloAspectJ {
 
    // Define a Pointcut is
    // collection of JoinPoint call sayHello of class HelloAspectJDemo.
    pointcut callSayHello(): call(* MainApp.sayHello());
 
    before() : callSayHello() {
        System.out.println("Before call sayHello");
    }
 
    after() : callSayHello()  {
        System.out.println("After call sayHello");
    }
 
    pointcut callMain(): call(* SpringAkkaApplication.main(String[] ));
    
    before() : callMain() {
        System.out.println("Before call callMain of SpringAkkaApplication");
    }
     
}