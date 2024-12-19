//Provide libraries for String stuffs that does not fit anywhere else

public class Str {
    public static String section(String x, String keyword, int index) {
        if (x.indexOf(keyword)==-1) {
            //Returns itself if no keyword is found at all within String and index is at 0
            return (index == 0) ? x : "";
        } else if (frequency(x, keyword)<index) {
            //Returns nothing if the string cannot be split enough for method to index the section
            return "";
        } else if (index < 0) {
            //Returns nothing if the index is nvalid
            return "";
        }
        for (int i = 0; i < index; i++) {
            x = x.substring(x.indexOf(keyword) + 1);
        }

        return (x.indexOf(keyword)!=-1) ? x.substring(0, x.indexOf(keyword)) : x;
    }

    //Check frequency of a specific keyword in a String
    public static int frequency(String x, String keyword) {
        int freq = 0;
        while (true) {
            if (x.indexOf(keyword)!=-1) {
                x = x.substring(0, x.indexOf(keyword)) + x.substring(x.indexOf(keyword) + keyword.length());
                freq++;
            } else {
                return freq;
            }
        }
    }

    //A method that removes all of the keyword from the aforementioned String
    public static String removeAll(String x, String keyword) {
        String newString = x;
        while (true) {
            if (x.indexOf(keyword)>-1) {
                newString = newString.substring(0, x.indexOf(keyword)) + newString.substring(x.indexOf(keyword) + keyword.length());
            } else {
                return newString;
            }
        }
    }

    public static String happensFirst(String x, String keyword1, String keyword2) {
        for (int i = 0; i<x.length(); i++) {
            if (x.substring(i, i + 1).equals(keyword1)) {
                return keyword1;
            } else if (x.substring(i, i + 1).equals(keyword2)) {
                return keyword2;
            }
        }
        return "";
    }

    //My own String.lastIndexOf
    public static int lastIndexOf(String x, String keyword) {
        String modifiedX = x;
        if (modifiedX.indexOf(keyword)<0) {
            return -1;
        } else {
            int lastIndex = 0;
            while (true) {
                if (modifiedX.indexOf(keyword)>=0) {
                    lastIndex = modifiedX.indexOf(keyword);
                    //Replaces the spaces with space as to not ruin the indexes system
                    for (int i = lastIndex; i<lastIndex + keyword.length(); i++) {
                        modifiedX = modifiedX.substring(0, i) + " " + modifiedX.substring(i + 1);
                    }
                } else {
                    return lastIndex;
                }
            }
        }
    }

    //A method that produces space from number mainly to prevent index logic errors
    public static String produceSpace(int number) {
        String spaceVal = "";
        for (int i = 0; i<number; i++) {
            spaceVal += " ";
        }
        return spaceVal;
    }
}