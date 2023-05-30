package pong.cena;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.TextureIO;
import pong.objetos.*;
import pong.fases.SegundaFase;
import pong.menu.Menu;

import java.io.File;
import java.io.IOException;

public class Cena implements GLEventListener, KeyListener {

    private Tela tela;
    private Bastao bastao;

    private Obstaculo obstaculo;

    private Bola bola;

    private Vida vida;

    public void init(GLAutoDrawable drawable) {
        tela = Tela.getInstance();
        bastao = Bastao.getInstance();
        obstaculo = Obstaculo.getInstance();
        bola = Bola.getInstance();
        vida = Vida.getInstance();

        GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(GL2.GL_DEPTH_TEST);

        //Habilita as cores do objeto 3D
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);

        gl.glEnable(GL2.GL_DEPTH_TEST); // Habilita o teste de profundidade
        gl.glDepthFunc(GL2.GL_LEQUAL); // Define a função de teste de profundidade

        try {
            File imageFile = new File("src/imagens/tennis.jpg"); // Caminho da imagem de fundo
            tela.setBackgroundTexture(TextureIO.newTexture(imageFile, true));
        } catch (IOException e) {
            e.printStackTrace();
        }

        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
    }

    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        GLUT glut = new GLUT();


        tela.configuraTela(gl);

        switch (tela.getOpcao()) {
            case 0:
               tela.setMenu(new Menu());
               tela.getMenu().start(gl);
               break;
            case 2:
                tela.setMenu(new Menu());

                gl.glPushMatrix();
                this.gameOver();


                if (tela.isInicio()) {
                    this.inicio(gl);
                }

                bola.setPosicaoBolaY(-(bola.getcY() + bola.getrY()) + bola.getMoveY());
                bola.setPosicaoBolaX(-(bola.getcX() + bola.getrX()) + bola.getMoveX());



                if (tela.getPontuacaoAtual() == tela.getPontuacaoFase2() && tela.isFase2()) {
                    SegundaFase segundaFase = new SegundaFase();
                    segundaFase.iniciarFase2();
                }

                if (tela.getPontuacaoAtual() >= tela.getPontuacaoFase2()) {
                    tela.getMenu().jogo("Segunda Fase");
                    SegundaFase segundaFase = new SegundaFase();
                    segundaFase.configurarFase2();
                } else {
                    tela.getMenu().jogo("Primeira Fase");
                }


                if (bola.getPosicaoBolaY() <= bastao.getBastaoY1()) {
                    bola.configuraColisaoBola();
                }

                bola.definirDirecaoBola(gl);

                bola.desenhaBola(gl);
                bastao.desenhaBastao(gl);

                if (tela.getPontuacaoAtual() >= tela.getPontuacaoFase2()) {
                    obstaculo.desenhaObstaculo(gl);
                }

                desenharVidas(gl, glut);
                tela.getMenu().pontos(tela.getPontuacaoAtual());
                break;
            case 3:
                tela.setMenu(new Menu());
                tela.getMenu().pause();
                break;
            case 4:
                tela.setMenu(new Menu());
                tela.getMenu().gameOver();
                break;
            case 5:
                tela.getMenu().jogo("Segunda Fase");
                tela.setMenu(new Menu());
                tela.getMenu().segundaFase();
                break;
            case 6:
                tela.setMenu(new Menu());
                tela.getMenu().venceu();
                break;
        }

        gl.glFlush();
    }

    private void desenharVidas(GL2 gl, GLUT glut) {
        float incr = 0;
        for (int i = 0; i < tela.getVidas(); i += 1) {
            vida.desenhaVida(gl, glut, incr);
            incr += 0.1f;
        }
    }



    private void inicio(GL2 gl) {
        bola.setMoveY(bola.getMoveY() - bola.getVelocidade());
        gl.glTranslatef(0, bola.getMoveY(), 0);
        if (bola.getPosicaoBolaY() <= bastao.getBastaoY1()) {
            tela.setPontuacaoAtual(tela.getPontuacaoAtual() - tela.getPontosPorBatida());
            tela.setInicio(false);
        }
    }

    private void gameOver() {
        float BOLA_X = 0;
        BOLA_X = (bola.getPosicaoBolaX() + bola.getrX());

        if (bola.getPosicaoBolaY() <= bastao.getBastaoY1()
                && BOLA_X < bastao.getBastaoPontaEsquerda()) {

            tela.setVidas(tela.getVidas() - 1);
            tela.setPontuacaoAtual(tela.getPontuacaoAtual() - tela.getPontosPorBatida());
            tela.reset(true);

            if (tela.getVidas() == 0) {
                tela.setOpcao(4);
                tela.setVidas(5);
                tela.reset(true);
            }
        }

        if (bola.getPosicaoBolaY() <= bastao.getBastaoY1()
                && bola.getPosicaoBolaX() > bastao.getBastaoPontaDireita()) {

            tela.setVidas(tela.getVidas() - 1);
            tela.setPontuacaoAtual(tela.getPontuacaoAtual() - tela.getPontosPorBatida());
            tela.reset(true);

            if (tela.getVidas() == 0) {
                tela.setOpcao(4);
                tela.setVidas(5);
                tela.reset(true);
            }
        }
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        // obtem o contexto grafico Opengl
        GL2 gl = drawable.getGL().getGL2();
        // ativa a matriz de projeção
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity(); // lê a matriz identidade
        // projeção ortogonal (xMin, xMax, yMin, yMax, zMin, zMax)
        gl.glOrtho(-1, 1, -1, 1, -1, 1);
        // ativa a matriz de modelagem
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    public void dispose(GLAutoDrawable drawable) {
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
            case KeyEvent.VK_RIGHT:
                if (bastao.getBastaoPontaDireita() < 1) {
                    bastao.setMoveBastaoX(bastao.getMoveBastaoX() + 0.1f);
                    bastao.setBastaoPontaEsquerda(bastao.getBastaoX1() + (bastao.getMoveBastaoX()));
                    bastao.setBastaoPontaDireita(bastao.getBastaoX2() + (bastao.getMoveBastaoX()));
                }
                break;

            case KeyEvent.VK_LEFT:
                if (bastao.getBastaoPontaEsquerda() > -1) {
                    bastao.setMoveBastaoX(bastao.getMoveBastaoX() - 0.1f);
                    bastao.setBastaoPontaEsquerda(bastao.getBastaoX1() + (bastao.getMoveBastaoX()));
                    bastao.setBastaoPontaDireita(bastao.getBastaoX2() + (bastao.getMoveBastaoX()));
                }
                break;
            case KeyEvent.VK_ENTER:
                tela.setOpcao(2);
                break;
            case KeyEvent.VK_P:
                tela.setPause(!tela.isPause());
                if (tela.isPause()) {
                    tela.setOpcao(3);
                } else {
                    tela.setOpcao(2);
                }
                break;
        }
    }
   
    public void keyReleased(KeyEvent e) {
    }
}
