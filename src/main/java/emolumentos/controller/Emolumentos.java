package emolumentos.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Logger;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class Emolumentos {
	
	private static final double MIN_VALUE = 0.01;
	private static final double EMOLUMENTOS = 0.0050;
	private static final double LIQUIDACAO = 0.0275;
	private static final double TAXA_LIQUIDACAO = LIQUIDACAO / 100;
	private static final double TAXA_EMOLUMENTOS = EMOLUMENTOS / 100;
	private static final String TESTE_CSV_FILE = "C:\\Acoes\\Acoes.csv";
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.print("Data (DD/MM/YYYY): ");
		String date = scanner.nextLine();
		if (date.trim().equals("")) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			date = sdf.format(new Date(System.currentTimeMillis()));
		}
		
		System.out.print("Compra ou Venda? (C/V): ");
		String compraOuVenda = scanner.nextLine();
		
		System.out.print("Nome do Papel? ");
		String papel = scanner.nextLine();
		
		System.out.print("Quantidade de papéis? ");
		short qtdPapel = scanner.nextShort();
		
		System.out.print("Preço do Papel? ");
		BigDecimal papelValue = BigDecimal.valueOf(scanner.nextDouble());

		System.out.print("Taxa de Corretagem? ");
		BigDecimal taxaCorretagem = BigDecimal.valueOf(scanner.nextDouble());
		taxaCorretagem = taxaCorretagem.setScale(2, BigDecimal.ROUND_DOWN);
		
		// Limpa o scanner
		scanner.nextLine();
		
		System.out.print("Deseja gravar a operação num csv? (S/N): ");
		String useCSV = scanner.nextLine();
		
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
		
		//.withHeader("DATA", "PAPEL", "QTD", "VALOR", "TAXA LIQUID", "EMOLUMENTOS", "CORRETAGEM", "TOTAL BRUTO", "TOTAL LIQUIDO"));
		
		if (useCSV.equalsIgnoreCase("S")) {
			buildCSV(date, compraOuVenda, papel, qtdPapel, papelValue, taxaLiquidacao, emolumentos, taxaCorretagem, totalValuePapers, totalValue);
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
	
	private static void buildCSV(String date, String compraOuVenda, String papel, short qtdPapel, BigDecimal papelValue,
			BigDecimal taxaLiquidacao, BigDecimal emolumentosCSV, BigDecimal taxaCorretagem, BigDecimal totalValuePapers,
			BigDecimal totalValue) {
		try (FileWriter writer = new FileWriter(TESTE_CSV_FILE, true);
			CSVPrinter csvPrinter = new CSVPrinter(writer, getCSVFormat());)
		{
			csvPrinter.printRecord(date, compraOuVenda, papel, replaceComma(qtdPapel), replaceComma(papelValue),
					replaceComma(taxaLiquidacao), replaceComma(emolumentosCSV), replaceComma(taxaCorretagem), replaceComma(totalValuePapers),
					replaceComma(totalValue));

			csvPrinter.flush();
		} catch (IOException e) {
			Logger.getLogger("pau na manipulação do arquivo");
			e.printStackTrace();
		}
	}

	private static CSVFormat getCSVFormat() {
		File f = new File(TESTE_CSV_FILE);
		if (f.length() > 0) {
			return CSVFormat.DEFAULT;
		} else {
			return CSVFormat.DEFAULT.withHeader("DATA", "OPERACAO", "PAPEL",
					"QTD", "VALOR", "TAXA LIQUID", "EMOLUMENTOS", "CORRETAGEM", "TOTAL BRUTO", "TOTAL LIQUIDO");
		}
		
	}
	
	private static Object replaceComma(Object x) {
		return String.valueOf(x).replace(".", ",");
	}

}
