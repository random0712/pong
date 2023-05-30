package pong.objetos;

import com.jogamp.opengl.GL2;

public class Bola {

    private float cX = 0.0f;
    private float cY = 0.0f;
    private float rX = 0.05f;
    private float rY = 0.075f;
    private float posicaoBolaX = cX + rX;
    private float posicaoBolaY = cY + rY;

    private boolean subirReto = false;
    private boolean descerReto = false;
    private boolean subirDireita = false;
    private boolean subirEsquerda = false;
    private boolean descerDireita = false;
    private boolean descerEsquerda = false;

    private float moveY = 0;
    private float moveX = 0;
    private float velocidade = 0.012f;
    private static Bola instance;

    public Bola() {}

    public static Bola getInstance() {
        if(instance == null) {
            instance = new Bola();
        }

        return instance;
    }

    public float getMoveY() {
        return moveY;
    }

    public void setMoveY(float moveY) {
        this.moveY = moveY;
    }

    public float getMoveX() {
        return moveX;
    }

    public void setMoveX(float moveX) {
        this.moveX = moveX;
    }

    public float getVelocidade() {
        return velocidade;
    }

    public void setVelocidade(float velocidade) {
        this.velocidade = velocidade;
    }

    public float getcX() {
        return cX;
    }

    public void setcX(float cX) {
        this.cX = cX;
    }

    public float getcY() {
        return cY;
    }

    public void setcY(float cY) {
        this.cY = cY;
    }

    public float getrX() {
        return rX;
    }

    public void setrX(float rX) {
        this.rX = rX;
    }

    public float getrY() {
        return rY;
    }

    public void setrY(float rY) {
        this.rY = rY;
    }

    public float getPosicaoBolaX() {
        return posicaoBolaX;
    }

    public void setPosicaoBolaX(float posicaoBolaX) {
        this.posicaoBolaX = posicaoBolaX;
    }

    public float getPosicaoBolaY() {
        return posicaoBolaY;
    }

    public void setPosicaoBolaY(float posicaoBolaY) {
        this.posicaoBolaY = posicaoBolaY;
    }

    public boolean isSubirReto() {
        return subirReto;
    }

    public void setSubirReto(boolean subirReto) {
        this.subirReto = subirReto;
    }

    public boolean isDescerReto() {
        return descerReto;
    }

    public void setDescerReto(boolean descerReto) {
        this.descerReto = descerReto;
    }

    public boolean isSubirDireita() {
        return subirDireita;
    }

    public void setSubirDireita(boolean subirDireita) {
        this.subirDireita = subirDireita;
    }

    public boolean isSubirEsquerda() {
        return subirEsquerda;
    }

    public void setSubirEsquerda(boolean subirEsquerda) {
        this.subirEsquerda = subirEsquerda;
    }

    public boolean isDescerDireita() {
        return descerDireita;
    }

    public void setDescerDireita(boolean descerDireita) {
        this.descerDireita = descerDireita;
    }

    public boolean isDescerEsquerda() {
        return descerEsquerda;
    }

    public void setDescerEsquerda(boolean descerEsquerda) {
        this.descerEsquerda = descerEsquerda;
    }

    public void configuraColisaoBola() {
        Bastao bastao = Bastao.getInstance();
        Tela tela = Tela.getInstance();

        this.setDescerEsquerda(false);
        this.setDescerDireita(false);
        this.setDescerReto(false);

        tela.setPontuacaoAtual(tela.getPontuacaoAtual() + tela.getPontosPorBatida());

        bastao.setBastaoCentro((bastao.getBastaoPontaEsquerda() + bastao.getBastaoPontaDireita()) / 2);

        if (this.getPosicaoBolaX() == -this.getrX() && bastao.getBastaoCentro() == tela.getCentroTela() || this.getPosicaoBolaX() == bastao.getBastaoCentro()) {
            this.setSubirReto(true);
        }

        if (this.getPosicaoBolaX() > bastao.getBastaoCentro()) {
            if (bastao.getBastaoCentro() < tela.getCentroTela() && bastao.getBastaoCentro() >= tela.getMeioCentroTelaEsquerdo()) {
                this.setSubirDireita(true);
            }

            if (bastao.getBastaoCentro() < tela.getCentroTela() && bastao.getBastaoCentro() < tela.getMeioCentroTelaEsquerdo()) {
                this.setSubirEsquerda(true);
            }

            if (bastao.getBastaoCentro() > tela.getCentroTela() && bastao.getBastaoCentro() <= tela.getMeioCentroTelaDireito()) {
                this.setSubirDireita(true);
            }

            if (bastao.getBastaoCentro() > tela.getCentroTela() && bastao.getBastaoCentro() > tela.getMeioCentroTelaDireito()) {
                this.setSubirEsquerda(true);
            }
        }

        if (this.getPosicaoBolaX() < bastao.getBastaoCentro()) {
            if (bastao.getBastaoCentro() < tela.getCentroTela() && bastao.getBastaoCentro() > tela.getMeioCentroTelaEsquerdo()) {
                this.setSubirEsquerda(true);
            }

            if (bastao.getBastaoCentro() < tela.getCentroTela() && bastao.getBastaoCentro() <= tela.getMeioCentroTelaEsquerdo()) {
                this.setSubirDireita(true);
            }

            if (bastao.getBastaoCentro() > tela.getCentroTela() && bastao.getBastaoCentro() <= tela.getMeioCentroTelaDireito()) {
                this.setSubirEsquerda(true);
            }

            if (bastao.getBastaoCentro() > tela.getCentroTela() && bastao.getBastaoCentro() > tela.getMeioCentroTelaDireito()) {
                this.setSubirDireita(true);
            }
        }
    }

