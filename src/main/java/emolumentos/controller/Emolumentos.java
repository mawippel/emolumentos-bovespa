package emolumentos.controller;

import java.math.BigDecimal;
import java.util.Scanner;

public class Emolumentos {
	
	private static final double MIN_VALUE = 0.01;
	private static final double EMOLUMENTOS = 0.0050;
	private static final double LIQUIDACAO = 0.0275;

	public static void main(String[] args) {
		final double TAXA_LIQUIDACAO = LIQUIDACAO / 100;
		final double TAXA_EMOLUMENTOS = EMOLUMENTOS / 100;
		
		// Lê os dados da tela
		Scanner scanner = new Scanner(System.in);

		System.out.print("Compra ou Venda? (C/V): ");
		String compraOuVenda = scanner.nextLine();
		
		System.out.print("Quantidade de papéis? ");
		short qtdPapel = scanner.nextShort();
		
		System.out.print("Preço do Papel? ");
		BigDecimal papelValue = BigDecimal.valueOf(scanner.nextDouble());

		System.out.print("Taxa de Corretagem? ");
		BigDecimal taxaCorretagem = BigDecimal.valueOf(scanner.nextDouble());
		taxaCorretagem = taxaCorretagem.setScale(2, BigDecimal.ROUND_DOWN);
		
		System.out.print("Número de operações? ");
		BigDecimal numeroOperacoes = BigDecimal.valueOf(scanner.nextDouble());
		numeroOperacoes = numeroOperacoes.setScale(2, BigDecimal.ROUND_DOWN);
		taxaCorretagem = taxaCorretagem.multiply(numeroOperacoes);
		
		BigDecimal totalValuePapers = papelValue.multiply(BigDecimal.valueOf(qtdPapel));
		
		BigDecimal taxaLiquidacao = totalValuePapers.multiply(BigDecimal.valueOf(TAXA_LIQUIDACAO));
		taxaLiquidacao = taxaLiquidacao.setScale(2, BigDecimal.ROUND_DOWN);
		if (taxaLiquidacao.doubleValue() < 0.01) {
			taxaLiquidacao = BigDecimal.valueOf(0.01); //Taxa mínima cobrada = R$0,01.
		}
		
		BigDecimal emolumentos = totalValuePapers.multiply(BigDecimal.valueOf(TAXA_EMOLUMENTOS));
		emolumentos = emolumentos.setScale(2, BigDecimal.ROUND_DOWN);
		if (emolumentos.doubleValue() < MIN_VALUE) {
			emolumentos = BigDecimal.valueOf(MIN_VALUE); //Taxa mínima cobrada = R$0,01.
		}
		
		BigDecimal totalValue = BigDecimal.valueOf(0);
		totalValue = totalValue.setScale(2, BigDecimal.ROUND_DOWN);
		if (compraOuVenda.equalsIgnoreCase("C")) { // Se for compra adiciona as taxas
			totalValue = totalValue.add(totalValuePapers).add(emolumentos).add(taxaLiquidacao).add(taxaCorretagem);
		} else if(compraOuVenda.equalsIgnoreCase("V")) { // Se for venda subtrai as taxas
			totalValue = totalValue.add(totalValuePapers).subtract(emolumentos).subtract(taxaLiquidacao).subtract(taxaCorretagem);
		}
		
		System.out.println("Taxa de Liquidação: R$ " + taxaLiquidacao);
		System.out.println("Emolumentos: R$ " + emolumentos);
		System.out.println("O valor total da compra será de: R$ " + totalValue);
		
		if (compraOuVenda.equalsIgnoreCase("V")) {
			scanner.close();
			return; // encerra se for uma venda
		}
		
		System.out.println("============PROJEÇÃO DE VENDA E LUCRO===============");
		
		System.out.print("Qual será o valor de venda? R$ ");
		BigDecimal valorVenda = BigDecimal.valueOf(scanner.nextDouble());
		
		BigDecimal totalValuePapersVenda = valorVenda.multiply(BigDecimal.valueOf(qtdPapel));
		
		System.out.print("Taxa de corretagem para a venda: R$ ");
		taxaCorretagem = BigDecimal.valueOf(scanner.nextDouble());
		taxaCorretagem = taxaCorretagem.setScale(2, BigDecimal.ROUND_DOWN);
		
		// Cálculos de taxas
		taxaLiquidacao = totalValuePapersVenda.multiply(BigDecimal.valueOf(TAXA_LIQUIDACAO));
		taxaLiquidacao = taxaLiquidacao.setScale(2, BigDecimal.ROUND_DOWN);
		if (taxaLiquidacao.doubleValue() < 0.01) {
			taxaLiquidacao = BigDecimal.valueOf(0.01); //Taxa mínima cobrada = R$0,01.
		}
		
		emolumentos = totalValuePapersVenda.multiply(BigDecimal.valueOf(TAXA_EMOLUMENTOS));
		emolumentos = emolumentos.setScale(2, BigDecimal.ROUND_DOWN);
		if (emolumentos.doubleValue() < MIN_VALUE) {
			emolumentos = BigDecimal.valueOf(MIN_VALUE); //Taxa mínima cobrada = R$0,01.
		}
		
		totalValuePapersVenda = totalValuePapersVenda.subtract(emolumentos)
				.subtract(taxaLiquidacao).subtract(taxaCorretagem);
		
		System.out.print("O seu lucro líquido será de R$ " + totalValuePapersVenda.subtract(totalValue));
		
		scanner.close();
	}

}
