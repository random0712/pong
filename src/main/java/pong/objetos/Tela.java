package pong.objetos;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import pong.enums.MovimentoObstaculo;
import pong.menu.Menu;

public class Tela {
    private Menu menu;
    private int opcao = 0;
    private boolean inicio = true;
    private boolean pause = true;

    public int tonalizacao = GL2.GL_SMOOTH;

    private int pontuacaoAtual = 0;
    public int pontuacaoFase2 = 200;
    public int pontosPorBatida = 25;
    public int pontosParaVencer = 400;
    private boolean fase2 = true;

    private float limiteSuperior = 0.85f;
    private float limiteDireita = 0.87f;
    private float limiteEsquerda = -1;

    private float centroTela = 0.0f;
    private float meioCentroTelaEsquerdo = -0.5f;
    private float meioCentroTelaDireito = 0.5f;

    private int vidas = 5;

    private static Tela instance = null;

    public Tela() {
    }

    public static Tela getInstance() {
        if(instance == null) {
            instance = new Tela();
        }

        return instance;
    }

    private Texture backgroundTexture;

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public int getOpcao() {
        return opcao;
    }

    public void setOpcao(int opcao) {
        this.opcao = opcao;
    }

    public boolean isInicio() {
        return inicio;
    }

    public void setInicio(boolean inicio) {
        this.inicio = inicio;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public int getTonalizacao() {
        return tonalizacao;
    }

    public void setTonalizacao(int tonalizacao) {
        this.tonalizacao = tonalizacao;
    }

    public int getPontuacaoAtual() {
        return pontuacaoAtual;
    }

    public void setPontuacaoAtual(int pontuacaoAtual) {
        this.pontuacaoAtual = pontuacaoAtual;
    }

    public int getPontuacaoFase2() {
        return pontuacaoFase2;
    }

    public void setPontuacaoFase2(int pontuacaoFase2) {
        this.pontuacaoFase2 = pontuacaoFase2;
    }

    public int getPontosPorBatida() {
        return pontosPorBatida;
    }

    public void setPontosPorBatida(int pontosPorBatida) {
        this.pontosPorBatida = pontosPorBatida;
    }

    public int getPontosParaVencer() {
        return pontosParaVencer;
    }

    public void setPontosParaVencer(int pontosParaVencer) {
        this.pontosParaVencer = pontosParaVencer;
    }

    public boolean isFase2() {
        return fase2;
    }

    public void setFase2(boolean fase2) {
        this.fase2 = fase2;
    }



    public float getLimiteSuperior() {
        return limiteSuperior;
    }

    public void setLimiteSuperior(float limiteSuperior) {
        this.limiteSuperior = limiteSuperior;
    }

    public float getLimiteDireita() {
        return limiteDireita;
    }

    public void setLimiteDireita(float limiteDireita) {
        this.limiteDireita = limiteDireita;
    }

    public float getLimiteEsquerda() {
        return limiteEsquerda;
    }

    public void setLimiteEsquerda(float limiteEsquerda) {
        this.limiteEsquerda = limiteEsquerda;
    }


    public float getCentroTela() {
        return centroTela;
    }

    public void setCentroTela(float centroTela) {
        this.centroTela = centroTela;
    }

    public float getMeioCentroTelaEsquerdo() {
        return meioCentroTelaEsquerdo;
    }

    public void setMeioCentroTelaEsquerdo(float meioCentroTelaEsquerdo) {
        this.meioCentroTelaEsquerdo = meioCentroTelaEsquerdo;
    }

    public float getMeioCentroTelaDireito() {
        return meioCentroTelaDireito;
    }

    public void setMeioCentroTelaDireito(float meioCentroTelaDireito) {
        this.meioCentroTelaDireito = meioCentroTelaDireito;
    }

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }

    public Texture getBackgroundTexture() {
        return backgroundTexture;
    }

    public void setBackgroundTexture(Texture backgroundTexture) {
        this.backgroundTexture = backgroundTexture;
    }

    public void reset(boolean inicio) {

        Bastao bastao = Bastao.getInstance();
        Bola bola = Bola.getInstance();

        this.setInicio(inicio);

        this.setPause(false);

        bola.setMoveY(0);
        bola.setMoveX(0);
        bola.setVelocidade(0.01f);

        this.setLimiteSuperior(0.85f);
        this.setLimiteDireita(0.87f);
        this.setLimiteEsquerda(-1);

        bastao.setBastaoX1(-0.2f);
        bastao.setBastaoX2(0.2f);
        bastao.setBastaoY1(-0.8f);
        bastao.setBastaoY2(-0.9f);

        this.setCentroTela(0);
        this.setMeioCentroTelaEsquerdo(-0.5f);
        this.setMeioCentroTelaDireito(0.5f);

        bola.setcX(0.0f);
        bola.setcY(0.0f);
        bola.setrX(0.05f);
        bola.setrY(0.075f);
        bola.setPosicaoBolaX(bola.getcX() + bola.getrX());
        bola.setPosicaoBolaY(bola.getcY() + bola.getrY());

        bastao.setBastaoPontaEsquerda(bastao.getBastaoX1());
        bastao.setBastaoPontaDireita(bastao.getBastaoX2());
        bastao.setMoveBastaoX(0);

        bastao.setBastaoCentro(0);

        bola.setSubirReto(false);
        bola.setDescerReto(false);
        bola.setSubirDireita(false);
        bola.setSubirEsquerda(false);
        bola.setDescerDireita(false);
        bola.setDescerEsquerda(false);
    }

    public void configuraTela(GL2 gl) {
        // define a cor da janela (R, G, B, alpha)
        gl.glColor3f(1.0f,1.0f,1.0f);


//
        // limpa a janela com a cor especificada
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(-1, 1, -1, 1, -1, 1);

        gl.glMatrixMode(GL2.GL_MODELVIEW);

        // Desenhar um quadrado preenchendo toda a janela
        gl.glEnable(GL2.GL_TEXTURE_2D);
        this.getBackgroundTexture().bind(gl);
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2f(-1, -1);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2f(1, -1);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2f(1, 1);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2f(-1, 1);
        gl.glEnd();
        gl.glDisable(GL2.GL_TEXTURE_2D);
    }
}
