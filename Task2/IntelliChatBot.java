import java.util.*;
import java.time.*;

public class IntelliChatBot {

    private static Scanner scanner = new Scanner(System.in);
    private static Random random = new Random();

    public static void main(String[] args) {
        greetUser();
        startConversation();
    }

    private static void greetUser() {
        System.out.println("====================================");
        System.out.println("        Welcome to IntelliChat");
        System.out.println("====================================");
        System.out.println("Type 'exit' anytime to end the chat.\n");
    }

    private static void startConversation() {
        while (true) {
            System.out.print("You: ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("exit")) {
                System.out.println("Bot: It was nice talking to you. Goodbye.");
                break;
            }

            String response = generateResponse(input);
            System.out.println("Bot: " + response);
        }
    }

    private static String generateResponse(String input) {

        // Greetings
        if (containsKeywords(input, "hello", "hi", "hey", "greetings")) {
            return randomResponse(new String[]{
                    "Hello! How are you today?",
                    "Hi there! Nice to chat with you.",
                    "Hey! What's up?"
            });
        }

        // Ask about bot identity
        else if (containsKeywords(input, "your name", "who are you")) {
            return "I am IntelliChat, your friendly Java chatbot.";
        }

        // Date & Time
        else if (containsKeywords(input, "time")) {
            return "Current time is: " + LocalTime.now().withNano(0);
        }

        else if (containsKeywords(input, "date")) {
            return "Today's date is: " + LocalDate.now();
        }

        // Emotions
        else if (containsKeywords(input, "sad", "upset", "depressed", "unhappy", "bad")) {
            return randomResponse(new String[]{
                    "I am sorry you feel that way. Things will get better.",
                    "That's tough. Do you want to talk about it?",
                    "I understand. Sometimes sharing helps."
            });
        }

        else if (containsKeywords(input, "happy", "good", "great", "awesome")) {
            return randomResponse(new String[]{
                    "That's wonderful to hear!",
                    "Keep that positive energy!",
                    "Glad you are feeling good!"
            });
        }

        // Gratitude & courtesy
        else if (containsKeywords(input, "thank", "thanks")) {
            return randomResponse(new String[]{
                    "You're welcome!",
                    "Anytime! Glad to help.",
                    "No problem at all!"
            });
        }

        // Help or instructions
        else if (containsKeywords(input, "help", "what can you do")) {
            return "I can respond to greetings, emotions, small talk, do basic math, and tell date & time.";
        }

        // Small talk
        else if (containsKeywords(input, "how are you")) {
            return randomResponse(new String[]{
                    "I'm doing great! How about you?",
                    "I’m just code, but I feel productive!",
                    "Feeling chatty today. How about you?"
            });
        }

        // Math operations
        else if (input.matches(".*\\d+\\s*[+\\-*/]\\s*\\d+.*")) {
            return solveMath(input);
        }

        // Fallback generic responses
        else {
            return randomResponse(new String[]{
                    "Interesting. Tell me more.",
                    "Could you explain that differently?",
                    "I am learning every day. Can you elaborate?",
                    "That sounds nice. Go on."
            });
        }
    }

    private static boolean containsKeywords(String input, String... keywords) {
        for (String keyword : keywords) {
            if (input.contains(keyword)) return true;
        }
        return false;
    }

    private static String randomResponse(String[] responses) {
        return responses[random.nextInt(responses.length)];
    }

    private static String solveMath(String input) {
        try {
            input = input.replaceAll("\\s+", "");
            char operator = 0;

            for (char c : input.toCharArray()) {
                if (c == '+' || c == '-' || c == '*' || c == '/') {
                    operator = c;
                    break;
                }
            }

            String[] parts = input.split("[+\\-*/]");
            if (parts.length != 2) return "Sorry, I can only solve simple two-number calculations.";

            double num1 = Double.parseDouble(parts[0]);
            double num2 = Double.parseDouble(parts[1]);

            switch (operator) {
                case '+': return "Result: " + (num1 + num2);
                case '-': return "Result: " + (num1 - num2);
                case '*': return "Result: " + (num1 * num2);
                case '/': return num2 == 0 ? "Cannot divide by zero." : "Result: " + (num1 / num2);
                default: return "Invalid calculation.";
            }
        } catch (Exception e) {
            return "Sorry, I could not calculate that.";
        }
    }
}
