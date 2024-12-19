//This class interprets commands and execute the appropriate methods

public class Executor {
    private FileArray files;
    private final String username;
    private String currentDirectory;

    public Executor(String username, FileArray files) {
        this.username = username;
        this.files = files;
        this.currentDirectory = "/";
    }

    public Executor(String username) {
        this.username = username;
        this.files = new FileArray();
        this.currentDirectory = "/";
    }

    public String getUsername() {
        return username;
    }

    public String getPromptSimpleDirectory() {
        if (this.currentDirectory.equals("/")) {
            return "/";
        } else {
            return Str.section(currentDirectory, "/", Str.frequency(currentDirectory, "/"));
        }
    }

    //A method that takes commands argument from the MyProgram class and filter the text to choose the function to execute to execute the command
    public String execute(String argument) {
        if (Str.section(argument, " ", 0).equals("cat")) {
            return cat(argument.substring(argument.indexOf("cat") + ((argument.length() > "cat".length()) ? "cat".length() + 1 : "cat".length())));
        } else if (Str.section(argument, " ", 0).equals("cd")) {
            return cd(argument.substring(argument.indexOf("cd") + ((argument.length() > "cd".length()) ? "cd".length() + 1 : "cd".length())));
        } else if (Str.section(argument, " ", 0).equals("clear")) {
            return clear(argument.substring(argument.indexOf("clear") + ((argument.length() > "clear".length()) ? "clear".length() + 1 : "clear".length())));
        } else if (Str.section(argument, " ", 0).equals("cp")) {
            return cp(argument.substring(argument.indexOf("cp") + ((argument.length() > "cp".length()) ? "cp".length() + 1 : "cp".length())));
        } else if (Str.section(argument, " ", 0).equals("echo")) {
            return echo(argument.substring(argument.indexOf("echo") + ((argument.length() > "echo".length()) ? "echo".length() + 1 : "echo".length())));
        } else if (Str.section(argument, " ", 0).equals("help")) {
            return help(argument.substring(argument.indexOf("help") + ((argument.length() > "help".length()) ? "help".length() + 1 : "help".length())));
        } else if (Str.section(argument, " ", 0).equals("logname")) {
            return logname(argument.substring(argument.indexOf("logname") + ((argument.length() > "logname".length()) ? "loganme".length() + 1 : "logname".length())));
        } else if (Str.section(argument, " ", 0).equals("ls")) {
            return ls(argument.substring(argument.indexOf("ls") + ((argument.length() > "ls".length()) ? "ls".length() + 1 : "ls".length())));
        } else if (Str.section(argument, " ", 0).equals("mkdir")) {
            return mkdir(argument.substring(argument.indexOf("mkdir") + ((argument.length() > "mkdir".length()) ? "mkdir".length() + 1 : "mkdir".length())));
        } else if (Str.section(argument, " ", 0).equals("mv")) {
            return mv(argument.substring(argument.indexOf("mv") + ((argument.length() > "mv".length()) ? "mv".length() + 1 : "mv".length())));
        } else if (Str.section(argument, " ", 0).equals("nano")) {
            return nano(argument.substring(argument.indexOf("nano") + ((argument.length() > "nano".length()) ? "nano".length() + 1 : "nano".length())));
        } else if (Str.section(argument, " ", 0).equals("pwd")) {
            return pwd(argument.substring(argument.indexOf("pwd") + ((argument.length() > "pwd".length()) ? "pwd".length() + 1 : "pwd".length())));
        } else if (Str.section(argument, " ", 0).equals("raw")) {
            return raw(argument.substring(argument.indexOf("raw") + ((argument.length() > "raw".length()) ? "raw".length() + 1 : "raw".length())));
        } else if (Str.section(argument, " ", 0).equals("rm")) {
            return rm(argument.substring(argument.indexOf("rm") + ((argument.length() > "rm".length()) ? "rm".length() + 1 : "rm".length())));
        } else {
            return OutputLibrary.getUnknownCommandError() + Str.section(argument, " ", 0);
        }
    }

    //A method that combines current implicit directory and the directory given in argument and spits out the new directory
    private String combineDirectory(String arguedDirectory) {
        String newDirectory = currentDirectory;
        if (Str.section(arguedDirectory, "/", 0).equals("")) {
            return arguedDirectory;
        } else {
            for (int i = 0; i <= Str.frequency(arguedDirectory, "/"); i++) {
                if (Str.section(arguedDirectory, "/", i).equals("..")) {
                    newDirectory = newDirectory.substring(0, Str.lastIndexOf(newDirectory, "/"));
                } else {
                    if (!Str.section(arguedDirectory, "/", i).equals("")) {
                        newDirectory += ((currentDirectory.equals("/") ? "" : "/"))  + Str.section(arguedDirectory, "/", i);
                    }
                }
            }
        }
        if (newDirectory.equals("")) {
            newDirectory = "/";
        }
        return newDirectory;
    }


