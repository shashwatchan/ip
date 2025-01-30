import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

public class Noga {
    private static final String DATA_DIR = "data";
    private static final String DATA_FILE = "data/tasks.txt";
    private Task[] tasks;
    private int cur_index;

    public Noga() {
        tasks = new Task[101];
        cur_index = 1;
        createDataDirectory();
        loadTasks();
    }

    private void createDataDirectory() {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
        } catch (IOException e) {
            System.out.println("Warning: Could not create data directory");
        }
    }

    private void loadTasks() {
        try {
            if (!Files.exists(Paths.get(DATA_FILE))) {
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" \\| ");
                if (parts.length < 3) continue;

                String type = parts[0];
                boolean isDone = parts[1].equals("1");
                String description = parts[2];

                switch (type) {
                    case "T":
                        tasks[cur_index] = new Task(description);
                        break;
                    case "D":
                        if (parts.length < 4) continue;
                        tasks[cur_index] = new Deadline(description, parts[3]);
                        break;
                    case "E":
                        if (parts.length < 5) continue;
                        tasks[cur_index] = new Event(description, parts[3], parts[4]);
                        break;
                }

                if (isDone) {
                    tasks[cur_index].mark();
                }
                cur_index++;
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Warning: Could not load tasks from file");
        }
    }

    private void saveTasks() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE));
            for (int i = 1; i < cur_index; i++) {
                Task task = tasks[i];
                String line;
                if (task instanceof Event) {
                    Event event = (Event) task;
                    line = String.format("E | %d | %s | %s | %s",
                            task.isDone() ? 1 : 0,
                            task.getDescription(),
                            event.getStartTime(),
                            event.getEndTime());
                } else if (task instanceof Deadline) {
                    Deadline deadline = (Deadline) task;
                    line = String.format("D | %d | %s | %s",
                            task.isDone() ? 1 : 0,
                            task.getDescription(),
                            deadline.getBy());
                } else {
                    line = String.format("T | %d | %s",
                            task.isDone() ? 1 : 0,
                            task.getDescription());
                }
                writer.write(line);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Warning: Could not save tasks to file");
        }
    }

    public void run() {
        System.out.println("Hello I am Noga, what can I do for you?");
        Scanner scanner = new Scanner(System.in);

        while(true) {
            String userInput = scanner.nextLine().trim();
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
                try {
                    String[] parts = userInput.split(" ");
                    if (parts.length < 2) {
                        System.out.println("____________________________________________________________");
                        System.out.println("Please specify which task to mark (e.g., mark 1)");
                        System.out.println("____________________________________________________________");
                        continue;
                    }
                    int index = Integer.parseInt(parts[1]);
                    if (index < 1 || index >= cur_index) {
                        System.out.println("____________________________________________________________");
                        System.out.println("Task number " + index + " does not exist!");
                        System.out.println("____________________________________________________________");
                        continue;
                    }
                    tasks[index].mark();
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println(tasks[index]);
                    saveTasks();
                } catch (NumberFormatException e) {
                    System.out.println("____________________________________________________________");
                    System.out.println("Please provide a valid task number!");
                    System.out.println("____________________________________________________________");
                }
                continue;
            }
            if(userInput.startsWith("unmark")){
                try {
                    String[] parts = userInput.split(" ");
                    if (parts.length < 2) {
                        System.out.println("____________________________________________________________");
                        System.out.println("Please specify which task to unmark (e.g., unmark 1)");
                        System.out.println("____________________________________________________________");
                        continue;
                    }
                    int index = Integer.parseInt(parts[1]);
                    if (index < 1 || index >= cur_index) {
                        System.out.println("____________________________________________________________");
                        System.out.println("Task number " + index + " does not exist!");
                        System.out.println("____________________________________________________________");
                        continue;
                    }
                    tasks[index].unmark();
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println(tasks[index]);
                    saveTasks();
                } catch (NumberFormatException e) {
                    System.out.println("____________________________________________________________");
                    System.out.println("Please provide a valid task number!");
                    System.out.println("____________________________________________________________");
                }
                continue;
            }
            if(userInput.startsWith("delete")){
                try {
                    String[] parts = userInput.split(" ");
                    if (parts.length < 2) {
                        System.out.println("____________________________________________________________");
                        System.out.println("Please specify which task to delete (e.g., delete 1)");
                        System.out.println("____________________________________________________________");
                        continue;
                    }
                    int index = Integer.parseInt(parts[1]);
                    if (index < 1 || index >= cur_index) {
                        System.out.println("____________________________________________________________");
                        System.out.println("Task number " + index + " does not exist!");
                        System.out.println("____________________________________________________________");
                        continue;
                    }
                    System.out.println("____________________________________________________________");
                    System.out.println("Noted. I've removed this task:");
                    System.out.println("  " + tasks[index]);
                    
                    // Shift remaining tasks to fill the gap
                    for(int i = index; i < cur_index - 1; i++){
                        tasks[i] = tasks[i + 1];
                    }
                    cur_index--;
                    
                    System.out.println("Now you have " + (cur_index-1) + " tasks in the list.");
                    System.out.println("____________________________________________________________");
                    saveTasks();
                } catch (NumberFormatException e) {
                    System.out.println("____________________________________________________________");
                    System.out.println("Please provide a valid task number!");
                    System.out.println("____________________________________________________________");
                }
                continue;
            }
            
            System.out.println("____________________________________________________________");
            if(userInput.startsWith("todo")) {
                String description = userInput.substring(4).trim();
                if (description.isEmpty()) {
                    System.out.println("Please provide a description for your todo!");
                    System.out.println("____________________________________________________________");
                    continue;
                }
                tasks[cur_index] = new Task(description);
            } else if(userInput.startsWith("deadline")) {
                String[] parts = userInput.split(" /by ");
                if(parts.length != 2 || parts[0].substring(8).trim().isEmpty()) {
                    System.out.println("Please use the format: deadline <description> /by <deadline>");
                    System.out.println("____________________________________________________________");
                    continue;
                }
                String description = parts[0].substring(8).trim();
                tasks[cur_index] = new Deadline(description, parts[1]);
            } else if(userInput.startsWith("event")) {
                String[] parts = userInput.split(" /from | /to ");
                if(parts.length != 3 || parts[0].substring(5).trim().isEmpty()) {
                    System.out.println("Please use the format: event <description> /from <start-time> /to <end-time>");
                    System.out.println("____________________________________________________________");
                    continue;
                }
                String description = parts[0].substring(5).trim();
                tasks[cur_index] = new Event(description, parts[1], parts[2]);
            } else {
                System.out.println("I'm not sure what you mean. Here are the commands I understand:");
                System.out.println("  todo <description>");
                System.out.println("  deadline <description> /by <deadline>");
                System.out.println("  event <description> /from <start-time> /to <end-time>");
                System.out.println("  list");
                System.out.println("  mark <task-number>");
                System.out.println("  unmark <task-number>");
                System.out.println("  delete <task-number>");
                System.out.println("  bye");
                System.out.println("____________________________________________________________");
                continue;
            }
            
            System.out.println("Got it. I've added this task:");
            System.out.println("  " + tasks[cur_index]);
            cur_index++;
            System.out.println("Now you have " + (cur_index-1) + " tasks in the list.");
            System.out.println("____________________________________________________________");
            saveTasks();
        }
        System.out.println("Bye. Hope to see you again soon!");
    }

    public static void main(String[] args) {
        new Noga().run();
    }
}
