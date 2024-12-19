public class FileArray {
    //Sample path: /fbiopenup/Minecraft/cheatcodes
    //Sample raw storage: fbiopen up[Minecraft[cheatcodes{some minecraft cheat codes}]]
    private String raw;
    private static final String LEGAL_BRACKETS = "[]{}";
    private static final String LEGAL_FILE_NAME_CHARACTERS = "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    public FileArray() {
        this.raw = "";
    }

    public FileArray(String raw) {
        this.raw = raw;
    }

    public String getRaw() {
        return this.raw;
    }

    //file contents must match one of the characters above, also maybe used for username check aswell
    public static boolean isFileNameLegal(String name) {
        for (int i = 0; i<name.length(); i++) {
            if (!(LEGAL_FILE_NAME_CHARACTERS.indexOf(name.substring(i, i + 1)) >= 0)) {
                return false;
            }
        }
        return (name.length() > 0) ? true : false;
    }

    //if file content uses one of the legal brackets, not legal!
    public static boolean isFileContentLegal(String content) {
        for (int i = 0; i<content.length(); i++) {
            if ((LEGAL_BRACKETS.indexOf(content.substring(i, i + 1)) >= 0)) {
                return false;
            }
        }
        return true;
    }

    //This just assembles the raw for txt and hands everything to addRaw
    public boolean addTxt(String path, String name, String content) {
        if (!typeOf(path).equals("invalid") && isFileNameLegal(name) && isFileContentLegal(content) && listDirectory(path, true).indexOf(name)<0) {
            String newRaw = name + LEGAL_BRACKETS.substring(2, 3) + content + LEGAL_BRACKETS.substring(3, 4);
            return addRaw(path, newRaw);
        }
        return false;
    }

    //This just assembles the raw for folder and hands everything to addRaw
    public boolean addFolder(String path, String name) {
        if (!typeOf(path).equals("invalid") && isFileNameLegal(name) && listDirectory(path, true).indexOf(name)<0) {
            String newRaw = name + LEGAL_BRACKETS.substring(0, 2);
            return addRaw(path, newRaw);
        }
        return false;
    }

    //Main function that adds verything
    public boolean addRaw(String path, String raw) {
        if (!typeOf(path).equals("invalid")) {
            //Special condition for main path
            if (path.equals("/") || path.equals("")) {
                this.raw += raw;
                return true;
            } else {
                //Finds the index of the raw content
                int index = this.raw.indexOf(findRawContent(path));
                int endingIndex = index;
                boolean finished = false;
                //Finds the area after inside the nest inside the folder we would like to add this in
                for (endingIndex = index; !finished; endingIndex++) {
                    if (this.raw.substring(endingIndex, endingIndex + 1).equals(LEGAL_BRACKETS.substring(0, 1))) {
                        finished = true;
                    }
                }
                this.raw = this.raw.substring(0, endingIndex) + raw + this.raw.substring(endingIndex);
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean rename(String path, String name) {
        if (!typeOf(path).equals("invalid") && isFileNameLegal(name)) {
            //get starting, where the name can be indexed at
            int startingIndex = this.raw.indexOf(findRawContent(path));
            int endingIndex;
            boolean finished = false;
            //get ending, the index which the name stops at
            for (endingIndex = startingIndex; !finished; endingIndex++) {
                if (this.raw.substring(endingIndex, endingIndex + 1).equals(LEGAL_BRACKETS.substring(0, 1)) || this.raw.substring(endingIndex, endingIndex + 1).equals(LEGAL_BRACKETS.substring(2, 3))) {
                    finished = true;
                    endingIndex--;
                }
            }
            //now fill in the gap of the old name via the new name
            raw = raw.substring(0, startingIndex) + name + raw.substring(endingIndex);
            return true;
        } else {
            return false;
        }
    }

    public String viewTxt(String path) {
        if (typeOf(path).equals("file")) {
            int startingIndex = this.raw.indexOf(findRawContent(path));
            int endingIndex;
            boolean finished = false;
            //finds the index for start of txt file
            for (; !finished; startingIndex++) {
                if (this.raw.substring(startingIndex, startingIndex + 1).equals(LEGAL_BRACKETS.substring(2, 3))) {
                    finished = true;
                }
            }
            finished = false;
            //finds the index for ends of txt file
            for (endingIndex = startingIndex; !finished; endingIndex++) {
                if (this.raw.substring(endingIndex, endingIndex + 1).equals(LEGAL_BRACKETS.substring(3, 4))) {
                    finished = true;
                    endingIndex--;
                }
            }
            return this.raw.substring(startingIndex, endingIndex);
        } else {
            return OutputLibrary.getInvalidDirectoryError();
        }
    }

    //uses viewTxt but indexes the txt part to replace it
    public boolean editTxt(String path, String content) {
        /*
        if (typeOf(path).equals("file") && isFileContentLegal(content)) {
            int startingIndex = this.raw.indexOf(findRawContent(path));
            int endingIndex;
            boolean finished = false;
            for (;finished; startingIndex++) {
                if (this.raw.substring(startingIndex, startingIndex + 1).equals(LEGAL_BRACKETS.substring(2, 3))) {
                    startingIndex++;
                    finished = true;
                }
            }
            finished = false;
            for (endingIndex = startingIndex; finished; endingIndex++) {
                if (this.raw.substring(endingIndex, endingIndex + 1).equals(LEGAL_BRACKETS.substring(3, 4))) {
                    finished = true;
                }
            }
            this.raw = this.raw.substring(0, startingIndex) + content + this.raw.substring(endingIndex);
            return true;
        } else {
            return false;
        }*/
        String oldContent = viewTxt(path);
        if (oldContent.equals(OutputLibrary.getInvalidDirectoryError())) {
            return false;
        } else {
            int startingIndex = this.raw.indexOf(oldContent);
            int endingIndex = startingIndex + oldContent.length();
            this.raw = this.raw.substring(0, startingIndex) + content + this.raw.substring(endingIndex);
            return true;
        }
    }

    //remove a file
    public boolean remove(String path) {
        if (typeOf(path).equals("folder") || typeOf(path).equals("file")) {
            String toBeDel = findRawContent(path);
            //use the len method and indexOf to get rid of the aforementioned file
            raw = raw.substring(0, raw.indexOf(toBeDel)) + raw.substring(raw.indexOf(toBeDel) + toBeDel.length());
            return true;
        } else {
            return false;
        }
    }

    //This method works by first filtering out the part where it is about the directory trying to see in and then looping through everything else
    public String listDirectory(String path) {
        String directoryList = "";
        if (typeOf(path).equals("folder")) {
            //Special condition for listing the whole thing, different from listing inner folder or something
            if (path.equals("") || path.equals("/")) {
                int nestLevel = 0;
                String raw = this.raw;
                for (int i = 0; i < raw.length(); i++) {
                    if (raw.substring(i, i + 1).equals(LEGAL_BRACKETS.substring(0, 1)) || raw.substring(i, i + 1).equals(LEGAL_BRACKETS.substring(2, 3))) {
                        nestLevel++;
                    } else if (raw.substring(i, i + 1).equals(LEGAL_BRACKETS.substring(1, 2)) || raw.substring(i, i + 1).equals(LEGAL_BRACKETS.substring(3, 4))) {
                        nestLevel--;
                        directoryList += "   ";
                    } else if (nestLevel == 0) {
                        directoryList += raw.substring(i, i + 1);
                    }
                }
            } else {
                int nestLevel = 0;
                boolean folderEntered = false;
                String raw = findRawContent(path);
                for (int i = 0; i < raw.length(); i++) {
                    if (raw.substring(i, i + 1).equals(LEGAL_BRACKETS.substring(0, 1)) && !folderEntered) {
                        folderEntered = true;
                    } else if (raw.substring(i, i + 1).equals(LEGAL_BRACKETS.substring(0, 1)) || raw.substring(i, i + 1).equals(LEGAL_BRACKETS.substring(2, 3))) {
                        nestLevel++;
                    } else if (raw.substring(i, i + 1).equals(LEGAL_BRACKETS.substring(1, 2)) || raw.substring(i, i + 1).equals(LEGAL_BRACKETS.substring(3, 4))) {
                        nestLevel--;
                        directoryList += "   ";
                    } else if (folderEntered && nestLevel == 0) {
                        directoryList += raw.substring(i, i + 1);
                    }
                }
            }
        }
        return directoryList;
    }

    //A special version of listDirectory intended to prevent same file name
    public String listDirectory(String path, boolean special) {
        String directoryList = "";
        if (typeOf(path).equals("folder")) {
            //Special condition for listing the whole thing, different from listing inner folder or something
            if (path.equals("") || path.equals("/")) {
                int nestLevel = 0;
                String raw = this.raw;
                for (int i = 0; i < raw.length(); i++) {
                    if (raw.substring(i, i + 1).equals(LEGAL_BRACKETS.substring(0, 1)) || raw.substring(i, i + 1).equals(LEGAL_BRACKETS.substring(2, 3))) {
                        nestLevel++;
                    } else if (raw.substring(i, i + 1).equals(LEGAL_BRACKETS.substring(1, 2)) || raw.substring(i, i + 1).equals(LEGAL_BRACKETS.substring(3, 4))) {
                        nestLevel--;
                        directoryList += ",";
                    } else if (nestLevel == 0) {
                        directoryList += raw.substring(i, i + 1);
                    }
                }
            } else {
                int nestLevel = 0;
                boolean folderEntered = false;
                String raw = findRawContent(path);
                for (int i = 0; i < raw.length(); i++) {
                    if (raw.substring(i, i + 1).equals(LEGAL_BRACKETS.substring(0, 1)) && !folderEntered) {
                        folderEntered = true;
                    } else if (raw.substring(i, i + 1).equals(LEGAL_BRACKETS.substring(0, 1)) || raw.substring(i, i + 1).equals(LEGAL_BRACKETS.substring(2, 3))) {
                        nestLevel++;
                    } else if (raw.substring(i, i + 1).equals(LEGAL_BRACKETS.substring(1, 2)) || raw.substring(i, i + 1).equals(LEGAL_BRACKETS.substring(3, 4))) {
                        nestLevel--;
                        directoryList += ",";
                    } else if (folderEntered && nestLevel == 0) {
                        directoryList += raw.substring(i, i + 1);
                    }
                }
            }
        }
        return directoryList;
    }

    //A method that returns the type (txt or folder) of a file
    public String typeOf(String path) {
        //If empty path, since empty path is ALWAYS folder
        if (path.equals("/") || path.equals("")) {
            return "folder";
        } else if (Str.happensFirst(findRawContent(path), LEGAL_BRACKETS.substring(0, 1), LEGAL_BRACKETS.substring(2, 3)).equals(LEGAL_BRACKETS.substring(2, 3))) {
            return "file";
        } else if (Str.happensFirst(findRawContent(path), LEGAL_BRACKETS.substring(0, 1), LEGAL_BRACKETS.substring(2, 3)).equals(LEGAL_BRACKETS.substring(0, 1))) {
            return "folder";
        } else {
            return "invalid";
        }
    }

    //A method that will return the raw content of a file/folder, including the file/folder's name
    public String findRawContent(String path) {
        if (path.equals("/") || path.equals("")) {
            return this.raw;
        }

        //Invalid path moment (if path invokes prohibited brackets)
        if (Str.frequency(path, LEGAL_BRACKETS.substring(0, 1))>0||Str.frequency(path, LEGAL_BRACKETS.substring(1, 2))>0||Str.frequency(path, LEGAL_BRACKETS.substring(2, 3))>0||Str.frequency(path, LEGAL_BRACKETS.substring(3, 4))>0) {
            return "";
        }

        String raw = this.raw;

        for (int i = 1; i <= Str.frequency(path, "/"); i++) {
            //if it is not the last/topmost dir, as the topmost dir is handled to allow for texts aswell
            if (i != Str.frequency(path, "/")) {
                boolean finished = false;
                while (!finished) {
                    int position = raw.indexOf(Str.section(path, "/", i));
                    if (position == -1) {
                        return "";
                    }

                    //this gets the nest level of file by subtracting the openings from the closings
                    int openBrackets = Str.frequency(raw.substring(0, position), LEGAL_BRACKETS.substring(0, 1)) + Str.frequency(raw.substring(0, position), LEGAL_BRACKETS.substring(2, 3));
                    int closeBrackets = Str.frequency(raw.substring(0, position), LEGAL_BRACKETS.substring(1, 2)) + Str.frequency(raw.substring(0, position), LEGAL_BRACKETS.substring(3, 4));

                    //condition for if nest level is expected!
                    if (openBrackets - closeBrackets == i - 1) {
                        finished = true;
                        boolean finished2 = false;
                        String localNestLevel = "";
                        boolean startedFolderBracket = false;

                        //keep looping through until it gets from one end to the other end of directory via nest calculation
                        for (int v = position; v < raw.length(); v++) {
                            String ch = raw.substring(v, v + 1);

                            if ((ch.equals(LEGAL_BRACKETS.substring(0, 1)) || ch.equals(LEGAL_BRACKETS.substring(2, 3))) && !finished2) {
                                localNestLevel += LEGAL_BRACKETS.substring(0, 1);
                                startedFolderBracket = true;
                            } else if ((ch.equals(LEGAL_BRACKETS.substring(1, 2)) || ch.equals(LEGAL_BRACKETS.substring(3, 4))) && !finished2) {
                                localNestLevel = localNestLevel.substring(0, localNestLevel.length() - 1);
                            }

                            if (localNestLevel.equals("") && startedFolderBracket && !finished2) {
                                raw = raw.substring(position, v + 1);
                                finished2 = true;
                            }
                        }

                        if (!finished2) {
                            return "";
                        }
                        //removes the irrelevent part from the String so it can't be indexed next time
                    } else {
                        raw = raw.substring(0, position) + raw.substring(position + Str.section(path, "/", i).length());
                    }
                }
                //nearly same logic here
            } else {
                boolean finished = false;
                while (!finished) {
                    int position = raw.indexOf(Str.section(path, "/", i));
                    if (position == -1) {
                        return "";
                    }
                    int openBrackets = Str.frequency(raw.substring(0, position), LEGAL_BRACKETS.substring(0, 1)) + Str.frequency(raw.substring(0, position), LEGAL_BRACKETS.substring(2, 3));
                    int closeBrackets = Str.frequency(raw.substring(0, position), LEGAL_BRACKETS.substring(1, 2)) + Str.frequency(raw.substring(0, position), LEGAL_BRACKETS.substring(3, 4));
                    if (openBrackets - closeBrackets == i - 1) {
                        finished = true;
                        boolean finished2 = false;
                        String localNestLevel = "";
                        boolean startedBracket = false;

                        for (int v = position; v < raw.length(); v++) {
                            String ch = raw.substring(v, v + 1);

                            if ((ch.equals(LEGAL_BRACKETS.substring(0, 1)) || ch.equals(LEGAL_BRACKETS.substring(2, 3))) & !finished2) {
                                localNestLevel += LEGAL_BRACKETS.substring(0, 1);
                                startedBracket = true;
                            } else if ((ch.equals(LEGAL_BRACKETS.substring(1, 2)) || ch.equals(LEGAL_BRACKETS.substring(3, 4))) && !finished2) {
                                localNestLevel = localNestLevel.substring(0, localNestLevel.length() - 1);
                            }

                            if (localNestLevel.equals("") && startedBracket && !finished2) {
                                raw = raw.substring(position, v + 1);
                                finished2 = true;
                            }
                        }

                        if (!finished2) {
                            return "";
                        }
                    } else {
                        raw = raw.substring(0, position) + raw.substring(position + Str.section(path, "/", i).length());
                    }
                }
            }
        }
        return raw;
    }

    //To get the level (how deep the file/folder is in the hierarchy)
    public int getLvl(int start, int end) {
        return Str.frequency(getLocation(start, end), "/");
    }

    //A method uses to get the hierarchy of a file/folder
    public String getLocation(int start, int end) {
        String raw = this.raw;
        String nestLevel = "";
        String path = "/";
        String toBeDel = "";

        // Clear out unrelated folders in the local raw String
        for (int i = 0; i < start; i++) {
            if (raw.substring(i, i + 1).equals(LEGAL_BRACKETS.substring(0, 1))) {
                nestLevel += LEGAL_BRACKETS.substring(0, 1);
            } else if (raw.substring(i, i + 1).equals(LEGAL_BRACKETS.substring(1, 2))) {
                nestLevel = nestLevel.substring(0, nestLevel.length() - 1);
                boolean finished = false;
                boolean openingBracketDeleted = false;
                int inspectionPoint = i;

                while (!finished) {
                    if (inspectionPoint < 0) {
                        finished = true;
                    }
                    if (openingBracketDeleted && (raw.substring(inspectionPoint, inspectionPoint + 1).equals(LEGAL_BRACKETS.substring(0, 1)) || raw.substring(inspectionPoint, inspectionPoint + 1).equals(LEGAL_BRACKETS.substring(1, 2)))) {
                        finished = true;
                    } else {
                        if (raw.substring(inspectionPoint, inspectionPoint + 1).equals(LEGAL_BRACKETS.substring(0, 1))) {
                            openingBracketDeleted = true;
                        }
                        toBeDel += String.valueOf(inspectionPoint) + ",";
                    }
                    inspectionPoint--;
                }
            }
        }

        // Delete every part to be deleted from the toBeDel var
        for (int i = Str.frequency(toBeDel, ",") - 1; i >= 0; i--) {
            int startIndex = Integer.parseInt(Str.section(toBeDel, ",", i));
            int endIndex = (i + 1 < Str.frequency(toBeDel, ",")) ? Integer.parseInt(Str.section(toBeDel, ",", i + 1)) : startIndex + 1;

            raw = raw.substring(0, startIndex) + raw.substring(endIndex);
        }

        // Check the file location in front of all folder starters to see the path leading to the file
        for (int i = 0; i < start; i++) {
            if (raw.substring(i, i + 1).equals(LEGAL_BRACKETS.substring(0, 1))) {
                int inspectionPoint = i - 1;
                boolean finished = false;
                String folderName = "";

                while (!finished) {
                    if (inspectionPoint < 0 || !(LEGAL_FILE_NAME_CHARACTERS.indexOf(raw.substring(inspectionPoint, inspectionPoint + 1)) >= 0)) {
                        finished = true;
                    } else {
                        folderName = raw.substring(inspectionPoint, inspectionPoint + 1) + folderName;
                        inspectionPoint--;
                    }
                }
                path += folderName + "/";
            }
        }

        //Now add the name for the final destination
        boolean finished = false;
        for (int i = start;!finished;i++) {
            if (LEGAL_FILE_NAME_CHARACTERS.indexOf(raw.substring(i, i + 1))>=0) {
                path += raw.substring(i, i + 1);
            } else {
                if (LEGAL_FILE_NAME_CHARACTERS.substring(0, 1).indexOf(raw.substring(i, i + 1))>=0) {
                    path += "/";
                    finished = true;
                } else {
                    finished = true;
                }
            }
        }

        return path;
    }
}