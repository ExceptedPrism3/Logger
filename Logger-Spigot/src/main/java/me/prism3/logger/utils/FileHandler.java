package me.prism3.logger.utils;

import me.prism3.logger.Main;
import me.prism3.logger.utils.enums.LogCategory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static me.prism3.logger.utils.Data.fileDeletion;


/**
 * The `FileHandler` class is responsible for managing log files for different categories and handling file-based logs.
 * It provides methods to create log files, retrieve log files by category, and write log messages to these files.
 */
public class FileHandler {

    // Constants for folder and date format names
    private static final String LOGS_FOLDER_NAME = "Logs";
    private static final String PLAYER_DEATH_FOLDER_NAME = "Player Death";
    private static final String BACKUPS_FOLDER_NAME = "Backups";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final Map<String, File> logFileMap = new HashMap<>(); // Map to store log files by category
    private final File dataFolder; // The main data folder where log files are stored

    /**
     * Constructs a new `FileHandler` instance.
     *
     * @param dataFolder The main data folder where log files should be created.
     */
    public FileHandler(final File dataFolder) {
        this.dataFolder = dataFolder;
        dataFolder.mkdirs(); // Ensure the data folder exists
        this.createLogFiles(); // Create log files for each category
        this.createPlayerDeathBackupFolder(); // Create a separate folder for player death backups
        this.createDatabaseFolder();
        this.deleteFiles(this.dataFolder);
    }

    /**
     * Retrieves the log file associated with the given category.
     *
     * @param category The log category for which to retrieve the log file.
     * @return The log file for the specified category.
     */
    public File getLogFile(final LogCategory category) { return this.logFileMap.get(category.getFolderName()); }

    /**
     * Handles a file-based log entry by appending the log message to the log file.
     *
     * @param category    The log category for the message.
     * @param messagePath The path to the log message in the message file.
     * @param placeholders Map of placeholders and their corresponding values for message formatting.
     */
    public void handleFileLog(final LogCategory category, final String messagePath, final Map<String, String> placeholders) {

        final File logFile = getLogFile(category);

        if (logFile == null) {
            Log.severe("Log file not found for category " + category.getFolderName());
            return;
        }

        String message = Main.getInstance().getMessagesFile().get().getString(messagePath);

        if (message == null) {
            Log.severe("The following path " + messagePath + " could not be found, make sure it's not renamed or deleted. If the issue persists, re-create the messages file");
            return;
        }

        // Replace placeholders in the message
        for (Map.Entry<String, String> placeholder : placeholders.entrySet())
            message = message.replace(placeholder.getKey(), placeholder.getValue());

        // Append the log message to the log file
        try {
            Files.write(logFile.toPath(), (message + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        } catch (final IOException e) {
            Log.warning("An error occurred while logging into the appropriate file.");
            Log.warning(e.getMessage());
        }
    }

    /**
     * Creates log files for each category and stores them in the logFileMap.
     */
    private void createLogFiles() {

        for (LogCategory category : LogCategory.values()) {
            String displayName = category.getFolderName();
            File categoryFolder = this.createFolder(LOGS_FOLDER_NAME, displayName);
            File logFile = this.createLogFile(categoryFolder, this.getCurrentLogFileName());
            this.logFileMap.put(displayName, logFile);
        }
    }

    /**
     * Creates a folder with the given names as subdirectories in the data folder.
     *
     * @param folderNames Names of the subdirectories.
     * @return The created folder.
     */
    private File createFolder(final String... folderNames) {

        File folder = this.dataFolder;

        for (String folderName : folderNames) {
            folder = new File(folder, folderName);
            folder.mkdirs();
        }

        return folder;
    }

    /**
     * Creates a log file with the given name in the specified parent folder.
     *
     * @param parentFolder The folder where the log file will be created.
     * @param fileName     The name of the log file.
     * @return The created log file.
     */
    private File createLogFile(final File parentFolder, final String fileName) {

        final File logFile = new File(parentFolder, fileName);

        try {
            if (!logFile.exists())
                logFile.createNewFile();
        } catch (final IOException e) {
            Log.warning("An error occurred while creating the log file: " + e.getMessage());
        }

        return logFile;
    }

    /**
     * Creates the player death backup folder structure within the Player Death subdirectory.
     */
    private void createPlayerDeathBackupFolder() {
        this.createFolder(LOGS_FOLDER_NAME, PLAYER_DEATH_FOLDER_NAME, BACKUPS_FOLDER_NAME);
    }

    /**
     * Creates a folder for the given log category.
     *
     * @param category The log category for which to create a folder.
     */
    private void createCategoryFolder(final LogCategory category) {
        new File(dataFolder, category.getFolderName());
    }

    private void createDatabaseFolder() {
        this.createCategoryFolder(LogCategory.DATABASE);
    }

    /**
     * Generates the current log file name based on the date.
     *
     * @return The formatted current log file name.
     */
    private String getCurrentLogFileName() {
        final LocalDate currentDate = LocalDate.now();
        return currentDate.format(DATE_FORMAT) + ".log";
    }

    /**
     * Gets the folder for a specific log category.
     *
     * @param category The log category for which to retrieve the folder.
     * @return The folder for the specified category.
     */
    public File getCategoryFolder(final LogCategory category) {
        return this.createFolder(LOGS_FOLDER_NAME, category.getFolderName());
    }

    /**
     * Deletes log files that are older than a specified threshold.
     *
     * @param dataFolder The data folder where log files are stored.
     */
    private void deleteFiles(final File dataFolder) {

        if (fileDeletion <= 0)
            return;

        final long deadLine = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(fileDeletion);
        final File logsFolder = new File(dataFolder, "Logs");

        try (final Stream<Path> files = Files.walk(Paths.get(logsFolder.getPath()))) {

            files.filter(path -> {

                try {
                    return Files.isRegularFile(path) && Files.getLastModifiedTime(path).toMillis() < deadLine;
                } catch (final IOException ex) {
                    Log.severe("An error occurred while checking file modification time.");
                    return false;
                }
            }).forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (final IOException ex) {
                    Log.severe("An error occurred while deleting files.");
                }
            });
        } catch (final IOException e) {
            Log.severe("An error occurred while searching for files to delete.");
        }
    }
}
