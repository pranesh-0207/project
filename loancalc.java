import java.util.Scanner;

public class loancalc {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        System.out.print("Enter Loan Amount: ");
        double principal = s.nextDouble();

        System.out.print("Enter Annual Interest Rate (in %): ");
        double InterestRate = s.nextDouble();

        System.out.print("Enter Loan Tenure (in years): ");
        int time = s.nextInt();

        double monthlyInterestRate = (InterestRate / 100) / 12;
        int tenureMonths = time * 12;

        double emi = (principal * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, tenureMonths)) 
                     / (Math.pow(1 + monthlyInterestRate, tenureMonths) - 1);

        System.out.printf("Your Monthly EMI is: %.2f\n", emi);

        
}
}