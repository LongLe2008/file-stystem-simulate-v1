//Library of most of program's output

public class OutputLibrary {
    public static String getAllCommands() {
        return "cat   cd   clear   cp   echo   help   logname   ls   mkdir   mv   nano   pwd   raw   rm   shutdown";
    }

    //These are the set of methods that returns the help message specifically for the aforementioned commands
    public static String cat() {
        return "usage: cat <text_file>";
    }

    public static String cd() {
        return "usage: cd <directory>";
    }

    public static String clear() {
        return "usage: clear";
    }

    public static String cp() {
        return "usage: cp <directory>";
    }

    public static String echo() {
        return "usage: echo <argument> OR echo <text_content> > <file_name>";
    }

    public static String help() {
        return "usage: help OR help <command>";
    }

    public static String logname() {
        return "usage: logname";
    }

    public static String ls() {
        return "usage: ls OR ls <directory>";
    }

    public static String mkdir() {
        return "usage: mkdir <directory_name>";
    }

    public static String mv() {
        return "usage: mv <file> <directory>";
    }

    public static String nano() {
        return "usage: nano <text_file> <new_content>";
    }

    public static String pwd() {
        return "usage: pwd";
    }

    public static String raw() {
        return "usage: raw; a special command that returns the raw String in which files and folders are stored upon";
    }

    public static String rm() {
        return "usage: rm <file>";
    }

    public static String shutdown() {
        return "usage: shutdown";
    }

    public static String getUnknownCommandError() {
        return "Unsupported command: ";
    }

    public static String getIntroduction(String username) {
        return "Welcome " + username + ", to Terminal!\nTo print available commands, type 'help' and press Enter.\nType 'shutdown' to stop.\nI put a lot of work into this, so enjoy ^-^";
    }

    public static String getUsernamePrompt() {
        return "Please enter your username: ";
    }

    public static String getIllegalFileNameError() {
        return "error: illegal file name";
    }

    public static String getIllegalFileContentError() {
        return "error: illegal file content";
    }

    public static String getInvalidArgumentError() {
        return "error: invalid argument";
    }

    public static String getInvalidDirectoryError() {
        return "error: invalid directory";
    }

    public static String getIllegalUsernameError() {
        return "error: invalid username; username must only consists of normal letters, numbers or dashes and is less than 15 characters";
    }

    public static String getPrompt(String username, String currentDirectory) {
        return username + "@codehs " + currentDirectory + " $ ";
    }



}