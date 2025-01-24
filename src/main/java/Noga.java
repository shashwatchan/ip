import java.util.Scanner;


public class Noga {
    public static void main(String[] args) {
        System.out.println("Hello I am Noga, what can I do for you?");
        Scanner scanner = new Scanner(System.in);
        String tasks[] = new String[101];
        int cur_index = 1;

        while(true){
            String userInput = scanner.nextLine();
            if(userInput.equals("bye")){
                break;
            }
            if(userInput.equals("list")){
                for(int i = 1; i < cur_index; i++){
                    System.out.println(i + ". " + tasks[i]);
                }
                continue;
            }
            tasks[cur_index++] = userInput;
            System.out.println("added: "+ userInput);
        }
        System.out.println("Bye. Hope to see you again soon!");
    }
}
