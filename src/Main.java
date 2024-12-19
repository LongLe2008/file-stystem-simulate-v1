import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        //Keeps asking for username until an appropriate one is given
        String username = "";
        boolean finished = false;
        while (!finished) {
            System.out.print(OutputLibrary.getUsernamePrompt());
            username = scanner.nextLine();
            if (FileArray.isFileNameLegal(username) && username.length()<15) {
                finished = true;
            } else {
                System.out.println(OutputLibrary.getIllegalUsernameError());
            }
        }
        Executor executor = new Executor(username, new FileArray("README{Tip: There is no tips you got fooled lol}"));
        System.out.println(OutputLibrary.getIntroduction(username));

        while (true) {
            System.out.print(OutputLibrary.getPrompt(executor.getUsername(), executor.getPromptSimpleDirectory()));
            String command = scanner.nextLine();
            if (Str.section(command, " ", 0).equals("shutdown")) {
                System.out.print("Thank you for using our service.");
                return;
            } else {
                //Avoid spaces if theres no output
                String output = executor.execute(command);
                if (!output.equals("")) {
                    System.out.println(output);
                }
            }
        }
    }
}