    private String cat(String argument) {
        return files.viewTxt(combineDirectory(argument));
    }

    private String cd(String argument) {
        if (files.typeOf(combineDirectory(argument)).equals("folder")) {
            this.currentDirectory = combineDirectory(argument);
            return "";
        } else {
            return OutputLibrary.getInvalidDirectoryError();
        }
    }

    //Prints many lines, effectively clears the terminal
    private String clear(String argument) {
        return "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";
    }

    private String cp(String argument) {
        if (argument.indexOf(" ")<0) {
            return OutputLibrary.getInvalidArgumentError();
        } else {
            String movable = Str.section(argument, " ", 0);
            String destination = Str.section(argument, " ", 1);
            files.addRaw(destination, files.findRawContent(movable));
            return "";
        }
    }

    //This command can either echo to txt or echo to terminal
    private String echo(String argument) {
        if (!(Str.frequency(argument, ">")==1)) {
            return argument;
        } else {
            return (files.addTxt(currentDirectory, Str.section(argument, ">", 1), Str.section(argument, ">", 0))) ? "" : "unsuccessful";
        }
    }

    private static String help(String argument) {
        if (argument.equals("")) {
            return OutputLibrary.getAllCommands();
        } else {
            if (Str.section(argument, " ", 0).equals("cat")) {
                return OutputLibrary.cat();
            } else if (Str.section(argument, " ", 0).equals("cd")) {
                return OutputLibrary.cd();
            } else if (Str.section(argument, " ", 0).equals("clear")) {
                return OutputLibrary.clear();
            } else if (Str.section(argument, " ", 0).equals("cp")) {
                return OutputLibrary.cp();
            } else if (Str.section(argument, " ", 0).equals("echo")) {
                return OutputLibrary.echo();
            } else if (Str.section(argument, " ", 0).equals("help")) {
                return OutputLibrary.help();
            } else if (Str.section(argument, " ", 0).equals("logname")) {
                return OutputLibrary.logname();
            } else if (Str.section(argument, " ", 0).equals("ls")) {
                return OutputLibrary.ls();
            } else if (Str.section(argument, " ", 0).equals("mkdir")) {
                return OutputLibrary.mkdir();
            } else if (Str.section(argument, " ", 0).equals("mv")) {
                return OutputLibrary.mv();
            } else if (Str.section(argument, " ", 0).equals("nano")) {
                return OutputLibrary.nano();
            } else if (Str.section(argument, " ", 0).equals("pwd")) {
                return OutputLibrary.pwd();
            } else if (Str.section(argument, " ", 0).equals("raw")) {
                return OutputLibrary.raw();
            } else if (Str.section(argument, " ", 0).equals("rm")) {
                return OutputLibrary.rm();
            } else {
                return OutputLibrary.getUnknownCommandError() + Str.section(argument, " ", 0);
            }
        }
    }

    private String logname(String argument) {
        return this.username;
    }

    private String ls(String argument) {
        if (argument.equals("")) {
            return files.listDirectory(currentDirectory);
        } else {
            return files.listDirectory(combineDirectory(Str.section(argument, " ", 0)));
        }
    }

    private String mkdir(String argument) {
        return  (files.addFolder(currentDirectory, argument)) ? "" : "unsuccessful";
    }

    private String mv(String argument) {
        if (argument.indexOf(" ")<0) {
            return OutputLibrary.getInvalidArgumentError();
        } else {
            String movable = Str.section(argument, " ", 0);
            String destination = Str.section(argument, " ", 1);
            files.addRaw(destination, files.findRawContent(movable));
            files.remove(movable);
            return "";
        }
    }

    private String nano(String argument) {
        if (argument.indexOf(" ")<0) {
            return OutputLibrary.getInvalidArgumentError();
        } else {
            String path = Str.section(argument, " ", 0);
            String newContent = argument.substring(path.length() + 1);
            return (files.editTxt(path, newContent)) ? "" : "unsuccessful";
        }
    }

    private String pwd(String argument) {
        return currentDirectory;
    }

    private String raw(String argument) {
        return files.getRaw();
    }

    private String rm(String argument) {
        return (files.remove(combineDirectory(argument))) ? "" : "unsuccessful";
    }
}