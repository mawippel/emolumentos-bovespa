package emolumentos.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Emolumentos {
	
	public static void main(String[] args) {
		final double TAXA_LIQUIDACAO = 0.0275 / 100;
		final double TAXA_EMOLUMENTOS = 0.0050 / 100;
		final BigDecimal TAXA_CORRETAGEM = BigDecimal.valueOf(1.49);
		
		String compraOuVenda = "C";
		short qtdPapel = 30;
		BigDecimal papelValue = BigDecimal.valueOf(9.27);
		BigDecimal totalValuePapers = papelValue.multiply(BigDecimal.valueOf(qtdPapel));
		
		BigDecimal taxaLiquidacao = totalValuePapers.multiply(BigDecimal.valueOf(TAXA_LIQUIDACAO));
		taxaLiquidacao = taxaLiquidacao.setScale(2, BigDecimal.ROUND_DOWN);
		if (taxaLiquidacao.doubleValue() < 0.01) {
			taxaLiquidacao = BigDecimal.valueOf(0.01); //Taxa mínima cobrada = R$0,01.
		}
		
		BigDecimal emolumentos = totalValuePapers.multiply(BigDecimal.valueOf(TAXA_EMOLUMENTOS));
		emolumentos = emolumentos.setScale(2, BigDecimal.ROUND_DOWN);
		if (emolumentos.doubleValue() < 0.01) {
			emolumentos = BigDecimal.valueOf(0.01); //Taxa mínima cobrada = R$0,01.
		}
		
		BigDecimal totalValue = BigDecimal.valueOf(0);
		if (compraOuVenda.equals("C")) { // Se for compra adiciona as taxas
			totalValue = totalValue.add(totalValuePapers).add(emolumentos).add(taxaLiquidacao).add(TAXA_CORRETAGEM);
		} else if(compraOuVenda.equals("V")) { // Se for venda subtrai as taxas
			totalValue = totalValue.add(totalValuePapers).subtract(emolumentos).subtract(taxaLiquidacao).subtract(TAXA_CORRETAGEM);
		}
		
		System.out.println(totalValue);
		
	}

}
