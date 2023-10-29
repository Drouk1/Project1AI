public class Main {
    public static void main(String[] args) {
         // Check if no arguments were provided
         if(args.length < 1) {
            // Print an error message indicating the problem
            System.err.println("Error: Please provide the number of family members and their times.");
            // Exit the program with a status code indicating an error
            System.exit(1);
        
            // Check if the number of provided times (args.length - 1) doesn't match the number of family members specified in args[0]
            }else if(args.length-1 != Integer.parseInt(args[0])) {
            // Print an error message indicating the mismatch
            System.err.println("Error: The amount of family members given is not equal to the times given.");
            // Exit the program with a status code indicating an error
            System.exit(1);
            }
    }
}
