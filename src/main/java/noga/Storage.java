public class Storage {
    private static final String DATA_DIRECTORY = "data";
    private static final String DATA_FILE_PATH = "data/tasks.txt";

    public void save(TaskList tasks) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE_PATH))) {
            // Save tasks
        } catch (IOException e) {
            throw new StorageException("Could not save tasks", e);
        }
    }

    public TaskList load() {
        try {
            // Load tasks
        } catch (IOException e) {
            throw new StorageException("Could not load tasks", e);
        }
    }
} 