    public void configurarColisaoBolaFase2() {
        Obstaculo obstaculo = Obstaculo.getInstance();

        this.setVelocidade(0.015f);

        float BASTAO_2_CENTRO = (obstaculo.getObstaculoPontaEsquerda() + obstaculo.getObstaculoPontaDireita()) / 2;
        float BOLA_X = (this.getPosicaoBolaX() + this.getrX());
        float BASTAO_Y2 = (obstaculo.getObstaculoY2() - 0.2f);

        if (this.isSubirDireita() || this.isSubirEsquerda() || this.isSubirReto()) {

            if (this.getPosicaoBolaY() >= BASTAO_Y2 && this.getPosicaoBolaY() < obstaculo.getObstaculoY1()
                    && BOLA_X >= obstaculo.getObstaculoPontaEsquerda()
                    && BOLA_X <= obstaculo.getObstaculoPontaDireita()) {


                if (this.getPosicaoBolaX() == -this.getrX() || this.getPosicaoBolaX() == BASTAO_2_CENTRO && this.isSubirReto()) {
                    this.setDescerReto(true);
                    this.setSubirEsquerda(false);
                    this.setSubirDireita(false);
                    this.setSubirReto(false);
                }

                if (this.isSubirEsquerda()) {
                    this.setDescerEsquerda(true);
                    this.setSubirEsquerda(false);
                    this.setSubirDireita(false);
                    this.setSubirReto(false);
                }
                if (this.isSubirDireita()) {
                    this.setDescerDireita(true);
                    this.setSubirEsquerda(false);
                    this.setSubirDireita(false);
                    this.setSubirReto(false);
                }
            }
        }

        if (this.isDescerDireita() || this.isDescerEsquerda()) {

            obstaculo.setObstaculoPontaEsquerda(obstaculo.getObstaculoX1());
            obstaculo.setObstaculoPontaDireita(obstaculo.getObstaculoX2());

            if (this.getPosicaoBolaY() <= obstaculo.getObstaculoY1() && this.getPosicaoBolaY() > obstaculo.getObstaculoY2()
                    && BOLA_X >= obstaculo.getObstaculoPontaEsquerda()
                    && BOLA_X <= obstaculo.getObstaculoPontaDireita()) {


                if (this.isDescerDireita()) {
                    this.setSubirDireita(true);
                    this.setDescerReto(false);
                    this.setDescerEsquerda(false);
                    this.setDescerDireita(false);
                }
                if (this.isDescerEsquerda()) {
                    this.setSubirEsquerda(true);
                    this.setDescerReto(false);
                    this.setDescerEsquerda(false);
                    this.setDescerDireita(false);
                }
            }
        }
    }

    public void definirDirecaoBola(GL2 gl) {
        if (this.isSubirEsquerda()) {
            this.subirEsquerda(gl);
        }

        if (this.isSubirDireita()) {
            this.subirDireita(gl);
        }

        if (this.isDescerEsquerda()) {
            this.descerEsquerda(gl);
        }

        if (this.isDescerDireita()) {
            this.descerDireita(gl);
        }

        if (this.isSubirReto()) {
            this.subirReto(gl);
        }

        if (this.isDescerReto()) {
            this.descerReto(gl);
        }
    }

    public void desenhaBola(GL2 gl) {
        gerarIluminacao(gl);
        gl.glBegin(GL2.GL_POLYGON);
        gl.glColor3f(1, 1, 0);

        double lim = 2 * Math.PI;
        for (float i = 0; i < lim; i += 0.01) {
            float normalX = (float) Math.cos(i); // Normal x (mesmo que o raio)
            float normalY = (float) Math.sin(i); // Normal y (mesmo que o raio)

            gl.glNormal3f(normalX, normalY, 0.0f); // Especifica a normal do vértice
            gl.glVertex2d(this.getcX() + this.getrX() * Math.cos(i), this.getcY() + this.getrY() * Math.sin(i));
        }

        gl.glEnd();
        gl.glPopMatrix();
    }

