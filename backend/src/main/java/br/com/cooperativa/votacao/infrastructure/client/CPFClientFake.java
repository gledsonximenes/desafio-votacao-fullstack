package br.com.cooperativa.votacao.infrastructure.client;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CPFClientFake {
    private final Random random = new Random();

    public String validarCPF(String cpf) {
        if (!cpf.matches("\\d{11}")) {
            throw new IllegalArgumentException("CPF inválido. Deve conter 11 dígitos numéricos.");
        }

        // Simula aleatoriamente
        int result = random.nextInt(3);
        switch (result) {
            case 0 -> throw new CPFNotFoundException();
            case 1 -> throw new CPFUnableToVoteException();
            default -> {
                return "ABLE_TO_VOTE";
            }
        }
    }

    public static class CPFNotFoundException extends RuntimeException {}

    public static class CPFUnableToVoteException extends RuntimeException {}
}
