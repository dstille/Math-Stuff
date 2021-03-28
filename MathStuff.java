import java.util.*;
public class MathStuff {
    public static void main(String[] args) {
        System.out.println(toInt("11251234567", 10));
        long start = System.nanoTime();
        System.out.println(toInt("11251234567", 8));
        System.out.println("Using Math.pow: " + (System.nanoTime() - start));
        start = System.nanoTime();
        System.out.println(toInt2("11251234567", 8));
        System.out.println("Using product *= base: " + (System.nanoTime() - start));
        System.out.println(toTen(10021, 3));
        System.out.println(fromTen(88, 3));
        System.out.println(convertBases(7,10 , 2));
        System.out.println(convertBases(13,5,3));
        System.out.println(convertBases(43, 5, 4));
        System.out.println(convertBases2("1111111111111010", 2, 16));
        System.out.println(convertBases2("FFFA", 16, 2));
        System.out.println(convertBasesUnary(44, 5, 2));
        System.out.println(convertBasesUnary(555, 8, 4));
        start = System.nanoTime();
        System.out.println(Double.parseDouble("3.14195"));
        System.out.println(System.nanoTime() - start);
        start = System.nanoTime();
        System.out.println(toDouble("3.14195"));
        System.out.println(System.nanoTime() - start);
        System.out.println(power(1.414, 2));
    }

    public static double toDouble(String s) {
        int decimal = s.indexOf('.');
        if(decimal == -1)
          decimal = s.length();
        double sum = 0.0;
        double product = 1.0;
        for(int i = decimal - 1; i >= 0; i--) {
            sum += (s.charAt(i) - '0') * product;
            product *= 10;
        }
        product = 1.0;
        for(int i = decimal + 1; i < s.length(); i++) {
            product *= .1;
            sum += (s.charAt(i) - '0') * product;
        }
        return sum;
    }

    public static int toInt(String s) {
        int number = 0;
        int exp = 0;
        int i = s.length() - 1;
        while(i >= 0)
            number += (s.charAt(i--) - '0') * (int) Math.pow(10, exp++);
        return number;
    }

    public static int toInt(String s, int base) {
        int number = 0;
        int exp = 0;
        int i = s.length() - 1;
        while(i >= 0)
            number += (s.charAt(i--) - '0') * (int) Math.pow(base, exp++);
        return number;
    }

    public static int toInt2(String s, int base) {
        int number = 0;
        int product = 1;
        int i = s.length() - 1;
        while(i >= 0) {
            number += (s.charAt(i--) - '0') * product;
            product *= base;
        }
        return number;
    }


    public static int toTen(int n, int fromBase) {
        int number = 0;
        int exp = 0;
        while (n > 0) {
            number += (n % 10) * (int) Math.pow(fromBase, exp++);
            n /= 10;
        }
        return number;
    }

    public static int fromTen(int n, int toBase) {
        int number = 0;
        int exp = 0;
        while (n > 0) {
            number += (n % toBase) * (int) Math.pow(10, exp++);
            n /= toBase;
        }
        return number;
    }

    public static int convertBases(int n, int fromBase, int toBase) {
        int number = 0;
        int expFrom = 0;
        int sum = 0;
        while (n > 0) {
            int expTo = 0;
            number += (n % 10) * (int) Math.pow(fromBase, expFrom++);
            while(number > 0) {
                sum += (number % toBase) * (int) Math.pow(10, expTo++);
                number /= toBase;
            }
            n /= 10;
        }
        return sum;
    }

    public static int convertBases(String s, int fromBase, int toBase) {
        int number = 0;
        int expFrom = 0;
        int sum = 0;
        int i = s.length() - 1;
        while (i >= 0) {
            int expTo = 0;
            char c = s.charAt(i--);
            number += coefficient(c) * (int) (Math.pow(fromBase, expFrom++));
            while(number > 0) {
                sum += (number % toBase) * (int) Math.pow(10, expTo++) ;
                number /= toBase;
            }
        }
        return sum;
    }

    public static int coefficient(char c) {
        if(c - '0' > 9)
            return 10 + c - 'A';
        else
            return c - '0';
    }

    public static String value(int n) {
        if(n > 9)
            return (char)('A' + n - 10) + "";
        else
            return n + "";
    }

    public static String convertBases2(String s, int fromBase, int toBase) {
        Stack<Integer> sum = new Stack<>();
        int number = 0;
        int expFrom = 0;
        int expTo = 0;
        int i = s.length() - 1;
        while (i >= 0) {
            expTo = 0;
            char c = s.charAt(i--);
            number += coefficient(c) * (int) (Math.pow(fromBase, expFrom++));
            while(number > 0) {
                if(sum.size() < expTo + 1)
                    sum.add(0);
                sum.set(expTo, sum.get(expTo++) + number % toBase);
                number /= toBase;
            }
        }
        String out = "";
        while (expTo-- > 0)
            out += value(sum.pop());
        return out;
    }

    public static int convertBasesUnary(int n, int fromBase, int toBase) {
        int slotsFrom = 0;
        int slotsTo = 10;
        int out = 0;
        while( n / (int) Math.pow(10, ++slotsFrom) > 0);
        int [] from = new int [slotsFrom];
        int [] to = new int [slotsTo];
        while(--slotsFrom >= 0) {
            from [slotsFrom] = n % 10;
            n /= 10;
        }
        while(! isEmpty(from)) {
            minus( 1, from, fromBase);
            plus(1, to, toBase);
        }
        for(int a : to)
            out += (int) Math.pow(10, --slotsTo) * a;
        return out;
    }

    public static void minus(int n, int [] in, int base) {
        int index = in.length - 1;
        in [index] -= n;
        while(in [index] < 0) {
            in [index] += base;
            in [--index] -= 1;
        }
    }

    public static void plus(int n, int [] in, int base) {
        int index = in.length - 1;
        in [index] += n;
        while (in [index] >= base) {
            in [index] -= base;
            in [--index] += 1;
        }
    }

    public static boolean isEmpty(int [] in) {
        for(int n : in)
            if(n != 0)
                return false;
        return true;
    }


    public static double power(double number, int exp) {
        int temp = Math.abs(exp);
        double product = 1;
        while(temp-- >= 0)
            product *= number;
        if(exp < 0)
            return 1/product;
        return product;
    }

    public static double power2(double number, int exp) {
        double result = 1.;
        while(exp != 0) {
            if(exp < 0) {
                exp++;
                result /= number;
            }
            else if(exp > 0) {
                exp --;
                result *= number;
            }
        }
        return result;
    }

    public static double power3(double number, int exp) {
        if(exp < 0)
            return 1 / power3(number, -exp);
        double result = 1.;
        while(exp-- > 0)
            result *= number;
        return result;
    }

}
