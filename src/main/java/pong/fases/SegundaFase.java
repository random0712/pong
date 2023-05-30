package pong.fases;

import pong.objetos.Tela;
import pong.objetos.Bola;
import pong.objetos.Obstaculo;

public class SegundaFase {
    private Tela tela;
    private Obstaculo obstaculo;

    private Bola bola;

    public SegundaFase() {
        this.tela = Tela.getInstance();
        this.obstaculo = Obstaculo.getInstance();
        this.bola = Bola.getInstance();
    }

    public void configurarFase2() {
        bola.configurarColisaoBolaFase2();

        if (tela.getPontuacaoAtual() == tela.getPontosParaVencer()) {
            tela.setOpcao(6);
            tela.reset(true);
            tela.setPontuacaoAtual(0);
            tela.setVidas(5);
            tela.setFase2(true);
        }
    }


    public void iniciarFase2() {
        tela.setOpcao(5);
        tela.reset(false);
        tela.setFase2(false);
        bola.setDescerReto(true);
    }
}
