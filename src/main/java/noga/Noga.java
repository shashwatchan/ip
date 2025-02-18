package noga;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * A task management chatbot that helps users keep track of various tasks,
 * including todos, deadlines, and events.
 */
public class Noga {
    private static final String DATA_DIR = "data";
    private static final String DATA_FILE = "data/tasks.txt";
    private Task[] tasks;
    private int cur_index;

    /**
     * Creates a new Noga chatbot instance.
     * Initializes the task list and loads any existing tasks from storage.
     */
    public Noga() {
        tasks = new Task[101];
        cur_index = 1;
        createDataDirectory();
        loadTasks();
    }

    /**
     * Creates the data directory if it doesn't exist.
     */
    private void createDataDirectory() {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
        } catch (IOException e) {
            System.out.println("Warning: Could not create data directory");
        }
    }

    /**
     * Loads tasks from the storage file.
     * Each task is stored in a specific format depending on its type.
     */
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

                try {
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
                } catch (DateTimeParseException e) {
                    System.out.println("Warning: Skipping task with invalid date format: " + line);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Warning: Could not load tasks from file");
        }
    }

    /**
     * Saves all tasks to the storage file.
     * Each task is converted to its storage format before saving.
     */
    private void saveTasks() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE));
            for (int i = 1; i < cur_index; i++) {
                Task task = tasks[i];
                String line;
                if (task instanceof Event) {
                    line = ((Event) task).toFileString();
                } else if (task instanceof Deadline) {
                    line = ((Deadline) task).toFileString();
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

    /**
     * Shows all tasks scheduled for a specific date.
     *
     * @param dateStr the date to search for in format "yyyy-MM-dd"
     */
    private void showTasksOnDate(String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            System.out.println("Tasks on " + date.format(DateTimeFormatter.ofPattern("MMM d yyyy")) + ":");
            boolean found = false;
            
            for (int i = 1; i < cur_index; i++) {
                Task task = tasks[i];
                LocalDateTime taskDate = null;
                
                if (task instanceof Deadline) {
                    taskDate = ((Deadline) task).getBy();
                } else if (task instanceof Event) {
                    taskDate = ((Event) task).getStartTime();
                }
                
                if (taskDate != null && taskDate.toLocalDate().equals(date)) {
                    System.out.println(i + "." + task);
                    found = true;
                }
            }
            
            if (!found) {
                System.out.println("No tasks found on this date.");
            }
        } catch (DateTimeParseException e) {
            System.out.println("Please use the format yyyy-MM-dd (e.g., 2024-03-15)");
        }
    }

    private void findTasks(String keyword) {
        System.out.println("____________________________________________________________");
        System.out.println("Here are the matching tasks in your list:");
        boolean found = false;
        
        for (int i = 1; i < cur_index; i++) {
            if (tasks[i].getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println(i + "." + tasks[i]);
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("No matching tasks found.");
        }
        System.out.println("____________________________________________________________");
    }

    private void showHelp() {
        System.out.println("____________________________________________________________");
        System.out.println("Here are the commands I understand:");
        System.out.println("  todo <description>");
        System.out.println("    adds a todo task");
        System.out.println("  deadline <description> /by yyyy-MM-dd HHmm");
        System.out.println("    adds a deadline task (e.g., deadline homework /by 2024-03-15 2359)");
        System.out.println("  event <description> /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm");
        System.out.println("    adds an event (e.g., event meeting /from 2024-03-15 1400 /to 2024-03-15 1600)");
        System.out.println("  list");
        System.out.println("    shows all tasks");
        System.out.println("  find <keyword>");
        System.out.println("    finds tasks containing the keyword");
        System.out.println("  mark <task-number>");
        System.out.println("    marks a task as done");
        System.out.println("  unmark <task-number>");
        System.out.println("    marks a task as not done");
        System.out.println("  delete <task-number>");
        System.out.println("    deletes a task");
        System.out.println("  show date yyyy-MM-dd");
        System.out.println("    shows tasks on a specific date");
        System.out.println("  help");
        System.out.println("    shows this help message");
        System.out.println("  bye");
        System.out.println("    exits the program");
        System.out.println("____________________________________________________________");
    }

    /**
     * Starts the chatbot and processes user commands until exit.
     * Supports commands for adding, listing, marking, and managing tasks.
     */
    public void run() {
        System.out.println("Hello I am Noga, what can I do for you?");
        // System.out.println("Type 'help' to see what I can do!");
        Scanner scanner = new Scanner(System.in);

        while(true) {
            String userInput = scanner.nextLine().trim();
            if(userInput.equals("bye")){
                break;
            }
            if(userInput.equals("help")) {
                showHelp();
                continue;
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
            
            // Add new command to show tasks on a specific date
            if (userInput.startsWith("show date")) {
                String dateStr = userInput.substring(9).trim();
                if (dateStr.isEmpty()) {
                    System.out.println("Please specify a date (yyyy-MM-dd)");
                    continue;
                }
                showTasksOnDate(dateStr);
                continue;
            }
            
            // Add new command to find tasks
            if (userInput.startsWith("find")) {
                String keyword = userInput.substring(4).trim();
                if (keyword.isEmpty()) {
                    System.out.println("____________________________________________________________");
                    System.out.println("Please provide a keyword to search for!");
                    System.out.println("____________________________________________________________");
                    continue;
                }
                findTasks(keyword);
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
                    System.out.println("Please use the format: deadline <description> /by yyyy-MM-dd HHmm");
                    System.out.println("Example: deadline return book /by 2024-03-15 1800");
                    System.out.println("____________________________________________________________");
                    continue;
                }
                String description = parts[0].substring(8).trim();
                try {
                    tasks[cur_index] = new Deadline(description, parts[1].trim());
                } catch (DateTimeParseException e) {
                    System.out.println("Please use the format yyyy-MM-dd HHmm for the deadline");
                    System.out.println("Example: 2024-03-15 1800 for March 15, 2024, 6:00 PM");
                    System.out.println("____________________________________________________________");
                    continue;
                }
            } else if(userInput.startsWith("event")) {
                String[] parts = userInput.split(" /from | /to ");
                if(parts.length != 3 || parts[0].substring(5).trim().isEmpty()) {
                    System.out.println("Please use the format: event <description> /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm");
                    System.out.println("Example: event project meeting /from 2024-03-15 1400 /to 2024-03-15 1600");
                    System.out.println("____________________________________________________________");
                    continue;
                }
                String description = parts[0].substring(5).trim();
                try {
                    tasks[cur_index] = new Event(description, parts[1].trim(), parts[2].trim());
                } catch (DateTimeParseException e) {
                    System.out.println("Please use the format yyyy-MM-dd HHmm for the dates");
                    System.out.println("Example: 2024-03-15 1400 for March 15, 2024, 2:00 PM");
                    System.out.println("____________________________________________________________");
                    continue;
                }
            } else {
                System.out.println("I'm not sure what you mean.");
                System.out.println("Type 'help' to see what I can do!");
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

    /**
     * Main entry point of the application.
     * Creates and runs a new instance of the Noga chatbot.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        new Noga().run();
    }
}
