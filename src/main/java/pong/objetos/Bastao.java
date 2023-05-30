package pong.objetos;

import com.jogamp.opengl.GL2;

public class Bastao {

    private float bastaoX1 = -0.2f;
    private float bastaoX2 = 0.2f;
    private float bastaoY1 = -0.8f;
    private float bastaoY2 = -0.9f;

    private float bastaoPontaEsquerda = bastaoX1;
    private float bastaoPontaDireita = bastaoX2;
    private float moveBastaoX = 0;

    private float bastaoCentro = 0;
    private static Bastao instance = null;

    public Bastao() {
    }

    public static Bastao getInstance() {
        if(instance == null) {
            instance = new Bastao();
        }

        return instance;
    }

    public float getBastaoX1() {
        return bastaoX1;
    }

    public void setBastaoX1(float bastaoX1) {
        this.bastaoX1 = bastaoX1;
    }

    public float getBastaoX2() {
        return bastaoX2;
    }

    public void setBastaoX2(float bastaoX2) {
        this.bastaoX2 = bastaoX2;
    }

    public float getBastaoY1() {
        return bastaoY1;
    }

    public void setBastaoY1(float bastaoY1) {
        this.bastaoY1 = bastaoY1;
    }

    public float getBastaoY2() {
        return bastaoY2;
    }

    public void setBastaoY2(float bastaoY2) {
        this.bastaoY2 = bastaoY2;
    }

    public float getBastaoPontaEsquerda() {
        return bastaoPontaEsquerda;
    }

    public void setBastaoPontaEsquerda(float bastaoPontaEsquerda) {
        this.bastaoPontaEsquerda = bastaoPontaEsquerda;
    }

    public float getBastaoPontaDireita() {
        return bastaoPontaDireita;
    }

    public void setBastaoPontaDireita(float bastaoPontaDireita) {
        this.bastaoPontaDireita = bastaoPontaDireita;
    }

    public float getMoveBastaoX() {
        return moveBastaoX;
    }

    public void setMoveBastaoX(float moveBastaoX) {
        this.moveBastaoX = moveBastaoX;
    }

    public float getBastaoCentro() {
        return bastaoCentro;
    }

    public void setBastaoCentro(float bastaoCentro) {
        this.bastaoCentro = bastaoCentro;
    }

    public void desenhaBastao(GL2 gl) {
        gl.glPushMatrix();
        gl.glColor3f(0.8f, 0.8f, 0.8f);
        gl.glTranslatef(this.getMoveBastaoX(), 0, 0);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex2d(this.getBastaoX1(), this.getBastaoY1());
        gl.glVertex2d(this.getBastaoX2(), this.getBastaoY1());
        gl.glVertex2d(this.getBastaoX2(), this.getBastaoY2());
        gl.glVertex2d(this.getBastaoX1(), this.getBastaoY2());
        gl.glEnd();
        gl.glPopMatrix();
    }
}
