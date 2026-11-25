public class Main {
    public static void main(String[] args) {
        System.out.println("== Завдання 1 ==");

        TimeSpan first = new TimeSpan();
        TimeSpan second = new TimeSpan(150);
        TimeSpan third = new TimeSpan(2, 30);
        TimeSpan copy = new TimeSpan(third);
        System.out.println("first = " + first + ", second = " + second
                + ", third = " + third + ", copy = " + copy);

        third.add(1, 45);
        third.add(30);
        third.add(second);
        System.out.println("third після додавання: " + third);

        third.subtract(1, 15);
        third.subtract(30);
        third.subtract(second);
        System.out.println("third після віднімання: " + third);

        third.scale(2);
        System.out.println("third після scale(2): " + third
                + " (" + third.getTotalHours() + " год, " + third.getTotalMinutes() + " хв)");

        try {
            new TimeSpan(-10);
        } catch (IllegalInputTimeSpanException e) {
            System.out.println("IllegalInputTimeSpanException: " + e.getMessage());
        }

        try {
            new TimeSpan(1, 75);
        } catch (IllegalInputTimeSpanException e) {
            System.out.println("IllegalInputTimeSpanException: " + e.getMessage());
        }

        try {
            first.subtract(10);
        } catch (NegativeTimeSpanException e) {
            System.out.println("NegativeTimeSpanException: " + e.getMessage());
        }
        System.out.println("first після невдалого віднімання: " + first);

        try {
            second.scale(0);
        } catch (IllegalScaleFactorException e) {
            System.out.println("IllegalScaleFactorException: " + e.getMessage());
        }

        System.out.println();
        System.out.println("== Завдання 2 ==");

        ClientInterface clientInterface = new ClientInterface();
        clientInterface.start();
    }
}
