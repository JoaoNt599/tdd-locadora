package br.ce.wcaquino.servicos;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class LocacaoServiceTest {
	
	private LocacaoService service;
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@SuppressWarnings("deprecation")
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setup() {
		service = new LocacaoService();
	}
	
	@Test
	public void deveAlugarFilme() throws Exception {
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//cenario
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));
		
		//acao
		Locacao locacao;
		
		locacao = service.alugarFilme(usuario, filmes);
			
		//verificacao
		error.checkThat(locacao.getValor(),is(equalTo(5.0)));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
	}
	
	// forma elegante
	@Test(expected = FilmeSemEstoqueException.class)
	public void deveLancarExcecaoAOAlugarFilmeSemEstoque() throws Exception {
		//cenario
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 4.0));
				
		//acao
		@SuppressWarnings("unused")
		Locacao locacao;
				
		service.alugarFilme(usuario, filmes);				
	}
	
	// forma robusta
	@SuppressWarnings("deprecation")
	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
		// cenario
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 5.0));
		
		// acao
		try {
			service.alugarFilme(null, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuário vazio"));
		}
	}
	
	// forma nova
	@Test
	public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		Usuario usuario = new Usuario("Usuario 1");
		
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");
		
		// acao
		service.alugarFilme(usuario, null);
	}
	
	@Test
	public void devePagar75cNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(
				new Filme("Filme 1", 2, 4.0), 
				new Filme("Filme 2", 2, 4.0), 
				new Filme("Filme 3", 2, 4.0));
		
		//acao
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		//verificacao | ex: 4 + 4 + 3 = 11
		assertThat(resultado.getValor(), is(11.0));
	}
	
	@Test
	public void devePagar50cNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(
				new Filme("Filme 1", 2, 4.0), 
				new Filme("Filme 2", 2, 4.0), 
				new Filme("Filme 3", 2, 4.0),
				new Filme("Filme 4", 2, 4.0));
		
		//acao
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		//verificacao | ex: 4 + 4 + 3 + 2 = 13
		assertThat(resultado.getValor(), is(13.0));
	}
	
	@Test
	public void devePagar25cNoFilme5() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(
				new Filme("Filme 1", 2, 4.0), 
				new Filme("Filme 2", 2, 4.0), 
				new Filme("Filme 3", 2, 4.0),
				new Filme("Filme 4", 2, 4.0),
				new Filme("Filme 5", 2, 4.0));
		
		//acao
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		//verificacao | ex: 4 + 4 + 3 + 2 + 1 = 14
		assertThat(resultado.getValor(), is(14.0));
	}
	
	@Test
	public void devePagar0cNoFilme6() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(
				new Filme("Filme 1", 2, 4.0), 
				new Filme("Filme 2", 2, 4.0), 
				new Filme("Filme 3", 2, 4.0),
				new Filme("Filme 4", 2, 4.0),
				new Filme("Filme 5", 2, 4.0),
				new Filme("Filme 6", 2, 4.0));
		
		//acao
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		//verificacao | ex: 4 + 4 + 3 + 2 + 1 = 14
		assertThat(resultado.getValor(), is(14.0));
	}
	
	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//canario
		Usuario usario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 5.0));
		
		//acao
		Locacao retorno = service.alugarFilme(usario, filmes);
		
		//verificacao
		boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
		Assert.assertTrue(ehSegunda);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
