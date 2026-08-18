package ru.bank.account_service.infrastructure.util;
import java.util.Random;

public class AccountNumberGenerated {

    private static final Random RANDOM = new Random();

    public static String generatedAccountNumber(){
        StringBuilder sb = new StringBuilder();
        sb.append(RANDOM.nextInt(9) + 1);
        for (int i = 1; i < 20; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }

}
