import java.util.Scanner;

public class Noga {
    public static void main(String[] args) {
        System.out.println("Hello I am Noga, what can I do for you?");
        Scanner scanner = new Scanner(System.in);
        while(true){
            String userInput = scanner.nextLine();
            if(userInput.equals("bye")){
                break;
            }
            System.out.println(userInput);
        }
        System.out.println("Bye. Hope to see you again soon!");
    }
}
