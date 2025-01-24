import java.util.Scanner;

public class Noga {
    public static void main(String[] args) {
        System.out.println("Hello I am Noga, what can I do for you?");
        Scanner scanner = new Scanner(System.in);
        Task tasks[] = new Task[101];
        int cur_index = 1;

        while(true){
            String userInput = scanner.nextLine();
            if(userInput.equals("bye")){
                break;
            }
            if(userInput.equals("list")){
                System.out.println("Here are the tasks in your list:");
                for(int i = 1; i < cur_index; i++){
                    System.out.println(i + ". " + tasks[i]);
                }
                continue;
            }
            if(userInput.startsWith("mark")){
                int index = Integer.parseInt(userInput.split(" ")[1]);
                tasks[index].mark();
                System.out.println("Nice! I've marked this task as done:");
                System.out.println(tasks[index]);
                continue;
            }
            if(userInput.startsWith("unmark")){
                int index = Integer.parseInt(userInput.split(" ")[1]);
                tasks[index].unmark();
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println(tasks[index]);
                continue;
            }
            tasks[cur_index++] = new Task(userInput);
            System.out.println("added: "+ userInput);
        }
        System.out.println("Bye. Hope to see you again soon!");
    }
}
