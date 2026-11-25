public class TimeSpan {

    private int hours;
    private int minutes;

    public TimeSpan() {
        this(0, 0);
    }

    public TimeSpan(int minutes) {
        if (minutes < 0) {
            throw new IllegalInputTimeSpanException(
                    "Кількість хвилин не може бути від'ємною: " + minutes);
        }
        setTotalMinutes(minutes);
    }

    public TimeSpan(int hours, int minutes) {
        checkHoursAndMinutes(hours, minutes);
        this.hours = hours;
        this.minutes = minutes;
    }

    public TimeSpan(TimeSpan span) {
        checkNotNull(span);
        this.hours = span.getHours();
        this.minutes = span.getMinutes();
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void add(int hours, int minutes) {
        checkHoursAndMinutes(hours, minutes);
        setTotalMinutes(getTotalMinutes() + hours * 60 + minutes);
    }

    public void add(int minutes) {
        if (minutes < 0) {
            throw new IllegalInputTimeSpanException(
                    "Кількість хвилин для додавання не може бути від'ємною: " + minutes);
        }
        setTotalMinutes(getTotalMinutes() + minutes);
    }

    public void add(TimeSpan span) {
        checkNotNull(span);
        add(span.getHours(), span.getMinutes());
    }

    public void subtract(int hours, int minutes) {
        checkHoursAndMinutes(hours, minutes);
        subtractMinutes(hours * 60 + minutes);
    }

    public void subtract(int minutes) {
        if (minutes < 0) {
            throw new IllegalInputTimeSpanException(
                    "Кількість хвилин для віднімання не може бути від'ємною: " + minutes);
        }
        subtractMinutes(minutes);
    }

    public void subtract(TimeSpan span) {
        checkNotNull(span);
        subtract(span.getHours(), span.getMinutes());
    }

    public double getTotalHours() {
        return hours + minutes / 60.0;
    }

    public int getTotalMinutes() {
        return hours * 60 + minutes;
    }

    public void scale(int factor) {
        if (factor <= 0) {
            throw new IllegalScaleFactorException("Множник повинен бути більшим за 0: " + factor);
        }
        setTotalMinutes(getTotalMinutes() * factor);
    }

    @Override
    public String toString() {
        return hours + " год " + minutes + " хв";
    }

    private void subtractMinutes(int amount) {
        int result = getTotalMinutes() - amount;
        if (result < 0) {
            throw new NegativeTimeSpanException(
                    "Інтервал не може стати від'ємним: " + this + " - " + amount + " хв");
        }
        setTotalMinutes(result);
    }

    private void setTotalMinutes(int totalMinutes) {
        this.hours = totalMinutes / 60;
        this.minutes = totalMinutes % 60;
    }

    private static void checkHoursAndMinutes(int hours, int minutes) {
        if (hours < 0 || minutes < 0) {
            throw new IllegalInputTimeSpanException(
                    "Години та хвилини не можуть бути від'ємними: " + hours + " год " + minutes + " хв");
        }
        if (minutes > 59) {
            throw new IllegalInputTimeSpanException(
                    "Кількість хвилин повинна бути від 0 до 59: " + minutes);
        }
    }

    private static void checkNotNull(TimeSpan span) {
        if (span == null) {
            throw new IllegalInputTimeSpanException("Інтервал часу не може бути null");
        }
    }
}
