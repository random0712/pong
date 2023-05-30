package pong.objetos;

import com.jogamp.opengl.GL2;
import pong.enums.MovimentoObstaculo;

public class Obstaculo {

    private float obstaculoX1 = -0.2f;
    private float obstaculoX2 = 0.2f;
    private final float obstaculoY1 = 1f;
    private final float obstaculoY2 = 0.9f;

    private float obstaculoPontaEsquerda = obstaculoX1;
    private float obstaculoPontaDireita = obstaculoX2;

    private MovimentoObstaculo movimentoObstaculo = MovimentoObstaculo.DIREITA;


    private static Obstaculo instance = null;



    public Obstaculo() {
    }

    public static Obstaculo getInstance() {
        if(instance == null) {
            instance = new Obstaculo();
        }

        return instance;
    }

    public float getObstaculoX1() {
        return obstaculoX1;
    }

    public void setObstaculoX1(float obstaculoX1) {
        this.obstaculoX1 = obstaculoX1;
    }

    public float getObstaculoX2() {
        return obstaculoX2;
    }

    public void setObstaculoX2(float obstaculoX2) {
        this.obstaculoX2 = obstaculoX2;
    }

    public float getObstaculoY1() {
        return obstaculoY1;
    }

    public float getObstaculoY2() {
        return obstaculoY2;
    }

    public float getObstaculoPontaEsquerda() {
        return obstaculoPontaEsquerda;
    }

    public void setObstaculoPontaEsquerda(float obstaculoPontaEsquerda) {
        this.obstaculoPontaEsquerda = obstaculoPontaEsquerda;
    }

    public float getObstaculoPontaDireita() {
        return obstaculoPontaDireita;
    }

    public void setObstaculoPontaDireita(float obstaculoPontaDireita) {
        this.obstaculoPontaDireita = obstaculoPontaDireita;
    }

    public MovimentoObstaculo getMovimentoObstaculo() {
        return movimentoObstaculo;
    }

    public void setMovimentoObstaculo(MovimentoObstaculo movimentoObstaculo) {
        this.movimentoObstaculo = movimentoObstaculo;
    }

    public void desenhaObstaculo(GL2 gl) {
        Tela tela = Tela.getInstance();

        if(this.getObstaculoX2() >= tela.getLimiteDireita()) {
            this.setMovimentoObstaculo(MovimentoObstaculo.ESQUERDA);
        }

        if(this.getObstaculoX1() <= tela.getLimiteEsquerda()) {
            this.setMovimentoObstaculo(MovimentoObstaculo.DIREITA);
        }

        float deslocamentoX = this.getMovimentoObstaculo() == MovimentoObstaculo.DIREITA
                ? 0.01f
                : -0.01f;

        this.setObstaculoX1(this.getObstaculoX1() + deslocamentoX);
        this.setObstaculoX2(this.getObstaculoX2() + deslocamentoX);

        this.setObstaculoPontaEsquerda(this.getObstaculoX1());
        this.setObstaculoPontaDireita(this.getObstaculoX2());

        gl.glPushMatrix();
        gl.glColor3f(1, 1, 1);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(this.getObstaculoX1(), this.getObstaculoY2());
        gl.glVertex2d(this.getObstaculoX2(), this.getObstaculoY2());
        gl.glVertex2d(this.getObstaculoX2(), this.getObstaculoY1());
        gl.glVertex2d(this.getObstaculoX1(), this.getObstaculoY1());
        gl.glEnd();
        gl.glPopMatrix();
    }
}
