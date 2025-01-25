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
                System.out.println("____________________________________________________________");
                System.out.println("Here are the tasks in your list:");
                for(int i = 1; i < cur_index; i++){
                    System.out.println(i + "." + tasks[i]);
                }
                System.out.println("____________________________________________________________");
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
            
            System.out.println("____________________________________________________________");
            if(userInput.startsWith("todo")) {
                String description = userInput.substring(5);
                tasks[cur_index] = new Task(description);
            } else if(userInput.startsWith("deadline")) {
                String[] parts = userInput.split(" /by ");
                if(parts.length == 2) {
                    String description = parts[0].substring(9);
                    tasks[cur_index] = new Deadline(description, parts[1]);
                }
            } else if(userInput.startsWith("event")) {
                String[] parts = userInput.split(" /from | /to ");
                if(parts.length == 3) {
                    String description = parts[0].substring(6);
                    tasks[cur_index] = new Event(description, parts[1], parts[2]);
                }
            } else {
                tasks[cur_index] = new Task(userInput);
            }
            
            System.out.println("Got it. I've added this task:");
            System.out.println("  " + tasks[cur_index]);
            cur_index++;
            System.out.println("Now you have " + (cur_index-1) + " tasks in the list.");
            System.out.println("____________________________________________________________");
        }
        System.out.println("Bye. Hope to see you again soon!");
    }
}
