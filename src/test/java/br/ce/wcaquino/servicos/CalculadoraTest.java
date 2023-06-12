package br.ce.wcaquino.servicos;

import org.junit.Before;
import org.junit.Test;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;
import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class CalculadoraTest {

	private Calculadora calc;
	
	@Before
	public void setup() {
		calc = new Calculadora();
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void deveSomarDoisValores() {
		//cenario
		int a = 5;
		int b = 3;
		Calculadora calc = new Calculadora();
		
		//acao
		int resultado = calc.somar(a, b);
		
		//verificacao
		Assert.assertEquals(8, resultado);
	}
	
	@Test
	public void deveSubtrairDoisValores() {
		//cenario
		int a = 8;
		int b = 5;
		Calculadora calc = new Calculadora();
		
		//acao
		int resultado = calc.subtrair(a, b);
		
		//verificacao
		Assert.assertEquals(3, resultado);
	}
	
	@Test
	public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {
		//cenario
		int a = 4;
		int b = 8;
		Calculadora calc = new Calculadora();
		
		//acao
		int resultado = calc.dividir(a, b);
		
		//verificacao
		Assert.assertEquals(0, resultado);
	}
	
	@Test
	public void deveMultiplicarDoisValores() {
		//cenario
		int a = 5;
		int b = 6;
		Calculadora calc = new Calculadora();
		
		//acao
		int resultado = calc.multiplicar(a, b);
		
		//verificacao
		Assert.assertEquals(30, resultado);
	}	
	
	@Test(expected = NaoPodeDividirPorZeroException.class)
	public void deveLancarExcecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {
		
		int a = 10;
		int b = 0;
		Calculadora calc = new Calculadora();
		
		calc.dividir(a, b);
	}
}