    private void subirDireita(GL2 gl) {
        Tela tela = Tela.getInstance();

        this.setMoveY(this.getMoveY() + this.getVelocidade());
        this.setMoveX(this.getMoveX() + this.getVelocidade());
        gl.glTranslatef(this.getMoveX(), this.getMoveY(), 0);
        if (this.getPosicaoBolaX() >= tela.getLimiteDireita()) {
            this.setSubirEsquerda(true);
            this.setSubirDireita(false);
            this.setSubirReto(false);
            this.setDescerEsquerda(false);
            this.setDescerDireita(false);
            this.setDescerReto(false);
        }

        if (this.getPosicaoBolaY() >= tela.getLimiteSuperior()) {
            this.setDescerDireita(true);
            this.setSubirReto(false);
            this.setSubirDireita(false);
            this.setSubirEsquerda(false);
            this.setDescerEsquerda(false);
            this.setDescerReto(false);
        }
    }

    private void subirEsquerda(GL2 gl) {
        Tela tela = Tela.getInstance();

        this.setMoveX(this.getMoveX() - this.getVelocidade());
        this.setMoveY(this.getMoveY() + this.getVelocidade());
        gl.glTranslatef(this.getMoveX(), this.getMoveY(), 0);
        if (this.getPosicaoBolaX() <= tela.getLimiteEsquerda()) {
            this.setSubirDireita(true);
            this.setSubirEsquerda(false);
            this.setSubirReto(false);
            this.setDescerReto(false);
            this.setDescerDireita(false);
            this.setDescerEsquerda(false);
        }

        if (this.getPosicaoBolaY() >= tela.getLimiteSuperior()) {
            this.setDescerEsquerda(true);
            this.setSubirEsquerda(false);
            this.setSubirDireita(false);
            this.setSubirReto(false);
            this.setDescerDireita(false);
            this.setDescerReto(false);
        }
    }

    private void descerEsquerda(GL2 gl) {
        Tela tela = Tela.getInstance();

        this.setMoveX(this.getMoveX() - this.getVelocidade());
        this.setMoveY(this.getMoveY() - this.getVelocidade());
        gl.glTranslatef(this.getMoveX(), this.getMoveY(), 0);
        if (this.getPosicaoBolaX() <= tela.getLimiteEsquerda()) {
            this.setDescerDireita(true);
            this.setDescerEsquerda(false);
            this.setDescerReto(false);
            this.setSubirReto(false);
            this.setSubirDireita(false);
            this.setSubirEsquerda(false);
        }
    }

    private void descerDireita(GL2 gl) {
        Tela tela = Tela.getInstance();

        this.setMoveX(this.getMoveX() + this.getVelocidade());
        this.setMoveY(this.getMoveY() - this.getVelocidade());
        gl.glTranslatef(this.getMoveX(), this.getMoveY(), 0);
        if (this.getPosicaoBolaX() >= tela.getLimiteDireita()) {
            this.setDescerEsquerda(true);
            this.setDescerDireita(false);
            this.setDescerReto(false);
            this.setSubirReto(false);
            this.setSubirDireita(false);
            this.setSubirEsquerda(false);
        }
    }

    private void subirReto(GL2 gl) {
        Tela tela = Tela.getInstance();

        this.setMoveY(this.getMoveY() + this.getVelocidade());
        gl.glTranslatef(0, this.getMoveY(), 0);
        if (this.getPosicaoBolaY() >= tela.getLimiteSuperior()) {
            this.setDescerReto(true);
            this.setSubirReto(false);
            this.setSubirDireita(false);
            this.setSubirEsquerda(false);
            this.setDescerDireita(false);
            this.setDescerEsquerda(false);
        }
    }

    private void descerReto(GL2 gl) {
        this.setMoveY(this.getMoveY() - this.getVelocidade());
        gl.glTranslatef(0, this.getMoveY(), 0);
    }

    private static void gerarIluminacao(GL2 gl) {
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_COLOR_MATERIAL);

//                Defina a posição da fonte de luz:
        float[] lightPosition = {1, 1, 0.0f, 1.0f}; // Posição da luz no centro da bola (z = 0)
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPosition, 0);

//                 Defina as propriedades do material para a bola:


        float[] ambient = {0.2f, 0.2f, 0.2f, 1.0f};
        float[] diffuse = {1.0f, 0.0f, 0.0f, 1.0f};
        float[] specular = {1.0f, 1.0f, 1.0f, 1.0f};
        float shininess = 10.0f;

        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, ambient, 0);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, diffuse, 0);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, specular, 0);
        gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, shininess);
    }
}
