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
     * @return String containing the tasks found on the specified date
     */
    private String showTasksOnDate(String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            StringBuilder response = new StringBuilder();
            response.append("Tasks on ").append(date.format(DateTimeFormatter.ofPattern("MMM d yyyy"))).append(":\n");
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
                    response.append(i).append(".").append(task).append("\n");
                    found = true;
                }
            }
            
            if (!found) {
                response.append("No tasks found on this date.");
            }
            return response.toString();
        } catch (DateTimeParseException e) {
            return "Please use the format yyyy-MM-dd (e.g., 2024-03-15)";
        }
    }

    private String findTasks(String keyword) {
        StringBuilder response = new StringBuilder();
        response.append("Here are the matching tasks in your list:\n");
        boolean found = false;
        
        for (int i = 1; i < cur_index; i++) {
            if (tasks[i].getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                response.append(i).append(".").append(tasks[i]).append("\n");
                found = true;
            }
        }
        
        if (!found) {
            response.append("No matching tasks found.");
        }
        return response.toString();
    }

    /**
     * Processes a single user input and returns the appropriate response
     * @param userInput the input string from the user
     * @return the response string to display
     */
    public String getResponse(String userInput) {
        userInput = userInput.trim();
        
        if (userInput.equals("bye")) {
            return "Bye. Hope to see you again soon!";
        }
        
        if (userInput.equals("help")) {
            StringBuilder response = new StringBuilder();
            response.append("Here are the commands I understand:\n");
            response.append("  todo <description>\n");
            response.append("    adds a todo task\n");
            response.append("  deadline <description> /by yyyy-MM-dd HHmm\n");
            response.append("    adds a deadline task (e.g., deadline homework /by 2024-03-15 2359)\n");
            response.append("  event <description> /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm\n");
            response.append("    adds an event (e.g., event meeting /from 2024-03-15 1400 /to 2024-03-15 1600)\n");
            response.append("  list\n");
            response.append("    shows all tasks\n");
            response.append("  find <keyword>\n");
            response.append("    finds tasks containing the keyword\n");
            response.append("  mark <task-number>\n");
            response.append("    marks a task as done\n");
            response.append("  unmark <task-number>\n");
            response.append("    marks a task as not done\n");
            response.append("  delete <task-number>\n");
            response.append("    deletes a task\n");
            response.append("  show date yyyy-MM-dd\n");
            response.append("    shows tasks on a specific date\n");
            response.append("  help\n");
            response.append("    shows this help message\n");
            response.append("  bye\n");
            response.append("    exits the program");
            return response.toString();
        }
        
        if (userInput.equals("list")) {
            StringBuilder response = new StringBuilder("Here are the tasks in your list:\n");
            for (int i = 1; i < cur_index; i++) {
                response.append(i).append(".").append(tasks[i]).append("\n");
            }
            return response.toString();
        }
        
        if (userInput.startsWith("mark")) {
            try {
                String[] parts = userInput.split(" ");
                if (parts.length < 2) {
                    return "Please specify which task to mark (e.g., mark 1)";
                }
                int index = Integer.parseInt(parts[1]);
                if (index < 1 || index >= cur_index) {
                    return "Task number " + index + " does not exist!";
                }
                tasks[index].mark();
                saveTasks();
                return "Nice! I've marked this task as done:\n" + tasks[index];
            } catch (NumberFormatException e) {
                return "Please provide a valid task number!";
            }
        }
        
        if (userInput.startsWith("unmark")) {
            try {
                String[] parts = userInput.split(" ");
                if (parts.length < 2) {
                    return "Please specify which task to unmark (e.g., unmark 1)";
                }
                int index = Integer.parseInt(parts[1]);
                if (index < 1 || index >= cur_index) {
                    return "Task number " + index + " does not exist!";
                }
                tasks[index].unmark();
                saveTasks();
                return "OK, I've marked this task as not done yet:\n" + tasks[index];
            } catch (NumberFormatException e) {
                return "Please provide a valid task number!";
            }
        }
        
        if (userInput.startsWith("delete")) {
            try {
                String[] parts = userInput.split(" ");
                if (parts.length < 2) {
                    return "Please specify which task to delete (e.g., delete 1)";
                }
                int index = Integer.parseInt(parts[1]);
                if (index < 1 || index >= cur_index) {
                    return "Task number " + index + " does not exist!";
                }
                String taskDescription = tasks[index].getDescription();
                System.out.println("____________________________________________________________");
                System.out.println("Noted. I've removed this task:");
                System.out.println("  " + tasks[index]);
                
                // Shift remaining tasks to fill the gap
                for (int i = index; i < cur_index - 1; i++) {
                    tasks[i] = tasks[i + 1];
                }
                cur_index--;
                
                saveTasks();
                return "Now you have " + (cur_index - 1) + " tasks in the list.";
            } catch (NumberFormatException e) {
                return "Please provide a valid task number!";
            }
        }
        
        if (userInput.startsWith("show date")) {
            String dateStr = userInput.substring(9).trim();
            if (dateStr.isEmpty()) {
                return "Please specify a date (yyyy-MM-dd)";
            }
            return showTasksOnDate(dateStr);
        }
        
        if (userInput.startsWith("find")) {
            String keyword = userInput.substring(4).trim();
            if (keyword.isEmpty()) {
                return "Please provide a keyword to search for!";
            }
            return findTasks(keyword);
        }
        
        if (userInput.startsWith("todo")) {
            String description = userInput.substring(4).trim();
            if (description.isEmpty()) {
                return "Please provide a description for your todo!";
            }
            tasks[cur_index] = new Task(description);
            cur_index++;
            saveTasks();
            return "Got it. I've added this task:\n  " + tasks[cur_index - 1] + 
                   "\nNow you have " + (cur_index - 1) + " tasks in the list.";
        }
        
        if (userInput.startsWith("deadline")) {
            String[] parts = userInput.split(" /by ");
            if (parts.length != 2 || parts[0].substring(8).trim().isEmpty()) {
                return "Please use the format: deadline <description> /by yyyy-MM-dd HHmm";
            }
            String description = parts[0].substring(8).trim();
            try {
                tasks[cur_index] = new Deadline(description, parts[1].trim());
            } catch (DateTimeParseException e) {
                return "Please use the format yyyy-MM-dd HHmm for the deadline";
            }
            cur_index++;
            saveTasks();
            return "Got it. I've added this task:\n  " + tasks[cur_index - 1] + 
                   "\nNow you have " + (cur_index - 1) + " tasks in the list.";
        }
        
        if (userInput.startsWith("event")) {
            String[] parts = userInput.split(" /from | /to ");
            if (parts.length != 3 || parts[0].substring(5).trim().isEmpty()) {
                return "Please use the format: event <description> /from yyyy-MM-dd HHmm /to yyyy-MM-dd HHmm";
            }
            String description = parts[0].substring(5).trim();
            try {
                tasks[cur_index] = new Event(description, parts[1].trim(), parts[2].trim());
            } catch (DateTimeParseException e) {
                return "Please use the format yyyy-MM-dd HHmm for the dates";
            }
            cur_index++;
            saveTasks();
            return "Got it. I've added this task:\n  " + tasks[cur_index - 1] + 
                   "\nNow you have " + (cur_index - 1) + " tasks in the list.";
        }
        
        return "I'm not sure what you mean.\nType 'help' to see what I can do!";
    }

    /**
     * Starts the chatbot and processes user commands until exit.
     * Supports commands for adding, listing, marking, and managing tasks.
     */
    public void run() {
        System.out.println("Hello I am Noga, what can I do for you?");
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            String userInput = scanner.nextLine();
            String response = getResponse(userInput);
            System.out.println("____________________________________________________________");
            System.out.println(response);
            System.out.println("____________________________________________________________");
            
            if (userInput.trim().equals("bye")) {
                break;
            }
        }
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
